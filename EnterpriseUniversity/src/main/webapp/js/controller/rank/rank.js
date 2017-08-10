/*
 * 排行统计
 * */
angular.module('ele.rankStatistic', [])
.controller('EmpLearningTimeRankController', ['$scope', '$http', '$timeout', '$q', 'dialogService', function ($scope, $http , $timeout, $q, dialogService) {
	$scope.$emit('cm', { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-go', cmn: '学习时长排行'});
	$scope.url = "/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingListStudyTime?pageNum=";
	$scope.deferred = $q.defer();
	var $httpPramsArr = ["beginTimeq", "endTimeq", "orgName", "oneDeptName", "twoDeptName", "threeDeptName"];
	var init$httpPrams = function(){
		for(var i in $httpPramsArr){
			if($scope[$httpPramsArr[i]] != "" && $scope[$httpPramsArr[i]] != undefined){
				$scope.$httpPrams = $scope.$httpPrams + "&" + $httpPramsArr[i] + "=" + $scope[$httpPramsArr[i]];
			}
		}
	}
	var getDate = function(step){
		var dd = new Date();
	    dd.setDate(dd.getDate() + step);
		return dd.getFullYear() + "-" + (dd.getMonth()+1) + "-" + dd.getDate();
	}
	var initLegend = function(){
		if($scope.beginTimeq != "" && $scope.beginTimeq != undefined && $scope.endTimeq != "" && $scope.endTimeq != undefined){
			return $scope.beginTimeq + "--" + $scope.endTimeq;
		}else if($scope.beginTimeq != "" && $scope.beginTimeq != undefined){
			return $scope.beginTimeq + "--" + getDate(0);
		}else if($scope.endTimeq != "" && $scope.endTimeq != undefined){
			return "截止" + $scope.endTimeq;
		}else{
			return getDate(0);
		}
	}
	var formatChartData = function(rs){
		var chartData = {
			subtext :"学习时长",	
			chartLegend	: [],	
			chartYAxis : [],
			chartData : [] 
		};
		if(!rs || !rs.data){
			return chartData;
		}
		var chartDataItem = [];
		for(var i in rs.data){
			var currItem = rs.data[i];
			chartData.chartYAxis.push(currItem.name);
			chartDataItem.push(parseFloat((currItem.time/60).toFixed(2)));
		}
		chartData.chartData.push(chartDataItem);
		if(chartData.chartData.length > 0) {
			chartData.chartLegend.push(initLegend());
		}
		return chartData;
	}
	var findOrgname = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.orgs = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	}();
	var findFirstDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.firstDepts = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findSecondDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.secondDepts = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findThirdDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.thirdDepts = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	//分页
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			init$httpPrams();
			console.log("$scope.$httpPrams = ", $scope.$httpPrams);
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				var chartData = formatChartData(response);
				//$scope.deferred = $q.defer();
				//console.log(chartData);
				$scope.$broadcast("$$chartData", chartData);
				$scope.$emit('isLoading', false);
				//$scope.deferred.resolve(chartData);
				//$scope.deferred = $q.defer();
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTimeq = "";
		$scope.endTimeq = "";
	}
	$scope.moreQueryOptionSearch = function(){
		$scope.showMoreQueryOption = !$scope.showMoreQueryOption;
	}
	$scope.searchData = function(){
		$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.onChange() : $scope.paginationConf.currentPage = 1;
	}
	$scope.changeSeclectedOrgcode = function(){
		if($scope.orgName != "" && $scope.orgName != undefined) {
			findFirstDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedOnedeptcode = function(){
		if($scope.oneDeptName != "" && $scope.oneDeptName != undefined) {
			findSecondDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedDeptname = function(){
		if($scope.twoDeptName != "" && $scope.twoDeptName != undefined) {
			findThirdDeptName(); 
		}
		$scope.searchData();
	}
	$scope.changeSeclectedThirdName = function(){
		$scope.searchData();
	}
	
	/*$scope.$watch(function(){
		return 
	}, function(){
		$scope.searchData();
	});*/
	$scope.downloadstuCourseRankingExcel = function(){
		$scope.$emit('isLoading',true);
		location.href="/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingListStudyTimeExcel?pageNum=" 
			+ $scope.paginationConf.currentPage + "&pageSize=" +($scope.paginationConf.itemsPerPage=="全部" ? -1: $scope.paginationConf.itemsPerPage)
			+ ($scope.activityName ?  "&activityName=" + $scope.activityName  : "")
			+ ($scope.beginTime ?  "&beginTimeq=" + $scope.beginTime  : "")
			+ ($scope.endTime ?  "&endTimeq=" + $scope.endTime  : "")
			+ ($scope.orgName ?  "&orgName=" + $scope.orgName  : "")
			+ ($scope.oneDeptName ?  "&oneDeptName=" + $scope.oneDeptName  : "")
			+ ($scope.twoDeptName ?  "&twoDeptName=" + $scope.twoDeptName  : "")
			+ ($scope.threeDeptName ?  "&threeDeptName=" + $scope.threeDeptName : "");
		$http.get($scope.$httpUrl).success(function (response) {
			$scope.$emit('isLoading', false);
		})
	}
	
}])
.controller('EmpLearningScoreRankController', ['$scope', '$http', '$timeout', '$q', 'dialogService', function ($scope, $http , $timeout, $q, dialogService) {
	$scope.$emit('cm', { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gg', cmn: '学员排行'});
	$scope.url = "/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingListScore?pageNum=";
	$scope.deferred = $q.defer();
	var $httpPramsArr = ["beginTimeq", "endTimeq", "orgName", "oneDeptName", "twoDeptName", "threeDeptName"];
	var init$httpPrams = function(){
		for(var i in $httpPramsArr){
			if($scope[$httpPramsArr[i]] != "" && $scope[$httpPramsArr[i]] != undefined){
				$scope.$httpPrams = $scope.$httpPrams + "&" + $httpPramsArr[i] + "=" + $scope[$httpPramsArr[i]];
			}
		}
	}
	var getDate = function(step){
		var dd = new Date();
	    dd.setDate(dd.getDate() + step);
		return dd.getFullYear() + "-" + (dd.getMonth()+1) + "-" + dd.getDate();
	}
	var initLegend = function(){
		if($scope.beginTimeq != "" && $scope.beginTimeq != undefined && $scope.endTimeq != "" && $scope.endTimeq != undefined){
			return $scope.beginTimeq + "--" + $scope.endTimeq;
		}else if($scope.beginTimeq != "" && $scope.beginTimeq != undefined){
			return $scope.beginTimeq + "--" + getDate(0);
		}else if($scope.endTimeq != "" && $scope.endTimeq != undefined){
			return "截止" + $scope.endTimeq;
		}else{
			return getDate(0);
		}
	}
	var formatChartData = function(rs){
		var chartData = {
			subtext :"学员学分排行",	
			chartLegend	: [],	
			chartYAxis : [],
			chartData : [] 
		};
		if(!rs || !rs.data){
			return chartData;
		}
		var chartDataItem = [];
		for(var i in rs.data){
			var currItem = rs.data[i];
			chartData.chartYAxis.push(currItem.name);
			chartDataItem.push(parseFloat(currItem.exchangeNumber.toFixed(2)));
		}
		chartData.chartData.push(chartDataItem);
		if(chartData.chartData.length > 0) {
			chartData.chartLegend.push(initLegend());
		}
		return chartData;
	}
	var findOrgname = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.orgs = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	}();
	var findFirstDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.firstDepts = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findSecondDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.secondDepts = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findThirdDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.thirdDepts = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	//分页
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			init$httpPrams();
			console.log("$scope.$httpPrams = ", $scope.$httpPrams);
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			
			$http.get($scope.$httpUrl).success(function (response) {
				var chartData = formatChartData(response);
				//$scope.deferred = $q.defer();
				//console.log(chartData);
				$scope.$broadcast("$$chartData", chartData);
				$scope.$emit('isLoading', false);
				//$scope.deferred.resolve(chartData);
				//$scope.deferred = $q.defer();
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTimeq = "";
		$scope.endTimeq = "";
	}
	$scope.moreQueryOptionSearch = function(){
		$scope.showMoreQueryOption = !$scope.showMoreQueryOption;
	}
	$scope.searchData = function(){
		$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.onChange() : $scope.paginationConf.currentPage = 1;
	}
	$scope.changeSeclectedOrgcode = function(){
		if($scope.orgName != "" && $scope.orgName != undefined) {
			findFirstDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedOnedeptcode = function(){
		if($scope.oneDeptName != "" && $scope.oneDeptName != undefined) {
			findSecondDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedDeptname = function(){
		if($scope.twoDeptName != "" && $scope.twoDeptName != undefined) {
			findThirdDeptName(); 
		}
		$scope.searchData();
	}
	$scope.changeSeclectedThirdName = function(){
		$scope.searchData();
	}
	
	$scope.downloadLearningscoreRankingExcel = function(){
		$scope.$emit('isLoading', true);
		location.href="/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingListScoreExcel?pageNum=" 
			+ $scope.paginationConf.currentPage + "&pageSize=" + ($scope.paginationConf.itemsPerPage=="全部" ? -1: $scope.paginationConf.itemsPerPage) 
			+ ($scope.activityName ?  "&activityName=" + $scope.activityName  : "")
			+ ($scope.beginTime ?  "&beginTimeq=" + $scope.beginTime  : "")
			+ ($scope.endTime ?  "&endTimeq=" + $scope.endTime  : "")
			+ ($scope.orgName ?  "&orgName=" + $scope.orgName  : "")
			+ ($scope.oneDeptName ?  "&oneDeptName=" + $scope.oneDeptName  : "")
			+ ($scope.twoDeptName ?  "&twoDeptName=" + $scope.twoDeptName  : "")
			+ ($scope.threeDeptName ?  "&threeDeptName=" + $scope.threeDeptName : "");
		$http.get($scope.$httpUrl).success(function (response) {
			$scope.$emit('isLoading', false);
		})
	}
	
}])
.controller('EmpLearningGradeRankController', ['$scope', '$http', '$timeout', '$q', 'dialogService', function ($scope, $http , $timeout, $q, dialogService) {
	$scope.$emit('cm', { pmc:'pmc-g', pmn: '统计报表', cmc:'cmc-gq', cmn: '课程排行'});
	$scope.url = "/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingListCourseScore?pageNum=";
	$scope.deferred = $q.defer();
	var $httpPramsArr = ["beginTimeq", "endTimeq", "orgName", "oneDeptName", "twoDeptName", "threeDeptName"];
	var init$httpPrams = function(){
		for(var i in $httpPramsArr){
			if($scope[$httpPramsArr[i]] != "" && $scope[$httpPramsArr[i]] != undefined){
				$scope.$httpPrams = $scope.$httpPrams + "&" + $httpPramsArr[i] + "=" + $scope[$httpPramsArr[i]];
			}
		}
	}
	var getDate = function(step){
		var dd = new Date();
	    dd.setDate(dd.getDate() + step);
		return dd.getFullYear() + "-" + (dd.getMonth()+1) + "-" + dd.getDate();
	}
	var initLegend = function(){
		if($scope.beginTimeq != "" && $scope.beginTimeq != undefined && $scope.endTimeq != "" && $scope.endTimeq != undefined){
			return $scope.beginTimeq + "--" + $scope.endTimeq;
		}else if($scope.beginTimeq != "" && $scope.beginTimeq != undefined){
			return $scope.beginTimeq + "--" + getDate(0);
		}else if($scope.endTimeq != "" && $scope.endTimeq != undefined){
			return "截止" + $scope.endTimeq;
		}else{
			return getDate(0);
		}
	}
	var formatChartData = function(rs){
		var chartData = {
			subtext :"课程评分排行",	
			chartLegend	: [],	
			chartYAxis : [],
			chartData : [] 
		};
		if(!rs || !rs.data){
			return chartData;
		}
		var chartDataItem = [];
		for(var i in rs.data){
			var currItem = rs.data[i];
			chartData.chartYAxis.push(currItem.courseName);
			chartDataItem.push(parseFloat((currItem.score).toFixed(2)));
		}
		chartData.chartData.push(chartDataItem);
		if(chartData.chartData.length > 0) {
			chartData.chartLegend.push(initLegend());
		}
		return chartData;
	}
	var findOrgname = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findOrgname").success(function (response) {
	        $scope.orgs = response;
	    }).error(function () {
			dialogService.setContent("所属组织下拉菜单初始化错误").setShowDialog(true);
	    });
	}();
	var findFirstDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName" + "?code=" + $scope.orgName).success(function (response) {
	        $scope.firstDepts = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findSecondDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findSecondDeptName" + "?code=" + $scope.oneDeptName).success(function (response) {
	        $scope.secondDepts = response;
	    }).error(function () {
			dialogService.setContent("二级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	var findThirdDeptName = function(){
		$http.get("/enterpriseuniversity/services/empLearningAction/findThirdDeptName" + "?code=" + $scope.twoDeptName).success(function (response) {
	        $scope.thirdDepts = response;
	    }).error(function () {
			dialogService.setContent("三级部门下拉菜单初始化错误").setShowDialog(true);
	    });
	}
	//分页
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 13,
		perPageOptions:[20,50,100,'全部'],
		rememberPerPage: 'perPageItems',
		onChange: function(){
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			$scope.$emit('isLoading', true);
			init$httpPrams();
			console.log("$scope.$httpPrams = ", $scope.$httpPrams);
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" +
			($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)+ $scope.$httpPrams;
			$http.get($scope.$httpUrl).success(function (response) {
				var chartData = formatChartData(response);
				//$scope.deferred = $q.defer();
				//console.log(chartData);
				$scope.$broadcast("$$chartData", chartData);
				$scope.$emit('isLoading', false);
				//$scope.deferred.resolve(chartData);
				//$scope.deferred = $q.defer();
				$scope.paginationConf.totalItems = response.count;
				$scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			})
		}
	}
	$scope.reSetSearchOption = function(){
		$scope.beginTimeq = "";
		$scope.endTimeq = "";
	}
	$scope.moreQueryOptionSearch = function(){
		$scope.showMoreQueryOption = !$scope.showMoreQueryOption;
	}
	$scope.searchData = function(){
		$scope.paginationConf.currentPage == 1 ? $scope.paginationConf.onChange() : $scope.paginationConf.currentPage = 1;
	}
	$scope.changeSeclectedOrgcode = function(){
		if($scope.orgName != "" && $scope.orgName != undefined) {
			findFirstDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedOnedeptcode = function(){
		if($scope.oneDeptName != "" && $scope.oneDeptName != undefined) {
			findSecondDeptName();
		}
		$scope.searchData();
	}
	$scope.changeSeclectedDeptname = function(){
		if($scope.twoDeptName != "" && $scope.twoDeptName != undefined) {
			findThirdDeptName(); 
		}
		$scope.searchData();
	}
	$scope.changeSeclectedThirdName = function(){
		$scope.searchData();
	}
	
	$scope.downloadLearningCoursescoreRankingExcel = function(){
		$scope.$emit('isLoading', true);
		location.href="/enterpriseuniversity/services/stuCourseRanking/stuCourseRankingCourseScoreExcel?pageNum=" 
			+ $scope.paginationConf.currentPage + "&pageSize=" +($scope.paginationConf.itemsPerPage=="全部" ? -1: $scope.paginationConf.itemsPerPage)
			+ ($scope.activityName ?  "&activityName=" + $scope.activityName  : "")
			+ ($scope.beginTime ?  "&beginTimeq=" + $scope.beginTime  : "")
			+ ($scope.endTime ?  "&endTimeq=" + $scope.endTime  : "")
			+ ($scope.orgName ?  "&orgName=" + $scope.orgName  : "")
			+ ($scope.oneDeptName ?  "&oneDeptName=" + $scope.oneDeptName  : "")
			+ ($scope.twoDeptName ?  "&twoDeptName=" + $scope.twoDeptName  : "")
			+ ($scope.threeDeptName ?  "&threeDeptName=" + $scope.threeDeptName : "");
		$http.get($scope.$httpUrl).success(function (response) {
			$scope.$emit('isLoading', false);
		})
	}
}])
//横向条状图表
.directive('barRankChart', function() {
	return {
        scope: {
        	id: "@" 
        },
        restrict: 'E',
        template: '<div style="min-height:800px"></div>',
        replace: true,
        link: function(scope, iElm, iAttrs, controller) {
        	scope.$on("$$chartData", function(e, data){
        		scope.legend = data.chartLegend;
        		scope.yAxis = data.chartYAxis;
        		scope.chartData = data.chartData;
        		 
        		var option = {
        			title : {
        				show: true,
        				text: "排行统计",
        				subtext : /*"学习时长排行"*/data.subtext,
        				itemGap: 15,
        				padding: 10
        			},
                    tooltip: {
                        show: true,
                        trigger: "axis"
                    },
                    legend: {
                    	show: false,
                        data: scope.legend
                    },
                    xAxis: [{
                        type: 'value'
                    }],
                    grid: { // 控制图的大小，调整下面这些值就可以，
                        x: 300,
                        x2: 40,
                        y: 40,
                        y2: 40// y2可以控制 X轴跟Zoom控件之间的间隔，避免以为倾斜后造成 label重叠到zoom上
                    },
                    yAxis: [{
                        type: 'category',
                        axisLabel: {
                        	interval: 0,
                        	rotate: 30
                        },
                        data: scope.yAxis
                    }],
                    series: function() {
                        var serie = [];
                        for (var i = 0; i < scope.legend.length; i++) {
                            var item = {
                                name: scope.legend[i],
                                type: 'bar',
                                data: scope.chartData && scope.chartData[i]
                            };
                            serie.push(item);
                        }
                        return serie;
                    } ()
            	};
                // 基于准备好的dom，初始化echarts图表
                var myChart = echarts.init(document.getElementById(scope.id), 'macarons');
                // 为echarts对象加载数据
                myChart.setOption(option);
        	});
        }
    };
});
 

