/**
 * Created by lirui on 2017/5/22.
 */
;(function (JQ) {
    'use strict';
    
    var Login = function () {
        this.tab_ = JQ('.tab-navs');
        this.part_ = JQ('.part');
        this.btn_ = JQ('#dialog');
        this.dialog_ = JQ('.dialog');
        this.dialogBG_ = JQ('.dialog-bg');
        this.dialogWP_ = JQ('.dialog-wrapper');
        this.next_ = JQ('.next');
        this.dialogCT_ = JQ('.dialog-wrapper > div');
        this.close_ = JQ('.close-icon');
        this.right_ = JQ('.right-arrow');
        this.init();
    };

    Login.prototype = {
        init:function () {
            this.listen();
        },
        listen:function () {
            var that = this;

            //tab切换
            that.tab_.on('click', 'a', function () {
                var index = JQ(this).index(),
                    active = JQ(this).parent().attr('active-index');

                that.part_.hide().eq(index).show();
                if( index == active ) {
                    return;
                };
                active === '0'
                    ? JQ(this).parent().attr('active-index','1')
                    : JQ(this).parent().attr('active-index','0');
            });

            //打开dialog
            that.btn_.bind('click', function () {

                that.dialogBG_.show().addClass('show');
                that.dialogWP_.addClass('show');
                that.dialog_.addClass('show');
            });

            //向左切换
            that.next_.bind('click', function () {
                var idx = parseInt(JQ(this).attr('idx'));

                that.dialogCT_.eq(idx).addClass('dialog-left');
                that.dialogCT_.eq(idx + 1).removeClass('dialog-right');
            });

            //关闭dialog
            that.close_.bind('click', function () {

                that.dialogBG_.hide().removeClass('show');
                that.dialogWP_.removeClass('show');
                that.dialog_.removeClass('show');
                //清空class
                setTimeout(function () {
                    that.dialogCT_.removeClass('dialog-left').first()
                            .siblings().addClass('dialog-right');
                },500);
            });

            //向右切换
            that.right_.bind('click', function () {
                var idx = parseInt(JQ(this).attr('idx'));

                that.dialogCT_.eq(idx - 1).removeClass('dialog-left');
                that.dialogCT_.eq(idx).addClass('dialog-right');
            });
        }
    };

    JQ(function () {
        var login = new Login();
    });

})($)