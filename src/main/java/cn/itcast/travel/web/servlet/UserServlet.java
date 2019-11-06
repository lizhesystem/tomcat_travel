package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    private UserService server = new UserServiceImpl();

    // 注册
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置接收的字符编码
        request.setCharacterEncoding("utf-8");
        // 获取验证码
        String check = request.getParameter("check");
        // 获取服务器生成的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute(checkcode_server);
        // 判断验证码
        if (check.equalsIgnoreCase(checkcode_server)) {
            // 封装User对象
            User user = new User();
            Map<String, String[]> map = request.getParameterMap();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            // 调用regist方法验证用户是否注册成功
            server = new UserServiceImpl();
            boolean flag = server.regist(user);
            ResultInfo info = new ResultInfo();
            if (flag) {
                info.setFlag(true);
            } else {
                info.setFlag(false);
                info.setErrorMsg("注册失败");
            }
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);

            // 验证码错误
        } else {
            // 封装序列化对象,里面封装了错误信息
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误");

            super.writeValue(info, response);
//            ObjectMapper mapper = new ObjectMapper();
//            String json = mapper.writeValueAsString(info);
//            // 转出json格式，设置编码，发给客户端
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write(json);
        }
    }

    // 登录
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        String code = request.getParameter("check");

        // 获取验证码.获取后删掉
        HttpSession session = request.getSession();
        String CHECKCODE_SERVER = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute(CHECKCODE_SERVER);
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();

        // 验证验证码
        if (code.equalsIgnoreCase(CHECKCODE_SERVER)) {

            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            server = new UserServiceImpl();
            User u = server.checkByuser(user.getUsername(), user.getPassword());

            if (u != null && "Y".equals(u.getStatus())) {
                //验证成功
                session.setAttribute("ObjUser", u);
                info.setFlag(true);
            }

            if (u != null && !"Y".equals(u.getStatus())) {
                // 未激活
                info.setFlag(false);
                info.setErrorMsg("尚未激活,请去邮箱激活");
            }

            if (u == null) {
                // 用户不存在
                info.setFlag(false);
                info.setErrorMsg("用户名密码错误!");
            }

            // 发送错误信息到前端
            String json = mapper.writeValueAsString(info);
            response.getWriter().write(json);


        } else {
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");
            // 返回到前端
            String errJson = mapper.writeValueAsString(info);
            response.getWriter().write(errJson);
        }
    }

    // 激活
    public void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        String code = request.getParameter("code");
        if (code != null) {
            server = new UserServiceImpl();
            User u = server.checkcode(code);
            if (u != null) {
                String text = "恭喜您激活成功,欢迎<a href='login.html'>登录</a>";
                response.getWriter().write(text);
            } else {
                response.getWriter().write("激活失败,请联系管理员");
            }
        }
    }

    // 退出登录按钮
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 清除session
        request.getSession().invalidate();
        // 跳转到login页面
//        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    // 查找用户，显示出来
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json;charset=utf-8");
        // 获取激活成功session存的对象
        User user = (User) request.getSession().getAttribute("ObjUser");

        String json = super.writeString(user);

        response.getWriter().write(json);


    }
}
