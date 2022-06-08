package boardtwo.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import boardtwo.model.BoardAttachDao;
import boardtwo.model.BoardAttachDto;
import boardtwo.model.BoardDao;
import boardtwo.model.BoardDto;

public class ContentAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		int num = Integer.parseInt(req.getParameter("num")); //해당 글번호
		String pageNum = req.getParameter("pageNum"); //해당페이지 번호
		BoardDao dbPro = BoardDao.getInstance();
		
		BoardDto article = dbPro.getArticle(num);
		
		BoardAttachDao dbBAD = BoardAttachDao.getInstance();
				
		String fileName="";
						
		if (dbBAD.select(num) != "") {
			fileName=dbBAD.select(num);
		} else {
			fileName="no file";
		}
	
		System.out.println(fileName);
		req.setAttribute("num", new Integer(num));
		req.setAttribute("pageNum", new Integer(pageNum));
		req.setAttribute("article", article);
		req.setAttribute("fileName", fileName);
		
		return "/boardtwo/content.jsp";
	}

}
