package com.example.zxt.data.api;

/**
 * Create by andy on 2019/9/20 14:16
 * <p>
 * Description:
 */
public class Utils {
    public static boolean isChese(String str2) {
        int flage = 0;
        for (char c : str2.toCharArray()) {
            if ((c >= 0x4E00 && c <= 0x9FA5) || isChinese(c)) {
                flage = 1;
                System.out.println("发现中文字符：" + c);
                break; //有一个中文字符就返回
            }
        }
        return !(flage == 1);
    }

    private static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


}


