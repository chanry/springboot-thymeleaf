/**
 * Created by LR on 2017/6/30.
 */

;(function() {
    'use strict';

    var PageSqzyz = function() {
        this.$Tips = $('.tips');
        this.$From = $('.Js_change');
        this.$Sub = $('.Js_sub');
        this.$DatePicker = $('.Js_datePicker');
        this.$Iframe = $('iframe[name="volunteer"]');
        this.init();
    };

    PageSqzyz.prototype = {
        init: function() {
            this.listen();
        },
        addVolunteer: function() {
            var that = this;
            var data = that.$From.serialize();
            Q($.post("/gallery/setting/addVolunteer", data))
                .then(function(data) {
                    if (data.result) {
                        that.$Tips.hide();
                        Ewin.alert('申请志愿者成功');
                    } else {
                        if (data.msg == '') {
                            that.tipMessage(data.data);
                        } else {
                            Ewin.alert(data.msg);
                        }
                    };
                })['catch'](function(e) {
                    throw new Error(e);
                });
        },
        tipMessage: function(data) {
            var that = this;

            $.each(data, function(i, v) {
                $('input[name="' + v.key + '"]').next('.tips').show().empty().text(v.desc);
                if (v.key === 'endTime') {
                    var text = $('input[name="' + v.key + '"]').next().text();
                    $('input[name="' + v.key + '"]').next('.tips').text(text + ';' + v.desc);
                };
            });
        },
        listen: function() {
            var that = this;

            that.$Sub.on('click', function() {
                that.addVolunteer();
            });

            that.$DatePicker.datetimepicker({
                minView: "month",
                autoclose: true,
                language: 'zh-CN'
            });

            that.$Iframe.load(function() {
                $(this).height($(this).contents().find('html').height())
            });

        }
    };

    $(function() {
        new PageSqzyz();
    });

})();
