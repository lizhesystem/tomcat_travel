package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteService {
    // 获取route_list.html里的分页对象，带rname搜索
    PageBean pageQuery(int currentPage, int pageSize, int cid,String rname);

    // 根据rid获取route对象里的详细信息
    Route findOne(String rid);

    // 获取前5的count最多的route对象
    List<Route> findHot();
}
