/*
 * 权限管理
 * */
angular.module('ele.reportAcemon', [])
/*
 * 学习行为报表
 */
.controller('EmpLearningActionController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval', function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval) {
	$scope.$emit('cm', { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-ga', cmn: '学员统计'});
		$scope.a = $scope.formData = {
				arayacak:'N'
			};
		$scope.viewSignUpStatistic = function(){
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
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgname).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.onedeptname).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoCode).success(function (response) {
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
	var getCondition = function(){
		$scope.$httpPrams = "";
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
        if ($scope.onedeptname != undefined && $scope.onedeptname !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&onedeptname=" + $scope.onedeptname
        }
        if ($scope.orgname != undefined && $scope.orgname !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&orgname=" + $scope.orgname
        }
		if ($scope.threeCode != undefined && $scope.threeCode !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&threeCode=" + $scope.threeCode
        }
		if ($scope.twoCode != undefined && $scope.twoCode !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&twoCode=" + $scope.twoCode
        }
		return $scope.$httpPrams;
	}
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.url = "/enterpriseuniversity/services/empLearningAction/empLearningActionList?pageNum=";
				$scope.$httpPrams=getCondition();
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
	            	$scope.$emit('isLoading', false);
	            	dialogService.setContent("学习行为报表统计异常！").setShowDialog(true);
	            });
			}
	};
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    $scope.changeSeclectOption = function(){
    	$scope.search();
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
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow = function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
    $scope.downloadEmpLearningActionExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+getCondition();
		location.href="/enterpriseuniversity/services/empLearningAction/downloadempLearningActionExcel?"+$httpPrams;
		var closeEmpLearningAction = $interval(function(){
			if(cookieService.getCookie("empLearningAction") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeEmpLearningAction);
			}
    	},500);
	}
}])
/*
 * 问卷-学员内容导出
 */
		.controller('QuestionnaireDetailController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval) {
			$scope.$emit('cm', { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gi', cmn: '问卷-学员内容'});
			$scope.a = $scope.formData = {
					arayacak:'N'
				};
			$scope.viewSignUpStatistic = function(){
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
			$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgname).success(function (response) {
		        $scope.method2 = response;
		    }).error(function () {
				dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
		    });
		}
		
		var method3 = function(){
			$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.onedeptname).success(function (response) {
		        $scope.method3 = response;
		    }).error(function () {
				dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
		    });
		}
		var method4 = function(){
			$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoCode).success(function (response) {
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
		$scope.changeSeclectOption5 = function(){
		method6();
		$scope.search();
		}
		$scope.status = [
		             {name: "进行中", value: "1"},
		             {name: "已结束", value: "2"}
			       	 ];
		$scope.isRequired = [
		             {name: "选修", value: "N"},
		             {name: "必修", value: "Y"}
			       	 ];
		var getCondition = function(){
			$scope.$httpPrams = "";
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
			if ($scope.completedTime !=undefined && $scope.completedTime !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&completedTime=" + $scope.completedTime
			}
			if ($scope.examName !=undefined && $scope.examName !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&examName=" + $scope.examName
			}
	        if ($scope.orgname != undefined && $scope.orgname !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&orgname=" + $scope.orgname
	        }
			if ($scope.threeCode != undefined && $scope.threeCode !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&threeCode=" + $scope.threeCode
	        }
			if ($scope.twoCode != undefined && $scope.twoCode !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&twoCode=" + $scope.twoCode
	        }
			if ($scope.onedeptname != undefined && $scope.onedeptname !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&onedeptname=" + $scope.onedeptname
	        }
			if ($scope.activityStatus != undefined && $scope.activityStatus !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activityStatus=" + $scope.activityStatus
	        }
			if ($scope.isRequired1 != undefined && $scope.isRequired1 !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired1
	        }
			if ($scope.projectId != undefined && $scope.projectId !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.projectId
	        }
			if ($scope.activityName != undefined && $scope.activityName !="") {
	        	$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
	        }
			return $scope.$httpPrams;
		}
		//活动状态完
		$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/empLearningAction/findQuestionnaireDetailList?pageNum=";
				getCondition();
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
				$scope.$emit('isLoading', true);
				$http.get($scope.$httpUrl).success(function(response) {
					$scope.$emit('isLoading', false);
					$scope.page = response;
					$scope.paginationConf.totalItems = response.count;
					$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				}).error(function(){
		        	$scope.$emit('isLoading', false);
		        	dialogService.setContent("问卷导出行为异常！").setShowDialog(true);
		        });
			}
		};
		$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
		};
		$scope.changeSeclectOption = function(){
		$scope.search();
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
		$scope.completedTime = '';
		document.getElementById('completedTime').value="";
		} 
		pickedCompletedTimeFunc = function(){
		$scope.$emit('completedTime', $dp.cal.getDateStr());
		}
		clearCompletedTimeFunc = function(){
		$scope.$emit('completedTime', '');
		}
		$scope.$on('completedTime', function(e,value) {
		$scope.completedTime = value;
		});
		//回车自动查询
		$scope.autoSearch = function($event){
		if($event.keyCode==13){
			$scope.search();
		}
		}
		
		//高亮显示选中行
		var preRowItem;
	    $scope.highLightCurrRow = function(item){
	    	if(preRowItem){
	    		preRowItem.checked = undefined;
	    	}
	    	item.checked = !item.checked ; 
	    	preRowItem = item;
	    }  		 
	    $scope.downLoadQuestionnaireDetailExcel = function(){
			$scope.$emit('isLoading', true);
			$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+getCondition();
			location.href="/enterpriseuniversity/services/empLearningAction/downLoadQuestionnaireDetailExcel?"+$httpPrams;
			var closeQuestionnaireDetail = $interval(function(){
				if(cookieService.getCookie("questionnaireDetail") != null){
					$scope.$emit('isLoading', false);
					$interval.cancel(closeQuestionnaireDetail);
				}
	    	},500);
		}
}])
