package com.yada.ssp.appServer.dao;

import com.yada.ssp.appServer.model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RefundDao extends JpaRepository<Refund, String>, CrudRepository<Refund, String> {
}