package com.liqing.demotest.controller;

import com.liqing.demotest.service.ex.*;
import com.liqing.demotest.until.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpSession;

@RestControllerAdvice
public class BaseController {
    /** 操作成功的状态码 */
    public static final int OK = 200;

    /**
     * 从HttpSession对象中获取uid
     * @param session HttpSession对象
     * @return 当前登录的用户的id
     */
    protected final Integer getUidFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("uid").toString());
    }

    /**
     * 从HttpSession对象中获取用户名
     * @param session HttpSession对象
     * @return 当前登录的用户名
     */
    protected final String getUsernameFromSession(HttpSession session) {
        return session.getAttribute("username").toString();
    }

    /** @ExceptionHandler用于统一处理方法抛出的异常 */
    @ExceptionHandler(ServiceException.class)
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<Void>(e);
        if (e instanceof UsernameDuplicateException) {
            result.setState(4000);
            result.setMessage("用户名已经被占用");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("注册失败，请联系系统管理员");
        }else if (e instanceof UserNotFoundException){
            result.setState(4001);
            result.setMessage("用户名或密码错误");
        }else if (e instanceof PasswordNotMatchException){
            result.setState(4002);
            result.setMessage("用户名或密码错误");
        }else if (e instanceof UserNullException){
            result.setState(3999);
            result.setMessage("用户名和密码不能为空");
        }
        return result;
    }

}
