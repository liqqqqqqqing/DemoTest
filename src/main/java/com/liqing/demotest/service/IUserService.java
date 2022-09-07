package com.liqing.demotest.service;

import com.liqing.demotest.entity.User;

public interface IUserService {

    /**
     * 用户注册
     * @param user 用户数据
     */
    void reg(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功的用户数据
     */
    User login(String username, String password);

    /**
     * 修改密码
     * @param uid 当前登录的用户id * @param username 用户名
     * @param oldPassword 原密码 * @param newPassword 新密码 */
     void changePassword(Integer uid, String username, String oldPassword,
                               String newPassword);

    /**
     * 获取当前登录的用户的信息
     * @param uid 当前登录的用户的id * @return 当前登录的用户的信息 */
    User getByUid(Integer uid);


    /**
     * 修改用户资料
     * @param uid 当前登录的用户的id
     * @param username 当前登录的用户名 * @param user 用户的新的数据
     */
    void changeInfo(Integer uid, String username, User user);
}
