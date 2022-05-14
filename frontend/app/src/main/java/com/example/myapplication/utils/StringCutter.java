package com.example.myapplication.utils;

/**
 * 字符串剪切，防止列表中一段字太长影响观感
 */
public class StringCutter {
    public static String cutter(String s, int max) {
        if (s.length() >= max) {
            return s.substring(0, max) + "...";
        }
        return s;
    }
}
