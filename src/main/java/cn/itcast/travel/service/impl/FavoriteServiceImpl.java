package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoritedao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = favoritedao.isfavorite(rid, uid);
        return favorite != null;
    }

    @Override
    public void addFavorite(String rid, String uid) {
        favoritedao.add(Integer.parseInt(rid), uid);
    }

    @Override
    public PageBean<Route> findMyfavorite(int uid, int currentPage, int pageSize) {
        PageBean<Route> pageBean = new PageBean<Route>();

        // 封装pageBean
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        // 获取总页码
        int totalCount = favoritedao.CountMyFavorite(uid);

        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;

        int start = (currentPage - 1) * pageSize;
        List<Route> routesLists = favoritedao.MyFavorite(uid, start, pageSize);

        pageBean.setTotalPage(totalPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setList(routesLists);

        return pageBean;
    }

    @Override
    public PageBean<Route> findfavoriteRank(int currentPage, int pageSize) {
        PageBean<Route> pageBean = new PageBean<Route>();

        // 封装pageBean
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        // 获取总页码
        int totalCount = favoritedao.CountFavoriteRank();

        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;

        int start = (currentPage - 1) * pageSize;
        List<Route> routesLists = favoritedao.FavoriteRank(start, pageSize);

        pageBean.setTotalPage(totalPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setList(routesLists);

        return pageBean;

    }
}
