/**
 * Created by LR on 2017/6/23.
 */

;(function () {
    'use strict';

    var PageWdsc = function () {
        this.$LeftNav = $('.Js_leftNav');
        this.$Content = $('.Js_content');
        this.$Delect = $('.Js_delect');
        this.chnlnName = this.$LeftNav.find('.cur').text();
        this.init();
    };

    PageWdsc.prototype = {
        init: function () {
            this.initTpl();
            this.initChannelIdAjax();
            this.listen();
        },
        initTpl: function () {

            this.commonTpl = _.template(
                '<div>' +
                    '<div class="nr clearfix collect">' +
                        '<a href="<%=PUBURL%>"><img src="<%=IMGURL%>" /></a>' +
                        '<div class="nr-wz">' +
                            '<a href="<%=PUBURL%>" target="view_window"><%=TITLE%></a>' +
                            '<% if (NAME === "展览") { %>' +
                                '<p>展览时间:<%=KAISSJ%>至<%=JIESSJ%></p>' +
                            '<% } else if (NAME === "典藏鉴赏") { %>' +
                                '<p>登记号:<%=SERIALNUMBER%></p>' +
                                '<p>作者:<%=AUTHOR%></p>' +
                                '<p>创作年代:<%=CREATIONAGE%></p>' +
                                '<p>类别:<%=CATEGORY%></p>' +
                                '<p>规格:<%=SPECIFICATIONS%></p>' +
                                '<p>收藏时间:<%=COLLECTIONIME%></p>' +
                            '<% } else {  %>' +
                                // '<p class="text-overflow"><%=name.content%></p>' +
                            '<% } %>' +
                        '</div>' +
                    '</div>' +
                    '<div class="float-right dis-in-b">' +
                        '<input type="checkbox" name="delect" data-id="<%= DOCID %>">' +
                    '</div>' +
                '</div>'
            );

        },
        initChannelIdAjax: function() {
            var that = this;
            
            Q($.getJSON('/gallery/getChannelList')).then(function(data) {
                if (data.result) {
                    var id = _.result(_.find(data.data, function(value) {
                        return value.chnlnName === that.chnlnName;
                    }), 'channelId');
                    that.initAjax(id);
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        findValue: function (_array, desc) {
            return _.result(_.find(_array, function(value) {
                return value.desc === desc
            }), 'value');
        },
        initAjax: function (id) {
            var that = this;

            Q($.getJSON('/gallery/collect/getCollects',
                {
                    channelId: id
                })
            ).then(function (data) {
                if (data.result) {
                    that.built(data.data);
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        delectAjax: function(data) {
            Q($.post('/gallery/collect/delete', {
                docId: data
            })).then(function(data) {
                if (data.result) {
                	Ewin.alert("取消成功").on(function () {
                        window.location.reload();
                    });
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
        built: function (data) {
            var that = this;

            that.$Content.empty();
            if (_.size(data) === 0) {
                that.$Content.append('您还没有收藏该栏目的文档，请点击“展览”后，在具体文章下对你感兴趣的文档标题下右上角点击“收藏”进行收藏');
                return;
            };

            if (that.chnlnName === '展览') {
                //展览
                $.each(data, function (index, value) {
                    that.$Content.append(that.commonTpl({
                        NAME:'展览',
                        DOCID: value.docId,
                        IMGURL: value.imgUrl,
                        PUBURL: value.pubUrl,
                        TITLE: value.title,
                        SPONSOR: that.findValue(value.customFields, '主办单位'),
                        ZHANTING: that.findValue(value.customFields, '展览地点'),
                        KAISSJ: Uilt.format(that.findValue(value.customFields, '开始时间')),
                        JIESSJ: Uilt.format(that.findValue(value.customFields, '结束时间'))
                    }));
                });
            } else if (that.chnlnName === '典藏鉴赏') {
                //典藏
                $.each(data, function(index, value) {
                    that.$Content.append(that.commonTpl({
                            NAME:'典藏鉴赏',
                            DOCID: value.docId,
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
            } else {
                $.each(data, function (index, value) {
                    that.$Content.append(that.commonTpl({
                        NAME:'',
                        DOCID: value.docId,
                        IMGURL: value.imgUrl,
                        PUBURL: value.pubUrl,
                        TITLE: value.title,
                        CONTENT: value.content
                    }));
                });
            };
        },
        listen: function() {
            var that = this;

            that.$Delect.on('click', function(e) {
                e.preventDefault();

                var data = [];
                $.each($(':checkbox:checked'), function(index, value) {
                    data.push($(value).attr('data-id'));
                });
                if (data.length > 0) {
                	that.delectAjax(data.join(","));
                } else {
                	Ewin.alert('请选择删除对象');
                }
            });

        }
    };

    $(function () {
        new PageWdsc();
    });

})()
