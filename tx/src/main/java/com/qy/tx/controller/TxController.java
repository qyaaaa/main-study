package com.qy.tx.controller;

import com.qy.tx.service.TxCosService;
import com.qy.tx.service.TxSmsService;
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
}
