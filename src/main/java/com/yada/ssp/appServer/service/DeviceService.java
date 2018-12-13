package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.dao.DeviceDao;
import com.yada.ssp.appServer.model.Device;
import com.yada.ssp.appServer.model.UserInfoPK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bjy on 2018/8/21.
 * 推送设备Service
 */

@Service
public class DeviceService {
    private final DeviceDao deviceDao;

    @Autowired
    public DeviceService(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    public Device saveAndUpdate(String merNo, String loginName, String termNo,
                                String pushType, String deviceNo, String platform, String pushFlag) {
        Device device = new Device();
        device.setMerNo(merNo);
        device.setLoginName(loginName);
        device.setTermNo(termNo);
        device.setDeviceNo(deviceNo);
        device.setPushType(pushType);
        device.setPlatform(platform);
        device.setPushFlag(pushFlag);
        return deviceDao.saveAndFlush(device);
    }

    public void delete(UserInfoPK userInfoPK) {
        if(deviceDao.existsById(userInfoPK)){
            deviceDao.deleteById(userInfoPK);
        }
    }

    public List<Device> findListByMerNo(String merNo) {
        return deviceDao.findByMerNo(merNo);
    }
}
