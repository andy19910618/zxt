package com.tezwez.base.helper

import java.util.regex.Pattern

/**
 * @program:
 * @description: 校验工具类
 * @author: Tim
 * @create: 2019-01-05 16:21
 */
object ValidatorUtil {
    /**
     * 正则表达式：验证用户名
     */
    val REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$"
    /**
     * 正则表达式：验证验证码
     */
    val REGEX_VERIFICATIONCODE = "^[0-9]*[1-9][0-9]*\$"

    /**
     * 正则表达式：验证密码
     */
//    val REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$"
    val REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{6,20}\$"

    /**
     * 正则表达式：验证密码
     */
    val REGEX_6_PASSWORD = "^\\d{6}"

    /**
     * 正则表达式：验证手机号
     */
    val REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"

    /**
     * 正则表达式：验证邮箱
     */
//    val REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
    val REGEX_EMAIL = "^([0-9A-Za-z\\-_\\.]+)@([0-9a-z]+\\.[a-z]{2,3}(\\.[a-z]{2})?)$"

    /**
     * 正则表达式：验证个汉字
     */
    val REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$"
    val REGEX_CHINESE_EN = "^[a-zA-Z\\u4e00-\\u9fa5]+\$"

    /**
     * 正则表达式：验证身份证
     */
    val REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)"
    val REGEX_BANK_CARD = "^\\d{18}\$"
    val REGEX_NUM = "^[\\.\\d]*\$"



    /**
     * 正则表达式：验证URL
     */
    val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"

    /**
     * 正则表达式：验证IP地址
     */
    val REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"

    /**
     * 正则表达式：验证地址
     */
    val ADDRESS_URL = "[a-z0-9A-Z:]+[a-zA-Z]+[a-z0-9A-Z:]+\$"


    /**
     * 正则表达式：验证Memo 值  只能是 英文 和 数字
     */
    val MEMO_MATCH = "[a-z0-9A-Z]+\$"


    /**
     * 校验 地址格式
     *
     * @param address
     * @return 校验通过返回true，否则返回false
     */
    fun isAddress(address: String): Boolean {
        return Pattern.matches(ADDRESS_URL, address)
    }

    /**
     * 校验 地址格式
     *
     * @param memo
     * @return 校验通过返回true，否则返回false
     */
    fun isMemo(memo: String): Boolean {
        return Pattern.matches(MEMO_MATCH, memo)
    }

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    fun isUsername(username: String): Boolean {
        return Pattern.matches(REGEX_USERNAME, username)
    }

    /**
     * 校验数字
     *
     * @param str
     * @return 校验通过返回true，否则返回false
     */
    fun isNum(str: String): Boolean {
        return Pattern.matches(REGEX_NUM, str)
    }
    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    fun isVerificationCode(verificationCode: String): Boolean {
        return Pattern.matches(REGEX_VERIFICATIONCODE, verificationCode)
    }


    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    fun isPassword(password: String): Boolean {
        return Pattern.matches(REGEX_PASSWORD, password)
    }


    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    fun is6Password(password: String): Boolean {
        return Pattern.matches(REGEX_6_PASSWORD, password)
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    fun isMobile(mobile: String): Boolean {
        return Pattern.matches(REGEX_MOBILE, mobile)
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    fun isEmail(email: String): Boolean {
        return Pattern.matches(REGEX_EMAIL, email)
    }

    /**
     * 91      * 校验汉字
     * 92      *
     * 93      * @param chinese
     * 94      * @return 校验通过返回true，否则返回false
     * 95
     */
    fun isChinese(chinese: String): Boolean {
        return Pattern.matches(REGEX_CHINESE, chinese)
    }


    fun isChineseOrEn(chinese: String): Boolean {
        return Pattern.matches(REGEX_CHINESE_EN, chinese)
    }

    /**
     * 101      * 校验身份证
     * 102      *
     * 103      * @param idCard
     * 104      * @return 校验通过返回true，否则返回false
     * 105
     */
    fun isIDCard(idCard: String): Boolean {
        return Pattern.matches(REGEX_ID_CARD, idCard)
    }

    /**
     * 111      * 校验URL
     * 112      *
     * 113      * @param url
     * 114      * @return 校验通过返回true，否则返回false
     * 115
     */
    fun isUrl(url: String): Boolean {
        return Pattern.matches(REGEX_URL, url)
    }

    /**
     * 121      * 校验IP地址
     * 122      *
     * 123      * @param ipAddr
     * 124      * @return
     * 125
     */
    fun isIPAddr(ipAddr: String): Boolean {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr)
    }

}