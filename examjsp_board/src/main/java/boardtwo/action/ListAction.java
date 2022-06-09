package boardtwo.action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import boardtwo.model.BoardAttachDao;
import boardtwo.model.BoardAttachDto;
import boardtwo.model.BoardDao;
import boardtwo.model.BoardDto;

public class ListAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		String pageNum = req.getParameter("pageNum"); //페이지 번호
		if(pageNum == null) {
			pageNum = "1";
		}
		int pageSize = 5;//한 페이지당 글 5개
		int currentPage = Integer.parseInt(pageNum); // 현재 페이지 = 1페이지
		
		int startRow = (currentPage - 1) * pageSize + 1;
		int endRow = currentPage * pageSize;//한 페이지의 마지막 글번호
		int count = 0;
		int number = 0;
		List<BoardDto> articleList = null;
		BoardDao dbPro = BoardDao.getInstance();
		count = dbPro.getArticleCount(); //전체 글 개수
		if (count > 0) { //현재 글이 잇으면
			articleList = dbPro.getArticles(startRow, endRow); //startRow~endRow까지 게시글을 list로 반환
		} else {
			articleList = Collections.emptyList();
		}
		number = count - (currentPage - 1) * pageSize; //글 목록에 표시할 글 번호
		
		//List에 첨부파일 한 줄 추가하기 위한 작업들..
		List<BoardAttachDto> attachList = null;
		BoardAttachDao dbBAD = BoardAttachDao.getInstance();
		attachList = dbBAD.getArticles();
		//System.out.println(attachList.isEmpty()); //결과 : false (문제 x)
		
		
		//해당 뷰에서 사용할 속성
		req.setAttribute("currentPage", new Integer(currentPage));
		req.setAttribute("startRow", new Integer(startRow));
		req.setAttribute("endRow", new Integer(endRow));
		req.setAttribute("count", new Integer(count));
		req.setAttribute("pageSize", new Integer(pageSize));
		req.setAttribute("number", new Integer(number));
		req.setAttribute("articleList", articleList);
		req.setAttribute("attachList", attachList); //첨부파일.
			
		return "/boardtwo/list.jsp"; 
	}

}
