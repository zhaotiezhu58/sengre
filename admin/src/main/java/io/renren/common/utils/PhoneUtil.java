package io.renren.common.utils;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtil {

    /**
     * 校验手机号码正确性
     * @param regCode
     * @param phoneNum
     * @return
     */
    public static boolean validPhone(String regCode,String phoneNum) {
        try {
            Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
            pn.setCountryCode(Integer.parseInt(regCode));
            pn.setNationalNumber(Long.valueOf(phoneNum));
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            return phoneUtil.isValidNumber(pn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
