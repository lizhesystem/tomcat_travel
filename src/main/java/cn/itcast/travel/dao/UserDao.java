package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    User findUsername(String username);

    void save(User user);

    User findCode(String code);

    void activate(String code);

    User findByuserAdpawd(String username, String password);
}
