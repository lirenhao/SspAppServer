package com.yada.ssp.appServer.web;

import com.yada.ssp.appServer.service.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Map;

@RestController
@RequestMapping(value = "/refund")
@Validated
public class RefundController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RefundService refundService;

    @Autowired
    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PostMapping
    public Map<String, String> index(OAuth2Authentication token,
                                     @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,32}$", message = "Format Error")
                                     @NotEmpty String pwd) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求退货", id[0], id[1]);
        return refundService.refund(id[0], pwd);
    }
}
