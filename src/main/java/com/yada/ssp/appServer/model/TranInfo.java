package com.yada.ssp.appServer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_V_TRANS")
public class TranInfo {

    @Id
    @Column(name = "CHANNEL_TRACE_NO")
    private String traceNo; // 渠道交易流水号
    @Column
    private String merNo; // 商户号
    @Column
    private String termNo; // 终端号
    @Column
    private String tranType; // 交易类型
    @Column
    private Float tranAmt; // 交易金额
    @Column
    private String cardNo; // 卡号
    @Column
    private String tranDate; // 交易日期
    @Column
    private String tranTime; // 交易时间
    @Column
    private String respCode; // 返回码
    @Column
    private String channel; // 交易渠道
    @Column
    private String tranFlag; // 交易标志 0-正常交易 1-已撤销 2-已经确认 3-已经确认 4-已调整 5-退货处理中
    @Column
    private String bankLsNo; // 银行流水号
    @Column
    private String merTraceNo; // 商户交易订单号
    @Column
    private String batchNo; // 批次号
    @Column
    private String rrn; // 参考号
    @Column
    private String debcreFlag; // 借贷记标识 1-加 2-减

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getTermNo() {
        return termNo;
    }

    public void setTermNo(String termNo) {
        this.termNo = termNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public Float getTranAmt() {
        return tranAmt;
    }

    public void setTranAmt(Float tranAmt) {
        this.tranAmt = tranAmt;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTranFlag() {
        return tranFlag;
    }

    public void setTranFlag(String tranFlag) {
        this.tranFlag = tranFlag;
    }

    public String getBankLsNo() {
        return bankLsNo;
    }

    public void setBankLsNo(String bankLsNo) {
        this.bankLsNo = bankLsNo;
    }

    public String getMerTraceNo() {
        return merTraceNo;
    }

    public void setMerTraceNo(String merTraceNo) {
        this.merTraceNo = merTraceNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getDebcreFlag() {
        return debcreFlag;
    }

    public void setDebcreFlag(String debcreFlag) {
        this.debcreFlag = debcreFlag;
    }
}
