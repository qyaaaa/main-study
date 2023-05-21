package com.qy.tx.service;

import org.springframework.web.multipart.MultipartFile;

public interface TxCosService {

    Object upload(MultipartFile file, String path);
}
