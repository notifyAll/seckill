/**
 * Created by notifyAll on 2017/7/21.
 */
//javascript 模块化
var seckill = {
    //封装秒杀相关 ajax的地址
    URL: {
        //获取系统时间的请求地址 ajax 请求
        now: function () {
            return '/seckill/time/now';
        },
        //生成获取秒杀接口的地址 ajax 请求
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        //生成秒杀地址
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execute';
        }

    },
    //    验证手机号
    validatePhone: function (phone) {
        //长度11 且为数字
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    // 计时交互
    countdown: function (seckillId, nowTime, startTime, endTime) {
        // console.log("seckillId, nowTime, startTime, endTime"+nowTime+" "+startTime+" "+endTime) //TODO
        //计时展示的区域
        var seckillBox = $('#seckill-box');
        // 时间判断验证
        if (nowTime >= endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束！');
        } else if (nowTime < startTime) {
            //    秒杀未开始 计时事件绑定 +1秒 是为了防止时间偏移 我不太懂 大佬这么说了肯定有道理
            var killTime = new Date(startTime + 1000);
            //计时服务 jquery.countdown 的计时插件  事件绑定    <%--倒计时插件--%>
            // <script src="https://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
            seckillBox.countdown(killTime, function (event) {
                //    时间变化时输出
                //    时间格式
                var format = event.strftime('秒杀计时: %D天 %H时 %M分 %S秒');
                console.log(format);
                seckillBox.html(format);
                //    时间完成后的回调事件
            }).on('finish.countdown', function () {
                // 客户端 计时完成  获取秒杀地址 控制显示逻辑 执行秒杀
                seckill.handleSeckillKill(seckillId, seckillBox);
            })
        } else {
            // 可以秒杀
            // console.log('执行到这里 可以秒杀');  //TODO
            //系统时间满足 获取秒杀地址 控制显示逻辑 执行秒杀
            seckill.handleSeckillKill(seckillId, seckillBox);
        }
    },
    //获取秒杀地址 控制显示逻辑 执行秒杀
    handleSeckillKill: function (seckillId, node) {
        //    处理秒杀逻辑
        node.hide() //隐藏节点 完成操作后在吧按钮显示
            .html('<button class="btn bg-primary btn-lg" id="killBtn">开始秒杀</button>'); //暂时按钮
        $.post(seckill.URL.exposer(seckillId), {/*y也不需要参数因为参数在url中了*/}, function (result) {
            //在回调函数里执行交互流程
            if (result && result['success']) {
                var exposer = result['data'];
                //    是否开启秒杀
                if (exposer['exposed']) {
                    //    开始秒杀
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl:' + killUrl); //TODO
                    //click 如果不取消是一直绑定  one就绑定一次 参数click表示这是点击
                    $('#killBtn').one('click', function () {
                        //执行秒杀请求
                        //1 先禁用按钮 可以防止用户多点几下  省的浪费服务器资源
                        $(this).addClass('disabled');
                        //2发送秒杀请求 执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果 这个laber 长得很像按钮
                                node.html("<span class='label label-success'>" + stateInfo + "</span>")
                            }
                        });
                    });
                    //完成这些操作后 吧按钮显示
                    node.show();
                } else {
                    //没开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //再次进行计时操作
                    seckill.countdown(seckillId, now, start, end);
                }
            } else {
                console.log('result:' + result);
            }
        })
    },
    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //    用户手机验证，计时交互
            //    规划交互流程

            //在cookie中 查手机号
            var killPhone = $.cookie('killPhone');

            //    验证手机号
            if (!seckill.validatePhone(killPhone)) {
                // 如果电话没有则绑定它
                // 先取到电话弹出框
                var killPhoneModal = $("#killPhoneModal");
                //控制输出
                killPhoneModal.modal({
                    show: true,//展示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘时间
                });
                //    给按钮绑定事件
                $("#killPhoneBtn").click(function () {
                    //输入电话的文本框
                    var killPhoneKey = $('#killPhoneKey').val();
                    // console.log('killPhoneKey='+killPhoneKey); //TODO
                    if (seckill.validatePhone(killPhoneKey)) {
                        //    电话写入cookie   {expires:7,path:"/seckill"}这个参数代表 有效期7天 只在/seckill有效
                        $.cookie("killPhone", killPhoneKey, {expires: 7, path: "/seckill"});
                        //刷新页面
                        window.location.reload();
                    } else {
                        //显示错误信息
                        $("#killPhoneMessage").hide().html("<laber class='label label-danger'>手机号错误!</laber>").show(300)
                    }
                });
            }

            //    获取传过来的三个参数
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];

            //    已经登录
            //    用户计时
            //    1获取系统当前时间 ajax请求  请求地址 参数 回调方法
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data']; //拿到时间
                    // console.log("result:nowTime"+nowTime) //TODO
                    //    时间判断
                    //     计时交互
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                } else {
                    console.log("result:" + result);
                }
            });
        }
    }
};