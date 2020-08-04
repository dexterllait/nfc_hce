package com.dexter.nfc.hce.common;

public class StringHelper {

    private static final String BLANK = "";

    public static boolean isEmpty(String str) {
        return str == null || BLANK.equals(str);
    }
}
