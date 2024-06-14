package com.qy.contentChcek.demo;

import com.aliyun.imageaudit20191230.Client;
import com.aliyun.imageaudit20191230.models.ScanTextRequest;
import com.aliyun.imageaudit20191230.models.ScanTextResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.springframework.beans.factory.annotation.Value;

/**
 * 视觉智能文本内容审核
 */
public class GreenTextCheck {

    @Value("aliyun.accessKeyId")
    private static String accessKeyId;

    @Value("aliyun.accessKeySecret")
    private static String accessKeySecret;

    public static com.aliyun.imageaudit20191230.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        /*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "imageaudit.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

    public static void main(String[] args_) throws Exception {
        Client client = GreenTextCheck.createClient(accessKeyId, accessKeySecret);
        ScanTextRequest.ScanTextRequestTasks tasks = new ScanTextRequest.ScanTextRequestTasks()
                .setContent("维修管道，联系weixin,你是傻逼");
        ScanTextRequest.ScanTextRequestTasks tasks2 = new ScanTextRequest.ScanTextRequestTasks()
                .setContent("维修管道，联系weixin,你是?");
        ScanTextRequest.ScanTextRequestLabels label1 = new ScanTextRequest.ScanTextRequestLabels().setLabel("ad");
        ScanTextRequest.ScanTextRequestLabels label2 = new ScanTextRequest.ScanTextRequestLabels().setLabel("abuse");
        ScanTextRequest scanTextRequest = new ScanTextRequest()
            .setLabels(java.util.Arrays.asList(
                label1, label2
            ))
            .setTasks(java.util.Arrays.asList(
                tasks,tasks2
            ));
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            ScanTextResponse response = client.scanTextWithOptions(scanTextRequest, runtime);
        } catch (TeaException error) {
            System.out.println(error);
        }
    }

}

