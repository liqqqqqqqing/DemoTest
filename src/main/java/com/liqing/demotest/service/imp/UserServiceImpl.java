package com.liqing.demotest.service.imp;

import com.liqing.demotest.entity.User;
import com.liqing.demotest.mapper.UserMapper;
import com.liqing.demotest.service.IUserService;
import com.liqing.demotest.service.ex.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户注册
     * @param user 用户数据
     */
    @Override
    public void reg(User user) {
        // 根据参数user对象获取注册的用户名
        String username = user.getUsername();
        String password = user.getPassword();
        if(username==""||password==""){
            //用户的注册的用户名或密码为空
            throw new UserNullException("用户名、密码不能为空");
        }
        // 调用持久层的User findByUsername(String username)方法，根据用户名查询用户数据
        User result = userMapper.findByUsername(username);
        // 判断查询结果是否不为null
        if(result != null){
            // 是：表示用户名已被占用，则抛出UsernameDuplicateException异常
            throw new UsernameDuplicateException("尝试注册的用户名[" + username + "]已经被占用");
        }

        //创建当前时间对象
        Date now = new Date();
        // 补全数据：加密后的密码（toUpperCase将小写字符转换成大写）
        String salt = UUID.randomUUID().toString().toUpperCase();
        String md5Password = getMd5Password(user.getPassword(), salt);
        user.setPassword(md5Password);
        // 补全数据：盐值
        user.setSalt(salt);
        // 补全数据：isDelete(0)
        user.setIsDelete(0);
        // 补全数据：4项日志属性
        user.setCreatedUser(username);
        user.setCreatedTime(now);
        user.setModifiedUser(username);
        user.setModifiedTime(now);

        Integer insertResult = userMapper.insert(user);
        if (insertResult != 1) {
            // 是:插入数据时出现某种错误，则抛出InsertException异常
            throw new InsertException("添加用户数据出现未知错误，请联系系统管理员"); }

    }
    /**
     * 执行密码加密
     * @param password 原始密码
     * @param salt 盐值
     * @return 加密后的密文
     */
    private String getMd5Password(String password, String salt) {
        /*
         * 加密规则：
         * 1、无视原始密码的强度
         * 2、使用UUID作为盐值，在原始密码的左右两侧拼接
         * 3、循环加密3次
         */
        for (int i = 0; i < 3; i++) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();
        }
        return password;
    }

    /**
     * 铜壶登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public User login(String username, String password) {
        //登录前 查询该用户是否存在
        User result = userMapper.findByUsername(username);
        if (result == null || result.getIsDelete() == 1) {
            // 结果为空 或者 盐值=1（表示该用户删除）   抛出错误
            throw new UserNotFoundException("没有该账户");

        }
        //还原密码 进行对比
        String salt = result.getSalt();
        String md5Password = getMd5Password(password, salt);
        // 判断查询结果中的密码，与以上加密得到的密码是否不一致
        if (!result.getPassword().equals(md5Password)) {
            // 是:抛出PasswordNotMatchException异常
            throw new PasswordNotMatchException("密码验证失败的错误");
        }
        //返回需要展示的用互信息
        User user = new User();
        // 将查询结果中的uid、username、avatar封装到新的user对象中
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }

    /**
     *
     * 修改用户密码
     * @param uid 当前登录的用户id * @param username 用户名
     * @param username
     * @param oldPassword 原密码 * @param newPassword 新密码
     * @param newPassword
     */
    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {

        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        // 检查查询结果是否为null
        if (result == null) {
        // 是:抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在");
        }
        // 检查查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) {
        // 是:抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在");
        }
        // 从查询结果中取出盐值
        String salt = result.getSalt();
        // 将参数oldPassword结合盐值加密，得到oldMd5Password
        String oldMd5Password = getMd5Password(oldPassword, salt);
        // 判断查询结果中的password与oldMd5Password是否不一致
        if (!result.getPassword().contentEquals(oldMd5Password)) {
        // 是:抛出PasswordNotMatchException异常
            throw new PasswordNotMatchException("原密码错误");
        }
        // 将参数newPassword结合盐值加密，得到newMd5Password
        String newMd5Password = getMd5Password(newPassword, salt);
        // 创建当前时间对象
        Date now = new Date();
        // 调用userMapper的updatePasswordByUid()更新密码，并获取返回值
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username,
                now);
        // 判断以上返回的受影响行数是否不为1
        if (rows != 1) {
        // 是:抛出UpdateException异常
        throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员"); }


    }

    /**
     *
     * 修改用户信息时的展示
     * @param uid 当前登录的用户的id * @return 当前登录的用户的信息
     * @return
     */
    @Override
    public User getByUid(Integer uid) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User userMessage = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if(userMessage == null){
            throw new UserNotFoundException("没有该用户信息");
        }
        Integer isDelete = userMessage.getIsDelete();
        if (isDelete == 1){
            throw new UserNotFoundException("该用户不存在");
        }
        //创建新的user对象
        User newUser = new User();
        // 将以上查询结果中的username/phone/email/gender封装到新User对象中
        newUser.setUsername(userMessage.getUsername());
        newUser.setPhone(userMessage.getPhone());
        newUser.setEmail(userMessage.getEmail());
        newUser.setGender(userMessage.getGender());

        return newUser;
    }

    /**
     * 修改用户信息
     * @param uid 当前登录的用户的id
     * @param username 当前登录的用户名 * @param user 用户的新的数据
     * @param user
     */
    @Override
    public void changeInfo(Integer uid, String username, User user) {
        // 调用userMapper的findByUid()方法，根据参数uid查询用户数据
        User result = userMapper.findByUid(uid);
        // 判断查询结果是否为null
        if (result == null) {
        // 是:抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在");
        }
        // 判断查询结果中的isDelete是否为1
        if (result.getIsDelete().equals(1)) {
        // 是:抛出UserNotFoundException异常
            throw new UserNotFoundException("用户数据不存在");
        }
        // 向参数user中补全数据:uid
        user.setUid(uid);
        // 向参数user中补全数据:modifiedUser(username)
        user.setModifiedUser(username);
        // 向参数user中补全数据:modifiedTime(new Date())
        user.setModifiedTime(new Date());
        // 调用userMapper的updateInfoByUid(User user)方法执行修改，并获取返回值
        Integer rows = userMapper.updateInfoByUid(user);
        // 判断以上返回的受影响行数是否不为1
        if (rows != 1) {
            // 是:抛出UpdateException异常
            throw new UpdateException("更新用户数据时出现未知错误，请联系系统管理员"); }
    }

}
