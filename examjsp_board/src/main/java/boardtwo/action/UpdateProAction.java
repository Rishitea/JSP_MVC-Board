package boardtwo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import boardtwo.model.BoardDao;
import boardtwo.model.BoardDto;

public class UpdateProAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		req.setCharacterEncoding("UTF-8");
		String pageNum = req.getParameter("pageNum");
		
		BoardDto article = new BoardDto();
		article.setNum(Integer.parseInt(req.getParameter("num")));
		article.setWriter(req.getParameter("writer"));
		article.setEmail(req.getParameter("email"));
		article.setSubject(req.getParameter("subject"));
		article.setPass(req.getParameter("pass"));
		article.setContent(req.getParameter("content"));
		
		BoardDao dbPro = BoardDao.getInstance();
		int check = dbPro.updateArticle(article);
		
		req.setAttribute("pageNum", new Integer(pageNum));
		req.setAttribute("check", new Integer(check));
		return "/boardtwo/updatePro.jsp";
	}

}
