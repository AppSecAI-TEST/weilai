// Custom scripts
$(document).ready(function () {

    // MetsiMenu
    $('#side-menu').metisMenu();

    // Collapse ibox function
    $('.collapse-link').click( function() {
        var ibox = $(this).closest('div.ibox');
        var button = $(this).find('i');
        var content = ibox.find('div.ibox-content');
        content.slideToggle(200);
        button.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
        ibox.toggleClass('').toggleClass('border-bottom');
        setTimeout(function () {
            ibox.resize();
            ibox.find('[id^=map-]').resize();
        }, 50);
    });

    // Close ibox function
    $('.close-link').click( function() {
        var content = $(this).closest('div.ibox');
        content.remove();
    });

    // Small todo handler
    $('.check-link').click( function(){
        var button = $(this).find('i');
        var label = $(this).next('span');
        button.toggleClass('fa-check-square').toggleClass('fa-square-o');
        label.toggleClass('todo-completed');
        return false;
    });

    // Append config box / Only for demo purpose
   /* $.get("skin-config.html", function (data) {
        $('body').append(data);
    });*/

    // minimalize menu
    $('.navbar-minimalize').click(function () {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    })

    // tooltips
    $('.tooltip-demo').tooltip({
        selector: "[data-toggle=tooltip]",
        container: "body"
    })

    // Move modal to body
    // Fix Bootstrap backdrop issu with animation.css
    $('.modal').appendTo("body")

    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebard-panel").css("min-height", heightWithoutNavbar + "px");
    }
    fix_height();

    // Fixed Sidebar
    // unComment this only whe you have a fixed-sidebar
            //    $(window).bind("load", function() {
            //        if($("body").hasClass('fixed-sidebar')) {
            //            $('.sidebar-collapse').slimScroll({
            //                height: '100%',
            //                railOpacity: 0.9,
            //            });
            //        }
            //    })

    $(window).bind("load resize click scroll", function() {
        if(!$("body").hasClass('body-small')) {
            fix_height();
        }
    })

    $("[data-toggle=popover]")
        .popover();
    
    resizeView();//重置视图高度
    
});


// For demo purpose - animation css script
function animationHover(element, animation){
    element = $(element);
    element.hover(
        function() {
            element.addClass('animated ' + animation);
        },
        function(){
            //wait for animation to finish before removing classes
            window.setTimeout( function(){
                element.removeClass('animated ' + animation);
            }, 2000);
        });
}

// Minimalize menu when screen is less than 768px
$(function() {
    $(window).bind("load resize", function() {
        if ($(this).width() < 769) {
            $('body').addClass('body-small')
        } else {
            $('body').removeClass('body-small')
        }
    })
})

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
        // Hide menu in order to smoothly turn on when maximize menu
        $('#side-menu').hide();
        // For smoothly turn on menu
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 100);
    } else if ($('body').hasClass('fixed-sidebar')){
        $('#side-menu').hide();
        setTimeout(
            function () {
                $('#side-menu').fadeIn(500);
            }, 300);
    } else {
        // Remove all inline style from jquery fadeIn function to reset menu state
        $('#side-menu').removeAttr('style');
    }
}

// Dragable panels
function WinMove() {
    var element = "[class*=col]";
    var handle = ".ibox-title";
    var connect = "[class*=col]";
    $(element).sortable(
        {
            handle: handle,
            connectWith: connect,
            tolerance: 'pointer',
            forcePlaceholderSize: true,
            opacity: 0.8
        })
        .disableSelection();
};
//写cookies
function setCookie(c_name, value, expiredays){
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + expiredays);
	document.cookie = c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires=" + exdate.toGMTString()) + ";path=/";
}
 
//读取cookies
function getCookie(name)
{
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return (arr[2]);
    else
        return null;
}

//删除cookies
function delCookie(name)
{
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
    	document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}

//选中的菜单高亮
function menu_active(obj) {
    $('li').removeClass('active');
	$(obj).parent().addClass('active').siblings().removeClass('active');
	$(obj).parent().parent().parent().addClass('active'); 
}
//监听窗口大小变化，自适应 
function re_height($page_main_view) {
	var $top_nav = $('#top-nav');
	var $page_breadcrumb = $('#page-breadcrumb');
	var $footer_nav = $('#footer-nav');
	var $window_height = $(window).innerHeight();
	$('#eleApp').height(parseInt($window_height));
	//document.getElementById("windowHeight").value=$window_height; 
	$page_main_view.height($window_height - parseInt($top_nav.outerHeight()) - parseInt($page_breadcrumb.outerHeight()) - parseInt($footer_nav.outerHeight()));
	$('#page').height(parseInt($page_main_view.innerHeight()));//重置切换视图的高度
	$('#page-left-container').height(parseInt($page_main_view.innerHeight()));
	$('#page-right-container').height(parseInt($page_main_view.innerHeight()));
}
//重置视图高度
function resizeView() {
	var $window = $(window);
	var $window_height = $window.innerHeight();//窗口内容显示区高度
	var $top_nav = $('#top-nav');//顶部导航栏
	var $left_nav_container = $('#left-nav-container');//左侧导航栏
	var $page_main_view = $('#page-main-view');//主视图区
	if (top.location != self.location) {
		top.location.reload();
	} else {
		//说明你的页面不是在if框架中显示
	}
	$left_nav_container.height($window_height - parseInt($top_nav.outerHeight()));
	
	re_height($page_main_view);
	
	$window.resize(function() {//浏览器窗口大小改变
		
		$window_height = $window.innerHeight();
		
		$left_nav_container.height($window_height - parseInt($top_nav.outerHeight()));
		
		re_height($page_main_view);
	});
}

Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}