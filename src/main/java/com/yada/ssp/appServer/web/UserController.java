package com.yada.ssp.appServer.web;

import com.yada.ssp.appServer.model.UserInfo;
import com.yada.ssp.appServer.model.UserInfoPK;
import com.yada.ssp.appServer.service.DeviceService;
import com.yada.ssp.appServer.service.RefundService;
import com.yada.ssp.appServer.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserInfoService userInfoService;
    private final DeviceService deviceService;
    private final RefundService refundService;

    @Autowired
    public UserController(UserInfoService userInfoService, DeviceService deviceService, RefundService refundService) {
        this.userInfoService = userInfoService;
        this.deviceService = deviceService;
        this.refundService = refundService;
    }

    /**
     * 获取用户信息
     *
     * @param token 授权信息
     * @return 商户信息
     */
    @GetMapping
    public UserInfo index(OAuth2Authentication token) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求获取用户信息", id[0], id[1]);
        return userInfoService.getUserInfo(new UserInfoPK(id[0], id[1]));
    }

    /**
     * 修改商户密码
     *
     * @param token  授权信息
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 修改是否成功
     */
    @PutMapping(value = "/updatePwd")
    public boolean updatePwd(OAuth2Authentication token, @NotEmpty String oldPwd,
                             @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,32}$", message = "Format Error")
                             @NotEmpty String newPwd) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求修改密码", id[0], id[1]);
        return userInfoService.updatePwd(new UserInfoPK(id[0], id[1]), oldPwd, newPwd);
    }

    /**
     * 推送绑定
     *
     * @param token    授权信息
     * @param pushType 设备类型
     * @param deviceNo 设备ID
     * @param platform 设备平台
     * @return 用户信息
     */
    @PostMapping(value = "/bindPush")
    public UserInfo bindPush(OAuth2Authentication token, @NotEmpty String pushType, @NotEmpty String deviceNo, @NotEmpty String platform) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求绑定推送", id[0], id[1]);
        UserInfo userInfo = userInfoService.getUserInfo(new UserInfoPK(id[0], id[1]));
        String pushFlag = userInfo.getRoles().contains("admin") ? "1" : "0";
        if (deviceService.saveAndUpdate(id[0], id[1], userInfo.getTermNo(), pushType, deviceNo, platform, pushFlag) != null) {
            return userInfo;
        }
        logger.warn("商户[{}]的[{}]用户请求绑定推送失败", id[0], id[1]);
        return null;
    }

    /**
     * 推送解绑
     *
     * @param token 授权信息
     */
    @DeleteMapping(value = "/unBindPush")
    public boolean unBindPush(OAuth2Authentication token) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求解绑推送", id[0], id[1]);
        deviceService.delete(new UserInfoPK(id[0], id[1]));
        return true;
    }

    /**
     * 用户是否开启退货
     *
     * @param token 授权信息
     */
    @GetMapping(value = "/refund")
    public boolean refund(OAuth2Authentication token) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求开启退货", id[0], id[1]);
        return refundService.get(id[0]) != null;
    }

    /**
     * 开启退货功能
     *
     * @param token 授权信息
     * @param pwd   退货密码
     */
    @PostMapping(value = "/refund")
    public boolean openRefund(OAuth2Authentication token, @Min(6) @Max(32) @NotEmpty String pwd) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求开启退货", id[0], id[1]);
        return refundService.open(id[0], pwd) != null;
    }

    /**
     * 关闭退货功能
     *
     * @param token 授权信息
     */
    @DeleteMapping(value = "/refund")
    public boolean closeRefund(OAuth2Authentication token) {
        String[] id = token.getOAuth2Request().getClientId().split("@");
        logger.info("商户[{}]的[{}]用户请求关闭退货", id[0], id[1]);
        refundService.close(id[0]);
        return true;
    }
}
