package com.qxcmp.user;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import com.qxcmp.core.entity.AbstractEntityService;
import com.qxcmp.core.support.IDGenerator;
import com.qxcmp.core.support.ImageGenerator;
import com.qxcmp.image.ImageService;
import com.qxcmp.security.Role;
import com.qxcmp.web.auth.UserAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Supplier;

/**
 * 用户服务
 *
 * @author aaric
 */
@Service
public class UserService extends AbstractEntityService<User, String, UserRepository> {

    private final ImageService imageService;
    private final ImageGenerator imageGenerator;

    /**
     * 储存用户登录Token，每个Token使用以后可以直接免密码登录，使用以后清楚掉该Token
     * <p>
     * Token的有效期为五分钟
     */
    private Map<String, UserAuthenticationToken> loginTokens = Maps.newConcurrentMap();

    public UserService(UserRepository repository, ImageService imageService, ImageGenerator imageGenerator) {
        super(repository);
        this.imageService = imageService;
        this.imageGenerator = imageGenerator;
    }

    /**
     * 获取当前认证的用户
     *
     * @return {null} 如果用户为登录
     */
    public User currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return null;
        }

        Object principle = authentication.getPrincipal();

        if (principle instanceof User) {
            return (User) principle;
        }

        return null;
    }

    /**
     * 判断用户是否含有某些权限
     *
     * @param user       用户
     * @param privileges 要判定的权限
     *
     * @return 如果用户拥有任意一个权限将返回真
     */
    public boolean hasRole(User user, String... privileges) {
        return user.getAuthorities().stream().anyMatch(authority -> {
            for (String privilege : privileges) {
                if (authority.getAuthority().equals("ROLE_" + privilege)) {
                    return true;
                }
            }

            return false;
        });
    }

    public List<User> findByAuthority(String privilege) {
        return repository.findByAuthority(privilege);
    }

    public Page<User> findByAuthority(String privilege, Pageable pageable) {
        return repository.findByAuthority(privilege, pageable);
    }

    public List<User> findByRole(Role role) {
        return repository.findByRole(role);
    }

    /**
     * 通过用户ID，用户名，邮箱，手机，OpenId来查询一个用户
     *
     * @param userId 查询键
     *
     * @return 查询后的用户
     */
    public Optional<User> findById(String userId) {

        if (findByUsername(userId).isPresent()) {
            return findByUsername(userId);
        }

        if (findByEmail(userId).isPresent()) {
            return findByEmail(userId);
        }

        if (findByPhone(userId).isPresent()) {
            return findByPhone(userId);
        }

        if (findByOpenID(userId).isPresent()) {
            return findByOpenID(userId);
        }

        if (findByUnionID(userId).isPresent()) {
            return findByUnionID(userId);
        }

        return Optional.empty();
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return repository.findByPhone(phone);
    }

    public Optional<User> findByOpenID(String openId) {
        return repository.findByOpenID(openId);
    }

    public Optional<User> findByUnionID(String unionId) {
        return repository.findByUnionId(unionId);
    }

    public Page<User> findWeixinUser(Pageable pageable) {
        return repository.findWeixinUser(pageable);
    }

    /**
     * 为用户生成默认头像
     *
     * @param user 用户
     */
    public void setDefaultPortrait(User user) {
        try {

            String name = user.getName();

            if (StringUtils.isBlank(name)) {
                name = user.getNickname();
            }

            if (StringUtils.isBlank(name)) {
                name = user.getUsername();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(imageGenerator.nextPortrait(name, 96), "jpg", byteArrayOutputStream);
            imageService.store(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), "jpg", 96, 96).ifPresent(image -> user.setPortrait(String.format("/api/image/%s.%s", image.getId(), image.getType())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为用户生成一个认证Token
     *
     * @param userId 用户ID
     *
     * @return 生成以后的Token
     */
    public String generateLoginToken(String userId) {
        String token = Hashing.sha256().hashString(userId + String.valueOf(System.currentTimeMillis()), Charset.defaultCharset()).toString();
        loginTokens.put(token, new UserAuthenticationToken(userId, DateTime.now().plusMinutes(5).toDate()));
        return token;
    }

    /**
     * 使用认证Token让用户进行认证
     * <p>
     * 认证成功以后用户将进入登录状态
     * <p>
     * 当Token绑定的用户ID和传入的用户ID匹配且Token未过期时，认证成功
     *
     * @param userId 要认证用户ID
     * @param token  认证Token
     *
     * @return 认证结果
     */
    public boolean tokenAuthentication(String userId, String token) {
        if (!loginTokens.containsKey(token)) {
            return false;
        }

        UserAuthenticationToken userLoginToken = loginTokens.get(token);
        loginTokens.remove(token);

        if (System.currentTimeMillis() > userLoginToken.getDateExpired().getTime()) {
            return false;
        }

        if (!StringUtils.equals(userId, userLoginToken.getUserId())) {
            return false;
        }

        return findOne(userLoginToken.getUserId()).map(user -> {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
            update(user.getId(), u -> u.setDateLogin(new Date()));
            return true;
        }).orElse(false);
    }

    /**
     * 查询某日期之后加入的用户
     *
     * @param date 日期
     *
     * @return 日期之后加入的用户
     */
    public List<User> findByDateCreate(Date date) {
        return repository.findAllByDateCreatedAfterOrderByDateCreatedDesc(date);
    }

    /**
     * 查询某日期之后登陆的用户
     *
     * @param date 日期
     *
     * @return 日期之后登陆的用户
     */
    public List<User> findByDateLogin(Date date) {
        return repository.findAllByDateLoginAfterOrderByDateCreatedDesc(date);
    }

    @Override
    public <S extends User> Optional<S> create(Supplier<S> supplier) {
        S user = supplier.get();

        if (StringUtils.isNotEmpty(user.getId())) {
            return Optional.empty();
        }

        user.setId(IDGenerator.next());

        return super.create(() -> user);
    }

    @Override
    protected <S extends User> String getEntityId(S entity) {
        return entity.getId();
    }

}
