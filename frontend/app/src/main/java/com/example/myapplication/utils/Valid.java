package com.example.myapplication.utils;

import android.widget.EditText;

import com.andreabaccega.formedittextvalidator.Validator;

public class Valid {
    /**
     * 判断是否为空白字符
     * 包括 null、空字符串、全由空白字符构成的字符串
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
     * 判断是否为数字
     * 由数字组成（非空）
     */
    public static boolean isNumber(String s) {
        return s.matches("^[0-9]+$");
    }

    /**
     * 判断是否为有效账号
     * 由字母、数字、下划线组成
     * 不以数字开头
     * 2 ~ 10 个字符
     */
    public static boolean isAccount(String s) {
        return s.matches("^[^0-9][\\w]{1,9}$");
    }

    /**
     * 判断是否为有效密码
     * 由字母、数字、下划线组成
     * 不以数字开头
     * 6 ~ 20 个字符
     */
    public static boolean isPassword(String s) {
        return s.matches("^[\\w]{6,20}$");
    }

    /**
     * 判断是否为有效性别
     * “男”、“女”或“保密”
     */
    public static boolean isGender(String s) {
        return s.matches("^男|女|保密$");
    }

    public static class NotBlankValidator extends Validator {
        public NotBlankValidator() {
            super("该项不能为空");
        }

        @Override
        public boolean isValid(EditText et) {
            return !isBlank(et.getText().toString());
        }
    }

    public static class NumberValidator extends Validator {
        public NumberValidator() {
            super("请输入数字");
        }

        @Override
        public boolean isValid(EditText et) {
            return isNumber(et.getText().toString());
        }
    }

    public static class AccountValidator extends Validator {
        public AccountValidator() {
            super("账号需由字母、数字、下划线组成；不以数字开头；2 ~ 10 个字符");
        }

        @Override
        public boolean isValid(EditText et) {
            return isAccount(et.getText().toString());
        }
    }

    public static class PasswordValidator extends Validator {
        public PasswordValidator() {
            super("密码需由字母、数字、下划线组成；不以数字开头；6 ~ 20 个字符");
        }

        @Override
        public boolean isValid(EditText et) {
            return isPassword(et.getText().toString());
        }
    }

    public static class GenderValidator extends Validator {
        public GenderValidator() {
            super("性别需为“男”、“女”或“保密”");
        }

        @Override
        public boolean isValid(EditText et) {
            return isGender(et.getText().toString());
        }
    }
}
