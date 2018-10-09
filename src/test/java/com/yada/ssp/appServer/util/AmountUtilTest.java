package com.yada.ssp.appServer.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AmountUtilTest {

    @Test
    public void parseToYuan() {
        assertEquals("1.00", AmountUtil.parseToYuan("100"));
    }

    @Test
    public void parseToCent() {
        assertEquals("101", AmountUtil.parseToCent("1.01"));
    }
}