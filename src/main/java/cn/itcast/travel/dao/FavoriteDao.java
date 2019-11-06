package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteDao {

    Favorite isfavorite(String rid, int uid);

    void add(int id, String uid);


    int CountMyFavorite(int uid);

    List<Route> MyFavorite(int uid, int start, int pageSize);

    int CountFavoriteRank();

    List<Route> FavoriteRank(int start, int pageSize);
}
