package com.qy.tx.service.Impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.qy.tx.config.CosConfig;
import com.qy.tx.service.TxCosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@Service
@Slf4j
public class TxCosServiceImpl implements TxCosService {

    @Autowired
    CosConfig cosConfig;

    @Override
    public Object upload(MultipartFile file, String path) {
        if (file == null) {
            return new UploadMsg(0, "文件为空", null);
        }
        // 获取原始上传的文件名
        String oldFileName = file.getOriginalFilename();
        // 获取文件类型[后缀]
        String suffix = oldFileName.substring(oldFileName.lastIndexOf("."));
        // 使用UUID生成新的文件名
        String newFileName = UUID.randomUUID() + suffix;
        // 上传文件按日期生成的路径
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(cosConfig.getAccessKey(), cosConfig.getSecretKey());
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getBucket()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = cosConfig.getBucketName();

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {
            localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/" + path + "/" + year + "/" + month + "/" + day + "/" + newFileName;
            // 上传文件的请求对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            // 使用COS客户端上传文件
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            return new UploadMsg(1, "上传成功", path + putObjectRequest.getKey());
        } catch (IOException e) {
            return new UploadMsg(-1, e.getMessage(), null);
        } finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }
    }

    private class UploadMsg {
        public int status;
        public String msg;
        public String path;

        public UploadMsg() {
            super();
        }

        public UploadMsg(int status, String msg, String path) {
            this.status = status;
            this.msg = msg;
            this.path = path;
        }
    }
}
