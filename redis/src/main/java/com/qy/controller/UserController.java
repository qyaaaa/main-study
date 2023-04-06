package com.qy.controller;

import com.qy.entity.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Resource
    private ListOperations<String, Object> listOperations;

    @Resource
    private HashOperations<String, String, Object> hashOperations;

    @PostMapping("/save")
    public void saveUser() {
        User user = new User();
        user.setId(1L);
        user.setContent("test");
        user.setName("qy");
        user.setDesc("2023");
        valueOperations.set(user.getId().toString(), user);
    }

    @GetMapping("/detail")
    public User detail(Long id) {
        User user = (User) valueOperations.get(id.toString());
        return user;
    }

    @PostMapping("/save-list")
    public void save() {
        List<User> users = new ArrayList<>();
        for (long i = 0; i < 5 ; i++) {
            User user = new User();
            user.setId(i);
            user.setContent("test");
            user.setName("qy");
            user.setDesc("2023");
            users.add(user);
        }
        listOperations.leftPush("users", users);
    }

    @GetMapping("/detail-list")
    public List<User> detailList(String search) {
        List<User>  users = (List<User>) listOperations.leftPop(search);
        return users;
    }

    @PostMapping("/save-hash")
    public void saveHash() {
        Map<String, User> map = new HashMap<>();
        for (long i = 0; i < 2 ; i++) {
            User user = new User();
            user.setId(i);
            user.setContent("test");
            user.setName("qy");
            user.setDesc("2023");
            map.put(String.valueOf(i), user);
        }
        for (String key : map.keySet()) {
            User user = map.get(key);
            hashOperations.put("users", key, user);
        }
    }

    @GetMapping("/detail-hash")
    public Map<String, Object> detailHash(String search) {
        Map<String, Object> users = hashOperations.entries(search);
        return users;
    }
}
