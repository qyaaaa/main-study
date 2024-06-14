package com.qy.contentChcek.demo;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.green20220302.Client;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.green20220302.models.TextModerationResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 内容审核增强版
 */
@Getter
@Setter
@Component
public class StrongTextCheck {

    @Value("aliyun.accessKeyId")
    private static String accessKeyId;

    @Value("aliyun.accessKeySecret")
    private static String accessKeySecret;
    //传入文本，返回一个map，包含了文本的审核结果
    public static Map greenTextScanStrongVersion(String content) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(accessKeyId);
        config.setAccessKeySecret(accessKeySecret);
        //接入区域和地址请根据实际情况修改
        config.setRegionId("cn-shanghai");
        config.setEndpoint("green-cip.cn-shanghai.aliyuncs.com");
        //连接时超时时间，单位毫秒（ms）。
        config.setReadTimeout(6000);
        //读取时超时时间，单位毫秒（ms）。
        config.setConnectTimeout(3000);
        // 注意，此处实例化的client请尽可能重复使用，避免重复建立连接，提升检测性能
        Client client = new Client(config);

        // 创建RuntimeObject实例并设置运行参数。
        RuntimeOptions runtime = new RuntimeOptions();
        runtime.readTimeout = 10000;
        runtime.connectTimeout = 10000;

        //检测参数构造
        Map<String,Object> resultMap= new HashMap<>();
        JSONObject serviceParameters = new JSONObject();
        //设置要审核的内容
        serviceParameters.put("content", content);

        if (serviceParameters.get("content") == null || serviceParameters.getString("content").trim().length() == 0) {
            resultMap.put("suggestion", "检测内容为空");
            return resultMap;
        }

        TextModerationRequest textModerationRequest = new TextModerationRequest();
        /*
        文本检测service：内容安全控制台文本增强版规则配置的serviceCode，示例：chat_detection
        */
        textModerationRequest.setService("comment_detection");
        textModerationRequest.setServiceParameters(serviceParameters.toJSONString());
        try {
            // 调用方法获取检测结果。
            TextModerationResponse response = client.textModerationWithOptions(textModerationRequest, runtime);

            // 自动路由。
            if (response != null) {
                // 服务端错误，区域切换到cn-beijing。
                if (500 == response.getStatusCode() || (response.getBody() != null && 500 == (response.getBody().getCode()))) {
                    // 接入区域和地址请根据实际情况修改。
                    config.setRegionId("cn-beijing");
                    config.setEndpoint("green-cip.cn-beijing.aliyuncs.com");
                    client = new Client(config);
                    response = client.textModerationWithOptions(textModerationRequest, runtime);
                }

            }
            // 打印检测结果。
            if (response != null) {
                if (response.getStatusCode() == 200) {
                    TextModerationResponseBody result = response.getBody();
                    Integer code = result.getCode();
                    if (code != null && code == 200) {
                        TextModerationResponseBody.TextModerationResponseBodyData data = result.getData();
                        if(data.getLabels().isEmpty()&& data.getReason().isEmpty()){
                            resultMap.put("suggestion", "pass");
                            return resultMap;
                        }
                        String labels=data.getLabels();
                        String reason=data.getReason();
                        resultMap.put("labels", labels);
                        resultMap.put("reason", reason);
                        resultMap.put("suggestion", "block");
                        return resultMap;

                    } else {
                        resultMap.put("information", "review");
                        return resultMap;
                    }
                } else {
                    resultMap.put("information", "review");
                    return resultMap;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        StrongTextCheck greenTextScan = new StrongTextCheck();
        greenTextScan.greenTextScanStrongVersion("维修管道，联系weixin,你是傻逼");
    }


}