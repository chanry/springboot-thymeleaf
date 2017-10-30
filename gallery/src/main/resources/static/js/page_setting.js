/**
 * Created by LR on 2017/6/30.
 */

;(function () {
	'use strict';

	var PageSetting = function () {
		this.$From = $('.Js_change');
		this.$Email = $('input[name="email"]');
		this.$NickName = $('input[name="nickName"]');
		this.$Phone = $('input[name="phone"]');
		this.$Tel = $('input[name="tel"]');
		this.$Sub = $('.Js_sub');
		this.init();
	};

	PageSetting.prototype = {
		init: function () {
			this.initAjax();
			this.listen();
		},
		initAjax: function () {
            var that = this;

            Q($.getJSON('/gallery/setting/getUserInfo'))
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
            that.$Email.val(data.email);
            that.$NickName.val(data.nickName);
            that.$Phone.val(data.mobile);
            that.$Tel.val(data.idcard);
        },
        updateInfo: function () {
        	var that = this;
        	
        	Q($.post("/gallery/setting/update", {
                email: that.$Email.val(),
                nickName: that.$NickName.val(),
                mobile: that.$Phone.val(),
                IDCard: that.$Tel.val()
            })).then(function (data) {
                if (data.result) {
                	Ewin.alert("修改成功").on(function () {
                        window.location.reload();
                    });
                } else {
                    Ewin.alert(data.msg);
                };
            })['catch'](function (e) {
                throw new Error(e);
            });
        },
		listen: function () {
			var that = this;

			that.$Sub.on('click', function () {
				//校验邮箱
				if (!new RegExp(/^.*@[^_]*$/).test(_.trim(that.$Email.val()))) {
					Ewin.alert('请输入正确的邮箱地址!').on(function () {
						that.$Email.focus();
					});
					return;
				};
				if (_.size(_.trim(that.$NickName.val())) === 0) {
					Ewin.alert('用户昵称不能为空!').on(function () {
						that.$NickName.focus();
					});
					return;
				};
				if (_.size(_.trim(that.$Phone.val())) === 0) {
					Ewin.alert('电话号码不能为空!').on(function () {
						that.$Phone.focus();
					});
					return;
				}
				//校验身份证
				if (!new RegExp(/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/).test(_.trim(that.$Tel.val()))) {
					Ewin.alert('请输入正确的身份证号码!').on(function () {
						that.$Tel.focus();
					});
					return;
				};

				//that.$From.submit();
				that.updateInfo();
			});
		}
	};

	$(function () {
		new PageSetting();
	});

})();
