package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService service = new RouteServiceImpl();
    private FavoriteService frService = new FavoriteServiceImpl();

    /**
     * 封装pagebean数据，包含分页内容和分页数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */


    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.接收参数
        String currentPageStr = request.getParameter("currentPage");   // 当前页
        String cidStr = request.getParameter("cid");                   // 线路cid
        String pageSizeStr = request.getParameter("pageSize");         // 每页多少条

        String rname = request.getParameter("rname");
        if (rname != null && rname.length() > 0) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }

        // 2.处理参数
        int cid = 0;  // 类别ID
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }


        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            // 默认第一次访问为空,那么给默认值，代表第一页。
            currentPage = 1;
        }


        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 每页显示5条
            pageSize = 5;
        }

        // 3.调用servive查询PageBean对象

        PageBean pg = service.pageQuery(currentPage, pageSize, cid, rname);

        // 4.将pageBean对象序列化json返回前台
        super.writeValue(pg, response);

    }

    /**
     * 根据rid封装route对象，返回给详情页展示
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");

        RouteService service = new RouteServiceImpl();
        Route route = service.findOne(rid);

        super.writeValue(route, response);

    }

    /**
     * 判断是否已收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("ObjUser");
        int uid = 0;
        if (user != null) {
            uid = user.getUid();
        } else {
            uid = 0;
        }

        boolean flag = frService.isFavorite(rid, uid);

        super.writeValue(flag, response);

    }


    /**
     * 点击收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rid = request.getParameter("rid");
        String uid = request.getParameter("uid");

        frService.addFavorite(rid, uid);
    }

    /**
     * 查看我的收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void myFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1.获取参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        // 获取session中的user对象
        User user = (User) request.getSession().getAttribute("ObjUser");
        int uid;
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }

        // 2.处理参数
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            // 默认第一次访问为空,那么给默认值，代表第一页。
            currentPage = 1;
        }

        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 每页显示5条
            pageSize = 12;
        }

        // 3.查询数据

        PageBean<Route> pageBean = frService.findMyfavorite(uid, currentPage, pageSize);

        // 4.返回数据

        super.writeValue(pageBean, response);

    }

    /**
     * 排行榜
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void FavoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");

        // 2.处理参数
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            // 默认第一次访问为空,那么给默认值，代表第一页。
            currentPage = 1;
        }

        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            // 每页显示5条
            pageSize = 8;
        }

        // 3.查询数据

        PageBean<Route> routePageBean = frService.findfavoriteRank(currentPage, pageSize);
        super.writeValue(routePageBean, response);

    }

    /**
     * 获取前5的收藏排行,把roote对象返回给前台
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void hot(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Route> hotlists = service.findHot();
        super.writeValue(hotlists, response);
    }
}

