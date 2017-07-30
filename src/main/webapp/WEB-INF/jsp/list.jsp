<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--引入jstl--%>
<%@include file="common/tag.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>秒杀列表页</title>
    <%@include file="common/head.jsp" %>

</head>
<body>
<%--页面显示部分--%>
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>秒杀列表</h2>
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th>名称</th>
                        <th>库存</th>
                        <th>开始时间</th>
                        <th>结束时间</th>
                        <th>创建时间</th>
                        <th>详情页</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="sk" items="${list}">
                        <tr>
                            <td>${sk.name}</td>
                            <td>${sk.number}</td>
                            <td>
                                <fmt:formatDate value="${sk.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${sk.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td>
                                <%--target="_blank 弹出新窗口--%>
                                <a href="/seckill/${sk.seckillId}/detail" class="btn btn-info" target="_blank">Link</a>
                            </td>
                        </tr>
                    </c:forEach>

                    </tbody>
                </table>
            </div>

        </div>
    </div>
</div>

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<%--<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>--%>
<script src="/js/jquery-1.9.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="/js/bootstrap.min.js"></script>
</body>
</html>
