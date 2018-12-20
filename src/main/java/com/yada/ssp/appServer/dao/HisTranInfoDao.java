package com.yada.ssp.appServer.dao;

import com.yada.ssp.appServer.model.HisTranInfo;
import com.yada.ssp.appServer.model.TranInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HisTranInfoDao extends JpaRepository<HisTranInfo, String>, CrudRepository<HisTranInfo, String> {

    @Query("SELECT t FROM HisTranInfo t WHERE t.merNo = ?1 AND t.tranDate  = ?2 AND t.respCode = '00' AND NOT t.tranFlag = '1' ORDER BY t.tranTime DESC ")
    List<HisTranInfo> findByMerNo(String merNo, String tranDate);

    @Query("SELECT t FROM HisTranInfo t WHERE t.merNo = ?1 AND t.termNo = ?2 AND t.tranDate  = ?3 AND t.respCode = '00' AND NOT t.tranFlag = '1' ORDER BY t.tranTime DESC ")
    List<HisTranInfo> findByMerNoAndTermNo(String merNo, String termNo, String tranDate);
}