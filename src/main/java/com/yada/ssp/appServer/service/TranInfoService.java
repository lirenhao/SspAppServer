package com.yada.ssp.appServer.service;

import com.yada.ssp.appServer.dao.HisTranInfoDao;
import com.yada.ssp.appServer.dao.TranInfoDao;
import com.yada.ssp.appServer.model.HisTranInfo;
import com.yada.ssp.appServer.model.TranInfo;
import com.yada.ssp.appServer.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TranInfoService {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    private final TranInfoDao tranInfoDao;
    private final HisTranInfoDao hisTranInfoDao;

    @Autowired
    public TranInfoService(TranInfoDao tranInfoDao, HisTranInfoDao hisTranInfoDao) {
        this.tranInfoDao = tranInfoDao;
        this.hisTranInfoDao = hisTranInfoDao;
    }

    public TranInfo getInfo(String id) {
        TranInfo tranInfo = tranInfoDao.findById(id).orElse(null);
        if (tranInfo == null) {
            tranInfo = hisTranInfoDao.findById(id).map(this::hisToInfo).orElse(null);
        }
        return tranInfo;
    }

    public List<TranInfo> getList(String merNo, String tranDate) {
        if (isHisDate(tranDate, 30)) {
            return hisTranInfoDao.findByMerNo(merNo, tranDate)
                    .stream().map(this::hisToInfo).collect(Collectors.toList());
        } else {
            return tranInfoDao.findByMerNo(merNo, tranDate);
        }
    }

    public List<TranInfo> getList(String merNo, String termNo, String tranDate) {
        if (isHisDate(tranDate, 30)) {
            return hisTranInfoDao.findByMerNoAndTermNo(merNo, termNo, tranDate)
                    .stream().map(this::hisToInfo).collect(Collectors.toList());
        } else {
            return tranInfoDao.findByMerNoAndTermNo(merNo, termNo, tranDate);
        }
    }

    boolean isHisDate(String tranDate, int day) {
        boolean result = false;
        String his = DateUtil.addDay(tranDate, day);
        String cur = DateUtil.getCurDate();
        try {
            Date hisDate = sdf.parse(his);
            Date curDate = sdf.parse(cur);
            result = hisDate.getTime() < curDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    private TranInfo hisToInfo(HisTranInfo his) {
        TranInfo info = new TranInfo();
        info.setLsId(his.getLsId());
        info.setTraceNo(his.getTraceNo());
        info.setMerNo(his.getMerNo());
        info.setTermNo(his.getTermNo());
        info.setTranType(his.getTranType());
        info.setTranAmt(his.getTranAmt());
        info.setCardNo(his.getCardNo());
        info.setTranDate(his.getTranDate());
        info.setTranTime(his.getTranTime());
        info.setRespCode(his.getRespCode());
        info.setChannel(his.getChannel());
        info.setTranFlag(his.getTranFlag());
        info.setMerTraceNo(his.getMerTraceNo());
        info.setBatchNo(his.getBatchNo());
        info.setRrn(his.getRrn());
        info.setDebcreFlag(his.getDebcreFlag());
        return info;
    }
}
