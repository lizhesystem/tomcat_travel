package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 去收藏表里查找数据,看是否存在，如果存在封装返回Favorite对象。
     *
     * @param rid route的rid的值
     * @param uid user的uid
     * @return
     */
    @Override
    public Favorite isfavorite(String rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from  tab_favorite  where rid = ? and uid = ? ";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return favorite;
    }

    /***
     * 插入到收藏tab_favorite表。
     * @param id route的rid的值
     * @param uid user的uid
     */
    @Override
    public void add(int id, String uid) {
        // 插入关系表,收藏后把当前的route表里count字段+1,
        String sql = " insert into  tab_favorite values( ?,?,?)";
        String sql2 = " update tab_route set count = count+1 where rid = ? ";
        template.update(sql, id, new Date(), uid);
        template.update(sql2, id);
    }

    /**
     * 根据uid查询已收藏的数据总量
     *
     * @param uid:用户的uid
     * @return
     */
    @Override
    public int CountMyFavorite(int uid) {
        String sql = " select count(*) from tab_favorite t1, tab_route t2 where t1.rid = t2.rid and uid = ?";
        return template.queryForObject(sql, Integer.class, uid);
    }

    /**
     * 分页查询我的收藏数据，关联两张表
     *
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> MyFavorite(int uid, int start, int pageSize) {
        List<Route> list = null;
        try {
            String sql = "select t2.* from tab_favorite t1, tab_route t2 where t1.rid = t2.rid and uid = ? limit ?,?";
            list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 获取tab_route总数，排行榜分页用到
     *
     * @return
     */
    @Override
    public int CountFavoriteRank() {
        String sql = " select count(*) from tab_route ";
        return template.queryForObject(sql, Integer.class);
    }


    /**
     *  倒叙查询排行榜数据分页返回。
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> FavoriteRank(int start, int pageSize) {
        List<Route> list = null;
        try {
            String sql = " select * from tab_route order by count desc LIMIT ?,? ";
            list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), start, pageSize);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return list;
    }
}
