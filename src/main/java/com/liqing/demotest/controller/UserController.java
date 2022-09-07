package com.liqing.demotest.controller;


import com.liqing.demotest.entity.User;
import com.liqing.demotest.service.IUserService;
import com.liqing.demotest.service.ex.ServiceException;
import com.liqing.demotest.until.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.liqing.demotest.controller.BaseController.OK;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private BaseController baseController;

    @RequestMapping("reg")
    public JsonResult<Void> reg(User user) {
        // 调用业务对象执行注册
        userService.reg(user);
        // 返回
        JsonResult<Void> result = new JsonResult<>(OK);
        result.setMessage("注册成功");
        return result;
    }

    @RequestMapping("login")
    public JsonResult<User> login(String username, String password, HttpSession session) {
        // 调用业务对象执行注册
        User data = userService.login(username, password);
        //登录成功后，将uid和username存入到HttpSession中
        session.setAttribute("uid", data.getUid());
        session.setAttribute("username", data.getUsername());
        // 将以上返回值和状态码OK封装到响应结果中并返回
        return new JsonResult<User>(OK, data);
    }

    @RequestMapping("change_password")
    public JsonResult<Void> changePassword(String oldPassword, String newPassword, HttpSession session) {
        // 调用session.getAttribute("")获取uid和username
        Integer uid = baseController.getUidFromSession(session);
        String username = baseController.getUsernameFromSession(session);
        // 调用业务对象执行修改密码
        userService.changePassword(uid, username, oldPassword, newPassword);
        return new JsonResult<Void>(OK);
    }

    @GetMapping("get_by_uid")
    public JsonResult<User> getByUid(HttpSession session){

        try {
            Integer uidFromSession = baseController.getUidFromSession(session);
        }catch (NullPointerException e){
            throw new ServiceException("请您重新登录");
        }
        Integer uidFromSession = baseController.getUidFromSession(session);

        User result = userService.getByUid(uidFromSession);

        return new JsonResult<User>(OK, result);
    }

    @RequestMapping("change_info")
    public JsonResult<Void> changeInfo(User user, HttpSession session) {
        Integer uid = null;
        String username = null;
       try {
           // 从HttpSession对象中获取uid和username
           uid = baseController.getUidFromSession(session);
           username =baseController.getUsernameFromSession(session);
       }catch (NullPointerException e){
           throw new ServiceException("请重新登录");
       }
        // 调用业务对象执行修改用户资料
        userService.changeInfo(uid, username, user);
        // 响应成功
        return new JsonResult<Void>(OK);
    }

}