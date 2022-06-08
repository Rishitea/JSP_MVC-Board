package boardtwo.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import boardtwo.model.ConnUtil;
import boardtwo.model.BoardDto;

//import boardAttach.ConnUtil;

public class BoardDao {
	private static BoardDao instance = null;
	private BoardDao() {}
	public static BoardDao getInstance() {
		if(instance == null) {
			synchronized(BoardDao.class) {
				instance = new BoardDao();
			}
		}
		return instance;
	}

	public int getArticleCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select count(*) from BOARD");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return count;
	}
	
	public List<BoardDto> getArticles(int start, int end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardDto> articleList = null;
		
		try {
			conn = ConnUtil.getConnection();
			String sql = "select * from (select rownum RNUM, NUM, WRITER, EMAIL, SUBJECT, PASS, REGDATE, READCOUNT, REF, STEP, DEPTH, CONTENT, IP from (select * from BOARD order by REF desc, STEP asc)) where RNUM >= ? and RNUM <= ?";
			pstmt = conn.prepareStatement(sql);
			System.out.println(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				articleList = new ArrayList<BoardDto>(5);
				do {
					BoardDto article = new BoardDto();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPass(rs.getString("pass"));
					article.setRegdate(rs.getTimestamp("regdate"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setStep(rs.getInt("step"));
					article.setDepth(rs.getInt("depth"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					articleList.add(article);
				} while (rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return articleList;
	}
	
	public List<BoardDto> getArticles(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardDto> articleList = null;
		try {
			conn=ConnUtil.getConnection();
			pstmt=conn.prepareStatement("select * from BOARD order by NUM desc");
			rs=pstmt.executeQuery();
			if(rs.next()) {
				articleList=new ArrayList<BoardDto>();
				do {
					BoardDto article = new BoardDto();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPass(rs.getString("pass"));
					article.setRegdate(rs.getTimestamp("regdate"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setStep(rs.getInt("step"));
					article.setDepth(rs.getInt("depth"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					articleList.add(article);
				}while(rs.next());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}	
		}
		return articleList;
	}
	
	public void insertArticle(BoardDto article) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = article.getNum(); 
		int ref= article.getRef();
		int step = article.getStep(); 
		int depth = article.getDepth();
		int number = 0; 
		String sql ="";
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select max(num) from BOARD");
			rs = pstmt.executeQuery();
			if(rs.next()) {
				number = rs.getInt(1)+1;
			} else { number = 1;} 
			if(num != 0) { //답글인경우
				sql = "update BOARD set STEP = STEP+1 where REF=? and STEP>?";
				pstmt.close();
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, step);
				pstmt.executeUpdate();
				step = step+1;
				depth = depth+1;
			}else { //새글
				ref=number;
				step=0;
				depth=0;
			}
			sql = "insert into BOARD (NUM, WRITER, EMAIL, SUBJECT, PASS, REGDATE, REF, STEP, DEPTH, CONTENT, IP) values (BOARD_SEQ.nextval,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPass());
			pstmt.setTimestamp(5, article.getRegdate());
			pstmt.setInt(6, ref);
			pstmt.setInt(7, step);
			pstmt.setInt(8, depth);
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getIp());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
	}
	
	public BoardDto getArticle(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDto article = null;
		
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("update BOARD set READCOUNT=READCOUNT+1 where NUM=?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			pstmt.close();
			pstmt = conn.prepareStatement("select * from BOARD where NUM=?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				article = new BoardDto();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPass(rs.getString("pass"));
				article.setRegdate(rs.getTimestamp("regdate"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setStep(rs.getInt("step"));
				article.setDepth(rs.getInt("depth"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return article;
	}
	
	public BoardDto updateGetArticle(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDto article = null;
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select * from BOARD where NUM = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				article = new BoardDto();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPass(rs.getString("pass"));
				article.setRegdate(rs.getTimestamp("regdate"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setStep(rs.getInt("step"));
				article.setDepth(rs.getInt("depth"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return article;
	}
	
	public int updateArticle(BoardDto article) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd ="";
		String sql="";
		int result = -1;
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select pass from BOARD where NUM = ?");
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dbpasswd=rs.getString("pass");
				if(dbpasswd.equals(article.getPass())) {
					sql = "update BOARD set WRITER=?,EMAIL=?,SUBJECT=?,CONTENT=? where NUM=?";
					pstmt.close();
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getContent());
					pstmt.setInt(5, article.getNum());
					pstmt.executeUpdate();
					result = 1;
				} else result = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return result;
	}
	
	public int deleteArticle(int num, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbPass ="";
		int result = -1;
		try {
			
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement("select PASS from BOARD where NUM = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dbPass = rs.getString("pass"); //dbPass=DB쿼리실행결과문(PASS)
				if(dbPass.equals(pass)) {//pass->웹입력값
					pstmt.close();
					pstmt = conn.prepareStatement("delete from BOARD where NUM = ?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();
					result = 1;
				} else result = 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null) try {rs.close(); } catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close(); } catch(SQLException e) {}
			if(conn != null) try {conn.close(); } catch(SQLException e) {}
		}
		return result;
	}
	public int findNum() { //현재 sequence 찾기 - attach_bno로 참조
		int x=0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select BOARD_SEQ.currval from dual";
		try {
			conn = ConnUtil.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				x = rs.getInt(1);
			}
			System.out.println("findNum : "+x);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) try {rs.close();} catch(SQLException e) {}
			if(pstmt!=null) try {pstmt.close();} catch(SQLException e) {}
			if(conn!=null) try {conn.close();} catch(SQLException e) {}	
		}
		
		return x;
	}
		
}
