package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    // 查询总记录数
    public int findTotalCount(int cid,String rname);

    // 查询cid--类型,start--从哪开始,pageSize--多少条记录
    public List<Route> findByPage(int cid,int start,int pageSize,String rname);

    public Route findOne(int id);

    List<Route> findHot();


}
