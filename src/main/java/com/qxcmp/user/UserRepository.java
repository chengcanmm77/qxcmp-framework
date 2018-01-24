package com.qxcmp.user;

import com.qxcmp.security.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 查找指定用户名的用户
     *
     * @param username 用户名
     *
     * @return 查找到的用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 查找指定邮箱的用户
     *
     * @param email 邮箱
     *
     * @return 查找到的用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 查找指定手机号的用户
     *
     * @param phone 手机号
     *
     * @return 查找到的用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 查找指定微信公众号OpenId的用户
     *
     * @param openID OpenId
     *
     * @return 查找到的用户
     */
    Optional<User> findByOpenID(String openID);

    /**
     * 根据用户UnionId查找
     *
     * @param unionId UnionId
     *
     * @return 查找到的用户
     */
    Optional<User> findByUnionId(String unionId);

    /**
     * 查找拥有指定权限的用户
     * <p>
     * 结果不包括Root用户
     *
     * @param privilege 权限
     *
     * @return 拥有该权限的用户列表
     */
    @Query("select user from User user join user.roles role inner join role.privileges privilege where privilege.name = :privilege and user.username <> 'administrator'")
    List<User> findByAuthority(@Param("privilege") String privilege);

    /**
     * 查找拥有指定角色的用户
     * 结果不包括Root用户
     *
     * @param role 角色
     *
     * @return 拥有该角色的用户列表
     */
    @Query("select user from User user inner join user.roles role where role = :role and user.username <> 'administrator'")
    List<User> findByRole(@Param("role") Role role);

    /**
     * 查找所有微信用户
     *
     * @param pageable 分页信息
     *
     * @return 微信用户
     */
    @Query("select user from User user where user.openID <> '' or user.unionId <> ''")
    Page<User> findWeixinUser(Pageable pageable);

    /**
     * 查找某日期之后加入的用户
     *
     * @param dateCreated 日期
     *
     * @return 日期之后加入的用户
     */
    List<User> findAllByDateCreatedAfterOrderByDateCreatedDesc(Date dateCreated);

    /**
     * 查找某日期之后登陆的用户
     *
     * @param dateLogin 日期
     *
     * @return 日期之后登陆的用户
     */
    List<User> findAllByDateLoginAfterOrderByDateCreatedDesc(Date dateLogin);
}
