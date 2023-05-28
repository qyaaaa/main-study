package com.qy.tx.service;

public interface TxCaptchaService {

    Boolean validateCaptcha(String ticket, String randStr, String ip);
}
