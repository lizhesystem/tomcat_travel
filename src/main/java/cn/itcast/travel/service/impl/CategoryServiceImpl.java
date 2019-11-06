package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoruDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Category> findAll() {
        CategoruDao dao = new CategoryDaoImpl();

        Jedis jedis = JedisUtil.getJedis();
        // 使用sortedset排序查询,查询cid和cname
        // 获取redis里的数据，数据类型是Tuple带编号有的有序1 xx , 2 xx ,3 xx ..
        Set<Tuple> categorys = jedis.zrangeWithScores("Category", 0, -1);

        // 先建一个空对象Category类的列表
        List<Category> lists = null;

        if (categorys == null || categorys.size() == 0) {
            // 如果为空去数据库获取
            lists = dao.findAllCategory();

            // 使用zadd放入redis库
            for (int i = 0; i < lists.size(); i++) {
                jedis.zadd("Category", lists.get(i).getCid(), lists.get(i).getCname());
            }

        } else {
            // 如果redis里有数据,取出来放到arrayList有序集合里
            lists = new ArrayList<Category>();
            // 循环获取的tuple类的的集合
            for (Tuple keys : categorys) {
                // 创建对象,把cid和cname获取出来，放到对象里。
                Category category = new Category();
                // getScore和getElement可以从redid储存的Tuple的key里获取数据
                category.setCid((int) keys.getScore());
                category.setCname(keys.getElement());
                // 添加到lists集合
                lists.add(category);
            }
        }
        // 返回数据
        return lists;
    }
}
