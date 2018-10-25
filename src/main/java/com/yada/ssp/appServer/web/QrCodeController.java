package com.yada.ssp.appServer.web;

import com.yada.ssp.appServer.model.UserInfoPK;
import com.yada.ssp.appServer.service.QrCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@RestController
@RequestMapping(value = "/qrCode")
@Validated
public class QrCodeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final QrCodeService qrCodeService;

    @Autowired
    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    /**
     * 获取用户二维码
     *
     * @param token 授权信息
     * @return 二维码的内容
     */
    @PostMapping(value = "/")
    public Map<String, String> create(OAuth2Authentication token, @NotEmpty String amt) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求获取付款码", id[0], id[1]);
        return qrCodeService.getQrCode(amt, new UserInfoPK(id[0], id[1]));
    }

    /**
     * 根据交易查询号查询结果
     *
     * @param token   授权信息
     * @param queryNo 交易查询号
     * @return 交易结果
     */
    @GetMapping(value = "/{queryNo}")
    public Map<String, String> query(OAuth2Authentication token, @PathVariable String queryNo) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求查询付款码结果", id[0], id[1]);
        return qrCodeService.queryResult(queryNo, new UserInfoPK(id[0], id[1]));
    }
}
