# Board with MVC Pattern

Created: June 9, 2022
Created by: Jieun Kim

# First Commit - 2022/06/08

# Second Commit - 2022/06/09

수정 내용 : 게시판 List에서 각 게시글의 첨부파일 유무를 볼 수 있도록 ListAction.java, List.jsp 수정

<aside>
💡 문제점 !!
DB설계 - 게시글(BOARD) / 첨부파일(BOARD_ATTACH) 별도 설정으로 값을 가져올 대상이 두 군데
값을 jsp로 보낸 후 ?? BOARD / BOARD_ATTACH 두 테이블간 연관 관계를 따져서 fileName을 가져올지 말지 정해야함 (default : NO FILE)

</aside>

```java
ListAction.java

List<BoardAttachDto> attachList = null;
		BoardAttachDao dbBAD = BoardAttachDao.getInstance();
		attachList = dbBAD.getArticles();
```

```java
<c:if test="${attachList.bno != article.num}">
			<td>${attachList.fileName}</td>
		</c:if>
		<c:if test="${attachList.bno != article.num}">
			<td>No File</td>
		</c:if>
```

<aside>
💡 1. List로 가져온 형태를 jsp 에서 뿌리기 위해 c:forEach 문 사용
2. bno - NumberFormatException 발생 : String → Integer로 변환시키기 위해 fmt:formatNumber~~사용
3. 숫자 eq ==  /  ne !=

</aside>

```java
<c:forEach var="attach" items="${attachList}">
			<c:set var="bno" value="${attach.bno}"/>
			<fmt:formatNumber var="bnos" type="number" value="${bno}"/>

			<c:if test="${bnos eq article.num}">
				<td>${attach.fileName}</td>
			</c:if>
			<c:if test="${bnos ne article.num}">
				<td>No File</td>
			</c:if>
		</c:forEach>
```

<aside>
💡 List.jsp에  첨부파일이 NO FILE로만 나온다..
확인 : bnos(attachList.bno) ⇒ 0이 나옴 (in list.jsp)
⇒ ListAction.java에서 attachList 자체가 없는건가?? 
⇒ isEmpty등으로 확인하니까 null도 아님!! (in ListAction.java)

^^ BoardAttachDao에 getArticles() (반환값 Dto List)에 query 값 설정을 제대로 안해둬서 그런 거였다. ^^

</aside>
