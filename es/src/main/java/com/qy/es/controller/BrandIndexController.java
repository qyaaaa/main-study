package com.qy.es.controller;

import cn.easyes.core.conditions.LambdaEsQueryWrapper;
import com.qy.es.mapper.BrandMapper;
import com.qy.es.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandIndexController {

    @Autowired
    private BrandMapper brandMapper;

    @GetMapping("/search")
    public List<Brand> search(String index, String name) {
        // 查询出所有标题为老汉的文档列表
        LambdaEsQueryWrapper<Brand> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.index(index);
        wrapper.like(Brand::getName, name);
        return brandMapper.selectList(wrapper);
    }
}
