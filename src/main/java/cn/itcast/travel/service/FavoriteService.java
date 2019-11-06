package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface FavoriteService {
    // 判断用户是否收藏
    boolean isFavorite(String rid, int uid);

    // 添加收藏
    void addFavorite(String rid, String uid);


    // 我的收藏
    PageBean<Route> findMyfavorite(int uid, int currentPage, int pageSize);

    // 收藏排行榜
    PageBean<Route> findfavoriteRank(int currentPage, int pageSize);
}
