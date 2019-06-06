package com.caimao.bana.server.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by WangXu on 2015/5/18.
 */
public class CheckIdCard {
    private String cardNumber;
    private Boolean cacheValidateResult = null;
    private Date cacheBirthDate = null;
    private static final String BIRTH_DATE_FORMAT = "yyyyMMdd";
    private static final Date MINIMAL_BIRTH_DATE = new Date(-2209017600000L);
    private static final int NEW_CARD_NUMBER_LENGTH = 18;
    private static final int OLD_CARD_NUMBER_LENGTH = 15;
    private static final char[] VERIFY_CODE = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

    private static final int[] VERIFY_CODE_WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    public boolean validate() {
        if (null == this.cacheValidateResult) {
            boolean result = true;
            result = (result) && (null != this.cardNumber);
            result = (result) && (18 == this.cardNumber.length());

            for (int i = 0; (result) && (i < 17); i++) {
                char ch = this.cardNumber.charAt(i);
                result = (result) && (ch >= '0') && (ch <= '9');
            }

            result = (result) && (calculateVerifyCode(this.cardNumber) == this.cardNumber.charAt(17));
            try {
                Date birthDate = getBirthDate();
                result = (result) && (null != birthDate);
                result = (result) && (birthDate.before(new Date()));
                result = (result) && (birthDate.after(MINIMAL_BIRTH_DATE));

                String birthdayPart = getBirthDayPart();
                String realBirthdayPart = createBirthDateParser().format(birthDate);

                result = (result) && (birthdayPart.equals(realBirthdayPart));
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            this.cacheValidateResult = Boolean.valueOf(result);
        }
        return this.cacheValidateResult.booleanValue();
    }

    public CheckIdCard(String cardNumber) {
        if (null != cardNumber) {
            cardNumber = cardNumber.trim();
            if (15 == cardNumber.length()) {
                cardNumber = contertToNewCardNumber(cardNumber);
            }
        }
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getAddressCode() {
        checkIfValid();
        return this.cardNumber.substring(0, 6);
    }

    public Date getBirthDate() {
        if (null == this.cacheBirthDate) {
            try {
                this.cacheBirthDate = createBirthDateParser().parse(getBirthDayPart());
            } catch (Exception e) {
                throw new RuntimeException("身份证的出生日期无效");
            }
        }
        return new Date(this.cacheBirthDate.getTime());
    }

    public boolean isMale() {
        return 1 == getGenderCode();
    }

    public boolean isFemal() {
        return false == isMale();
    }

    private int getGenderCode() {
        checkIfValid();
        char genderCode = this.cardNumber.charAt(16);
        return genderCode - '0' & 0x1;
    }

    private String getBirthDayPart() {
        return this.cardNumber.substring(6, 14);
    }

    private SimpleDateFormat createBirthDateParser() {
        return new SimpleDateFormat("yyyyMMdd");
    }

    private void checkIfValid() {
        if (false == validate())
            throw new RuntimeException("身份证号码不正确！");
    }

    private static char calculateVerifyCode(CharSequence cardNumber) {
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char ch = cardNumber.charAt(i);
            sum += (ch - '0') * VERIFY_CODE_WEIGHT[i];
        }
        return VERIFY_CODE[(sum % 11)];
    }

    private static String contertToNewCardNumber(String oldCardNumber) {
        StringBuilder buf = new StringBuilder(18);
        buf.append(oldCardNumber.substring(0, 6));
        buf.append("19");
        buf.append(oldCardNumber.substring(6));
        buf.append(calculateVerifyCode(buf));
        return buf.toString();
    }
}
