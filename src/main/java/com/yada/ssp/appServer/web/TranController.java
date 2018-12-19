package com.yada.ssp.appServer.web;

import com.yada.ssp.appServer.model.TranInfo;
import com.yada.ssp.appServer.service.MerchantService;
import com.yada.ssp.appServer.service.TranInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/tran")
public class TranController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TranInfoService tranInfoService;
    private final MerchantService merchantService;

    @Autowired
    public TranController(TranInfoService tranInfoService, MerchantService merchantService) {
        this.tranInfoService = tranInfoService;
        this.merchantService = merchantService;
    }

    /**
     * 获取商户的子商户信息(包含本商户)
     *
     * @param token 授权信息
     * @return 子商户信息
     */
    @GetMapping(value = "/subMer")
    public Map<String, String> subMerList(OAuth2Authentication token) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求获取子商户", id[0], id[1]);
        return merchantService.getSubMer(id[0]);
    }

    /**
     * 商户交易列表查询
     *
     * @param token    授权信息
     * @param merNo    查询交易的所属商户号
     * @param tranDate 查询交易的交易日期
     * @return 交易列表信息
     */
    @GetMapping(value = "/{merNo}")
    public List<TranInfo> list(OAuth2Authentication token, @PathVariable("merNo") String merNo, @RequestParam String tranDate) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求交易列表,交易查询的商户号是[{}]、交易日期是[{}]", id[0], id[1], merNo, tranDate);
        String pMerNo = id[0];
        if (merchantService.checkSubMer(pMerNo, merNo)) {
            return tranInfoService.getList(merNo, tranDate);
        }
        logger.warn("商户[{}]的[{}]用户请求交易列表错误,商户[{}]不是子商户", id[0], id[1], merNo);
        return null;
    }

    /**
     * 商户交易信息查询
     *
     * @param token 授权信息
     * @param merNo 查询交易的所属商户号
     * @param lsId  交易流水号
     * @return 交易信息
     */
    @GetMapping(value = "/{merNo}/{lsId}")
    public TranInfo info(OAuth2Authentication token, @PathVariable("merNo") String merNo, @PathVariable("lsId") String lsId) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求交易信息,交易信息的商户号是[{}]、流水号是[{}]", id[0], id[1], merNo, lsId);
        String pMerNo = id[0];
        if (merchantService.checkSubMer(pMerNo, merNo)) {
            return tranInfoService.getInfo(lsId);
        }
        logger.warn("商户[{}]的[{}]用户请求交易信息错误,商户[{}]不是子商户", id[0], id[1], merNo);
        return null;
    }

    /**
     * 商户终端交易列表查询
     *
     * @param token    授权信息
     * @param termNo   查询交易的终端号
     * @param tranDate 查询交易的交易日期
     * @return 交易列表信息
     */
    @GetMapping(value = "/term/{termNo}")
    public List<TranInfo> subList(OAuth2Authentication token, @PathVariable("termNo") String termNo, @RequestParam String tranDate) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求交易列表,交易查询的终端号是[{}]、交易日期是[{}]", id[0], id[1], termNo, tranDate);
        return tranInfoService.getList(id[0], termNo, tranDate);
    }
}
