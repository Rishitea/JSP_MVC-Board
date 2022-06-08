package boardtwo.action;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import boardtwo.model.BoardAttachDao;
import boardtwo.model.BoardDao;
import boardtwo.model.BoardDto;

public class WriteProAction implements CommandAction { //extends HttpServletRequest 
	
	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		req.setCharacterEncoding("UTF-8");
		
		String savePath = "/boardtwo/upload";
		int uploadFileSizeLimit=5*1024*1024;
		String encType="UTF-8";
		//ServletContext context = getServletContext();
		String prefix = "C:/javaStudy/jspwork/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/examjsp_board";
		String uploadPath = prefix + savePath; //attach - uploadPath
		try {
			MultipartRequest multi = new MultipartRequest(
					req, uploadPath, uploadFileSizeLimit, encType, new DefaultFileRenamePolicy());
			String fileName = multi.getFilesystemName("uploadFile"); //attach - fileName
		
		
		//DB에 게시글 insert..		
		BoardDto article = new BoardDto();
		article.setNum(Integer.parseInt(multi.getParameter("num")));
		article.setWriter(multi.getParameter("writer"));
		article.setEmail(multi.getParameter("email"));
		article.setSubject(multi.getParameter("subject"));
		article.setPass(multi.getParameter("pass"));
		article.setRegdate(new Timestamp(System.currentTimeMillis()));
		article.setRef(Integer.parseInt(multi.getParameter("ref")));
		article.setStep(Integer.parseInt(multi.getParameter("step")));
		article.setDepth(Integer.parseInt(multi.getParameter("depth")));
		article.setContent(multi.getParameter("content"));
		article.setIp("default");
		
		BoardDao dbPro = BoardDao.getInstance();
		dbPro.insertArticle(article);
		
		//DB에 파일업로드 BoardAttacDao - attachFile(uploadPath,fileName,bno)
		int bno = dbPro.findNum(); //attach-bno
		System.out.println(bno);
		System.out.println(fileName);
		if(fileName != null) {
			//Board_Attach 테이블에 insert하는 작업.
			BoardAttachDao dbBAD = BoardAttachDao.getInstance();
			dbBAD.attachFile(uploadPath, fileName, bno);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/boardtwo/writePro.jsp";
	}

}
