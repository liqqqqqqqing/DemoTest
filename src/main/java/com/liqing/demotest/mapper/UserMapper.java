package com.liqing.demotest.mapper;

import com.liqing.demotest.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


@Mapper
public interface UserMapper {

    Integer insert(User user);

    User findByUsername(String username);

    Integer updatePasswordByUid(Integer uid, String password, String
            modifiedUser, Date modifiedTime);

    User findByUid(Integer uid);

    Integer updateInfoByUid(User user);

    /**
     * 根据uid更新用户的头像
     * @param uid 用户的id
     * @param avatar 新头像的路径
     * @param modifiedUser 修改执行人
     * @param modifiedTime 修改时间
     * @return 受影响的行数
     */
    Integer updateAvatarByUid(
            Integer uid,
            String avatar,
            String modifiedUser,
            Date modifiedTime);

}
