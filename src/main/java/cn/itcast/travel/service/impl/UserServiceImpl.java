package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDapImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    UserDao userDao = new UserDapImpl();

    // 注册发送邮件
    @Override
    public boolean regist(User user) {

        User u = userDao.findUsername(user.getUsername());
        if (u == null) {
            // 用户不存在，设置状态为N，设置唯一UUID
            user.setStatus("N");
            user.setCode(UuidUtil.getUuid());
            // 保存用户
            userDao.save(user);

            String context = "<a href='http://192.168.31.253/travel/activateUserServlet?code=" + user.getCode() + "' >恭喜您注册成功，点击链接进行激活(#^.^#)</a>";
            boolean flag = MailUtils.sendMail(user.getEmail(), context, "注册激活邮箱");
            if (flag) {
                return true;
            } else {
                System.out.print("邮箱发送失败");
            }

            return true;
        } else {
            return false;
        }
    }

    // 获取uuid
    @Override
    public User checkcode(String code) {
        User u = userDao.findCode(code);
        if (u != null) {
            userDao.activate(u.getCode());
        }
        return u;
    }

    // 验证码用户名密码是否正确的dao
    @Override
    public User checkByuser(String username, String password) {
        return userDao.findByuserAdpawd(username,password);
    }

}
