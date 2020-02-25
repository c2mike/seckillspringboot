
var seckill = {

    //封装秒杀相关ajax的url
    URL: {
        now: function () {
            return '/seckill/timestamp';
        },
        exposer: function (seckillId,phone) {
            return '/seckill/' + seckillId + '/exposeurl/'+phone;
        },
        execution: function () {
            return '/seckill/commit';
        },
        queryResult:function(seckillId,phone)
        {
            return '/seckill/'+seckillId+'/result/'+phone;
        }
    },

    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;//直接判断对象会看对象是否为空,空就是undefine就是false; isNaN 非数字返回true
        } else {
            return false;
        }
    },

    //详情页秒杀逻辑
    detail: {
        //详情页初始化
        init: function (params) {
            //手机验证和登录,计时交互
            //规划我们的交互流程
            //在cookie中查找手机号
            var userPhone = $.cookie('userPhone');

            //验证手机号
            if (!seckill.validatePhone(userPhone)) {

                //绑定手机 控制输出
                var killPhoneModal = $('#killPhoneModal');

                killPhoneModal.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });

                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log("inputPhone: " + inputPhone);
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie(7天过期)
                        $.cookie('userPhone', inputPhone, {expires: 7, path: '/seckill'});
                        //验证通过　　刷新页面
                        window.location.reload();
                    } else {
                        //todo 错误文案信息抽取到前端字典里
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }

            //已经登录
            //计时交互
            else {

                var startTime = params['startTime'];
                var endTime = params['endTime'];
                var seckillId = params['seckillId'];
                console.log(startTime)
                $.get(seckill.URL.now(), {}, function (result) {
                    if (result) {


                        var nowTime = result['time'];
                        console.log(nowTime);
                        //时间判断 计时交互
                        seckill.countDown(seckillId, nowTime, startTime, endTime);
                    } else {
                        console.log(2);
                        alert('result: ' + result);
                    }
                });
            }
        }
    },

    handlerSeckill: function (seckillId, node) {
        //获取秒杀地址,控制显示器,执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

        $.get(seckill.URL.exposer(seckillId,$.cookie('userPhone')), {}, function (result) {

            if(result&& result['success'])
            {
                var md5 = result['md5'];
                var killUrl = seckill.URL.execution();
                console.log("killUrl:"+killUrl);
                $('#killBtn').one('click',function () {
                    $(this).addClass('disabled');
                    $.post(killUrl,
                        {
                            seckillId:seckillId,
                            md5:md5,
                            phone:$.cookie('userPhone')
                        }
                        ,function (result) {
                        var killResult = result['errorMessage'];
                        node.html('<span class="label label-success">' + killResult+'</span>');
                    })
                });
                node.show();

                var timerId = setInterval(function(){
                $.get(
                    seckill.URL.queryResult(seckillId,$.cookie('userPhone')),
                    {},
                    function (result) {
                        if(result&&result['success'])
                        {
                            node.html('<span class="label label-success">' + '秒杀成功'+'</span>');
                            clearInterval(timerId);
                        }
                        else
                        {
                            node.html('<span class="label label-success">' + result['errorMessage']+'</span>');
                        }

                    }
                );
                },10*1000);

            }
            else
            {
               var now = result['now'];
               var start = result['startTime'];
               var end = result['endTime'];
               seckill.countDown(seckillId, now, start, end);
            }
        });

    },

    countDown: function (seckillId, nowTime, startTime, endTime) {
        console.log(seckillId + '_' + nowTime + '_' + startTime + '_' + endTime);
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            //秒杀结束
            seckillBox.html('秒杀结束!');
        } else if (nowTime < startTime) {
            //秒杀未开始,计时事件绑定
            var killTime = new Date(startTime + 1000);//todo 防止时间偏移
            seckillBox.countdown(killTime, function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒 ');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                //时间完成后回调事件
                //获取秒杀地址,控制现实逻辑,执行秒杀
                console.log('______fininsh.countdown');
                seckill.handlerSeckill(seckillId, seckillBox);
            });
        } else {
            //秒杀开始
            seckill.handlerSeckill(seckillId, seckillBox);
        }
    }

}