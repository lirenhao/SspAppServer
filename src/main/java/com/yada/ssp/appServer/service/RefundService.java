package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.dao.RefundDao;
import com.yada.ssp.appServer.model.Refund;
import com.yada.ssp.appServer.net.SspClient;
import com.yada.ssp.appServer.util.TlvPacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Service
public class RefundService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RefundDao refundDao;
    private final PasswordEncoder md5PasswordEncoder;
    private final SspClient sspClient;

    @Autowired
    public RefundService(RefundDao refundDao, PasswordEncoder md5PasswordEncoder, SspClient sspClient) {
        this.refundDao = refundDao;
        this.md5PasswordEncoder = md5PasswordEncoder;
        this.sspClient = sspClient;
    }

    /**
     * 开启商户退货
     *
     * @param merNo 商户号
     * @return 退货设置
     */
    public Refund get(String merNo) {
        return refundDao.findById(merNo).orElse(null);
    }

    /**
     * 开启商户退货
     *
     * @param merNo 商户号
     * @param pwd   退货密码
     * @return 退货设置
     */
    public Refund open(String merNo, String pwd) {
        Refund refund = new Refund();
        refund.setMerNo(merNo);
        refund.setPassWord(md5PasswordEncoder.encode(pwd));
        return refundDao.saveAndFlush(refund);
    }

    /**
     * 关闭商户退货
     *
     * @param merNo 商户号
     */
    public void close(String merNo) {
        refundDao.deleteById(merNo);
    }

    public Map<String, String> refund(String merNo, String pwd) {
        Map<String, String> result = new HashMap<>();
        Refund refund = refundDao.findById(merNo).orElse(new Refund());
        if (refund.getPassWord().equals(md5PasswordEncoder.encode(pwd))) {
            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("042", merNo);
            String reqStr = TlvPacker.packer(reqMap);
            try {
                logger.info("发起退货的请求报文是[{}]", reqStr);
                ByteBuffer respBuffer = sspClient.send(ByteBuffer.wrap(reqStr.getBytes()));
                Map<String, String> respMap = TlvPacker.unPacker(new String(respBuffer.array()));
                result.put("respCode", respMap.get("039"));
                result.put("respMsg", respMap.get("040"));
                if ("00".equals(respMap.get("039"))) {
                    result.put("merNo", respMap.get("042"));
                } else {
                    logger.warn("发起退货失败,返回码是[{}],提示信息是[{}]", respMap.get("039"), respMap.get("040"));
                }
            } catch (IOException e) {
                logger.warn("发起退货异常,请求报文是[{}],异常信息是[{}]", reqStr, e.getMessage());
            }
        } else {
            result.put("respCode", "failed");
            result.put("respMsg", "Refund Password Error");
            logger.warn("发起退货失败,商户[%s]退货密码错误", merNo);
        }
        return result;
    }
}
