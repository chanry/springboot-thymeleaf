/**
 * Created by LR on 2017/6/27.
 */

;(function () {
    'use strict';

    var PageDingZhi = function () {
        this.$ChannelList = $('.Js_channelList');
        this.init();
    };

    PageDingZhi.prototype = {
        init: function () {
            this.initTpl();
            this.initAjax();
            this.listen();
        },
        initTpl: function () {
            this.channelTpl = _.template(
                '<div class="dy">' +
                    '<img src="<%= IMGURL %>">' +
                    '<input type="checkbox" class="xuanze" name="dy" <%= HASCHECK %> data-channelId="<%= CHANNELID %>">' +
                    '<div class="dycg">定制成功</div>' +
                    '<p><%= CHANNELNAME %></p>' +
                '</div>'
            );
        },
        initAjax: function () {
            var that = this;

            Q($.getJSON('/gallery/layout/getLayouts'))
                .then(function (data) {
                    if (data.result) {
                        that.built(data.data);
                    } else {
                        Ewin.alert(data.msg);
                    };
                })['catch'](function (e) {
                    throw new Error(e);
                });
        },
        built: function (data) {
            var that = this;

            that.$ChannelList.empty();
            $.each(data, function (key, value) {
                that.$ChannelList.append(that.channelTpl({
                    IMGURL: value.iconUrl,
                    CHANNELNAME: value.channelName,
                    HASCHECK: value.hasCheck ? 'checked' : null,
                    CHANNELID: value.channelId
                }));
            });
        },
        _promise : function (url, id) {
            return Q(
                $.post(url, {
                    channelId: id
                })
            )
        },
        animate_: function (dom, num, text) {
            var _text = text || '订阅成功';

            dom.next().text(_text);
            dom.next().stop().animate({
                'opacity':  num
            },1000);
        },
        listen: function () {
            var that = this;

            that.$ChannelList.on('click', 'input[name="dy"]', function () {
            	var $obj = $(this);
                if ($(this).attr('checked')) {
                    that._promise('/gallery/layout/add', $(this).attr('data-channelId'))
                        .then(function (data) {
                            if (data.result) {
                            	that.animate_($obj, 1);
                                    setTimeout(function() {
                                        that.animate_($obj, 0);
                                    } ,3000);
                            } else {
                                Ewin.alert(data.msg);
                            };
                        })['catch'](function (e) {
                        throw new Error(e);
                    });
                } else {
                    that._promise('/gallery/layout/delete', $(this).attr('data-channelId'))
                        .then(function (data) {
                            if (data.result) {
                            	that.animate_($obj, 1, '取消成功');
                                   setTimeout(function() {
                                        that.animate_($obj, 0, '取消成功');
                                    } ,3000);
                            } else {
                                Ewin.alert(data.msg);
                            };
                        })['catch'](function (e) {
                        throw new Error(e);
                    });
                };
            });
        }
    };

    $(function () {
        new PageDingZhi();
    });

})();
