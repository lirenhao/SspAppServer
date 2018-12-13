package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.dao.RefundDao;
import com.yada.ssp.appServer.model.Refund;
import com.yada.ssp.appServer.model.UserInfo;
import com.yada.ssp.appServer.model.UserInfoPK;
import com.yada.ssp.appServer.net.SspClient;
import com.yada.ssp.appServer.util.AmountUtil;
import com.yada.ssp.appServer.util.DateUtil;
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
    private final UserInfoService userInfoService;
    private final SspClient sspClient;

    @Autowired
    public RefundService(RefundDao refundDao, PasswordEncoder md5PasswordEncoder,
                         UserInfoService userInfoService, SspClient sspClient) {
        this.refundDao = refundDao;
        this.md5PasswordEncoder = md5PasswordEncoder;
        this.userInfoService = userInfoService;
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

    /**
     * 商户退货申请
     *
     * @param amt      金额(单位:元)
     * @param bankLsNo 银行流水号
     * @param pwd      退货密码
     * @param id       用户ID
     * @return 请求结果
     */
    public Map<String, String> refund(String amt, String bankLsNo, String pwd, UserInfoPK id) {
        UserInfo userInfo = userInfoService.getUserInfo(id);
        // 商户交易号最大长度是64位,0000 + YYYYMMDDHHmmss + 唯一标识
        String merTraceNo = "0000" + DateUtil.getCurDateTime() + (int) ((Math.random() * 9 + 1) * 1000);
        // 交易类型 交易退货-05、交易渠道 目前默认填05
        return refund("05", amt, userInfo.getCcyType().getCcyType(),
                userInfo.getTermNo(), userInfo.getMerNo(), merTraceNo, bankLsNo, pwd);
    }

    /**
     * 商户退货申请
     *
     * @param tranType   交易类型
     * @param tranAmt    金额(单位:元)
     * @param tranCry    币种
     * @param termNo     终端号
     * @param merNo      商户号
     * @param merTraceNo 商户交易号
     * @param bankLsNo   银行流水号
     * @param pwd        退货密码
     * @return 请求结果
     */
    Map<String, String> refund(String tranType, String tranAmt, String tranCry,
                               String termNo, String merNo, String merTraceNo, String bankLsNo, String pwd) {
        Map<String, String> result = new HashMap<>();
        Refund refund = refundDao.findById(merNo).orElse(new Refund());
        if (refund.getPassWord().equals(md5PasswordEncoder.encode(pwd))) {
            Map<String, String> reqMap = new HashMap<>();
            reqMap.put("931", tranType);
            reqMap.put("004", AmountUtil.parseToCent(tranAmt));
            reqMap.put("018", tranCry);
            reqMap.put("041", termNo);
            reqMap.put("042", merNo);
            reqMap.put("068", merTraceNo);
            reqMap.put("071", bankLsNo);
            String reqStr = TlvPacker.packer(reqMap);
            try {
                logger.info("发起退货的请求报文是[{}]", reqStr);
                ByteBuffer respBuffer = sspClient.send(ByteBuffer.wrap(reqStr.getBytes()));
                Map<String, String> respMap = TlvPacker.unPacker(new String(respBuffer.array()));
                result.put("respCode", respMap.get("039"));
                result.put("respMsg", respMap.get("040"));
            } catch (IOException e) {
                result.put("respCode", "error");
                result.put("respMsg", "Server exception");
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
