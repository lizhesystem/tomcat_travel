# 项目简介
一个支持用户注册登录的旅游信息展示网站。

# 主要功能
- 注册,发邮箱,激活,激活成功后正常登录。
- 网站主要提供【国内游】内的信息,支持分页查询和每个旅游项目的详情 
- 导航栏使用redis缓存数据
- 用户可以查看自己收藏的旅游线路列表以及详情
- 用户可以查看总的收藏排行

注意：
- BaseServlet里定义了一个公共的方法,使用反射机制来处理请求,这样直接定义方法名和请求名一致就能执行方法里的代码,不用再每个方法写doset和doget了。
```java
public class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取真实URI  /travel/user/*
        String uri = req.getRequestURI();
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);

        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void writeValue(Object obj, HttpServletResponse responce) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        responce.setContentType("application/json;charset=utf-8");
        String json = mapper.writeValueAsString(obj);
        responce.getWriter().write(json);
    }

    public String writeString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}

```

详细截图还有表结构查看：https://www.yuque.com/lizhesystem/java/project#osI2b
