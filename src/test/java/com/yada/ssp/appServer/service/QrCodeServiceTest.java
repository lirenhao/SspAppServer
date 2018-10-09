package com.yada.ssp.appServer.service;

import com.yada.sdk.net.TcpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QrCodeService.class)
public class QrCodeServiceTest {

    @MockBean
    private UserInfoService userInfoService;
    @MockBean
    private TcpClient tcpClient;
    @Autowired
    private QrCodeService qrCodeService;

    @Test
    public void getQrCode() {
        qrCodeService.getQrCode("01", "100", "20181009095533001",
                "156", "78945612", "100000000000666", "01");
    }

    @Test
    public void queryResult() {
        qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
    }
}