package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/Category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService category = new CategoryServiceImpl();

    public void Categorys(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> list = category.findAll();

        super.writeValue(list, response);

    }


}
