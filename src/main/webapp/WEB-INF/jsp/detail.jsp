<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp"%>

</head>
<body>

    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
                <h2>${seckill.name}</h2>
            </div>
            <div class="panel-body">
                <h2 class="text-danger">
                    <%--显示time图标--%>
                    <span class="glyphicon glyphicon-time"></span>
                    <%--展示倒计时--%>
                    <span class="glyphicon" id="seckill-box"></span>
                </h2>
            </div>
        </div>
    </div>

<%--弹出登录框--%>
    <div class="modal fade" id="killPhoneModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone"></span>
                        秒杀电话：
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                                placeholder="填写手机号^o^" class="form-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <%--验证信息--%>
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>
                        Submit
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.bootcss.com/jquery/2.0.0/jquery.min.js"></script>
   <%--<script src="/js/bootstrap.min.js"></script>--%>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
<%--使用cdn 获取公共js http://www.bootcdn.cn/ --%>
    <%--jquery cookie操作插件--%>
    <script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
        <%--倒计时插件--%>
    <script src="https://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
<%--开始编写交互逻辑--%>
    <script src="/resource/script/seckill.js"></script>

    <script type="text/javascript">
        $(function () {
//            使用el表达式传入参数
            seckill.detail.init({
                seckillId:${seckill.seckillId},
                startTime:${seckill.startTime.time}, //拿到毫秒时间
                endTime:${seckill.endTime.time}
            });
        });
    </script>
</body>
</html>

