package com.liqing.demotest.mapper;

import com.liqing.demotest.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;


@Mapper
public interface UserMapper {

    Integer insert(User user);

    User findByUsername(String username);

    Integer updatePasswordByUid(Integer uid, String password, String
            modifiedUser, Date modifiedTime);

    User findByUid(Integer uid);

    Integer updateInfoByUid(User user);

}
