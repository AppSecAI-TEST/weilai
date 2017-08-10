/*
 * 统计模块
 * */
angular.module('ele.statistic', [ 'ele.admin' ])
.controller('StatisticChartController', ['$scope', '$http', '$q', 'dialogService', function( $scope, $http, $q, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ee', cmn: '整体统计'};
	/*Date.prototype.Format = function (fmt) {
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
	}*/
	function getDate(step){
		var dd = new Date();
	    dd.setDate(dd.getDate()+step);
	    var y = dd.getFullYear();
	    var m = dd.getMonth()+1;
	    var d = dd.getDate();
		return y+"-"+m+"-"+d;
	}
	/*$scope.beginTime = new Date().Format("yyyy-MM-dd") + " 00:00:01";
	$scope.endTime = new Date().Format("yyyy-MM-dd") + " 23:59:59";*/
	$scope.beginTime = getDate(-1);
	$scope.endTime = getDate(0);
	$scope.searchData = function(){
		var params = '';
		if($scope.beginTime!=''&&$scope.endTime != ''){
			params = '?beginTime='+ $scope.beginTime + '&endTime=' + $scope.endTime ;
		}else if($scope.beginTime!=''&&$scope.endTime == ''){
			params = '?beginTime='+ $scope.beginTime ;
		}else if($scope.beginTime ==''&&$scope.endTime != ''){
			params = '?endTime='+ $scope.endTime ;
		}
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/wholeStatistics/getWholeStatistics"+params).success(function (response) {
			if(response.result=="error"){
				$scope.$emit('isLoading', false);
				dialogService.setContent("统计数据查询异常！").setShowDialog(true);
			}else{
				$scope.chartData = response;
				$scope.buildFusionCharts();
				$scope.$emit('isLoading', false);
			}
	    }).error(function (response) {
	    	$scope.$emit('isLoading', false); 
	    	dialogService.setContent("统计数据查询异常！").setShowDialog(true);
	    });
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
	 
	FusionCharts.ready(function(){
		/*$scope.$emit('isLoading', true); 
		$http.get("ele_tom/services/wholeStatistics/getWholeStatistics").success(function (response) {
			$scope.chartData = response;
			$scope.buildFusionCharts();
			$scope.$emit('isLoading', false); 
		}).error(function (response) {
			$scope.$emit('isLoading', false); 
	    	dialogService.setContent("统计数据查询异常！").setShowDialog(true);
	    });*/
		$scope.searchData();
	})
	$scope.buildFusionCharts = function(){
		$scope.newChart = new FusionCharts({
	        type: 'column2d',
	        renderAt: 'chart-container',
	        width: '100%',
	        height: '425',
	        dataFormat: 'json',
	        dataSource: {
	            "chart": {
	                "caption": "整体统计图表",
	                "subcaption": "活跃率" + ($scope.chartData.activeRate==undefined ? "0%":$scope.chartData.activeRate) + "（活跃用户数/累计用户数）",
	                "xaxisname": "统计项",
	                "yaxisname": "数量 ",
	                "formatNumberScale": "0",
	                "placeValuesInside" :"0",
	                "rotateValues": "0",
			        "showborder": "1",
	                "valueFontColor": "#0C0C0C",
	                "adjustDiv": "1",
	                "numdivlines": "4",
	                "yAxisMinValue": "0",     
	                "yAxisValueDecimals":"0",
	                "forceYAxisValueDecimals": "1", 
	                "baseFontSize":"14", 
	                "theme" : "fint" 
	            },
	            "data": [
	                {
	                    "label": "活跃用户数",
	                    "value": $scope.chartData.activeUsers,
	                    "color": "#0075c2"
	                }, 
	                {
	                    "label": "累计用户数",
	                    "value": $scope.chartData.totalUsers,
	                    //"value": 198,
	                    "color": "#22e0cc"
	                }, 
	                {
	                    "label": "累计课程数",
	                    "value": $scope.chartData.totalCourses,
	                    "color": "#71952d"
	                }, 
	                {
	                    "label": "累计播放次数",
	                    "value": $scope.chartData.totalViews,
	                    "color": "#d5841b"
	                     
	                }, 
	                {
	                    "label": "累计考试数",
	                    "value": $scope.chartData.totalExams,
	                    "color": "#af72e7"
	                }, 
	                {
	                    "label": "签到数",
	                    "value": $scope.chartData.totalSignIn,
	                    "color": "#ef7497"
	                }
	            ]
	        }
	    }).render();
	}
}])
.controller('CourseStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ea', cmn: '课程统计'};
	$scope.method = [
                {name: "线上", value: "Y"},
                {name: "线下", value: "N"}
	       	 ];
	// 显示课程统计列表 
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
				$scope.url = "/enterpriseuniversity/services/attendanceStatistics/attendanceStatisticsList?pageNum=";
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
				if ($scope.courseOnline != undefined) 
	            {
	            	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.courseOnline
	            }
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
	            	dialogService.setContent("课程统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
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
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    
	//高亮显示选中行
   /* $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.courseId = item.courseId; 
    	$scope.currHighLightRow.activityId = item.activityId; 
    }*/ 
    /*
	 * 勾选功能
	 */
	//容器
  //高亮显示选中行
 	$scope.choosedItemArrDetail = [];
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow1 = function(item){
    	$scope.currHighLightRow.courseNumber = item.courseNumber; 
    	if(item.checked){
			for(var i in $scope.choosedItemArrDetail){
				if($scope.choosedItemArrDetail[i].courseNumber==item.courseNumber){
					$scope.choosedItemArrDetail.splice(i,1); 
					break;
				}
			}
			item.checked = false;
		}else{
			item.checked = true;
			$scope.choosedItemArrDetail.push(item);
		}
    }
  //全选当前函数
	$scope.chooseCurrPageAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			var choosedItemArrBakDetail = $scope.choosedItemArrDetail.concat();
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				for(var j in choosedItemArrBakDetail){
					var cItem = choosedItemArrBakDetail[j];
					if(pItem.courseNumber == cItem.courseNumber){
						$scope.choosedItemArrDetail.splice($scope.choosedItemArrDetail.indexOf(cItem),1);
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
				for(var j in $scope.choosedItemArrDetail){
					var cItem = $scope.choosedItemArrDetail[j];
					if(pItem.courseNumber == cItem.courseNumber){
						hasContained = true;
						break;
					}
				}
				if(!hasContained){
					$scope.choosedItemArrDetail.push(pItem);
				}
			} 
		}
	}
	// 导出
	$scope.exportscore = function(){
		if($scope.choosedItemArrDetail.length<1){
			dialogService.setContent("未勾选！").setShowDialog(true);
			return ;
		}
		console.log($scope.choosedItemArrDetail);
		var codes="";
		var activeId="";
		for (var i = 0; i < $scope.choosedItemArrDetail.length; i++) {
			codes=codes+","+$scope.choosedItemArrDetail[i].courseNumber;
			activeId=activeId+","+$scope.choosedItemArrDetail[i].activityId;
		}
		location.href="/enterpriseuniversity/services/attendanceStatistics/downloadAttendanceStatisticsExcel?codes="+codes+"&activeId="+activeId;
	}
	
}])
//查看课程学习明细
.controller('viewSignUpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/seeAttendanceStatistics?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.activityId==undefined||$scope.courseId==undefined||$scope.courseName==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams = "&activityId="+$scope.activityId+"&courseId="+$scope.courseId;
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
	            	dialogService.setContent("课程统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewSignUpStatistic = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.activityId = item.activityId;
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
.controller('ExamStatisticController', ['$scope', '$http', 'dialogService', function ($scope, $http, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-eb', cmn: '考试统计'};
    $scope.url = "/enterpriseuniversity/services/examStatistics/queryExamStatistics?pageNum=";
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
            if ($scope.examName != undefined && $scope.examName != "") {
            	$scope.$httpPrams = $scope.$httpPrams+"&examName=" + $scope.examName
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
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("考试统计数据查询异常！").setShowDialog(true);
            });
        }
    };
    //搜索
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
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
	 
	/*FusionCharts.ready(function(){
		$scope.searchData();
	})*/
    
    //回车自动查询
    $scope.autoSearch = function($event){
    	if($event.keyCode==13){
    		$scope.search();
		}
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.examId = item.examId; 
    }
    
    /*
	 * 勾选功能
	 */
	//容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].examId==item.examId){
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
	//全选当前函数
	$scope.chooseCurrPageAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			var choosedItemArrBak = $scope.choosedItemArr.concat();
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				for(var j in choosedItemArrBak){
					var cItem = choosedItemArrBak[j];
					if(pItem.examId == cItem.examId){
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
					if(pItem.examId == cItem.examId){
						hasContained = true;
						break;
					}
				}
				if(!hasContained){
					$scope.choosedItemArr.push(pItem);
				}
			} 
		}
	}
	$scope.downloadExamStatisticsExcel = function(){
		if($scope.choosedItemArr.length<1){
			dialogService.setContent("未勾选！").setShowDialog(true);
			return ;
		}
		console.log($scope.choosedItemArr);
		var codes="";
		var examId="";
		for (var i = 0; i < $scope.choosedItemArr.length; i++) {
			codes=codes+","+$scope.choosedItemArr[i].code;
			examId=examId+","+$scope.choosedItemArr[i].examId;
		}
		location.href="/enterpriseuniversity/services/examStatistics/downloadExamStatisticsExcel?codes="+codes+"&examId="+examId;
	}
}])
//应该参加考试人员详情
.controller('viewExamStatisticDetailController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examStatistics/queryExamEmployeeStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.examId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&examId="+$scope.examId;
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
	            	dialogService.setContent("考试统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewExamStatisticDetail = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.examId = item.examId;
    	$scope.examName = item.examName;
    	$scope.search(); 
    }; 
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])

//实际参加考试人员详情
.controller('viewExamRealStatisticDetailController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examStatistics/queryExamRealEmployeeStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.examId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&examId="+$scope.examId;
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
	            	dialogService.setContent("考试统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewExamRealStatisticDetail = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.examId = item.examId;
    	$scope.examName = item.examName;
    	$scope.search(); 
    }; 
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])



.controller('ActivityStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ec', cmn: '活动统计'};
	// 显示活动统计列表
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
				$scope.url = "/enterpriseuniversity/services/activityStatistics/activityStatisticsList?pageNum=";
				if ($scope.activityName != undefined && $scope.activityName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
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
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
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
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(activity){
        $scope.currHighLightRow.activityId = activity.activityId; 
    } 
    /*
	 * 勾选功能
	 */
	//容器
	$scope.choosedItemArr = [];
	//单选函数
	$scope.chooseItem = function(item){
		if(item.checked){
			for(var i in $scope.choosedItemArr){
				if($scope.choosedItemArr[i].activityId==item.activityId){
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
	//全选当前函数
	$scope.chooseCurrPageAllItem = function(){
		if($scope.selectAll){
			$scope.selectAll = false;
			var choosedItemArrBak = $scope.choosedItemArr.concat();
			for(var i in $scope.page.data){
				var pItem = $scope.page.data[i];
				for(var j in choosedItemArrBak){
					var cItem = choosedItemArrBak[j];
					if(pItem.activityId == cItem.activityId){
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
					if(pItem.activityId == cItem.activityId){
						hasContained = true;
						break;
					}
				}
				if(!hasContained){
					$scope.choosedItemArr.push(pItem);
				}
			} 
		}
	}
	$scope.downloadActivityStatisticsExcel = function(){
		if($scope.choosedItemArr.length<1){
			dialogService.setContent("未勾选！").setShowDialog(true);
			return ;
		}
		console.log($scope.choosedItemArr);
		var codes="";
		var activityId="";
		for (var i = 0; i < $scope.choosedItemArr.length; i++) {
			codes=codes+","+$scope.choosedItemArr[i].code;
			activityId=activityId+","+$scope.choosedItemArr[i].activityId;
		}
		location.href="/enterpriseuniversity/services/activityStatistics/downloadActivityStatisticsExcel?codes="+codes+"&activityId="+activityId;
	}
    
}])
//查看培训活动统计明细
.controller('viewActivityStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/seeActivityStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.activityId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&activityId="+$scope.activityId;
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
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewActivityStatistic = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.activityId = item.activityId;
    	$scope.activityName = item.activityName;
    	$scope.search(); 
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])


//查看培训活动报名统计明细
.controller('viewActivityApplyStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/seeActivityApplyStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.activityId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&activityId="+$scope.activityId;
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
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewActivityApplyStatistic = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.activityId = item.activityId;
    	$scope.activityName = item.activityName;
    	$scope.search(); 
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])

//查看开放名单统计明细
.controller('viewActivityEmpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/seeActivityEmpStatistics?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.activityId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&activityId="+$scope.activityId;
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
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewActivityEmpStatistic = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.activityId = item.activityId;
    	$scope.activityName = item.activityName;
    	$scope.search(); 
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])



.controller('FeeStatisticController', ['$scope', '$http', 'dialogService', function( $scope, $http, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ed', cmn: '费用统计'};
	// 显示活动统计列表
	$scope.url = "/enterpriseuniversity/services/feeStatistics/findActityCostList?pageNum=";
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 13,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if ($scope.activityName != undefined && $scope.activityName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
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
	            	dialogService.setContent("费用统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
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
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.actitvtyId = item.actitvtyId; 
    }
}])
//查看费用统计详情
.controller('viewFeeDetailModalController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/feeStatistics/findActityCostDetailList?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if(!$scope.openModalDialog||$scope.activity==undefined||$scope.activity.actitvtyId==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams = "&activityId="+$scope.activity.actitvtyId;
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
	            	dialogService.setContent("费用统计详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item,$index){
    	$scope.currHighLightRow.item = item; 
    	$scope.currHighLightRow.index = $index; 
    }
	$scope.$parent.$parent.viewFeeDetail = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.activity = item;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])
//查看公开课统计列表
.controller('OpenCourseStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ef', cmn: '公开课统计'};
	// 显示课程统计列表 
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/attendanceStatistics/openCourseStatisticsList?pageNum=";
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
	            	dialogService.setContent("公开课统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
	};
	
	//查询下拉菜单改变后查询列表数据
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
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.courseId = item.courseId; 
    	$scope.currHighLightRow.activityId = item.activityId; 
    } 
}])
//查看公开课学习明细
.controller('OpenCourseLearnStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/openCourseLearnStatistics?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("公开课学习详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseLearnStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看线下课程统计列表
.controller('OfflineCourseStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-eg', cmn: '线下课程统计'};
	// 显示课程统计列表 
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50, 100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.url = "/enterpriseuniversity/services/attendanceStatistics/offlineCourseStatisticsList?pageNum=";
				if ($scope.courseName != undefined && $scope.courseName != "") {
					$scope.$httpPrams = $scope.$httpPrams + "&courseName=" + $scope.courseName
						.replace(/\%/g,"%25").replace(/\#/g,"%23")
						.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
						.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
				}
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
	            	dialogService.setContent("公开课统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	// 搜索按钮
	$scope.search = function() {
		$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
	};
	
	//查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
        $scope.currHighLightRow.courseId = item.courseId; 
    	$scope.currHighLightRow.activityId = item.activityId; 
    } 
}])
//查看线下课程签到人数
.controller('OfflineCourseSignStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/offlineCourseSignStatistics?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("公开课学习详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseLearnStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//学员统计开始
.controller('EmpStatisticController', ['$scope', '$http', 'dialogService', function ($scope, $http, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-eh', cmn: '学员统计'};
    $scope.url = "/enterpriseuniversity/services/emp/selectForEmp?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 13,
        perPageOptions: [10, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.empName != undefined && $scope.empName != "") {
            	$scope.$httpPrams = $scope.$httpPrams+"&name=" + $scope.empName
            		.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("学员数据查询异常！").setShowDialog(true);
            });
        }
    };
    //搜索
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
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
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
}])
.controller('viewCourseByEmpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/findempidPage?pageNum=";
	$scope.code = "";
	$scope.name = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.code==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&code="+$scope.code;
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
	            	dialogService.setContent("学员课程数据查询异常！").setShowDialog(true);
	            });
			}
	};
	/*$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};*/
	$scope.$parent.$parent.viewCourseStatistic = function(item){
		$scope.code = item.code;
		$scope.name = item.name;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;
	 	
	 	//$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.index = item.index; 
    }
}])
.controller('viewExamByEmpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/examStatistics/findEmpExam?pageNum=";
	$scope.code = "";
	$scope.name = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.code==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&code="+$scope.code;
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
	            	dialogService.setContent("学员考试数据查询异常！").setShowDialog(true);
	            });
			}
	};
	/*$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};*/
	$scope.$parent.$parent.viewExamStatistic = function(item){
		$scope.code = item.code;
		$scope.name = item.name;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	/*$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;*/
	 	
	 	//$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.examId = item.examId; 
    }
}])
.controller('viewActiveByEmpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/findEmpActive?pageNum=";
	$scope.code = "";
	$scope.name = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.code==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&code="+$scope.code;
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
	            	dialogService.setContent("学员活动数据查询异常！").setShowDialog(true);
	            });
			}
	};
	/*$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};*/
	$scope.$parent.$parent.viewActivityStatistic = function(item){
		$scope.code = item.code;
		$scope.name = item.name;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	/*$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;*/
	 	
	 	//$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.acitvityId = item.acitvityId; 
    }
}])
//学员统计结束

//平台统计开始
.controller('PingtaiStatisticController', ['$scope', '$http', 'dialogService', function ($scope, $http, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-e', pmn: '查询统计', cmc:'cmc-ei', cmn: '平台统计'};
    $scope.url = "/enterpriseuniversity/services/activityStatistics/findpingtai?pageNum=";
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
        	if($scope.beginTime!="" && $scope.beginTime != undefined){
        		$scope.$httpPrams = $scope.$httpPrams+'&beginTime='+ $scope.beginTime;
    		}
        	if($scope.endTime!="" && $scope.endTime != undefined){
    			$scope.$httpPrams = $scope.$httpPrams+'&endTime='+ $scope.endTime ;
    		}
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
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
}])
.controller('viewlearnStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/findLearnPingtai?pageNum=";
	$scope.createDate = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.createDate==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&createDate="+$scope.createDate;
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
	            	dialogService.setContent("平台学习人员数据查询异常！").setShowDialog(true);
	            });
			}
	};
	/*$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};*/
	$scope.$parent.$parent.viewlearnStatistic = function(item){
		$scope.createDate = item.createDate;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	/*$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;*/
	 	
	 	//$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
}])
.controller('viewpelStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/findPelPingtai?pageNum=";
	$scope.createDate = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.createDate==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&createDate="+$scope.createDate;
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
	            	dialogService.setContent("平台登录人员数据查询异常！").setShowDialog(true);
	            });
			}
	};
	/*$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};*/
	$scope.$parent.$parent.viewpelStatistic = function(item){
		$scope.createDate = item.createDate;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	/*$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;*/
	 	
	 	//$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
}])
.controller('viewLogintimeByEmpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/findloginTime?pageNum=";
	$scope.createDate = "";
	$scope.code = "";
	$scope.name = "";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 15,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function(data) {
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				if($scope.code==undefined||!$scope.openModalDialog){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&code="+$scope.code;
				}
				/*if($scope.beginTime!="" && $scope.beginTime != undefined){
	        		$scope.$httpPrams = $scope.$httpPrams+'&beginTime='+ $scope.beginTime;
	    		}
	        	if($scope.endTime!="" && $scope.endTime != undefined){
	    			$scope.$httpPrams = $scope.$httpPrams+'&endTime='+ $scope.endTime ;
	    		}*/
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
	            	dialogService.setContent("学员登录信息数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewPingtaiStatistic = function(item){
		$scope.code = item.code;
		$scope.name = item.name;
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	/*$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;*/
	 	
	 	$scope.search(); 
	};
	/*$scope.searchData = function(){
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
    });*/
	//模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(b){
    	$scope.currHighLightRow.createDate = b.createDate; 
    }
}])

//查看公开课应该学习人员明细
.controller('ViewCourseNeedStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseNeedStatistics?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	$scope.$parent.$parent.viewCourseNeedStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看点赞人员名单
.controller('ViewCourseThumbUpStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseThumbUpStatistic?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("点赞人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseThumbUpStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看收藏人员名单
.controller('ViewFavouriteCourseStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewFavouriteCourseStatistic?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("收藏人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseFavouriteStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看课程评论名单
.controller('ViewCourseCommentStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseCommentStatistic?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("课程评论人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseCommentStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看课程评分详情
.controller('ViewCourseScoreStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseScoreStatistic?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("课程评分人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewCourseScoreStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看开始学习人员名单
.controller('ViewStartLearnSectionStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewStartLearnSectionStatistic?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	            	dialogService.setContent("课程评分人员详情数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	$scope.$parent.$parent.viewStartLearnSectionStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看应学人员
.controller('ViewCourseCanlearnStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseCanlearnStatistics?pageNum=";
	$scope.openModalDialog = false;
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
				if($scope.courseId==undefined||$scope.courseName==undefined){
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
	$scope.$parent.$parent.viewCourseCanLearnStatistic = function(item){
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
    	$scope.currHighLightRow.code = item.code; 
    }
}])
//查看应学人员
.controller('ViewCourseSignStatisticController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
//	var method2 = function(){
	$scope.topDept="";
		$http.get("/enterpriseuniversity/services/empLearningAction/findFirstDeptName?code=1" ).success(function (response) {
	        $scope.methodDept = response;
	    }).error(function () {
			dialogService.setContent("一级部门下拉菜单初始化错误").setShowDialog(true);
	    });
//	}
	$scope.url = "/enterpriseuniversity/services/attendanceStatistics/viewCourseSignStatistics?pageNum=";
	$scope.openModalDialog = false;
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
				$scope.$httpPrams1 = "";
				if($scope.courseId==undefined||$scope.courseName==undefined){
					$scope.page = {};
					return;
				}else{
					$scope.$httpPrams ="&courseId="+$scope.courseId;
				}
				if($scope.topDept!=null && $scope.topDept!=""){
					$scope.$httpPrams1 = "&topDept="+$scope.topDept;
				}
				$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage
					+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage)
					+ $scope.$httpPrams+$scope.$httpPrams1;
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
	$scope.$parent.$parent.viewCourseSignStatistic = function(item){
	 	$scope.openModalDialog = true;
	 	dialogStatus.setHasShowedDialog(true);
	 	$scope.currHighLightRow = {};
	 	$scope.courseId = item.courseId;
	 	$scope.courseName = item.courseName;
	 	$scope.search(); 
	};
	//模态框关闭
	$scope.doClose = function (){
		$scope.topDept="";
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
	//高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
    $scope.selectDept = function(){
    	
    	$scope.search(); 
    }
}])
//查看已完成活动人员
.controller('viewCompletedActivityEmpController', ['$scope', '$http', 'dialogService', 'dialogStatus', function( $scope, $http, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/activityStatistics/seeCompletedActivityEmp?pageNum=";
	$scope.openModalDialog=false;
	$scope.paginationConf = {
			currentPage : 1,
			totalItems : 10,
			itemsPerPage : 20,
			pagesLength : 11,
			perPageOptions : [ 20, 50,100, '全部' ],
			rememberPerPage : 'perPageItems',
			onChange : function() {
				if(!$scope.openModalDialog||$scope.activityId==undefined){
	        		$scope.page = {};
	        		return;
	        	}
				$scope.$httpUrl = "";
				$scope.$httpPrams = "";
				$scope.$httpPrams = $scope.$httpPrams + "&activityId="+$scope.activityId;
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
	            	dialogService.setContent("活动统计数据查询异常！").setShowDialog(true);
	            });
			}
	};
	$scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code; 
    }
	$scope.$parent.$parent.viewCompletedActivityEmp = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.activityId = item.activityId;
    	$scope.activityName = item.activityName;
    	$scope.search(); 
    };
    //模态框关闭
	$scope.doClose = function (){
		$scope.openModalDialog=false;
		dialogStatus.setHasShowedDialog(false);
	};
}])
;