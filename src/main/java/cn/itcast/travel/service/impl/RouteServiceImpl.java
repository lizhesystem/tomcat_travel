package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    /**
     * 封装pageBean并且返回
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     * @param cid         类型
     * @return 返回根据页码封装好的数据
     */
    RouteDao routeDao = new RouteDaoImpl();
    SellerDao sellerDao = new SellerDaoImpl();
    RouteImgDao routeImgDao = new RouteImgDaoImpl();

    @Override
    public PageBean pageQuery(int currentPage, int pageSize, int cid, String rname) {
        // 封装pageBean对象,
        PageBean<Route> pageBean = new PageBean<Route>();


        // 设置当前页码,和每页显示条数
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        // 设置总数据量
        int totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.setTotalCount(totalCount);

        // 当前页-1*每页数据= 查询数据从哪开始（start）
        int start = (currentPage - 1) * pageSize;
        // 总list数据集合
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname);
        pageBean.setList(list);

        // 总共需要多少页（总页码）
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
        pageBean.setTotalPage(totalPage);

        return pageBean;
    }

    /**
     * 根据rid获取route对象，获取发布信息者和img缩略图set到route
     * @param rid：route表里的id
     * @return
     */
    @Override
    public Route findOne(String rid) {

        // 根据唯一的rid获取route对象,里面包含所有信息
        Route route = routeDao.findOne(Integer.parseInt(rid));

        // 获取route里的sid信息，就是那个sname 电话信息，设置到route里
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);


        // 获取详情页图片信息，tab_route_img表里关联route表里的rid。
        List<RouteImg> imgs = routeImgDao.findImg(route.getRid());
        route.setRouteImgList(imgs);

        return route;

    }

    @Override
    public List<Route> findHot() {
        return routeDao.findHot();
    }

}