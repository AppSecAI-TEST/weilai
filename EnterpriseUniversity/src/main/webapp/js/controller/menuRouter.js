/**
 * Created by Mr.xiang on 2016/3/26.
 */
angular.module('menu', ['pascalprecht.translate', 'ngSanitize'])
/*公用语言切换模块-start*/
.config(['$translateProvider', function($translateProvider){
	var lang = getCookie('lang')||window.localStorage.lang||'cn';
	$translateProvider.useStaticFilesLoader({
	    prefix: '../i18n/',
	    suffix: '.json'
    });
    $translateProvider.preferredLanguage(lang);
    $translateProvider.useSanitizeValueStrategy('sanitize');
}])
.factory('t', ['$translate', '$q', '$http', function($translate, $q, $http){
	var T = 
	{
		lang:"",
		langJsonData : {},
		init:function($scope){
			this.lang = getCookie('lang') || window.localStorage.lang || 'cn';
			$scope.isEnglish = this.lang =='en' ? true : false;
		},
		T : function(lang){
		 	$translate.use(lang);
		 	setCookie('lang', lang, 365);
	        window.localStorage.lang = lang;
	        window.location.reload();
		},
		getJsonData: function(){
			var jsonDefer = $q.defer();
    		$http.get('../i18n/' + this.lang + '.json').success(function(data){
			 	jsonDefer.resolve(data); 
    		});
		    return jsonDefer.promise;
		}
	};
	return T;
}])
.directive('ngBindPlaceholder', ['$sce', '$parse', '$compile', function($sce, $parse, $compile){
   return {
    restrict: 'A',
    compile: function ngBindHtmlCompile(tElement, tAttrs) {
      var ngBindHtmlGetter = $parse(tAttrs.ngBindPlaceholder);
      var ngBindPlaceholderWatch = $parse(tAttrs.ngBindPlaceholder, function getStringValue(value) {
        return (value || '').toString();
      });
      $compile.$$addBindingClass(tElement);

      return function ngBindHtmlLink(scope, element, attr) {
        $compile.$$addBindingInfo(element, attr.ngBindPlaceholder);

        scope.$watch(ngBindPlaceholderWatch, function ngBindWatchAction() {
          // we re-evaluate the expr because we want a TrustedValueHolderType
          // for $sce, not a string
          element.html($sce.getTrustedHtml(ngBindHtmlGetter(scope)) || '');
          var plc = element.text();
          element.attr('placeholder',plc);
        });
      };
    }
  };
}])
/*
 * 页面链接跳转路由控制
 * */
.controller('mainController', ['$scope', '$http', '$state', '$window', '$timeout', 'dialogService', 'sessionService', 'loadDialog', 'saveDialog', 't',  
                               function ( $scope, $http, $state, $window, $timeout, dialogService, sessionService, loadDialog, saveDialog, t) {
	
	$scope.lang = getCookie('lang') || window.localStorage.lang || 'cn';
	//自定义会话框
	$scope.dialog = dialogService;
	$scope.loadDialog = loadDialog;
	$scope.saveDialog = saveDialog;
	//登录验证
	$scope.sessionService = sessionService;
	$scope.sessionService.login();
	//$scope.sessionService.logout();//获取退出链接url
	//面包屑
    $scope.cm = {pmn: 'index.pmn', cmn: 'index.cmn'};
    //默认欢迎页
    $state.go("welcomePage", null); 
    //监听面包屑导航栏状态
    $scope.$on('cm', function(e, currentMenu){
    	e.stopPropagation();
    	$scope.cm = currentMenu;
    });
    //加载时显示遮罩层
    //接收子作用域中的加载状态
    $scope.$on('isLoading', function(e, flag) {
    	e.stopPropagation();
    	if(flag){
    		$scope.loadDialog.addLoadingItem();  
    	}else{
    		$scope.loadDialog.deleteLoadingItem();  
    	}
    });
    //监听表单保存事件,遮盖层显示与隐藏控制
    $scope.$on('isSaving', function(e, flag){
    	e.stopPropagation();
    	if(flag){
    		$scope.saveDialog.saving();
    	}else{
    		$scope.saveDialog.saved();
    	}
    });
    //切换语言
    $scope.toggleLang = function(){
    	if($scope.lang == "cn"){
    		$scope.lang = "en";
    	}else{
    		$scope.lang = "cn";
    	}
    	t.T($scope.lang);
    }
    //菜单跳转函数
    $scope.go = function (checkAuthorityUrl, goWhere, param) {
        /*
         * 参数：checkAuthorityUrl   校验权限请求地址
         * 参数：goWhere             路由跳转地址
         * 参数：param               传递参数
         * */
        $http.get("/enterpriseuniversity/services/page" + checkAuthorityUrl + (param!=null ? ("?id="+param.id):""))
            .success(function (response) {
            	if(response!="401")
            	{
            		if (param == null) 
            		{
                        $state.go(goWhere, null, {reload: true});
                    }
            		else 
            		{
                        $state.go(goWhere, param);
                    }
            	}
            	else
        		{
            		$state.go("401", null);
        		}
            })
            .error(function (response,state) {
                //没有权限         配置无权限页面
            	if(state==401){
            		$state.go("401", null);
            	}else{
            		
            	}
            });
    }
}])
//权限不足提示页控制器
.controller('NoAuthorityController',['$scope',function($scope){
	$scope.$parent.cm = {pmn: '权限提示页 ', cmn: ''};
}]);