package com.qy.contentChcek.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.VideoAsyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class VideoCheck {

    @Value("aliyun.accessKeyId")
    private static String accessKeyId;

    @Value("aliyun.accessKeySecret")
    private static String accessKeySecret;

    public static void main(String[] args) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(
            "cn-shanghai",
            accessKeyId,
            accessKeySecret);
        DefaultProfile.addEndpoint("cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        // 注意：此处实例化的client尽可能重复使用，提升检测性能。避免重复建立连接。
        IAcsClient client = new DefaultAcsClient(profile);

        VideoAsyncScanRequest videoAsyncScanRequest = new VideoAsyncScanRequest();
        videoAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定API返回格式。
        videoAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法。

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("url", "请填写公网可访问的视频HTTP、HTTPS URL地址");

        tasks.add(task);
        /**
         * 设置要检测的场景。计费是依据此处传递的场景计算。
         * 视频默认1秒截取一帧，您可以自行控制截帧频率。收费按照视频的截帧数量以及每一帧的检测场景计算。
         * 举例：1分钟的视频截帧60张，检测色情（对应场景参数porn）和暴恐涉政（对应场景参数terrorism）2个场景，收费按照60张色情+60张暴恐涉政进行计费。
         */
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("porn", "terrorism"));
        data.put("tasks", tasks);
        data.put("callback", "http://www.aliyundoc.com/xxx.json");
        data.put("seed", "yourPersonalSeed");

        videoAsyncScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 请务必设置超时时间。
         */
        videoAsyncScanRequest.setConnectTimeout(3000);
        videoAsyncScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(videoAsyncScanRequest);

            if (httpResponse.isSuccess()) {
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                System.out.println(JSON.toJSONString(scrResponse, true));
                int requestCode = scrResponse.getIntValue("code");
                // 每一个视频的检测结果。
                JSONArray taskResults = scrResponse.getJSONArray("data");
                if (200 == requestCode) {
                    for (Object taskResult : taskResults) {
                        // 单个视频的处理结果。
                        int taskCode = ((JSONObject) taskResult).getIntValue("code");
                        if (200 == taskCode) {
                            // 保存taskId用于轮询结果。
                            System.out.println(((JSONObject) taskResult).getString("taskId"));
                        } else {
                            // 单个视频处理失败，原因视具体的情况详细分析。
                            System.out.println("task process fail. task response:" + JSON.toJSONString(taskResult));
                        }
                    }
                } else {
                    /**
                     * 表明请求整体处理失败，原因视具体的情况详细分析。
                     */
                    System.out.println("the whole scan request failed. response:" + JSON.toJSONString(scrResponse));
                }
            } else {
                System.out.println("response not success. status:" + httpResponse.getStatus());
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
