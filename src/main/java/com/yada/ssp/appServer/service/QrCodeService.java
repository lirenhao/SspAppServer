package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.config.QueryStatusProperties;
import com.yada.ssp.appServer.model.UserInfo;
import com.yada.ssp.appServer.model.UserInfoPK;
import com.yada.ssp.appServer.net.SspClient;
import com.yada.ssp.appServer.util.AmountUtil;
import com.yada.ssp.appServer.util.TlvPacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class QrCodeService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserInfoService userInfoService;
    private final SspClient sspClient;
    private final QueryStatusProperties queryStatusProperties;

    @Autowired
    public QrCodeService(UserInfoService userInfoService, SspClient sspClient, QueryStatusProperties queryStatusProperties) {
        this.userInfoService = userInfoService;
        this.sspClient = sspClient;
        this.queryStatusProperties = queryStatusProperties;
    }

    public Map<String, String> getQrCode(String amt, UserInfoPK id) {
        UserInfo userInfo = userInfoService.getUserInfo(id);
        String termLsNo = String.valueOf(new Date().getTime());
        // 交易类型 动态二维码请求-01、交易渠道 目前默认填01
        return getQrCode("01", amt, termLsNo, userInfo.getCcyType().getCcyType(),
                userInfo.getTermNo(), userInfo.getMerNo(), "01");
    }

    /**
     * 获取付款码
     *
     * @param tranType 交易类型
     * @param tranAmt  金额(单位:元)
     * @param termLsNo 终端流水号
     * @param tranCry  币种
     * @param termNo   终端号
     * @param merNo    商户号
     * @param channel  交易渠道
     * @return 报文结果
     */
    Map<String, String> getQrCode(String tranType, String tranAmt, String termLsNo,
                                  String tranCry, String termNo, String merNo, String channel) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("931", tranType);
        reqMap.put("004", AmountUtil.parseToCent(tranAmt));
        reqMap.put("011", termLsNo);
        reqMap.put("018", tranCry);
        reqMap.put("041", termNo);
        reqMap.put("042", merNo);
        reqMap.put("070", channel);
        String reqStr = TlvPacker.packer(reqMap);
        Map<String, String> result = new HashMap<>();

        try {
            // 发起生成二维码请求
            ByteBuffer respBuffer = sspClient.send(ByteBuffer.wrap(reqStr.getBytes()));
            Map<String, String> respMap = TlvPacker.unPacker(new String(respBuffer.array()));
            result.put("respCode", respMap.get("039"));
            result.put("respMsg", respMap.get("040"));
            if ("00".equals(respMap.get("039"))) {
                result.put("tranAmt", AmountUtil.parseToYuan(respMap.get("004")));
                result.put("tranCry", respMap.get("018"));
                result.put("queryNo", respMap.get("065"));
                result.put("qrCode", respMap.get("066"));
                result.put("timeout", respMap.get("067"));
            } else {
                logger.warn("获取付款码失败,返回码是[{}],提示信息是[{}]", respMap.get("039"), respMap.get("040"));
            }
        } catch (IOException e) {
            logger.warn("获取付款码异常,请求报文是[{}],异常信息是[{}]", reqStr, e.getMessage());
        }
        return result;
    }

    public Map<String, String> queryResult(String queryNo, UserInfoPK id) {
        String termNo = userInfoService.getTermNo(id);
        String termLsNo = String.valueOf(new Date().getTime());
        // 交易类型 动态二维码请求-02
        return queryResult("02", termLsNo, termNo, id.getMerNo(), queryNo);
    }

    /**
     * 交易状态查询
     *
     * @param tranType 交易类型
     * @param termLsNo 终端流水号
     * @param termNo   终端号
     * @param merNo    商户号
     * @param queryNo  交易查询号
     * @return 报文结果
     */
    Map<String, String> queryResult(String tranType, String termLsNo,
                                    String termNo, String merNo, String queryNo) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("931", tranType);
        reqMap.put("011", termLsNo);
        reqMap.put("041", termNo);
        reqMap.put("042", merNo);
        reqMap.put("065", queryNo);
        String reqStr = TlvPacker.packer(reqMap);
        Map<String, String> result = new HashMap<>();
        try {
            // 发起交易查询请求
            ByteBuffer respBuffer = sspClient.send(ByteBuffer.wrap(reqStr.getBytes()));
            Map<String, String> respMap = TlvPacker.unPacker(new String(respBuffer.array()));
            result.put("respMsg", respMap.get("040"));
            if (queryStatusProperties.getSuccess().contains(respMap.get("039"))) {
                result.put("respCode", "success");
                result.put("tranAmt", AmountUtil.parseToYuan(respMap.get("004")));
                result.put("tranCry", respMap.get("018"));
                result.put("tranNo", respMap.get("068"));
            } else if (queryStatusProperties.getWaiting().contains(respMap.get("039"))) {
                result.put("respCode", "waiting");
            } else if (queryStatusProperties.getFailed().contains(respMap.get("039"))) {
                result.put("respCode", "failed");
                logger.warn("交易失败,返回码是[{}],提示信息是[{}],交易查询号是[{}]",
                        respMap.get("039"), respMap.get("040"), queryNo);
            }
        } catch (IOException e) {
            logger.warn("交易状态查询异常,请求报文是[{}],异常信息是[{}]", reqStr, e.getMessage());
        }
        return result;
    }
}
