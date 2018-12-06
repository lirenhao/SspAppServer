package com.yada.ssp.appServer.dao;

import com.yada.ssp.appServer.model.TranInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TranInfoDao extends JpaRepository<TranInfo, String>, CrudRepository<TranInfo, String> {

    List<TranInfo> findByMerNoAndTranDateLike(String merNo, String tranDate);

    List<TranInfo> findByMerNoAndTermNoAndTranDateLike(String merNo, String termNo, String tranDate);
}