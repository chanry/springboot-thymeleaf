define(["脚本_swiper"], function() {
	var PageIndex = function() {
		this.$ListBox = $('.js_listBox');
		this.$Row02Pre = $('.js_row02Pre');
		this.$Row02Next = $('.js_row02Next');
		this.init();
	}

	PageIndex.prototype = {
		init: function() {
			this.listen();
		},

		listen: function() {
			var that = this;

			//切换新闻、公告、媒体报道
			that.$ListBox.each(function(e) {
				var $this = $(this),
				      $ListWrap = $this.find('.js_listWrap'),
				      $ListCon = $ListWrap.find('.js_listCon'),
				      $PrevBtn = $this.find('.js_prevBtn'),
				      $NextBtn = $this.find('.js_nextBtn'),
				      listConSize = $ListCon.length,
				      listConWidth = $ListCon.eq(0).width(),
				      index = 0;

				$ListWrap.width(listConSize * listConWidth);
				
				$PrevBtn.click(function() {
					if(!$(this).hasClass('disabled')) {
						$NextBtn.removeClass('disabled');
						index = index - 1;
						$ListWrap.animate({
							'left': -(index * listConWidth)
						}, 500);

						if(index == 0) {
							$(this).addClass('disabled');
						}
					}
				});

				$NextBtn.click(function() {
					if(!$(this).hasClass('disabled')) {
						$PrevBtn.removeClass('disabled');
						index = index + 1;
						$ListWrap.animate({
							'left': -(index * listConWidth)
						}, 500);

						if(index == listConSize - 1) {
							$(this).addClass('disabled');
						}
					}
				});
			});

			var mySwiper = new Swiper('.swiper-container', {
				autoplay: 3000,
				effect : 'fade',
				fade: {
					crossFade: true
				}
			});
			var listSize1 = $('.bannerbox .swiper-slide').length;
			var index1=0;
			that.$Row02Pre.click(function() {
				if(!$(this).hasClass('disabled')) {
						$('.js_row02Next').removeClass('disabled');
						index1 = index1 - 1;
						mySwiper.slidePrev();
						if(index1 == 0) {
							$(this).addClass('disabled');
						}
					}
				
			});

			that.$Row02Next.click(function() {
				if(!$(this).hasClass('disabled')) {
						$('.js_row02Pre').removeClass('disabled');
						index1 = index1 + 1;
						mySwiper.slideNext();

						if(index1 == listSize1 - 1) {
							$(this).addClass('disabled');
						}
					}
				
			});
		}
	}

	window.pageIndex = new PageIndex();
});