package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.dao.HisTranInfoDao;
import com.yada.ssp.appServer.dao.TranInfoDao;
import com.yada.ssp.appServer.util.DateUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TranInfoService.class)
public class TranInfoServiceTest {

    @MockBean
    private TranInfoDao tranInfoDao;
    @MockBean
    private HisTranInfoDao hisTranInfoDao;
    @Autowired
    private TranInfoService tranInfoService;

    @Test
    public void isHisDate() {
        String tranDate = DateUtil.getCurDate();
        Assert.assertTrue(tranInfoService.isHisDate(tranDate, -1));
        Assert.assertFalse(tranInfoService.isHisDate(tranDate, 0));
        Assert.assertFalse(tranInfoService.isHisDate(tranDate, 1));
    }
}