/**
 * Created by LR on 2017/6/20.
 */

;(function () {
    'use strict';

    var PageRgzx = function () {
        this.$List = $('.tabList');
        this.$Body = $('.tabBody');
        this.$Calendar = $('.Js_calendarTable');
        this.$Hot = $('.Js_hot');
        this.$Event = $('.Js_event');
        this.day_ = this.$Calendar.find('td.currentDay').attr('data');
        this.tabObj = [{
            name: "news",
            dom: ".Js_news"
        }, {
            name: "shows",
            dom: ".Js_exhibition"
        }, {
            name: "acts",
            dom: ".Js_education"
        }];
        this.channelNameArray = ['展览','媒体中心','公共教育','研究策展', '典藏鉴赏','社会服务'];
        this.init();
    };

    PageRgzx.prototype = {
        init:function () {
            this.initTpl();
            this.initAjax();
            this.listen();
        },
        initAjax: function () {
            this.initChannelIdAjax();
            this.initTabAjax();
            this.initHotAjax();
        },
        initTpl:function () {
            
            this.tabTpl = _.template(
                '<li>' +
                    '<a href="<%=PUBURL%>" target="view_window"><%=TITLE%></a>' +
                    '<span><%=PUBTIME%></span>' +
                '</li>'
            );

            this.commonTpl = _.template(
                '<div class="nr clearfix">' +
                    '<a href="<%=PUBURL%>"><img src="<%=IMGURL%>" /></a>' +
                    '<div class="nr-wz">' +
                        '<a href="<%=PUBURL%>" target="view_window"><%=TITLE%></a>' +
                        '<% if (NAME === "展览") { %>' +
                            '<p>展览时间:<%=KAISSJ%>至<%=JIESSJ%></p>' +
                            '<p>主办单位:<%=SPONSOR%></p>' +
                            '<p>展览场地:<%=ZHANTING%></p>' +
                        '<% } else if (NAME === "典藏鉴赏") { %>' +
                            '<p class="dcjs">登记号:<%=SERIALNUMBER%></p>' +
                            '<p class="dcjs">作者:<%=AUTHOR%></p>' +
                            '<p class="dcjs">创作年代:<%=CREATIONAGE%></p>' +
                            '<p class="dcjs">类别:<%=CATEGORY%></p>' +
                            '<p class="dcjs">规格:<%=SPECIFICATIONS%></p>' +
                            '<p class="dcjs">收藏时间:<%=COLLECTIONIME%></p>' +
                        '<% } else {  %>' +
                            '<p class="text-overflow"><%=CONTENT%></p>' +
                        '<% } %>' +
                    '</div>' +
                '</div>'
            );

        },
        findValue: function (_array, desc) {
            return _.result(_.find(_array, function(value) {
                return value.desc === desc
            }), 'value');
        },
        initChannelIdAjax: function() {
            var that = this;
            
            Q($.getJSON('/gallery/getChannelList')).then(function(data) {
                if (data.result) {
                    that.channelIdArray = data.data;
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        initTabAjax:function () {
            var that = this;

            Q($.getJSON('/gallery/getDateDocument',{
                date: that.day_
            }))
            .then(function (data) {
                if (data.result) {
                    that.builtTabInfo(data.data);
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        initHotAjax: function () {
            var that = this;

            Q($.getJSON('/gallery/getPcenterInfo'))
            .then(function (data) {
                if (data.result) {
                    that.builtHotInfo(data.data);
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        doAjax: function (obj, dom, url) {
            var that = this;

            Q($.getJSON(url, {
                    channelId: obj.channelId
            }))
            .then(function (data) {
                if (data.result) {
                    that.builtListInfo(data.data, dom, obj.chnlnName);
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });

        },
        builtTabInfo: function (data) {
            var that = this;


            $.each(that.tabObj, function(index, value) {
                $(that.tabObj[value.dom]).empty();
                if (_.size(data[value.name]) === 0 ) {
                    $(value.dom).empty().append('当日没有相关信息');
                } else {
                    $(value.dom).empty();
                    $.each(data[value.name], function(i, v) {
                        $(value.dom).append(that.tabTpl({
                            PUBURL: v.pubUrl,
                            TITLE: v.title,
                            PUBTIME: value.dom !== '.Js_news' 
                                                ? '[' + Uilt.format(that.findValue(v.customFields, '开始时间')) + '—' + Uilt.format(that.findValue(v.customFields, '结束时间')) + ']'
                                                : Uilt.format(v.pubTime)
                        }));
                    });
                };
            });

        },
        builtHotInfo: function (data) {
            var that = this;

            that.$Hot.each(function (index) {
                var $this = $(this),
                    documents = data[that.channelNameArray[index]] ? data[that.channelNameArray[index]] : [];

                documents && $this.parents(".ss-con02").show();
                $this.empty();

                if (documents.length === 0 ) {
                    $this.parents(".ss-con02").hide();
                } else {
                     if (index === 0) {
                        //展览
                        that.builtZlData(documents, $this);
                    } else if (index === 4) {
                        //典藏鉴赏
                        that.builtDcjsData(documents, $this);
                    } else {
                        that.builtOtherData(documents, $this, that.channelNameArray[index]);
                    };
                }; 
            });

        },
        builtListInfo: function (data, dom, name) {
            var that = this;
            
            dom.empty();
            if (data.length === 0) {
                dom.append('暂无数据');
            } else {
                if (name === '展览') {
                    //展览
                    that.builtZlData(data, dom);
                } else if (name === '典藏鉴赏') {
                    //典藏鉴赏
                    that.builtDcjsData(data, dom);
                } else {
                    that.builtOtherData(data, dom, name);
                };
            };
        },
        builtZlData: function(data, dom) {
            var that = this;

            $.each(data, function(index, value) {
                dom.append(that.commonTpl({
                    NAME:'展览',
                    IMGURL: value.imgUrl,
                    PUBURL: value.pubUrl,
                    TITLE: value.title,
                    SPONSOR: that.findValue(value.customFields, '主办单位'),
                    ZHANTING: that.findValue(value.customFields, '展览地点'),
                    KAISSJ: Uilt.format(that.findValue(value.customFields, '开始时间')),
                    JIESSJ: Uilt.format(that.findValue(value.customFields, '结束时间'))
                }));
            });
        },
        builtDcjsData: function(data, dom) {
            var that = this;

            $.each(data, function(index, value) {
                dom.append(that.commonTpl({
                    NAME:'典藏鉴赏',
                    IMGURL: value.imgUrl,
                    PUBURL: value.pubUrl,
                    TITLE: value.title,
                    SERIALNUMBER: value.serial_Number,
                    AUTHOR: value.author,
                    CREATIONAGE: value.creation_age,
                    CATEGORY: value.category,
                    SPECIFICATIONS: value.specifications,
                    COLLECTIONIME: value.collection_Time
                }));
            });
        },
        builtOtherData: function(data, dom, name) {
            var that = this;

            $.each(data, function (index, value) {
                dom.append(that.commonTpl({
                    NAME:'',
                    IMGURL: value.imgUrl,
                    PUBURL: value.pubUrl,
                    TITLE: value.title,
                    CONTENT: name === '公共教育' ? '' : value.content.substring(0, 74)
                }));
            });   
        },
        listen:function () {
            var that = this;

            //tab切换
            Uilt.TabSwitch('.Js_list', '.Js_body', 'cur');

            //日历
            that.$Calendar.on('click', 'td', function () {
                that.$Calendar.find('.currentDay').removeClass('currentDay').addClass('currentMonth');
                $(this).removeClass('currentMonth').addClass('currentDay');

                that.day_ = $(this).attr('data');
                that.initTabAjax();
            });

            //收藏
            this.$Event.on('click', function(e) {
                e.preventDefault();

                var $this = $(this),
                    channelObj = _.find(that.channelIdArray, function(value) {
                        return value.chnlnName === $this.parents('.Js_list').attr('data-name');
                    }),
                    dom,
                    url;

                if ($this.text().indexOf('收藏') > -1) {
                    dom = $this.parents('.ss-con02').find('.collect'),
                    url = '/gallery/collect/getCollects';
                               
                } else {
                    dom = $this.parents('.ss-con02').find('.lately'),
                    url = '/gallery/access/lately';                         
                };
                
                that.doAjax(channelObj, dom, url);
            });
        }
    };

    $(function () {
        new PageRgzx();
    })

})();
