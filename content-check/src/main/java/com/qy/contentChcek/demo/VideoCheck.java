package com.qy.contentChcek.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.VideoAsyncScanRequest;
import com.aliyuncs.green.model.v20180509.VideoAsyncScanResultsRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
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
//        asyncscan();
        getResult();
    }

    public static void getResult() throws UnsupportedEncodingException {
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-shanghai",
                accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-shanghai", "Green", "green.cn-beijing.aliyuncs.com");
        // 注意：此处实例化的client尽可能重复使用，提升检测性能。避免重复建立连接。
        IAcsClient client = new DefaultAcsClient(profile);

        VideoAsyncScanResultsRequest videoAsyncScanResultsRequest = new VideoAsyncScanResultsRequest();
        videoAsyncScanResultsRequest.setAcceptFormat(FormatType.JSON);

        List<String> taskList = new ArrayList<String>();
        // 这里添加要查询的taskId。提交任务的时候需要自行保存taskId。
//        taskList.add("vi4xyUwFJ@rRc7NCdUyHXRvd-1A1mpn");
//        taskList.add("vi7NFkAgjfrx@5YpO8LBlLjX-1A1nu$");
        taskList.add("vi3$Ku2SKxbBx58lC6ezYU@O-1A2AcB");

        videoAsyncScanResultsRequest.setHttpContent(JSON.toJSONString(taskList).getBytes("UTF-8"), "UTF-8", FormatType.JSON);

        /**
         * 请务必设置超时时间。
         */
        videoAsyncScanResultsRequest.setConnectTimeout(3000);
        videoAsyncScanResultsRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(videoAsyncScanResultsRequest);
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
                        if (280 == taskCode) {
                            // 检测中。
                            // taskId。
                            System.out.println(((JSONObject) taskResult).getString("taskId"));
                        } else if (200 == taskCode) {
                            // taskId。
                            System.out.println(((JSONObject) taskResult).getString("taskId"));
                            // 检测结果。
                            JSONArray results = ((JSONObject) taskResult).getJSONArray("results");
                            for (Object result : results) {
                                // 视频检测结果的分类。
                                System.out.println(((JSONObject) result).getString("label"));
                                // 置信度分数，取值范围：0（表示置信度最低）~100（表示置信度最高）。
                                System.out.println(((JSONObject) result).getString("rate"));
                                // 视频检测场景，与调用请求中的检测场景一致。
                                System.out.println(((JSONObject) result).getString("scene"));
                                // 建议您执行的后续操作。取值：
                                // pass：结果正常，无需进行其他操作。
                                // review：结果不确定，需要人工审核。
                                // block：结果违规，建议直接删除或者限制公开。
                                System.out.println(((JSONObject) result).getString("suggestion"));
                            }
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

    public static void asyncscan() throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile(
            "cn-shanghai",
            accessKeyId,
            accessKeySecret);
        DefaultProfile.addEndpoint("cn-shanghai", "Green", "green.cn-beijing.aliyuncs.com");
        // 注意：此处实例化的client尽可能重复使用，提升检测性能。避免重复建立连接。
        IAcsClient client = new DefaultAcsClient(profile);

        VideoAsyncScanRequest videoAsyncScanRequest = new VideoAsyncScanRequest();
        videoAsyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定API返回格式。
        videoAsyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法。

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
//        task.put("url", "https://keyin-stage.oss-cn-beijing.aliyuncs.com/video/post/c7c88c78a9ea56f8389049a1773e53955b8f.mp4");
//        task.put("url", "https://keyin-stage.oss-cn-beijing.aliyuncs.com/video/post/3c228f1963d25b93abec4eae20ce575b381c.mp4");
        task.put("url", "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4");
        tasks.add(task);
        /**
         * 设置要检测的场景。计费是依据此处传递的场景计算。
         * 视频默认1秒截取一帧，您可以自行控制截帧频率。收费按照视频的截帧数量以及每一帧的检测场景计算。
         * 举例：1分钟的视频截帧60张，检测色情（对应场景参数porn）和暴恐涉政（对应场景参数terrorism）2个场景，收费按照60张色情+60张暴恐涉政进行计费。
         */
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("porn", "terrorism", "logo", "ad", "live"));
        data.put("tasks", tasks);
//        data.put("callback", "http://www.aliyundoc.com/xxx.json");
//        data.put("seed", "yourPersonalSeed");
        /**
         * 如果检测视频画面的同时需要检测语音是否有风险内容，传递以下参数。
         * 注意语音的计费是按照时长进行，即该视频的时长*语音反垃圾的单价。
         */
        data.put("audioScenes", Arrays.asList("antispam"));

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
