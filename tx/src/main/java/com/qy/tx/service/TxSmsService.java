package com.qy.tx.service;

public interface TxSmsService {

    Boolean sendSms(String phone, String code);
}
