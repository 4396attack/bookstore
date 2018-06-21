package su.web.servlet.admin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import su.entity.Book;
import su.entity.Category;
import su.service.BookService;
import su.service.CategoryService;

/**
 * Servlet implementation class addbookServlet
 */
@WebServlet("/admin/addbookServlet")
/**
 * 添加图书
 * 因为涉及图片的上传，只支持post方法
 * 所以使用httpservlet
 * @author 杨玉杰
 *
 */
public class addbookServlet extends HttpServlet {
	private CategoryService cservice=new CategoryService();
	private BookService bservice=new BookService();
	/**
	 * 接收请求中的图书信息；
	 * 将图书的图片保存到项目指定文件夹中
	 * 调用bookservice的add方法，将图书储存到数据库中
	 * 显示新的图书列表
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * 设置相应请求编码，防止乱码
		 */
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charsert=utf-8");
		DiskFileItemFactory dsf=new DiskFileItemFactory();
		ServletFileUpload sf=new ServletFileUpload(dsf);
		/**
		 * 得到所有表单项
		 * 循环遍历，判断是否为文件表单项
		 * 若是普通表单项，取出值
		 * 若是文件表单项，即图书的封面，保存到指定路径并得到绝对路径；
		 * 将图书信息封装到book对象中
		 * 调用bookservice的add方法将信息存入数据库
		 * 回调到list.jsp显示最新的额图书列表
		 */
		try {
			Book book=new Book();
			List<FileItem> flist=sf.parseRequest(request);
			Map<String,String> map=new HashMap<String,String>();
			for(FileItem item:flist){
				if(item.isFormField()){//若为普通表单项
					map.put(item.getFieldName(), item.getString("utf-8"));
				}else{//若为文件表单项
					
					String filename=item.getName();
					
					/**
					 * 上传的文件名是绝对路径，所以判断，支取文件名部分
					 * 加上UUID避免文件名重复冲突格式为UUID_文件名
					 */
					int index=filename.lastIndexOf("\\");
					if(index!=-1){//如果包含绝对路径
						filename=filename.substring(index);//截图文件名部分
						filename=CommonUtils.uuid()+"_"+filename;//更改文件名格式
					}
					/**
					 * 判断文件的后缀名，只支持jpg格式
					 */
					if(!filename.toLowerCase().endsWith("jpg")){//若文件名不是jpg格式 显示错误信息到add.jsp
						request.setAttribute("map", map);
						request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
						return ;
					}
					String path=this.getServletContext().getRealPath("/book_img");//得到项目中存放图书照片的绝对路径；
					File file=new File(path,filename);//在指定路径下创建文件对象，用于保存图片
					try {
						item.write(file);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					/**
					 * 将book的image属性值存入map中
					 */
					String image="book_img/"+filename;
					map.put("image", image);
				}
				
			}
			/**
			 * 将信息分装到book对象中
			 */
			book=CommonUtils.toBean(map, Book.class);
			book.setBid(CommonUtils.uuid());
			try {
				Category category=cservice.selectById(map.get("cid"));
				book.setCategory(category);
				System.out.println("---------------------------------------------------------------");
				System.out.println(book);
				bservice.add(book);//添加book信息到数据库
				request.getRequestDispatcher("/admin/showbookServlet?method=show").forward(request, response);
				return ;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
			
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		}
	}

}
