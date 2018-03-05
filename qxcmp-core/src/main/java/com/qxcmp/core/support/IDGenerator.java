package com.qxcmp.core.support;

import com.google.common.hash.Hashing;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 主键生成工具
 * <p>
 * 生成算法如下 <ol> <li>获取随机UUID</li> <li>对生成的UUID进行SHA哈希</li> <li>返回哈希后的字符串Base64编码</li> </ol>
 *
 * @author aaric
 */
public class IDGenerator {

    private static long index = 0;

    /**
     * 生成一个订单号
     *
     * @return 生成一个订单号
     */
    public static synchronized String order() {

        if (index == 1000000) {
            index = 0;
        }

        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + new DecimalFormat("000000").format(index++);
    }

    /**
     * 返回一个随机UUID，只保留数字或者字母
     * <p>
     * 平台默认主键生成算法
     *
     * @return 返回一个随机UUID
     */
    public static String next() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 将随机UUID进行SHA-384哈希
     * <p>
     * 一般用于图片主键生成
     *
     * @return 将随机UUID进行SHA-384哈希
     */
    public static String sha384() {
        return Hashing.sha384().hashBytes(next().getBytes()).toString();
    }
}
