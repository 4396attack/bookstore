package su.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import su.entity.Category;
import su.service.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class categoryServlet
 */
@WebServlet("/admin/categoryServlet")
public class categoryServlet extends BaseServlet {
	private CategoryService cservice=new CategoryService();
	/**
	 * 查看所有分类
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String selectCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			List<Category> list=cservice.selectAll();
			request.setAttribute("list", list);
			return "f:/adminjsps/admin/category/list.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 将要修改的分类信息显示到页面上
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String mod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		try {
			Category category=cservice.selectById(cid);
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/mod.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String doMod(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
			cservice.mod(category);
			request.setAttribute("msg", "修改成功!");
			return "f:/adminjsps/admin/msg.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Category category=CommonUtils.toBean(request.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		try {
			cservice.add(category);
			request.setAttribute("msg", "添加成功!");
			return "f:/adminjsps/admin/msg.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		try {
			Category category=cservice.selectById(cid);
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/del.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public String doDel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		try {
			cservice.del(cid);
			request.setAttribute("msg", "删除成功!");
			return "f:/adminjsps/admin/msg.jsp";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
