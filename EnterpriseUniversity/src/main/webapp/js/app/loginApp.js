function request(strParame) {
    var args = new Object( );
    var query = location.search.substring(1);
    var pairs = query.split("&");
    // Break at ampersand 
    for (var i = 0; i < pairs.length; i++) 
    {
        var pos = pairs[i].indexOf('=');
        if (pos == - 1) {
            continue;
        }
        var argname = pairs[i].substring(0, pos);
        var value = pairs[i].substring(pos + 1);
        value = decodeURIComponent(value);
        args[argname] = value;
    }
    return args[strParame];
}

angular.module('loginApp',[])
.controller('LoginFormController', ['$scope', '$http', '$timeout', '$interval', '$window', function($scope, $http, $timeout, $interval, $window) {
	var token = request("token"), userId = request("userId");
   	$scope.loginSuccessed = false;
   	$scope.isProcessLogin = false;
	$scope.showLoginForm = true;//默认显示登录表单
	$scope.formData = {};
	$scope.errorMessage = {};//错误提示信息
	$scope.successMessage = {};//成功提示信息
	$scope.showQrcodeLoginForm = false; 
	$scope.formData.userName = userId;
	var qrPath, authCode, baseUrl = "/enterpriseuniversity/services/api/getMethod?callback=JSON_CALLBACK&", baseVideoUrl = "/enterpriseuniversity/";
	/*查询二维码*/
	var queryQrcode = function (){
		$http.jsonp(baseUrl + "apiName=" + "QR_LOGIN" + "&token=" + token + "&userId=" + userId + "&apiType=" + "myInfo")
		.success(function(data, status, header, config) 
		{
			if(data.status == 'Y'){
				qrPath = data.results.qrPath;
				authCode = data.results.authCode;
				$scope.qrcodeStyle = {
				 	"background": "url("+ baseVideoUrl + qrPath +") no-repeat  center top",
                	"background-size":"cover"
        		};
			}
	  	})
		.error( function(data) {
			 
		});
	}
	
	if(token && userId){
		queryQrcode();
	}
	/* 切换新用户、密码找回表单 */
	$scope.openNewUserLoginForm = function(type){
		if(type==='newUser'){
			$scope.formTitle = '新用户登录';
		}else{
			$scope.formTitle = '修改密码';
		}
		$scope.showLoginForm = false;
		$scope.formData = {};
	}
	/* 重置input值 */
	$scope.resetUserName = function(){
	   	$scope.formData.userName = undefined;
	   	$scope.errorMessage.invalidUserName = undefined;
	   	$scope.errorMessage.invalidPassword = undefined;
   	}
   	$scope.resetPassword = function(){
	   	$scope.formData.password = undefined;
	   	$scope.errorMessage.invalidPassword = undefined;
   	}
   	$scope.resetPhoneNumber = function(){
	   	$scope.formData.phone = undefined;
	   	$scope.errorMessage.invalidPhone = undefined;
   	}
   	$scope.checkPhone = function () {
   		if(!new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone)){
   			$scope.errorMessage.invalidPhone = "输入手机号不合法";
   		}else{
   			$scope.errorMessage.invalidPhone = undefined;
   		}
   	}
   	$scope.checkPassword = function(){
   		if(!new RegExp(/^\w{6,15}$/).test($scope.formData.password)){
			$scope.errorMessage.invalidPassword = "请输入6-15位字符密码";
   		} else{
   			$scope.errorMessage.invalidPassword = undefined;
   		}
   	}
   	/* 初始化表单状态 */
   	$scope.initStatus = function(){
   		$scope.loginForm.$submitted = false;
   		$scope.successMessage = {};
   	}
   	var qrcodeTimer ;
   	/* 切换登录窗口 */
	$scope.toggleSite = function(mark){
		$scope.formData = {};
		$scope.formData.userName = userId;
		$scope.errorMessage = {};
		if(mark=='Y'){
	 		$scope.showQrcodeLoginForm = true; 
	 		doInterval();//定时调用接口
		}else{
	 		$scope.showQrcodeLoginForm = false; 
	 		cancelInterval();
		}
	}
   	/* 获取手机验证码 */
	var timer;
	$scope.countTime = 60;
	$scope.identifyCodeIsSend = false;
	$scope.sendIdentifyCode = function(){
		if($scope.identifyCodeIsSend){
			return;
		}
		$scope.errorMessage = {};
		$scope.successMessage = {};
		$scope.formData.captcha = undefined;
		if(!$scope.formData.phone){
			$scope.errorMessage.invalidPhone = "请输入手机号";
			return;
		} 
		if(!(new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone))){
			$scope.errorMessage.invalidPhone = "输入手机号不合法";
			return;
   		}
		
		timer = $interval(function(){
			if($scope.countTime<=0){
				$interval.cancel(timer);
				timer = undefined;
				$scope.countTime = 60;
				$scope.identifyCodeIsSend = false;
				return;
			}
			$scope.countTime --;
		}, 1000);
		
		$scope.identifyCodeIsSend = true;
		$http({
 			method : 'POST',
		 	url  : '/enterpriseuniversity/services/userFirstLog/sendCaptchaByPhone',
		 	data : JSON.stringify({"phone" : $scope.formData.phone}), 
		 	headers : { 'Content-Type': 'application/json' }  
	 	}).success(function(response){
	 		if(response.captcha!=undefined){
	 			$scope.formData.callbackCaptcha = response.captcha;
	 			$scope.successMessage.hasSendedCaptcha = "手机验证码已发送";
	 		}else{
	 			$scope.errorMessage.invalidPhone = response.result;
	 		}
	 	}).error(function(){
	 		$scope.errorMessage.invalidCaptcha = "手机验证码发送异常";
	 	});
	}
   	/* 完成密码修改 */
   	$scope.submitNewPassword = function(){
   		$scope.resetPasswordForm.$submitted = true;
   		$scope.errorMessage = {};
		$scope.successMessage = {};
   		if(!$scope.formData.phone){
			$scope.errorMessage.invalidPhone = "请输入手机号";
			return;
		} 
   		if(!(new RegExp(/^1[3|4|5|7|8]\d{9}$/).test($scope.formData.phone))){
			$scope.errorMessage.invalidPhone = "输入手机号不合法";
			return;
   		}
   		if(!$scope.formData.callbackCaptcha){
			$scope.errorMessage.invalidCaptcha = "请发送验证码";
			return;
		}
   		if(!$scope.formData.captcha){
			$scope.errorMessage.invalidCaptcha = "请输入验证码";
			return;
		} 
		if(!$scope.formData.password){
			$scope.errorMessage.invalidPassword = "请输入新密码";
			return;
		} 
   		/* (?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,15} 必须包含字母的正则*/
		if(!(new RegExp(/^\w{6,15}$/).test($scope.formData.password))){
			$scope.errorMessage.invalidPassword = "请输入6-15位字符密码";
			return;
   		}  
   		//验证码校验
   		if($scope.formData.callbackCaptcha && $scope.formData.callbackCaptcha == $scope.formData.captcha){
   			/* 加密 */
	   		var password = hex_md5($scope.formData.password);
	   		
   			$http({
   	 			method : 'POST',
   			 	url  : '/enterpriseuniversity/services/userFirstLog/updatePassword',
   			 	data : JSON.stringify({"password" : password , "phone" : $scope.formData.phone }), 
   			 	headers : { 'Content-Type': 'application/json' }  
   		 	}).success(function(response){
   		 		if(response.result=="success"){
   		 			//alert("修改密码成功"); 
   		 			window.location.href="/enterpriseuniversity/services/backend/sys/loginpage";
   		 		}else{
   		 			$scope.errorMessage.loginErrorMsg ="修改密码失败";  
   		 		}
   		 	}).error(function(){
   		 		$scope.errorMessage.loginErrorMsg ="修改密码异常";  
   		 	});
   		}else{
   			$scope.errorMessage.invalidCaptcha = "验证码错误";
   		}
   	}
   	/* 扫码登录*/
   	var questionCount = 30, timer2, isprocessed, issuccessed = false;
   	$scope.isError = false;
   	function doInterval(){
   		if(!angular.isDefined(timer2)){
			timer2 = $interval(function(){
				if(!(isprocessed || $scope.isError || questionCount <=0)){
					if(issuccessed){
						$interval.cancel(timer2);
						timer2 = undefined;
					}else {
						getQr_result();
					}
				}else{
					$interval.cancel(timer2);
					timer2 = undefined;
					$scope.toggleSite();
					questionCount = 30;
				}
			}, 2000);
		}
   	}
   	function cancelInterval(){
   		if(angular.isDefined(timer2)){
   			$interval.cancel(timer2);
   			timer2 = undefined;
   			questionCount = 30;
   		}
   	}
   	function getQr_result(){
   		questionCount -= 1;
		isprocessed = true;  
		$http.jsonp(baseUrl + "apiName=" + "QR_RESULT" + "&userId=" + userId + "&token=" + token + "&apiType=" + "myInfo" + "&authCode=" + authCode) 
		.success( function (data, status, header, config) 
		{
			isprocessed = false; 
		    if(data.status=='Y'){
		    	if(data.results && data.results.status=='Y'){
		    		issuccessed = true;
		    		$http.get("/enterpriseuniversity/services/backend/sys/qrlogin?authCode="+authCode).success(function (response){
		    			$scope.isProcessLogin = false;
		    			if(response=='success'){
		    				$scope.processMessage = "登录中成功";
		    				$scope.isProcessLogin = false;
		    		 		window.location.href = "/enterpriseuniversity/services/menu";
		    			}
		    		});
//		    		window.location.href = "/enterpriseuniversity/services/backend/sys/qrLogin?authCode="+authCode;
		    	} 
		    }else{
		    	$scope.isError = true;
		    }
		}) 
		.error( function (data) 
		{
			isprocessed = false;
			$scope.isError = true;
		});
	}
    /* 账号密码登录 */
    $scope.doLogin = function(){
		$scope.loginForm.$submitted = true;
		$scope.errorMessage = {};
		$scope.successMessage = {};
	 	if($scope.formData.userName==undefined){
	 		$scope.errorMessage.invalidUserName = "请输入用户名";
	 		return;
	 	}
	 	if($scope.formData.password==undefined){
	 		$scope.errorMessage.invalidPassword = "请输入密码";
	 		return;
	 	}
	 	$scope.processMessage = "登录中...";
		$scope.isProcessLogin = true;
		
		//加密密码
		var password = hex_md5($scope.formData.password);
		console.log("password",password);
        var formData = {userName:$scope.formData.userName,password:password};
        
   	 	$http({
	 		method : 'POST',
		 	url  : '/enterpriseuniversity/services/backend/sys/login',
		 	data : $.param(formData),  
		 	headers : { 'Content-Type' : 'application/x-www-form-urlencoded' }  
	 	}).success(function(response){
			$scope.isProcessLogin = false;
			if(response=='success'){
				$scope.processMessage = "登录中成功";
				$scope.isProcessLogin = false;
		 		window.location.href = "/enterpriseuniversity/services/menu";
	 		} else if(response=='incorrectusername'){
		 		$scope.errorMessage.invalidUserName = "用户名不存在";
		 	}else if(response.indexOf('passworderror')!=-1){
		 		$scope.errorMessage.invalidPassword = "用户名或密码错误"; 
		 	}else if(response=='permissiondenied'){
		 		$scope.errorMessage.systemError = "用户无登录权限"; 
			}else if (response=='locked'){
				$scope.errorMessage.locked="多次密码错误，用户已锁定!";
			}
	 	}).error(function(){
			$scope.isProcessLogin = false;
		 	$scope.errorMessage.systemError = "系统忙，请稍后重试";
	 	})
	}
}]) 
.directive('windowResize', ['$window', function ($window) {
	return function (scope, element) {
  		var w = angular.element($window);
      	scope.getWindowHeightAndWidth = function () {
          	return {
              	'h': w.outerHeight(),
              	'w': w.outerWidth()
          	};
      	};
      	scope.$watch(scope.getWindowHeightAndWidth, function (newValue, oldValue) {
          	scope.windowHeight = newValue.h;
          	scope.windowWidth = newValue.w;
          	//设置对话框margin-top
          	scope.setBodyStyle = function () {
          		return {
                   'width':newValue.w + 'px',
                   'height':newValue.h + 'px'
              	};
          	};
          	scope.setFormStyle = function () {
          		return {
                   	'padding-top':(((newValue.h-400)/2)>0?(newValue.h-400)/2:0) + 'px'
              	};
          	};
      	}, true);
      
   	 	w.bind('resize', function () {
          scope.$apply();
      	});
  }
}])
.factory('dialogService',function(){
	
	
	
});