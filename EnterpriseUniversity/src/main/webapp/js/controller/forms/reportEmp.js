/*
 * 权限管理
 * */
angular.module('ele.reportEmps', [])
/*
 * 学员-课程评论报表
 */
.controller('EmpCourseCommentController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gz', cmn: '学员报表-课程评论'};
	
	$scope.a = $scope.formData = {
			arayacak:'N'
		};
		$scope.advancedSearch = function(){
			if($scope.a.arayacak=="N"){
				$scope.a.arayacak="Y";
			}else{
				$scope.a.arayacak="N";
			}
		}
		$scope.commentIn = [
			                 {code: "Y", name: "有"},
			                 {code: "N", name: "无"}
			 	       	 ];
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	
	
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	var method5 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method5 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method6 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method6 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	$scope.changeSeclectOption1 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		method5();
		$scope.search();
	}
	$scope.changeSeclectOption5 = function(){
		method6();
		$scope.search();
	}
	$scope.changeSeclectOption6 = function(){
		$scope.search();
	}
	$scope.changeSeclectOption7 = function(){
		$scope.search();
	}
	
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.courseNumber != undefined && $scope.courseNumber != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&courseNumber=" + $scope.courseNumber
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.courseName != undefined && $scope.courseName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
		if ($scope.orgName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
        }
		if ($scope.jobName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&jobName=" + $scope.jobName
        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		if ($scope.commentNull != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&commentNull=" + $scope.commentNull
        }
		return $scope.$httpPrams;
	}
	
	$scope.url = "/enterpriseuniversity/services/empCourseComment/empCourseCommentList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			$scope.$httpPrams=getCondition();
		
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow1= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
	$scope.searchData = function(){
		$scope.search();
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
	} 
	pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
    
    //导出
		$scope.downloadEmpCourseCommentExcel = function(){
			$scope.$emit('isLoading', true);
			$httpPrams=getCondition();
			$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
			location.href="/enterpriseuniversity/services/empCourseComment/downloadEmpCourseCommentExcel?" + $httpPrams;
			var closeActivityDept = $interval(function(){
				if(cookieService.getCookie("EmpCourseCommentOne") != null){
					$scope.$emit('isLoading', false);
					$interval.cancel(closeActivityDept);
				}
	    	},500);
		}
}])
/*
 * 学员报表-积分
 */
.controller('EmpScoreDetaileController', ['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus){
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gu', cmn: '积分统计'};
		
	$scope.a = $scope.formData = {
			arayacak:'N'
		};
		$scope.advancedSearch = function(){
			if($scope.a.arayacak=="N"){
				$scope.a.arayacak="Y";
			}else{
				$scope.a.arayacak="N";
			}
		}
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	
	
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	var method5 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method5 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method6 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method6 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	$scope.changeSeclectOption1 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		method5();
		$scope.search();
	}
	$scope.changeSeclectOption5 = function(){
		method6();
		$scope.search();
	}
	$scope.changeSeclectOption6 = function(){
	
		$scope.search();
	}
	
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime != undefined && $scope.examTotalTime !="") {
            $scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime;
        }
		if ($scope.orgName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
        }
		if ($scope.jobName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&jobName=" + $scope.jobName
        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		return $scope.$httpPrams;
	}
	
	$scope.url = "/enterpriseuniversity/services/empScoreDetaile/empScoreDetaileList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			$scope.$httpPrams=getCondition();
		
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow1= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
	$scope.searchData = function(){
		$scope.search();
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
	} 
	pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
    
    //导出
    $scope.downloadEmpScoreDetaileExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		location.href="/enterpriseuniversity/services/empScoreDetaile/downloadEmpScoreDetaileExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("EmpScoreDetaileOne") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
    
 /* //导出
    $scope.findEmpScoreDetaileOne= function(empCode){
    	$httpPrams=getCondition();
    	location.href="/enterpriseuniversity/services/empScoreDetaile/findEmpScoreDetaileOne?" + $httpPrams+"&empCode="+empCode;
    }*/
    
}])
//查看学员积分详情
.controller('viewEmpScoreController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus) {
	$scope.openModalDialog = false;
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime != undefined && $scope.examTotalTime !="") {
            $scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime;
        }
		if ($scope.orgName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
        }
		if ($scope.jobName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&jobName=" + $scope.jobName
        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		return $scope.$httpPrams;
	}
	$scope.url = "/enterpriseuniversity/services/empScoreDetaile/findEmpScoreDetaileOne?pageNum=";
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
	        	if(!$scope.code){
	        		$scope.page = {};
	        		return;
	        	}
	        	$scope.$httpPrams=getCondition();
	        	$scope.$httpUrl = "";
	        	$scope.$httpPrams = "&empCode="+$scope.code;
	            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
	            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
	            $scope.$emit('isLoading', true);
	            $http.get($scope.$httpUrl).success(function (response) {
	            	$scope.$emit('isLoading', false);
	                $scope.page = response;
	                $scope.paginationConf.totalItems = response.count;
	                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
	                
	            });
	        }
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.findEmpScoreDetaileOne = function(b){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.code=b.code;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(b){
    	$scope.currHighLightRow.code = b.code; 
    }
  //导出
    $scope.downloadViewEmpModelTemplateScoreDetaileExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		location.href="/enterpriseuniversity/services/empScoreDetaile/downloadEmpScoreDetaileOneExcel?"+$httpPrams+"&empCode="+$scope.code; 
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("EmpScoreDetaileTwo") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
}])

//[学习资源-组织部门报表统计（活动）]
.controller('LearningDeptActivityController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gd', cmn: '活动报表'};
	$scope.a = $scope.formData = {
		arayacak:'N'
	};
	$scope.advancedSearch = function(){
		if($scope.a.arayacak=="N"){
			$scope.a.arayacak="Y";
		}else{
			$scope.a.arayacak="N";
		}
	}
	
	$scope.activityIn = [
		                 {name: "进行中", value: "0"},
		                 {name: "已结束", value: "1"}
		 	       	 ];
	$scope.needApplyIn = [
		                 {name: "选修", value: "Y"},
		                 {name: "必修", value: "N"}
		 	       	 ];
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	
	
	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.changeSeclectOption1 = function(){
		method2();
		$scope.search();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
		$scope.search();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		$scope.search();
	}
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.activityName != undefined && $scope.activityName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime !=undefined && $scope.examTotalTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime
		}
        if ($scope.courseType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.courseType
        }
        if ($scope.activityState !=undefined ) {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityState=" + $scope.activityState
		}
        console.log($scope.needApply);
		if ($scope.needApply != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&needApply=" + $scope.needApply
        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityType=" + $scope.projectId
        }
		if ($scope.orgName != undefined) 
		{
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
		}
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		return $scope.$httpPrams;
	}
	
	//活动分类
	$http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
    });
	$scope.url = "/enterpriseuniversity/services/learningResourseOrgDeptActivity/learningResourseOrgDeptActivityList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);

			
			$scope.$httpPrams=getCondition();
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	$scope.changeSeclectOption8 = function(){
    	$scope.search();
    }
	
	$scope.changeSeclectOption7 = function(){
    	$scope.search();
    }
	$scope.changeSeclectOption5 = function(){
		$scope.search();
	}

	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
    
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
    
    $scope.reSetSearchOption = function(){
		$scope.examTotalTime = '';
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
		document.getElementById('examTotalTime').value="";
	} 
    pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
    $scope.searchData = function(){
		$scope.search();
	}
    
    //容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].code==item.code){
					$scope.choosedItemArr.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArr.push(item);
		}
	}
    
	$scope.initChoosedItemStatus = function(){
		$scope.selectAll = false;
		for(var i in $scope.page.data){
			var pItem = $scope.page.data[i];
			for(var j in $scope.choosedItemArr){
				var cItem = $scope.choosedItemArr[j];
				if(pItem.code == cItem.code){
					pItem.checked = true;
				}
			}
		}
	}
	
	//导出
    $scope.downlearningDeptActivityExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		location.href="/enterpriseuniversity/services/learningResourseOrgDeptActivity/downloadLearningResourseOrgDeptActivityExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("learningResourseOrgDeptActivityOne") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
    
}])


//[学习资源-组织部门报表统计（课程）]
.controller('LearningDeptCourseController', ['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gm', cmn: '课程统计'};
	$scope.a = $scope.formData = {
		arayacak:'N'
	};
	$scope.advancedSearch = function(){
		if($scope.a.arayacak=="N"){
			$scope.a.arayacak="Y";
		}else{
			$scope.a.arayacak="N";
		}
	}
/*	$scope.activityIn = [
		                 {name: "进行中", value: "0"},
		                 {name: "已结束", value: "1"}
		 	       	 ];
		$scope.needApply = [
		                 {name: "选修", value: "Y"},
		                 {name: "必修", value: "N"}
		 	       	 ];*/
	
		$http.get("/enterpriseuniversity/services/learningResourseOrgDeptCourse/findSectionClassify").success(function (response) {
	        $scope.method8 = response;
	    }).error(function () {
			dialogService.setContent("资源二级下拉菜单初始化错误").setShowDialog(true);
	    });
	
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	$scope.changeSeclectOption1 = function(){
		method2();
		$scope.search();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
		$scope.search();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		$scope.search();
	}
	$scope.changeSeclectOption6 = function(){
	
		$scope.search();
	}
	$scope.changeSeclectOption8=function(){
		$scope.search();
	}
	$scope.changeSeclectOption7=function(){
		$scope.search();
	}
	$scope.changeSeclectOption9=function(){
		$scope.search();
	}
	$scope.queryColumns = [
               {name:"线上资源", value: "1", checked:true},
               {name:"线下资源", value: "2", checked:true}
   	];
       /**
        * 构建查询字符串拼接;
        */
       var buildQueryColumnStr = function(){
       	 $scope.queryColumnStr = "";
       	 $($scope.queryColumns).each(function(index,obj){
    			if(obj.checked){
    				$scope.queryColumnStr += obj.value + ",";
    			}
       	 });
       	 $scope.allIsChecked = isAllChecked();
       	 return $scope.queryColumnStr;
       }
       /**
        * 选中所有的导出字段;
        */
       var checkAllQueryColumn = function(){
   		  $($scope.queryColumns).each(function(index,obj){
   				obj.checked = true; 
   		  });
   		  buildQueryColumnStr();
       }
       /**
        * 删除所有选中的导出字段;
        */
       var delCheckAllQueryColumn = function(){
       	 $($scope.queryColumns).each(function(index,obj){
    			obj.checked = false; 
       	 });
       	 buildQueryColumnStr();
       }
       /**
        * 是否所有都选中;
        */
       function isAllChecked(){
       	 var i = 0;
       	 $($scope.queryColumns).each(function(index,obj){
   				if(obj.checked){
   					i++;
   				}
   		 });
       	 return i == $scope.queryColumns.length;
       }
       /**
        * 切换导出自定义字段选中状态;
        */
       $scope.toggleColumnChecked = function(column){
       	if(column.checked){
       		column.checked = false ;
       	}else{
       		column.checked = true;
       	}
       	$scope.allIsChecked = isAllChecked();
       	buildQueryColumnStr();
       }
       /**
        * 切换所有导出自定义字段选中状态;
        */
       $scope.toggleCheckAllColumn = function(){
       	if($scope.allIsChecked){
       		delCheckAllQueryColumn();
       	} else{
       		checkAllQueryColumn();
       	}
       }
       //初始化自定义导出字符串;
       checkAllQueryColumn();
       $scope.allIsChecked = true;
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.courseName != undefined && $scope.courseName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime !=undefined && $scope.examTotalTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime
		}
        if ($scope.courseType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.courseType
        }
		if ($scope.activityId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityId=" + $scope.activityId
        }
		   if ($scope.activeState !=undefined ) {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activeState=" + $scope.activeState
			}
			if ($scope.isRequired != undefined) 
	        {
	        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired
	        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.orgName != undefined) 
		{
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
		}
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		if ($scope.sectionClassifyName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&sectionClassifyName=" + $scope.sectionClassifyName
        }
		if ($scope.courseOnline != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.courseOnline
        }
		if ($scope.openCourse != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&openCourse=" + $scope.openCourse
        }
		return $scope.$httpPrams;
	}
	
	
	//活动分类
	$http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
    });
	var method6 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findActivityName" + "?projectId=" + $scope.projectId).success(function (response) {
	        $scope.method6 = response;
	    }).error(function () {
			dialogService.setContent("活动名称下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.method7 = [
	                 {name: "线上课程", value: "Y"},
	                 {name: "线下课程", value: "N"}
	 	       	 ];
	$scope.method9 = [
	                 {name: "公开课", value: "Y"},
	                 {name: "非公开课", value: "N"}
	 	       	 ];
	$scope.url = "/enterpriseuniversity/services/learningResourseOrgDeptCourse/learningResourseOrgDeptCourseList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			$scope.$httpPrams=getCondition();
			
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		
		}
	}
	
	
	$scope.changeSeclectOption = function(){
    	$scope.search();
    }
	
	$scope.changeSeclectOption5 = function(){
    	$scope.search();
    	method6();
    }
	
	$scope.downLoadCourseThumbUpExcel=function(courseThumbUpCount,courseId,courseName){
		if(courseThumbUpCount==0){
			dialogService.setContent("没有点赞数").setShowDialog(true);
			return false;
		}
		$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&courseId="+courseId+"&courseName="+courseName;
		location.href="/enterpriseuniversity/services/learningResourseOrgDeptCourse/downLoadCourseThumbUpExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("learningResourseOrgDeptCourseThree") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
	}
	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(a){
    	$scope.currHighLightRow.courseId = a.courseId; 
    }
    
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
    
    $scope.reSetSearchOption = function(){
		$scope.examTotalTime = '';
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
		document.getElementById('examTotalTime').value="";
	} 
    pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('examTotalTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('examTotalTime', '');
	}
	$scope.$on('examTotalTime', function(e,value) {
		$scope.endTime = value;
    });
    $scope.searchData = function(){
		$scope.search();
	}
    
    //容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].code==item.code){
					$scope.choosedItemArr.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArr.push(item);
		}
	}
    
	$scope.initChoosedItemStatus = function(){
		$scope.selectAll = false;
		for(var i in $scope.page.data){
			var pItem = $scope.page.data[i];
			for(var j in $scope.choosedItemArr){
				var cItem = $scope.choosedItemArr[j];
				if(pItem.code == cItem.code){
					pItem.checked = true;
				}
			}
		}
	}
	
	//导出
    $scope.downloadLearningDeptCourseExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		if ($scope.queryColumnStr != ""){
			$httpPrams += "&column=" + $scope.queryColumnStr;
        }
		location.href="/enterpriseuniversity/services/learningResourseOrgDeptCourse/downloadLearningResourseOrgDeptCourseExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("learningResourseOrgDeptCourseOne") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
    
}])


//查看评论具体内容
.controller('ViewCourseCommontcCountController', ['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/learningResourseOrgDeptCourse/findCourseCommentList?pageNum=";
	$scope.openModalDialog = false;
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.courseName != undefined && $scope.courseName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime !=undefined && $scope.examTotalTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime
		}
        if ($scope.courseType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.courseType
        }
       
		if ($scope.activityId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityId=" + $scope.activityId
        }
		   if ($scope.activeState !=undefined ) {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activeState=" + $scope.activeState
			}
			if ($scope.isRequired != undefined) 
	        {
	        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired
	        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.orgName != undefined) 
		{
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
		}
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		return $scope.$httpPrams;
	}
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				/*if($scope.empCode==undefined||$scope.empCode==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&empCode="+$scope.empCode;
				}*/
				if($scope.courseId==undefined||$scope.courseId==undefined){
				$scope.page = {};
				return;
			}else{
				$scope.$httpPrams ="&courseId="+$scope.courseId;
			}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("公开课应该学习人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseCommentCount = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.empCode = item.empCode; 
    }
    
	//导出
    $scope.downloadViewCourseCommontModalExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
//		console.log($parent.getCondition());
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		location.href="/enterpriseuniversity/services/learningResourseOrgDeptCourse/downloadCourseCommentByCourseIdExcel?" + $httpPrams+"&courseId="+$scope.courseId;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("learningResourseOrgDeptCourseTwo") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
}])

//[学习资源-组织部门报表统计（考试）]
.controller('LearningResourseOrgDeptExamController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus){
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gc', cmn: '考试统计'};
	$scope.a = $scope.formData = {
		arayacak:'N'
	};
	$scope.advancedSearch = function(){
		if($scope.a.arayacak=="N"){
			$scope.a.arayacak="Y";
		}else{
			$scope.a.arayacak="N";
		}
	}
	
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	
	
	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	$scope.changeSeclectOption1 = function(){
		method2();
		$scope.search();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
		$scope.search();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		$scope.search();
	}
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.examName != undefined && $scope.examName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&examName=" + $scope.examName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime !=undefined && $scope.examTotalTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime
		}
        if ($scope.courseType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.courseType
        }
		if ($scope.activityId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityId=" + $scope.activityId
        }
		   if ($scope.activeState !=undefined ) {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activeState=" + $scope.activeState
			}
			if ($scope.isRequired != undefined) 
	        {
	        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired
	        }
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
        }
		if ($scope.orgName != undefined) 
		{
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
		}
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		return $scope.$httpPrams;
	}
	
	
	//活动分类
	$http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
    });
	var method6 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findActivityName" + "?projectId=" + $scope.projectId).success(function (response) {
	        $scope.method6 = response;
	    }).error(function () {
			dialogService.setContent("活动名称下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.method7 = [
	                 {name: "线上", value: "Y"},
	                 {name: "线下", value: "N"}
	 	       	 ];
	$scope.method9 = [
	                 {name: "公开课", value: "Y"},
	                 {name: "非公开课", value: "N"}
	 	       	 ];
	$scope.url = "/enterpriseuniversity/services/learningResourseOrgDeptExam/learningResourseOrgDeptExamList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			$scope.$httpPrams=getCondition();
			
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		
		}
	}
	
	$scope.changeSeclectOption = function(){
    	$scope.search();
    }
	
	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow1= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
    
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
    
    $scope.reSetSearchOption = function(){
		$scope.examTotalTime = '';
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
		document.getElementById('examTotalTime').value="";
	} 
    pickedStartTimeFunc = function(){
		$scope.$emit('startTime', $dp.cal.getDateStr());
 	}
	clearStartTimeFunc = function(){
		$scope.$emit('startTime', '');
 	}
	$scope.$on('startTime', function(e,value) {
		$scope.beginTime = value;
    });
	pickedEndTimeFunc = function(){
		$scope.$emit('endTime', $dp.cal.getDateStr());
	}
	clearEndTimeFunc = function(){
		$scope.$emit('endTime', '');
	}
	$scope.$on('endTime', function(e,value) {
		$scope.endTime = value;
    });
    $scope.searchData = function(){
		$scope.search();
	}
    
    //容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].code==item.code){
					$scope.choosedItemArr.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArr.push(item);
		}
	}
    
	$scope.initChoosedItemStatus = function(){
		$scope.selectAll = false;
		for(var i in $scope.page.data){
			var pItem = $scope.page.data[i];
			for(var j in $scope.choosedItemArr){
				var cItem = $scope.choosedItemArr[j];
				if(pItem.code == cItem.code){
					pItem.checked = true;
				}
			}
		}
	}
	
	//导出
    $scope.downloadLearningResourseOrgDeptExamExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		location.href="/enterpriseuniversity/services/learningResourseOrgDeptExam/downloadLearningResourseOrgDeptExamExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("learningResourseOrgDeptExamOne") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
    
}])

//学员报表-学习资源详细（课程）
.controller('EmpLearningDetailedCourseController', ['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval','dialogStatus',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval, dialogStatus){
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gn', cmn: '学员课程报表'};
	$scope.a = $scope.formData = {
		arayacak:'N'
	};
	$scope.advancedSearch = function(){
		if($scope.a.arayacak=="N"){
			$scope.a.arayacak="Y";
		}else{
			$scope.a.arayacak="N";
		}
	}
	$scope.method7 = [
		                 {name: "线上", value: "Y"},
		                 {name: "线下", value: "N"}
		 	       	 ];
	
		$scope.method9 = [
		                 {name: "公开课", value: "Y"},
		                 {name: "非公开课", value: "N"}
		 	       	 ];
	$http.get("/enterpriseuniversity/services/learningResourseOrgDeptCourse/findSectionClassify ").success(function (response) {
        $scope.method8 = response;
    }).error(function () {
		dialogService.setContent("二级资源类别拉菜单初始化错误").setShowDialog(true);
    });
	
	$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method1 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });

	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	$scope.changeSeclectOption1 = function(){
		method2();
		$scope.search();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
		$scope.search();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
		$scope.search();
	}
	$scope.changeSeclectOption4 = function(){
		$scope.search();
	}
	$scope.changeSeclectOption8=function(){
		$scope.search();
	}
	$scope.changeSeclectOption7=function(){
		$scope.search();
	}
	$scope.changeSeclectOption9=function(){
		$scope.search();
	}
	$scope.queryColumns = [
               {name:"资源信息-线上课程", value: "1", checked:true},
               {name:"资源信息-线下班次", value: "2" , checked:true}
   	];
       /**
        * 构建查询字符串拼接;
        */
       var buildQueryColumnStr = function(){
       	 $scope.queryColumnStr = "";
       	 $($scope.queryColumns).each(function(index,obj){
    			if(obj.checked){
    				$scope.queryColumnStr += obj.value + ",";
    			}
       	 });
       	 $scope.allIsChecked = isAllChecked();
       	 return $scope.queryColumnStr;
       }
       /**
        * 选中所有的导出字段;
        */
       var checkAllQueryColumn = function(){
   		  $($scope.queryColumns).each(function(index,obj){
   				obj.checked = true; 
   		  });
   		  buildQueryColumnStr();
       }
       /**
        * 删除所有选中的导出字段;
        */
       var delCheckAllQueryColumn = function(){
       	 $($scope.queryColumns).each(function(index,obj){
    			obj.checked = false; 
       	 });
       	 buildQueryColumnStr();
       }
       /**
        * 是否所有都选中;
        */
       function isAllChecked(){
       	 var i = 0;
       	 $($scope.queryColumns).each(function(index,obj){
   				if(obj.checked){
   					i++;
   				}
   		 });
       	 return i == $scope.queryColumns.length;
       }
       /**
        * 切换导出自定义字段选中状态;
        */
       $scope.toggleColumnChecked = function(column){
       	if(column.checked){
       		column.checked = false ;
       	}else{
       		column.checked = true;
       	}
       	$scope.allIsChecked = isAllChecked();
       	buildQueryColumnStr();
       }
       /**
        * 切换所有导出自定义字段选中状态;
        */
       $scope.toggleCheckAllColumn = function(){
       	if($scope.allIsChecked){
       		delCheckAllQueryColumn();
       	} else{
       		checkAllQueryColumn();
       	}
       }
       //初始化自定义导出字符串;
       checkAllQueryColumn();
       $scope.allIsChecked = true;
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.courseName != undefined && $scope.courseName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.activityName != undefined && $scope.activityName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.code != undefined && $scope.code != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&code=" + $scope.code
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.name != undefined && $scope.name != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&name=" + $scope.name
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
        if ($scope.examTotalTime !=undefined && $scope.examTotalTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime
		}
        if ($scope.courseType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.courseType
        }
       
		if ($scope.activityId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityId=" + $scope.activityId
        }
		   if ($scope.activityState !=undefined ) {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activityState=" + $scope.activityState
			}
			if ($scope.needApply != undefined) 
	        {
	        	$scope.$httpPrams = $scope.$httpPrams + "&needApply=" + $scope.needApply
	        }
		if ($scope.parentClassifyId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&parentClassifyId=" + $scope.parentClassifyId
        }
		if ($scope.orgName != undefined) 
		{
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
		}
		if ($scope.oneDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
        }
		if ($scope.twoDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
        }
		if ($scope.threeDeptName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
        }
		if ($scope.courseOnline != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.courseOnline
        }
		if ($scope.openCourse != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&openCourse=" + $scope.openCourse
        }
		if ($scope.sectionClassifyName !=undefined && $scope.sectionClassifyName !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&sectionClassifyName=" + $scope.sectionClassifyName
		}
		return $scope.$httpPrams;
	}
	
	//活动分类
	$http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
    });

	$scope.activityIn = [
	                 {name: "进行中", value: "0"},
	                 {name: "已结束", value: "1"}
	 	       	 ];
	$scope.needApplyIn = [
	                 {name: "选修", value: "Y"},
	                 {name: "必修", value: "N"}
	 	       	 ];
	$scope.url = "/enterpriseuniversity/services/empLearningDetailedCourse/empLearningDetailedCourseList?pageNum=";
	$scope.paginationConf = {
			currentPage:1,
			totalItems: 10,
			itemsPerPage:20,
			pagesLength: 13,
			perPageOptions:[20,50,100,'全部'],
			rememberPerPage: 'perPageItems',
			onChange: function(){
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$emit('isLoading', true);
				$scope.$httpPrams=getCondition();
				
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
				($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
				$http.get($scope.$httpUrl).success(function (response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				})
			
			}
		}
	
	$scope.changeSeclectOption = function(){
    	$scope.search();
    }
	
	$scope.changeSeclectOption5 = function(){
    	$scope.search();
    }
	
	$scope.changeSeclectOption6 = function(){
    	$scope.search();
    }
	
	//搜索
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow1= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    } 
    
    function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
    
    $scope.reSetSearchOption = function(){
		$scope.examTotalTime = '';
		document.getElementById('examTotalTime').value="";
	} 
    
    $scope.searchData = function(){
		$scope.search();
	}
    
/*    //容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].code==item.code){
					$scope.choosedItemArr.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArr.push(item);
		}
	}
    */
/*	//全选当前函数
	$scope.chooseCurrPageAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			var choosedItemArrBak = $scope.choosedItemArr.concat();
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				for(var j in choosedItemArrBak){
					var cItem = choosedItemArrBak[j];
					if(pItem.code == cItem.code){
						$scope.choosedItemArr.splice($scope.choosedItemArr.indexOf(cItem),1); 
					}
				}
				pItem.checked = false;
			}
		}else{
			$scope.selectAll = true;
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				pItem.checked = true;
				var hasContained = false;
				for(var j in $scope.choosedItemArr){
					var cItem = $scope.choosedItemArr[j];
					if(pItem.code == cItem.code){
						hasContained = true;
						break;
					}
				}
				if(!hasContained){
					$scope.choosedItemArr.push(pItem);
				}
			} 
		}
	}*/
	
	$scope.initChoosedItemStatus = function(){
		$scope.selectAll = false;
		for(var i in $scope.page.data){
			var pItem = $scope.page.data[i];
			for(var j in $scope.choosedItemArr){
				var cItem = $scope.choosedItemArr[j];
				if(pItem.code == cItem.code){
					pItem.checked = true;
				}
			}
		}
	}
    
	//导出
    $scope.downloadEmpLearningDeCourseExcel = function(){
    	$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams=$httpPrams+"&pageNum="+$scope.paginationConf.currentPage + "&pageSize=" +
		($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage);
		if ($scope.queryColumnStr != ""){
			$httpPrams += "&column=" + $scope.queryColumnStr;
        }
		location.href="/enterpriseuniversity/services/empLearningDetailedCourse/downloadEmpLearningDetailedCourseExcel?" + $httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("EmpLearningDetailedCourseOne") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
    }
}])

