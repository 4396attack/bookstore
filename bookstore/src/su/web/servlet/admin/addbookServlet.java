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
 * ���ͼ��
 * ��Ϊ�漰ͼƬ���ϴ���ֻ֧��post����
 * ����ʹ��httpservlet
 * @author �����
 *
 */
public class addbookServlet extends HttpServlet {
	private CategoryService cservice=new CategoryService();
	private BookService bservice=new BookService();
	/**
	 * ���������е�ͼ����Ϣ��
	 * ��ͼ���ͼƬ���浽��Ŀָ���ļ�����
	 * ����bookservice��add��������ͼ�鴢�浽���ݿ���
	 * ��ʾ�µ�ͼ���б�
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		 * ������Ӧ������룬��ֹ����
		 */
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charsert=utf-8");
		DiskFileItemFactory dsf=new DiskFileItemFactory();
		ServletFileUpload sf=new ServletFileUpload(dsf);
		/**
		 * �õ����б���
		 * ѭ���������ж��Ƿ�Ϊ�ļ�����
		 * ������ͨ���ȡ��ֵ
		 * �����ļ������ͼ��ķ��棬���浽ָ��·�����õ�����·����
		 * ��ͼ����Ϣ��װ��book������
		 * ����bookservice��add��������Ϣ�������ݿ�
		 * �ص���list.jsp��ʾ���µĶ�ͼ���б�
		 */
		try {
			Book book=new Book();
			List<FileItem> flist=sf.parseRequest(request);
			Map<String,String> map=new HashMap<String,String>();
			for(FileItem item:flist){
				if(item.isFormField()){//��Ϊ��ͨ����
					map.put(item.getFieldName(), item.getString("utf-8"));
				}else{//��Ϊ�ļ�����
					
					String filename=item.getName();
					
					/**
					 * �ϴ����ļ����Ǿ���·���������жϣ�֧ȡ�ļ�������
					 * ����UUID�����ļ����ظ���ͻ��ʽΪUUID_�ļ���
					 */
					int index=filename.lastIndexOf("\\");
					if(index!=-1){//�����������·��
						filename=filename.substring(index);//��ͼ�ļ�������
						filename=CommonUtils.uuid()+"_"+filename;//�����ļ�����ʽ
					}
					/**
					 * �ж��ļ��ĺ�׺����ֻ֧��jpg��ʽ
					 */
					if(!filename.toLowerCase().endsWith("jpg")){//���ļ�������jpg��ʽ ��ʾ������Ϣ��add.jsp
						request.setAttribute("map", map);
						request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
						return ;
					}
					String path=this.getServletContext().getRealPath("/book_img");//�õ���Ŀ�д��ͼ����Ƭ�ľ���·����
					File file=new File(path,filename);//��ָ��·���´����ļ��������ڱ���ͼƬ
					try {
						item.write(file);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					/**
					 * ��book��image����ֵ����map��
					 */
					String image="book_img/"+filename;
					map.put("image", image);
				}
				
			}
			/**
			 * ����Ϣ��װ��book������
			 */
			book=CommonUtils.toBean(map, Book.class);
			book.setBid(CommonUtils.uuid());
			try {
				Category category=cservice.selectById(map.get("cid"));
				book.setCategory(category);
				System.out.println("---------------------------------------------------------------");
				System.out.println(book);
				bservice.add(book);//���book��Ϣ�����ݿ�
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
