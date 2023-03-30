package com.qy.springsecurity.mapper;

import com.qy.springsecurity.entity.User;

public interface UserMapper {

    User findByUsername(String username);
}
