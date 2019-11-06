package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询出来cid=x条件的总count量，封装到route对象返回，integer类型。。
     *
     * @param cid 所属分类
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
//        String sql = "select count(*) from tab_route where cid = ?";

        String sql = "select count(*) from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);

        // 参数们
        List params = new ArrayList();

        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);
        }

        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ?  ");
            params.add("%" + rname + "%");
        }

        sql = sb.toString();


        return template.queryForObject(sql, Integer.class, params.toArray());
    }

    /**
     * 根据拼接的条件模糊查询出来显示的route数据
     *
     * @param cid      所属分类
     * @param start    从哪开始
     * @param pageSize 多少条数据
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
//        String sql = "select * from tab_route where cid = ? limit ?,?";
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder sb = new StringBuilder(sql);

        // 参数们
        List params = new ArrayList();

        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);
        }

        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }

        sb.append("  limit ? , ? ");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();

        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
    }

    // 根据rid查询当前route对象
    @Override
    public Route findOne(int id) {
        String sql = "select * from tab_route where rid = ? ";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), id);
    }

    // 获取route表里前5count最多的
    @Override
    public List<Route> findHot() {
        String sql = "select * from tab_route order by count desc limit 1,5 ";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
    }

//    @Override
//    public Route findOne(int rid) {
//        String sql = "select * from tab_route where rid = ? ";
//        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
//    }
}
