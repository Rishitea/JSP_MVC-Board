package boardtwo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WriteFormAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		int num = 0, ref = 1, step = 0, depth = 0;
		try {
			if(req.getParameter("num") != null) {
				num = Integer.parseInt(req.getParameter("num"));
				ref = Integer.parseInt(req.getParameter("ref"));
				step = Integer.parseInt(req.getParameter("step"));
				depth = Integer.parseInt(req.getParameter("depth"));
			}
		} catch (Exception e) {e.printStackTrace();}
		
		req.setAttribute("num", new Integer(num));
		req.setAttribute("ref", new Integer(ref));
		req.setAttribute("step", new Integer(step));
		req.setAttribute("depth", new Integer(depth));
		return "/boardtwo/writeForm.jsp";
	}

}
