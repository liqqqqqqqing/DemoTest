package com.liqing.demotest.service;

import com.liqing.demotest.entity.User;
import com.liqing.demotest.mapper.UserMapper;
import com.liqing.demotest.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private IUserService userService;


    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("LIQING");
            user.setPassword("123456");
            user.setGender(1);
            user.setPhone("17858802974");
            user.setEmail("lower@tedu.cn");
            user.setAvatar("xxxx");
            userService.reg(user);
            System.out.println("注册成功!");
        } catch (ServiceException e) {
            System.out.println("注册失败!" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void  login(){

        try{
            String username = "liqing";
            String passWorld = "123456";
            User login = userService.login(username, passWorld);
            String s = login.getUid().toString();
            System.out.println("用户的id："+s);


        }catch (Exception e)
        {
            System.out.println("用户名或密码不存在!" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void changePassword(){

        try{
            Integer uid = 5;
            String username = "lower";
            String oldPassword = "123456";
            String newPassword = "888888";
            userService.changePassword(uid, username, oldPassword, newPassword);
            System.out.println("密码修改成功!");
        }catch (ServiceException e){
            System.out.println("密码修改失败!" + e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
    }
    @Test
   public void getById(){

        try {
            Integer uid = 20;
            User user = userService.getByUid(uid);
            System.out.println(user);
        }catch (ServiceException e){
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
   }

   @Test
   public void changeInfo(){

        try{
            Integer uid = 20;
            String username = "数据管理员";
            User user = new User();
            user.setPhone("15512328888");
            user.setEmail("admin03@cy.cn");
            user.setGender(2);
            userService.changeInfo(uid, username, user);
            System.out.println("OK.");
        }catch (ServiceException e){
            System.out.println(e.getClass().getSimpleName());
            System.out.println(e.getMessage());
        }
   }

}

