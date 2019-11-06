package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDapImpl implements UserDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    // 查询username是否存在的方法,如果不存在返回null...
    @Override
    public User findUsername(String username) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return user;
    }

    // 保存用户方法
    @Override
    public void save(User user) {
        String sql = "insert into tab_user (username,password,name,birthday,sex,telephone,email,status,code) values (?,?,?,?,?,?,?,?,?)";
        template.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(), user.getStatus(), user.getCode());
    }

    // 激活邮箱跳转根据code查询用户存在方法
    @Override
    public User findCode(String code) {
//        User user = null;
//        String sql = "select * from tab_user where code = ?";
//        try {
//            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
//        } catch (DataAccessException e) {
//            e.printStackTrace();
//        }
//        return user;
        String sql = "select * from tab_user where code = ?";
        User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        if (user == null) {
            return null;
        } else {
            return user;
        }
    }

    // 更改激活状态
    @Override
    public void activate(String code) {
        String sql = "update tab_user set status='Y' where code = ?";
        template.update(sql, code);
    }

    // 验证用户密码密码
    @Override
    public User findByuserAdpawd(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if(user == null){
            return null;
        }else{
            return user;
        }
    }

}
