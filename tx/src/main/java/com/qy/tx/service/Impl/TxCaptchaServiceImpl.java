package com.qy.tx.service.Impl;

import com.qy.tx.config.CaptchaConfig;
import com.qy.tx.service.TxCaptchaService;
import com.tencentcloudapi.captcha.v20190722.CaptchaClient;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultRequest;
import com.tencentcloudapi.captcha.v20190722.models.DescribeCaptchaResultResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TxCaptchaServiceImpl implements TxCaptchaService {

    @Autowired
    private CaptchaConfig captchaConfig;

    //https://cloud.tencent.com/document/product/1110/36926
    public Boolean validateCaptcha(String ticket, String randStr, String ip) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(captchaConfig.getAccessKey(), captchaConfig.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("captcha.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            CaptchaClient client = new CaptchaClient(cred, "", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DescribeCaptchaResultRequest req = new DescribeCaptchaResultRequest();
            req.setCaptchaAppId(captchaConfig.getCaptchaAppId());
            req.setAppSecretKey(captchaConfig.getAppSecretKey());
            req.setCaptchaType(9L);
            req.setUserIp(ip);
            req.setTicket(ticket);
            req.setRandstr(randStr);

            // 返回的resp是一个DescribeCaptchaResultResponse的实例，与请求对象对应
            DescribeCaptchaResultResponse resp = client.DescribeCaptchaResult(req);
            // 输出json格式的字符串回包
            System.out.println(DescribeCaptchaResultResponse.toJsonString(resp));
            log.info(DescribeCaptchaResultResponse.toJsonString(resp));
            Long evilLevel = resp.getEvilLevel();
            if (evilLevel == 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
            return Boolean.FALSE;
        }
    }
}
