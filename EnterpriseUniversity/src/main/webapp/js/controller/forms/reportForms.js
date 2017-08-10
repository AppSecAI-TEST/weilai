/*
 * 权限管理
 * */
angular.module('ele.reportForms', [])
//学员报表-学习资源详细（考试）
.controller('LearningDeptExamController', ['$scope', '$http', '$timeout','dialogService','cookieService','$interval',function ($scope, $http , $timeout, dialogService,cookieService,$interval) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gh', cmn: '学员考试报表'};
	$scope.learningDeptExam = $scope.formData = {
		arayacak:'N'
	};
	$scope.advancedSearch = function(){
		if($scope.learningDeptExam.arayacak=="N"){
			$scope.learningDeptExam.arayacak="Y";
		}else{
			$scope.learningDeptExam.arayacak="N";
		}
	}
	$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method1 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });

	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgcode).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.onedeptcode).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptCode).success(function (response) {
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
	$scope.activityIn = [
	                 {name: "进行中", value: "0"},
	                 {name: "已结束", value: "1"}
	 	       	 ];
	$scope.needApply = [
	                 {name: "选修", value: "Y"},
	                 {name: "必修", value: "N"}
	 	       	 ];
    $scope.queryColumns = [
            {name:"资源信息", value: "1", checked:true},
            {name:"所属活动信息", value: "2" , checked:true}
	];
    /**
     * 构建查询字符串拼接;
     */
    var buildQueryColumnStr = function(){
    	 $scope.queryColumnStr = "";
    	 $($scope.queryColumns).each(function(index,obj){
 			if(obj.checked){
 				$scope.queryColumnStr += obj.name+","+obj.value + "|";
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
	var getCondition = function(){
		$scope.$httpPrams = "";
		if ($scope.examName != undefined && $scope.examName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&examName=" + $scope.examName
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
		if ($scope.examTotalTime != undefined && $scope.examTotalTime !="") {
            $scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime;
        }
		if ($scope.isRequired != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&needApply=" + $scope.isRequired;
        }
        if ($scope.activeState != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityIn=" + $scope.activeState;
        }
        if ($scope.projectId != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method5=" + $scope.projectId;
        }
        if ($scope.activityId != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method6=" + $scope.activityId;
        }
        if ($scope.orgcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method1=" + $scope.orgcode;
        }
        if ($scope.onedeptcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method2=" + $scope.onedeptcode;
        }
        if ($scope.twoDeptCode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method3=" + $scope.twoDeptCode;
        }
        if ($scope.threeDeptCode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method4=" + $scope.threeDeptCode;
        }
        if ($scope.queryColumnStr != "" && $scope.queryColumnStr != undefined){
        	$scope.$httpPrams = $scope.$httpPrams + "&queryColumnStr=" + $scope.queryColumnStr;
        }
        return  $scope.$httpPrams;
	}
	$scope.url = "/enterpriseuniversity/services/learningDeptExam/learningDeptExamList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 8,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			getCondition();
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
		document.getElementById('examTotalTime').value="";
	} 
    
    $scope.searchData = function(){
		$scope.search();
	}
    $scope.downloadLearningDeptExamExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+getCondition();
		location.href="/enterpriseuniversity/services/learningDeptExam/downloadLearningDeptExamExcel?"+$httpPrams;
		var closeLearningDeptExam = $interval(function(){
			if(cookieService.getCookie("learningDeptExam") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeLearningDeptExam);
			}
    	},500);
	}
}])

//活动组织部门统计
.controller('ActivityDeptController',['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval',function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gb', cmn: '部门-活动统计'};
   
	
	    $http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
	        $scope.method = response;
	    }).error(function () {
			dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
	    });
	    
	    var method1=function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findActivityName" + "?projectId=" + $scope.projectId).success(function (response) {
	        $scope.method1 = response;
	    }).error(function () {
			dialogService.setContent("活动名称下拉菜单初始化错误").setShowDialog(true);
	    });
   }
		$scope.changeSeclectOption5 = function(){
			method1();
			$scope.search();
		}
		$scope.changeSeclectOption6 = function(){
			$scope.search();
		}
	 $scope.method2 = [
	           	      {name: "进行中", value: "1"},
	           	      {name: "未开始", value: "2"},
	           	      {name: "已过期", value: "3"}
	   ];
	 $scope.method3 = [
	                   {name: "必修", value: "N"},
	                   {name: "选修", value: "Y"}
	   ];
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });

	var method5 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgname).success(function (response) {
	        $scope.method5 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	var method6 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.method6 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method7 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.method7 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	$scope.changeSeclectOption1 = function(){
		method5();
		$scope.search();
	}
	$scope.changeSeclectOption2 = function(){
		method6();
		$scope.search();
	}
	$scope.changeSeclectOption3 = function(){
		method7();
		$scope.search();
	}
	var getCondition=function(){
		$scope.$httpPrams="";
		if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
		}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
		}
		/*if ($scope.activityType != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityType=" + $scope.activityType
        }*/
		if ($scope.projectId != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityType=" + $scope.projectId
        }
		if ($scope.activityName != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
        }
		if ($scope.status != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&status=" + $scope.status
        }
		if ($scope.isRequired != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired
        }
		if ($scope.orgname != undefined) 
        {
        	$scope.$httpPrams = $scope.$httpPrams + "&orgname=" + $scope.orgname
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
	 $scope.url = "/enterpriseuniversity/services/activityDept/activityDeptList?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
		onChange : function() {
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			$scope.$httpPrams=getCondition();
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
				+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+$scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function(response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			}).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("活动-组织部门查询异常！").setShowDialog(true);
            });
		}
    }
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
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
    
    var preRowItem;
    $scope.highLightCurrRow1= function(item){
    	if(preRowItem){
    		preRowItem.checked = undefined;
    	}
    	item.checked = !item.checked ; 
    	preRowItem = item;
    }  
 
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    
    /*
	 * 勾选功能
	 */
	//容器
	$scope.choosedItemArr = [];
	$scope.downloadActivityStatisticsExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+getCondition();
		location.href="/enterpriseuniversity/services/activityDept/downloadActivityDeptListExcel?"+$httpPrams;
		var closeActivityDept = $interval(function(){
			if(cookieService.getCookie("activityDept") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeActivityDept);
			}
    	},500);
	}
}])

.controller('orgPingtaiReportController', ['$scope', '$http', 'dialogService','cookieService','$interval',function ($scope, $http, dialogService,cookieService,$interval) {
    $scope.$parent.cm = {pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gj', cmn: '平台统计'};
    $scope.url = "/enterpriseuniversity/services/empLearningAction/orgPingtaiList?1=1";
    var getCondition = function(){
    	$scope.$httpPrams = "";
    	 if ($scope.beginTime !=undefined && $scope.beginTime !="") {
         	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
 		}
         if ($scope.endTime !=undefined && $scope.endTime !="") {
         	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
 		}
			if ($scope.deptname != undefined && $scope.deptname !="") {
         	$scope.$httpPrams = $scope.$httpPrams + "&deptname=" + $scope.deptname
         }
			if ($scope.onedeptname != undefined && $scope.onedeptname !="") {
         	$scope.$httpPrams = $scope.$httpPrams + "&onedeptname=" + $scope.onedeptname
         }
			if ($scope.thirdName != undefined && $scope.thirdName !="") {
         	$scope.$httpPrams = $scope.$httpPrams + "&thirdName=" + $scope.thirdName
         }
    	return $scope.$httpPrams;
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	getCondition();
            $scope.$httpUrl = $scope.url + $scope.$httpPrams;
            	//+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
           
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                //$scope.paginationConf.totalItems = response.count;
              //  $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("平台统计数据查询异常！").setShowDialog(true);
            });
        }
    };
    //搜索
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    $scope.flag ="N";
    $scope.highsearchData = function () {
    	
    	if($scope.flag=="Y"){
    		$scope.flag = "N";
    	}else{
    		$scope.flag = "Y";
    	}
    };
    //时间段查询
    /*$scope.beginTime = getDate(-1);
	$scope.endTime = getDate(0);
	*/
	$scope.searchData = function(){
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
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
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.createDate = item.createDate; 
    }
    $http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method1 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });
	$scope.flag ="N";
    $scope.highsearchData = function () {
    	
    	if($scope.flag=="Y"){
    		$scope.flag = "N";
    	}else{
    		$scope.flag = "Y";
    	}
    };

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
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.deptname).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	$scope.changeSeclectOption1 = function(){
		method2();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
	}
	$scope.downLoadPingtaiExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		location.href="/enterpriseuniversity/services/empLearningAction/downLoadPingtaiExcel?"+$httpPrams.substring(1);
		var closePingtai = $interval(function(){
			if(cookieService.getCookie("Pingtai") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closePingtai);
			}
    	},500);
	}
}])
.controller('StuLearnResourcesController', ['$scope', '$http', '$state','dialogService','$timeout','cookieService','$interval', function ($scope, $http, $state,dialogService,$timeout,cookieService,$interval) {
    $scope.$emit("cm", { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gl', cmn: '学员统计'}); 
    $scope.url = "/enterpriseuniversity/services/empLearningAction/stuLearnResourcesList?pageNum=";
    $scope.downurl = "/enterpriseuniversity/services/empLearningAction/downLoadStuleranResourceExcel?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.adminName != undefined && $scope.adminName != "") {

            	$scope.$httpPrams = $scope.$httpPrams + "&name=" 
            		+ $scope.adminName.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            if ($scope.beginTime !=undefined && $scope.beginTime !="") {
            	$scope.$httpPrams = $scope.$httpPrams + "&beginTimeq=" + $scope.beginTime
        	}
            if ($scope.endTime !=undefined && $scope.endTime !="") {
            	$scope.$httpPrams = $scope.$httpPrams + "&endTimeq=" + $scope.endTime
        	}
        	if ($scope.deptname != undefined && $scope.deptname !="") {
            	$scope.$httpPrams = $scope.$httpPrams + "&deptname=" + $scope.deptname
            }
        	if ($scope.onedeptname != undefined && $scope.onedeptname !="") {
            	$scope.$httpPrams = $scope.$httpPrams + "&onedeptname=" + $scope.onedeptname
            }
        	if ($scope.thirdName != undefined && $scope.thirdName !="") {
            	$scope.$httpPrams = $scope.$httpPrams + "&thirdName=" + $scope.thirdName
            }
        	 
        	$scope.$emit('isLoading', true);
            
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
             $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });
        }
    };
    
    var columeStr, columeStrArr;
    var buildQueryColumeStr = function(){
    	columeStr = "";
    	columeStrArr = [];
    	for(var i in $scope.queryColumes){
    		if($scope.queryColumes[i].checked){
    			columeStrArr.push($scope.queryColumes[i].value);
    		}
    	}
    	columeStr =  columeStrArr.join(",");
    	
    }
    
    var checkAllQueryColume = function(){
    	for(var i in $scope.queryColumes){
    		$scope.queryColumes[i].checked = true; 
    	}
    	buildQueryColumeStr();
    }
    
    var delCheckAllQueryColume = function(){
    	for(var i in $scope.queryColumes){
    		$scope.queryColumes[i].checked = false; 
    	}
    	buildQueryColumeStr();
    }
    
    $scope.queryColumes = [
       {name:"必修活动内线上课程和考试", value: "mokuai1", checked:true},
       {name:"选修活动内线上课程和考试", value: "mokuai2" , checked:true},
       {name:"非活动内线上课程和考试", value: "mokuai3", checked:true},
       {name:"必修活动内线下课程", value: "mokuai4", checked:true},
       {name:"选修活动内线下课程", value: "mokuai5", checked:true},
    ];
    
    $scope.toggleColumeChecked = function(colume){
    	if(colume.checked){
    		 colume.checked = false ;
    		 $scope.allIsChecked = false; 
    	}else{
    		colume.checked = true;
    	}
    	
    	buildQueryColumeStr();
    }
    
    $scope.toggleCheckAllColume = function(){
    	if($scope.allIsChecked){
    		delCheckAllQueryColume() ;
    		$scope.allIsChecked = false; 
    	} else{
    		checkAllQueryColume() ;
    		$scope.allIsChecked = true;
    	}
    }
    
    buildQueryColumeStr();
    $scope.allIsChecked=true;
    $scope.reportResources = function(){
    	$scope.$emit('isLoading', true);
    	$scope.$downhttpUrl = "";
    	$scope.$downPrams = "";
        if ($scope.adminName != undefined && $scope.adminName != "") {

        	$scope.$downPrams = $scope.$downPrams + "&name=" 
        		+ $scope.adminName.replace(/\%/g,"%25").replace(/\#/g,"%23")
    			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
    			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
        }
        if ($scope.beginTime !=undefined && $scope.beginTime !="") {
        	$scope.$downPrams = $scope.$downPrams + "&beginTimeq=" + $scope.beginTime
    	}
        if ($scope.endTime !=undefined && $scope.endTime !="") {
        	$scope.$downPrams = $scope.$downPrams + "&endTimeq=" + $scope.endTime
    	}
    	if ($scope.deptname != undefined && $scope.deptname !="") {
        	$scope.$downPrams = $scope.$downPrams + "&deptname=" + $scope.deptname
        }
    	if ($scope.onedeptname != undefined && $scope.onedeptname !="") {
        	$scope.$downPrams = $scope.$downPrams + "&onedeptname=" + $scope.onedeptname
        }
    	if ($scope.thirdName != undefined && $scope.thirdName !="") {
        	$scope.$downPrams = $scope.$downPrams + "&thirdName=" + $scope.thirdName
        }
    	if(columeStr!=""){
    		$scope.$downPrams = $scope.$downPrams + "&columes=" + columeStr
    	}
    	$scope.$downhttpUrl = $scope.downurl + $scope.paginationConf.currentPage 
    	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
    	+ $scope.$downPrams;
    	location.href=$scope.$downhttpUrl;
    	var closeStuleranResource = $interval(function(){
			if(cookieService.getCookie("stuleranResource") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeStuleranResource);
			}
    	},500);
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
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    $scope.searchData = function(){
		$scope.search();
	}
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
     
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.userId = item.userId; 
    }  
    
	$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method1 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });
	$scope.flag ="N";
    $scope.highsearchData = function () {
    	
    	if($scope.flag=="Y"){
    		$scope.flag = "N";
    	}else{
    		$scope.flag = "Y";
    	}
    };
   /* location.href="/enterpriseuniversity/services/empLearningAction/downloadempLearningActionExcel?codes=" + codes + $httpPrams;
    location.href="/enterpriseuniversity/services/empLearningAction/downLoadStuleranResourceExcel?pageNum={{page.pageNum}}&pageSize={{page.pageSize}}&beginTimeq={{beginTime}}&endTimeq={{endTime}}&deptname={{deptname}}&onedeptname={{onedeptname}}&thirdName={{thirdName}}&name={{adminName}}
    */
    
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
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.deptname).success(function (response) {
	        $scope.method4 = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	
	$scope.changeSeclectOption1 = function(){
		method2();
	}
	$scope.changeSeclectOption2 = function(){
		method3();
	}
	$scope.changeSeclectOption3 = function(){
		method4();
	}
}])

//学员报表-活动详情
.controller('EmpActivityDetailController', ['$scope', '$http', '$timeout','dialogService','cookieService','$interval', function ($scope, $http , $timeout, dialogService,cookieService,$interval) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gf', cmn: '学员活动报表'};
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
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgcode).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}

	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.onedeptcode).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.deptname).success(function (response) {
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
	$scope.activityIn = [
	                 {name: "进行中", value: "0"},
	                 {name: "已过期", value: "1"},
	                 {name: "未开始", value: "2"}
	 	       	 ];
	$scope.needApply = [
	                 {name: "选修", value: "Y"},
	                 {name: "必修", value: "N"}
	 	       	 ];
	var getCondition=function(){
		$scope.$httpPrams="";
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
		if ($scope.examTotalTime != undefined && $scope.examTotalTime !="") {
            $scope.$httpPrams = $scope.$httpPrams + "&examTotalTime=" + $scope.examTotalTime;
        }
		if ($scope.isRequired != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&needApply=" + $scope.isRequired;
        }
        if ($scope.activeState != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&activityIn=" + $scope.activeState;
        }
        if ($scope.projectId != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method5=" + $scope.projectId;
        }
        if ($scope.activityId != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method6=" + $scope.activityId;
        }
        if ($scope.orgcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method1=" + $scope.orgcode;
        }
        if ($scope.onedeptcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method2=" + $scope.onedeptcode;
        }
        if ($scope.deptname != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method3=" + $scope.deptname;
        }
        if ($scope.thirdName != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method4=" + $scope.thirdName;
        }
    	return $scope.$httpPrams;
	}
	$scope.url = "/enterpriseuniversity/services/empActivityDetail/empActivityDetailList?pageNum=";
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
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
			+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+$scope.$httpPrams;
			
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
    
    $scope.reSetSearchOption = function(){
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
	};
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
	
	function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	};
    
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
	$scope.downloadEmpActivityDetaiListExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams=getCondition();
		$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+$scope.$httpPrams;
//		alert($httpPrams);
		location.href="/enterpriseuniversity/services/empActivityDetail/empActivityDetailExcel?"+$httpPrams;
/*		$http.get($scope.$httpUrl).success(function (response) {
			$scope.$emit('isLoading', false);
		})*/
		var closeEmpActivityDept =  $interval(function(){
			if(cookieService.getCookie("empActivityDeta") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeEmpActivityDept);
			}
    	},500);
	}
}])

//学员-活动
.controller('EmpActivityController', ['$scope', '$http', '$timeout','dialogService', 'cookieService','$interval',function ($scope, $http , $timeout, dialogService ,cookieService,$interval) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-ge', cmn: '学员活动统计'};
	
	$scope.empActivity = $scope.formData = {
			arayacak:'N'
	};
	
	$scope.advancedSearch = function(){
		if($scope.empActivity.arayacak=="N"){
			$scope.empActivity.arayacak="Y";
		}else{
			$scope.empActivity.arayacak="N";
		}
	};
	
	//活动分类
	$http.get("/enterpriseuniversity/services/empLearningAction/findActivityClassify").success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("活动分类下拉菜单初始化错误").setShowDialog(true);
    });
	
	$scope.isRequireds = [
        {name: "选修", value: "N"},
        {name: "必修", value: "Y"}
   	];
	
	$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method1 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });
	var method2 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgcode).success(function (response) {
	        $scope.method2 = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method3 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.onedeptcode).success(function (response) {
	        $scope.method3 = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var method4 = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.deptname).success(function (response) {
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
	
	$scope.changeSeclectOption = function(){
    	$scope.search();
    }
	
	$scope.changeSeclectOption5 = function(){
    	$scope.search();
    }
	 $scope.queryColumns = [
            {name:"必修活动统计", value: "1", checked:true},
            {name:"选修活动统计", value: "2" , checked:true}
	];
    /**
     * 构建查询字符串拼接;
     */
    var buildQueryColumnStr = function(){
    	 $scope.queryColumnStr = "";
    	 $($scope.queryColumns).each(function(index,obj){
 			if(obj.checked){
 				$scope.queryColumnStr += obj.name+","+obj.value + "|";
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
	var getCondition = function(){
		$scope.$httpPrams = "";
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
        if ($scope.projectId != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method5=" + $scope.projectId;
        }
        if ($scope.isRequired != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&isRequired=" + $scope.isRequired;
        }
        if ($scope.orgcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method1=" + $scope.orgcode;
        }
        if ($scope.onedeptcode != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method2=" + $scope.onedeptcode;
        }
        if ($scope.deptname != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method3=" + $scope.deptname;
        }
        if ($scope.thirdName != undefined) {
        	$scope.$httpPrams = $scope.$httpPrams + "&method4=" + $scope.thirdName;
        }
        if ($scope.queryColumnStr != "" && $scope.queryColumnStr != undefined){
        	$scope.$httpPrams = $scope.$httpPrams + "&queryColumnStr=" + $scope.queryColumnStr;
        }
        return $scope.$httpPrams;
	}
	$scope.url = "/enterpriseuniversity/services/empActivity/empActivityList?pageNum=";
	$scope.paginationConf = {
		currentPage:1,
		totalItems: 10,
		itemsPerPage:20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = getCondition();
			$scope.$emit('isLoading', true);
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
				$scope.page = response;
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	};
	
	//搜索
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
    };
    
    //重置
    $scope.reSetSearchOption = function(){
		$scope.beginTime = '';
		$scope.endTime = '';
		document.getElementById('startTime').value="";
		document.getElementById('endTime').value="";
	};
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
	
	function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	};
	
	//查询时间搜索
	$scope.searchData = function(){
		$scope.search();
	};
	
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    };
     
    //高亮显示选中行
    var preRowItem;
    $scope.highLightCurrRow= function(item){
		if(preRowItem){
			preRowItem.checked = undefined;
		}
		item.checked = !item.checked ; 
		preRowItem = item;
    }
    
    $scope.downloadEmpActivityExcel = function(){
		$scope.$emit('isLoading', true);
		$httpPrams="pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+getCondition();
		location.href="/enterpriseuniversity/services/empActivity/downloadEmpActivityExcel?"+$httpPrams;
		var closeEmpActivityLoading = $interval(function(){
			if(cookieService.getCookie("empActivity") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeEmpActivityLoading);
			}
    	},500);
	}
}])

//统计报表-部门-活动学习数据
.controller('orgActivitylearnController', ['$scope', '$http','$state', '$timeout','dialogService','cookieService','$interval', function ($scope, $http,$state, $timeout, dialogService,cookieService,$interval) {
	$scope.$parent.cm = { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gy', cmn: '活动统计'};
	
	$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
        $scope.method4 = response;
    }).error(function () {
		dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
    });

var method5 = function(){
	$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgname).success(function (response) {
        $scope.method5 = response;
    }).error(function () {
		dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
    });
}

var method6 = function(){
	$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
        $scope.method6 = response;
    }).error(function () {
		dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
    });
}
var method7 = function(){
	$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
        $scope.method7 = response;
    }).error(function () {
		dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
    });
}
$scope.changeSeclectOption1 = function(){
	method5();
	$scope.search();
}
$scope.changeSeclectOption2 = function(){
	method6();
	$scope.search();
}
$scope.changeSeclectOption3 = function(){
	method7();
	$scope.search();
}
$scope.changeSeclectOption = function(){
	$scope.search();
}
var getCondition=function(){
		if ($scope.orgName != undefined && $scope.orgName != "") {
			$scope.$httpPrams = $scope.$httpPrams + "&orgName=" + $scope.orgName
				.replace(/\%/g,"%25").replace(/\#/g,"%23")
				.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
				.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
		}
		if ($scope.orgname != undefined) 
	    {
	    	$scope.$httpPrams = $scope.$httpPrams + "&orgname=" + $scope.orgname
	    }
		if ($scope.oneDeptName != undefined) 
	    {
	    	$scope.$httpPrams = $scope.$httpPrams + "&oneDeptName=" + $scope.oneDeptName
	    }
		if ($scope.twoDeptName != undefined) 
	    {
	    	$scope.$httpPrams = $scope.$httpPrams + "&twoDeptName=" + $scope.twoDeptName
	    }
	//	alert($scope.threeDeptName)
		if ($scope.threeDeptName != undefined) 
	    {
	    	$scope.$httpPrams = $scope.$httpPrams + "&threeDeptName=" + $scope.threeDeptName
	    }
		return $scope.$httpPrams;
}
	$scope.url = "/enterpriseuniversity/services/orgActivityLearn/orgActivitylearn?pageNum=";
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
//			alert($scope.$httpUrl)
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
    
    //导出
    $scope.downloadOrgActivityLearnExcel=function(){
    	$scope.$emit('isLoading',true);
    	$httpPrams=getCondition();
		$httpPrams=$httpPrams+"pageNum="+$scope.paginationConf.currentPage+"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+$scope.$httpPrams;
		location.href="/enterpriseuniversity/services/orgActivityLearn/orgActivityLearnExcel?"+$httpPrams;
		var closeOrgActivityLearn =  $interval(function(){
			if(cookieService.getCookie("orgActivityLearn") != null){
				$scope.$emit('isLoading', false);
				$interval.cancel(closeOrgActivityLearn);
			}
    	},500);
    }
}])
