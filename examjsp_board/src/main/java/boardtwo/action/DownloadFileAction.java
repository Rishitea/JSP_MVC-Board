package boardtwo.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import boardtwo.model.BoardDao;

public class DownloadFileAction implements CommandAction{

	@Override
	public String requestPro(HttpServletRequest req, HttpServletResponse resp) throws Throwable {
		
		req.setCharacterEncoding("UTF-8");
		String fileName = req.getParameter("fileName"); //파일명			
		//String savePath = getServletContext().getRealPath("/boardtwo/upload");
		String prefix = "C:/javaStudy/jspwork/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/examjsp_board/boardtwo/upload/";
		String savePath = prefix; //경로
		
		OutputStream os = resp.getOutputStream();
		
		File file = new File(savePath,fileName);
		
		String originFileName = new String(fileName.getBytes(),"iso-8859-1");
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + originFileName + "\"");
		resp.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
		resp.setHeader("Content-Length", ""+file.length());
	
		InputStream is = new FileInputStream(file);
				
		byte buffer[] = new byte[(int)file.length()];
		int leng = 0;
		while( (leng = is.read(buffer)) > 0){
			os.write(buffer,0,leng);
		}
		is.close();
		os.close();		
		
		return "/boardtwo/content.jsp";
	}
}
