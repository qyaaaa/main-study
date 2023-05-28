package com.qy.tx.controller;

import com.qy.tx.service.TxCaptchaService;
import com.qy.tx.service.TxCosService;
import com.qy.tx.service.TxSmsService;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/tx")
public class TxController {

    @Autowired
    private TxCosService txCosService;

    @Autowired
    private TxSmsService txSmsService;

    @Autowired
    private TxCaptchaService txCaptchaService;

    @PostMapping
    @ResponseBody
    public Object upload(@RequestParam(value = "file") MultipartFile file) {
        return txCosService.upload(file, "test");
    }

    @PostMapping("/send")
    public Boolean sendSms(String phone, String code) {
        Boolean result = txSmsService.sendSms("+86" + phone, code);
        return result;
    }

    @PostMapping("/captcha")
    public Boolean validateCaptcha(String ticket, String randStr, String ip) {
        Boolean result = txCaptchaService.validateCaptcha(ticket, randStr, ip);
        return result;
    }
}
