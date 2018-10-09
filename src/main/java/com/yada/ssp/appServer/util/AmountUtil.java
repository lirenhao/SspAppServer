package com.yada.ssp.appServer.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountUtil {

    /**
     * 将分转化为字符串的元
     *
     * @param money 金额，以分为单位
     * @return 返回金额元
     */
    public static String parseToYuan(String money) {
        return new BigDecimal(money).divide(new BigDecimal(100), 2, RoundingMode.DOWN).toString();
    }


    /**
     * 将字符串的元转化为分
     *
     * @param money 金额，以元为单位
     * @return 返回金额分
     */
    public static String parseToCent(String money) {
        BigDecimal doubleMoney = new BigDecimal(money);
        doubleMoney = doubleMoney.multiply(new BigDecimal(100));
        long longMoney = doubleMoney.longValue();
        return String.valueOf(longMoney);
    }

}
