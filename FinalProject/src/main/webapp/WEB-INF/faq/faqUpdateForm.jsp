<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../common/common.jsp" %>

faqUpdateForm.jsp<br>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>FAQ 수정화면</title>
    <style type="text/css">
        .err {
            font-size: 9pt;
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<div class="container my-4">
    <h2 class="text-center mb-4">FAQ 수정화면</h2>

    <% String[] faq_category = {"숙소","교통","조회/변경/취소","커뮤니티","채팅","결제","회원","기타"}; %>

    <form:form commandName="faq" method="post" action="update.faq" onsubmit="return confirmUpdate()">
        <input type="hidden" name="faq_id" value="${faq.faq_id}">
        <input type="hidden" name="whatColumn" value="${param.whatColumn}">
        <input type="hidden" name="keyword" value="${param.keyword}">
        <table class="table table-bordered">
            <tr>
                <th>카테고리</th>
                <td>
                    <select name="faq_category" class="form-control">
                        <option value="">카테고리를 선택하세요</option>
                        <c:set var="faq_category" value="<%=faq_category %>"/>
                        <c:forEach var="i" begin="0" end="${fn:length(faq_category)-1 }">
                            <option value="${faq_category[i]}" <c:if test="${faq.faq_category eq faq_category[i] }">selected</c:if>>${faq_category[i]}</option>
                        </c:forEach>
                    </select>
                    <form:errors path="faq_category" cssClass="err"/>
                </td>
            </tr>
            <tr>
                <th>질문</th>
                <td>
                    <input type="text" name="question" value="${faq.question}" size="45" class="form-control">
                    <form:errors path="question" cssClass="err"/>
                </td>
            </tr>
            <tr>
                <th>답변</th>
                <td>
                    <textarea name="answer" rows="10" cols="45" class="form-control">${faq.answer}</textarea>
                    <form:errors path="answer" cssClass="err"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" class="text-center">
                    <button type="submit" class="btn btn-primary">수정</button>
                    <button type="button" class="btn btn-secondary" onClick="goList('${param.whatColumn}', '${param.keyword}')">FAQ 목록</button>
                </td>
            </tr>
        </table>
    </form:form>
</div>

<script type="text/javascript">
    function goList(whatColumn, keyword) {
        location.href = "list.faq?whatColumn=" + whatColumn + "&keyword=" + keyword;
    }
    function confirmUpdate() {
        return confirm("수정하시겠습니까?");
    }
</script>
</body>
</html>