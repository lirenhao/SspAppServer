package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.config.QueryStatusProperties;
import com.yada.ssp.appServer.net.SspClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QrCodeService.class)
public class QrCodeServiceTest {

    @MockBean
    private UserInfoService userInfoService;
    @MockBean
    private SspClient sspClient;
    @MockBean
    private QueryStatusProperties queryStatusProperties;
    @Autowired
    private QrCodeService qrCodeService;

    @Test
    public void getQrCode() throws IOException {
        Mockito.when(sspClient.send(Mockito.any(ByteBuffer.class)))
                .thenReturn(ByteBuffer.wrap("9310002010040003123011000600123401800031560390002300400013MessageFormat0410008107894560420015100000000000666".getBytes()))
                .thenReturn(ByteBuffer.wrap("9310002010040003101011000615390801200142018100918204301800031560390002000400008Approved041000878945612042001510000000000066606500262018100918204300000000702306601620002010102121531104000441234567810000000000066652045411530315654041.015802CN5925Test Merchant 123456789016003BBM6106111   62280512000000007023070878945612630410060670003180".getBytes()))
                .thenThrow(new IOException("Mock IOException"));

        Map<String, String> result1 = qrCodeService.getQrCode("01", "100",
                "20181009095533001", "156", "78945612", "100000000000666", "01");
        assertEquals(2, result1.size());
        assertEquals("30", result1.get("respCode"));
        assertEquals("MessageFormat", result1.get("respMsg"));

        Map<String, String> result2 = qrCodeService.getQrCode("01", "100",
                "20181009095533001", "156", "78945612", "100000000000666", "01");
        assertEquals(7, result2.size());
        assertEquals("00", result2.get("respCode"));
        assertEquals("Approved", result2.get("respMsg"));
        assertEquals("1.01", result2.get("tranAmt"));
        assertEquals("156", result2.get("tranCry"));
        assertEquals("20181009182043000000007023", result2.get("queryNo"));
        assertEquals("0002010102121531104000441234567810000000000066652045411530315654041.015802CN5925Test Merchant 123456789016003BBM6106111   6228051200000000702307087894561263041006", result2.get("qrCode"));
        assertEquals("180", result2.get("timeout"));

        Map<String, String> result3 = qrCodeService.getQrCode("01", "100",
                "20181009095533001", "156", "78945612", "100000000000666", "01");
        assertEquals(0, result3.size());
    }

    @Test
    public void queryResult() throws IOException {
        Mockito.when(sspClient.send(Mockito.any(ByteBuffer.class)))
                .thenReturn(ByteBuffer.wrap("0390002000400008Approved00400031010180003156068002620181009182043000000007023".getBytes()))
                .thenReturn(ByteBuffer.wrap("0390002010400007waiting".getBytes()))
                .thenReturn(ByteBuffer.wrap("0390002020400006failed".getBytes()))
                .thenReturn(ByteBuffer.wrap("0390002030400007unknown".getBytes()))
                .thenThrow(new IOException("Mock IOException"));

        Mockito.when(queryStatusProperties.getSuccess()).thenReturn(Collections.singletonList("00"));
        Mockito.when(queryStatusProperties.getWaiting()).thenReturn(Collections.singletonList("01"));
        Mockito.when(queryStatusProperties.getFailed()).thenReturn(Collections.singletonList("02"));

        Map<String, String> result1 = qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
        assertEquals(5, result1.size());
        assertEquals("success", result1.get("respCode"));
        assertEquals("Approved", result1.get("respMsg"));
        assertEquals("1.01", result1.get("tranAmt"));
        assertEquals("156", result1.get("tranCry"));
        assertEquals("20181009182043000000007023", result1.get("tranNo"));

        Map<String, String> result2 = qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
        assertEquals(2, result2.size());
        assertEquals("waiting", result2.get("respCode"));
        assertEquals("waiting", result2.get("respMsg"));

        Map<String, String> result3 = qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
        assertEquals(2, result3.size());
        assertEquals("failed", result3.get("respCode"));
        assertEquals("failed", result3.get("respMsg"));

        Map<String, String> result4 = qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
        assertEquals(1, result4.size());
        assertEquals("unknown", result4.get("respMsg"));

        Map<String, String> result5 = qrCodeService.queryResult("02", "20181009095533002",
                "78945612", "100000000000666", "20180927173152000000005065");
        assertEquals(0, result5.size());
    }
}