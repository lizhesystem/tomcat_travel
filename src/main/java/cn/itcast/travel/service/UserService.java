package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    boolean regist(User user);


    User checkcode(String code);

    User checkByuser(String username, String password);
}
