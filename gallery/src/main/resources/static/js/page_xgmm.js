/**
 * Created by LR on 2017/6/30.
 */

;(function () {
	'use strict';

	var PageXgmm = function () {
		this.$From = $('.Js_change');
		this.$OldPwd = $('input[name="oldPwd"]');
		this.$NewPwd = $('input[name="newPwd"]');
		this.$EnsurePwd = $('input[name="ensurePwd"]');
		this.$Sub = $('.Js_sub');
		this.init();
	};

	PageXgmm.prototype = {
		init: function () {
			this.listen();
		},
        pwdReset: function () {
        	var that = this;
        	
        	$.post("/gallery/setting/pwdReset", {
                oldPwd: that.$OldPwd.val(),
                newPwd: that.$NewPwd.val(),
                ensurePwd: that.$EnsurePwd.val()
            }).then(function (data) {
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

				if (_.size(_.trim(that.$OldPwd.val())) === 0) {
					Ewin.alert('原密码不能为空!').on(function () {
						that.$OldPwd.focus();
					});
					return;
				};
				if (_.size(_.trim(that.$NewPwd.val())) === 0) {
					Ewin.alert('新密码不能为空!').on(function () {
						that.$NewPwd.focus();
					});
					return;
				};
				if (that.$NewPwd.val() != that.$EnsurePwd.val()) {
					Ewin.alert('两次输入的密码不一致').on(function () {
						that.$EnsurePwd.focus();
					});
					return;
				};
				
				that.pwdReset();
			});
		}
	};

	$(function () {
		new PageXgmm();
	});

})();
