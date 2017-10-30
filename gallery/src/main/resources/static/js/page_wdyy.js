/**
 * Created by LR on 2017/6/23.
 */

;(function () {
    'use strict';

    var PageWdsc = function () {
         this.$YuYue = $('.Js_yuYue');
         this.$MyYuYue = $('.Js_myYuYue');
         this.$Modal = $('.Js_modal');
         this.$YyBtn = $('.Js_yyBtn');
         this.$Appts = '';
         this.$DateStr = '';
         this.realName_ = $('input[name="realname"]');
         this.tel_ = $('input[name="Tel"]');
         this.mobile_ = $('input[name="mobile"]');
         this.ticketNum_ = $('select[name="ticketNum"]');
         this.init();
    };

    PageWdsc.prototype = {
        init: function () {
            this.initTpl();
            this.doAjax();
            this.listen();
        },
        initTpl: function () {
            this.yuYueTpl = _.template(
                '<tr>' +
                    '<td><%= KEY %></td>' +
                    '<td><%= APPTDATE %></td>' +
                    '<td><%= TICKETNUM %></td>' +
                    '<td>' +
                        '<a href="javascript:;" data-appts="<%= APPTDATE %>" data-dateStr="<%= DATESTR %>" data-docId="<%= DOCID %>"><%= STATE %></a>' +
                    '</td>' +
                '</tr>'
            );

        },
        doAjax: function () {
            var that = this;

            Q.all([
                $.getJSON('/gallery/appt/getAppts'),
                $.getJSON('/gallery/appt/getMyAppts')
            ]).then(function (data) {
                that.built(data);
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        built: function (data) {
            var that = this,
                _yy = data[0].data,
                _myYy = data[1].data;

            //预约列表
            that.$YuYue.empty();
            $.each(_yy, function (key, value) {
                that.$YuYue.append(that.yuYueTpl({
                    KEY: key + 1,
                    APPTDATE: value.apptDate,
                    TICKETNUM: value.ticketNum === 0
                                    ? value.status
                                    : '已经预约' + (2000 - value.ticketNum) + '张,可预约' + value.ticketNum + '张',
                    STATE: value.ticketNum === 0 ? '不能预约' : '预约',
                    DATESTR: value.dateStr,
                    DOCID: ''
                }));
            });
            //我的预约
            that.$MyYuYue.empty();
            $.each(_myYy, function (key, value) {
                that.$MyYuYue.append(that.yuYueTpl({
                    KEY: key + 1,
                    APPTDATE: value.apptDate,
                    TICKETNUM: '预约票数' + value.ticketNum + '张',
                    STATE: '取消预约',
                    DATESTR: '',
                    DOCID: value.docId
                }));
            });
        },
        listen: function () {
            var that = this;

            //预约
            that.$YuYue.on('click', 'a', function (e) {
                e.stopPropagation();

                that.$Appts = $(this).attr('data-appts');
                that.$DateStr = $(this).attr('data-dateStr');
                that.$Modal.modal('show');
            });

            that.$YyBtn.on('click', function (e) {
                e.stopPropagation();

                Q($.post('/gallery/appt/add',{
                     realname: _.trim(that.realName_.val()),
                     IDCardNum: _.trim(that.tel_.val()),
                     mobile: _.trim(that.mobile_.val()),
                     ticketNum: _.trim(that.ticketNum_.val()),
                     apptDate: _.trim(that.$Appts),
                     dateStr: _.trim(that.$DateStr)
                }))
                .then(function (data) {
                    if (data.result) {
                        Ewin.alert("预约成功").on(function () {
                            that.$Modal.modal('hide');
                            window.location.reload();
                        });
                    } else {
                        Ewin.alert(data.msg);
                    };
                })['catch'](function (e) {
                    throw new Error(e);
                });
            });
            //取消预约
            that.$MyYuYue.on('click', 'a', function (e) {
                e.stopPropagation();

                Q($.post('/gallery/appt/delete', {
                    docId: $(this).attr('data-docId')
                }))
                .then(function (data) {
                    if (data.result) {
                        Ewin.alert("取消预约成功").on(function () {
                            window.location.reload();
                        });
                    } else {
                        Ewin.alert(data.msg);
                    };
                })['catch'](function (e) {
                    throw new Error(e);
                });
            });
        }
    };

    $(function () {
        new PageWdsc();
    })

})()
