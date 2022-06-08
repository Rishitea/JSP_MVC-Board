package boardtwo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardAttachDao {
	
	private static BoardAttachDao instance = null;
	private BoardAttachDao() {}
	public static BoardAttachDao getInstance() {
		if(instance==null) {
			synchronized(BoardAttachDao.class) {
				instance = new BoardAttachDao();
			}
		}
		return instance;
	}
	
	public void attachFile(String uploadPath, String fileName, int bno) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "insert into BOARD_ATTACH (UUID,UPLOADPATH,FILENAME,BNO) values(SEQ_BA_ID.nextval,?,?,?)";
		try {
			conn = ConnUtil.getConnection();
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,uploadPath);
			pstmt.setString(2, fileName);
			pstmt.setInt(3, bno);
			pstmt.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}	
		}
	}

	public String select(int bno) { //bno
		String get="no file";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select fileName from BOARD_ATTACH where bno = ?");
			pstmt.setInt(1, bno);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				get=rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}	
		}
		return get;
	}
	public List<BoardAttachDto> getArticles(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardAttachDto> attachList = null;
		try {
			conn=ConnUtil.getConnection();
			pstmt=conn.prepareStatement("select UUID,UPLOADPATH,FILENAME,BNO from BOARD_ATTACH");
			rs=pstmt.executeQuery();
			if(rs.next()) {
				attachList=new ArrayList<BoardAttachDto>();
				do {
					BoardAttachDto attach = new BoardAttachDto();
					attach.setFileName(rs.getString("fileName"));
					
					attachList.add(attach);
				}while(rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}	
		}
		return attachList;
	}
	
	
}
