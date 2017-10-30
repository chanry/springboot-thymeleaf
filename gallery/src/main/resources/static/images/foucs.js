//焦点大图
$(function () {
    var winWidth;
    var picWidth;
    var sWidth;
    $(window).resize(function(){
        winWidth = $(window).width();
        picWidth = winWidth;
        sWidth = picWidth;
        $(".banner").width(picWidth);
        $("#focus-top").width(picWidth);
        $("#focus-top ul li").width(picWidth);
        $("#focus-top ul li img").width(picWidth);
        //导航位置自适应
        $(".nav").css({
            left: function() {
                return picWidth + 10;
            }
        });
        $("#focus-top ul").css("width", sWidth * (len));
    }).resize();

    var len = $("#focus-top ul li").length;
    var index = 0;
    var picTimer;
    var btn = "<div class='btnBg'></div><div class='btn'>";

    for (var i = 0; i < len; i++) {
        btn += "<span></span>";
    }
    btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
    $("#focus-top").append(btn);
    $("#focus-top .btnBg").css("opacity", 0);
    $("#focus-top .btn span").css("opacity", 0.4).mouseenter(function () {
        index = $("#focus-top .btn span").index(this);
        showPics(index);
    }).eq(0).trigger("mouseenter");
    $("#focus-top .preNext").css("opacity", 0.0).hover(function () {
        $(this).stop(true, false).animate({ "opacity": "0.5" }, 500);
    }, function () {
        $(this).stop(true, false).animate({ "opacity": "0" }, 500);
    });
    $("#focus-top .pre").click(function () {
        index -= 1;
        if (index == -1) { index = len - 1; }
        showPics(index);
    });
    $("#focus-top .next").click(function () {
        index += 1;
        if (index == len) { index = 0; }
        showPics(index);
    });

    $("#focus-top").hover(function () {
        clearInterval(picTimer);
    }, function () {
        picTimer = setInterval(function () {
            showPics(index);
            index++;
            if (index == len) { index = 0; }
        }, 6000);
    }).trigger("mouseleave");
    function showPics(index) {
        var nowLeft = -index * sWidth;
        $("#focus-top ul").stop(true, false).animate({ "left": nowLeft }, 300);
        $("#focus-top .btn span").stop(true, false).animate({ "opacity": "0.4" }, 300).eq(index).stop(true, false).animate({ "opacity": "1" }, 300);
    }
});


//左边焦点图
$(function() {
    var sWidth = $(".focus01").width(); //获取焦点图的宽度（显示面积）
    var len = $(".focus01 ul li").length; //获取焦点图个数
    var index = 0;
    var picTimer;

    //以下代码添加数字按钮和按钮后的半透明条，还有上一页、下一页两个按钮
    var btn = "<div class='btnBg'></div><div class='btn'>";
    for(var i=0; i < len; i++) {
        btn += "<span></span>";
    }
    btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
    $(".focus01").append(btn);
    $(".focus01 .btnBg").css("opacity",0.5);

    //为小按钮添加鼠标滑入事件，以显示相应的内容
    $(".focus01 .btn span").css("opacity",0.4).mouseenter(function() {
        index = $(".focus01 .btn span").index(this);
        showPics(index);
    }).eq(0).trigger("mouseenter");
    //本例为左右滚动，即所有li元素都是在同一排向左浮动，所以这里需要计算出外围ul元素的宽度
    $(".focus01 ul").css("width",sWidth * (len));

    //鼠标滑上焦点图时停止自动播放，滑出时开始自动播放
    $(".focus01").hover(function() {
        clearInterval(picTimer);
    },function() {
        picTimer = setInterval(function() {
            showPics(index);
            index++;
            if(index == len) {index = 0;}
        },4000); //此4000代表自动播放的间隔，单位：毫秒
    }).trigger("mouseleave");

    //显示图片函数，根据接收的index值显示相应的内容
    function showPics(index) { //普通切换
        var nowLeft = -index*sWidth; //根据index值计算ul元素的left值
        $(".focus01 ul").stop(true,false).animate({"left":nowLeft},300); //通过animate()调整ul元素滚动到计算出的position
        $(".focus01 .btn span").removeClass("on").eq(index).addClass("on"); //为当前的按钮切换到选中的效果
        $(".focus01 .btn span").stop(true,false).animate({"opacity":"0.4"},300).eq(index).stop(true,false).animate({"opacity":"1"},300); //为当前的按钮切换到选中的效果
    }
    //获取图片的alt当标题显示
    $(".focus01 li").each(function(){
        $(this).find(".tit").html($(this).find("img").attr("alt"));
    })
});

//右边轮播图片
$(function() {
    var sWidth = $(".focus02").width(); //获取焦点图的宽度（显示面积）
    var len = $(".focus02 ul li").length; //获取焦点图个数
    var index = 0;
    var picTimer;

    //以下代码添加数字按钮和按钮后的半透明条，还有上一页、下一页两个按钮
    var btn = "<div class='btnBg'></div><div class='btn'>";
    for(var i=0; i < len; i++) {
        btn += "<span></span>";
    }
    btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
    $(".focus02").append(btn);
    $(".focus02 .btnBg").css("opacity",0.5);

    //为小按钮添加鼠标滑入事件，以显示相应的内容
    $(".focus02 .btn span").css("opacity",0.4).mouseenter(function() {
        index = $(".focus02 .btn span").index(this);
        showPics(index);
    }).eq(0).trigger("mouseenter");
    //本例为左右滚动，即所有li元素都是在同一排向左浮动，所以这里需要计算出外围ul元素的宽度
    $(".focus02 ul").css("width",sWidth * (len));

    //鼠标滑上焦点图时停止自动播放，滑出时开始自动播放
    $(".focus02").hover(function() {
        clearInterval(picTimer);
    },function() {
        picTimer = setInterval(function() {
            showPics(index);
            index++;
            if(index == len) {index = 0;}
        },4000); //此4000代表自动播放的间隔，单位：毫秒
    }).trigger("mouseleave");

    //显示图片函数，根据接收的index值显示相应的内容
    function showPics(index) { //普通切换
        var nowLeft = -index*sWidth; //根据index值计算ul元素的left值
        $(".focus02 ul").stop(true,false).animate({"left":nowLeft},300); //通过animate()调整ul元素滚动到计算出的position
        $(".focus02 .btn span").removeClass("on").eq(index).addClass("on"); //为当前的按钮切换到选中的效果
        $(".focus02 .btn span").stop(true,false).animate({"opacity":"0.4"},300).eq(index).stop(true,false).animate({"opacity":"1"},300); //为当前的按钮切换到选中的效果
    }
});
