
/**
 * Created by Wanglg on 2016/3/30.
 */
angular.module('ele.activity',['ele.admin'])
.controller('ActivityController', ['$scope', '$http', 'dialogService','$timeout',function ($scope, $http ,dialogService,$timeout) {
    //用于显示面包屑状态
    //$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动'};
    $scope.$emit('cm', {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动'});
    //http请求url
    $scope.url = "/enterpriseuniversity/services/activity/findList?pageNum=";
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
        	$scope.$emit('isLoading', true);
            if ($scope.activityName != undefined && $scope.activityName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&activityName=" + $scope.activityName
                	.replace(/\%/g,"%25").replace(/\#/g,"%23")
        			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
        			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });
        }
    };
    //搜索按钮函数
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
    //添加
    $scope.doAdd = function(){
        $scope.go('/activity/addActivity','addActivity',null);
    }
    //编辑
    $scope.doEdit = function(activity){
        $scope.go('/activity/editActivity','editActivity',{id:activity.activityId});
    }
    //查看
    $scope.doView = function(activity){
    	 $scope.go('/activity/detailActivity','detailActivity',{id:activity.activityId});
    }
    //复制
    $scope.doCopy = function(activity){
    	$scope.go('/activity/copyActivity','copyActivity',{id:activity.activityId});
    };
    //删除
    $scope.doDelete = function(t){
    	if(t.creatorId==$scope.sessionService.user.code||$scope.sessionService.user.id==0){
    	dialogService.setContent("确定删除培训活动?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
        	$http({
        		url: "/enterpriseuniversity/services/activity/delete?activityId=" + t.activityId,
                method: "DELETE"
            }).success(function (response) {
            	//延迟弹框
		    	$timeout(function(){
		    		if(response.result=="protected"){
		    			dialogService.setContent("不能删除已经开始的培训").setShowDialog(true);
		    		}else{
		    			dialogService.setContent("删除培训活动成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
		    				$scope.paginationConf.onChange();
	    				}
		    		}
		    	},200);                
            }).error(function(){
		    	$timeout(function(){
		    		dialogService.setContent("网络异常,未能成功删除培训活动,请重新登录后再试").setShowDialog(true);
		    	},200);
		    }); 
    	}
    }else{
    		dialogService.setContent("需要创建人删除！").setShowDialog(true);
    }
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(activity){
        $scope.currHighLightRow.activity = activity.activityId; 
    }
}])
//费用统计
.controller('ActivityFeesController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    $scope.openFeeModalDialog = false;
    $scope.$parent.$parent.doCost = function(activity){
    	$scope.tomFree={activityId:activity.activityId};
		$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+activity.activityId).success(function (response) {
			$scope.tomFree.fees = response;
			$scope.currHighLightRow = {};
			$scope.openFeeModalDialog = true;
			dialogStatus.setHasShowedDialog(true);
		}); 
    }
    //关闭费用统计
    $scope.closeFeeModalDialog = function (){
 		$scope.openFeeModalDialog = false;
 		dialogStatus.setHasShowedDialog(false);
 	};
    //添加费用
 	var feeIndex = 0;//唯一性标记
    $scope.addFee = function() {
    	feeIndex = feeIndex+1;
    	var feeItem = {
			activityId:$scope.tomFree.activityId,
			feeId:"",
			feeName:"",
			fee:"",
			remark:"",
			creator:"",
			operator:"",
			updateTime:"",
			status:"",
			tip:"1",
			feeIndex:feeIndex
    	};
    	$scope.tomFree.fees.push(feeItem);
    };
    //删除费用统计
    $scope.deleteFee = function(fee){
    	dialogService.setContent('确定要删除当前培训活动费用吗？').setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		if(fee.feeId!==""){
    			$http({
        			url: "/enterpriseuniversity/services/activity/deleteCost?activityId="+fee.activityId+"&feeId=" + fee.feeId,
        			method: "DELETE"
        		}).success(function(data) {
        			$timeout(function(){
        				dialogService.setContent("删除培训活动费用成功！").setShowSureButton(false).setShowDialog(true);
        				$timeout(function(){
        					dialogService.sureButten_click();
	        				$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+$scope.tomFree.activityId).success(function (response) {
	    						$scope.tomFree.fees = response;
	    					});
        				},2000);
        			},500);
        		}).error(function(){
        			$timeout(function(){
        				dialogService.setContent("删除培训活动费用失败！").setShowSureButton(false).setShowDialog(true);
        				$timeout(function(){
        					dialogService.sureButten_click();
        				},2000);
        			},500);
        		});
    		}else{
    			$scope.tomFree.fees.splice($scope.tomFree.fees.indexOf(fee), 1);
    		}
    	}
    }
    //费用统计保存
    $scope.saveFees = function (){
    	$scope.addFeesForm.$submitted = true;
    	if($scope.addFeesForm.$invalid){
    		dialogService.setContent("新增培训活动费用存在不合法填写字段!").setShowDialog(true) 
    		return;
    	}
    	var activityFees = {listFrees:[]};
    	for(var i in $scope.tomFree.fees){
    		if($scope.tomFree.fees[i].tip=="1"){
    			activityFees.listFrees.push($scope.tomFree.fees[i]);
    		}
    	}
    	if(activityFees.listFrees.length>0){
    		$http({
    			method : 'POST',
    			url  : '/enterpriseuniversity/services/activity/addCost',
    			data : activityFees, 
    			headers : { 'Content-Type': 'application/json' }
    		}).success(function(data) {
    			$scope.addFeesForm.$submitted = false;
				dialogService.setContent("培训活动费用保存成功！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
					dialogService.sureButten_click();
    				$http.get("/enterpriseuniversity/services/activity/findCost?activityId="+$scope.tomFree.activityId).success(function (response) {
						$scope.tomFree.fees = response;
					});
				},2000);
    		}).error(function(){
    			$scope.addFeesForm.$submitted = false;
				dialogService.setContent("培训活动费用保存失败！").setShowSureButton(false).setShowDialog(true);
				$timeout(function(){
					dialogService.sureButten_click();
				},2000);
    		});
    	}else{
    		$scope.addFeesForm.$submitted = false;
    		dialogService.setContent("无新增培训活动费用记录!").setShowDialog(true);
    	}
    };
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function($index){
    	$scope.currHighLightRow.index = $index;
    }
}])
//校验
.directive('requiredActivityemps', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
				  var deptManagerIds = scope.formData==undefined?[]:scope.formData.deptManagerIds==null||scope.formData.deptManagerIds==undefined?[]:scope.formData.deptManagerIds;
				  var empsIds = value==undefined||value==null?[]:value;
		          return deptManagerIds.length+empsIds.length;
		      }, function(){
		    	  var deptManagerIds = scope.formData==undefined?[]:scope.formData.deptManagerIds==null||scope.formData.deptManagerIds==undefined?[]:scope.formData.deptManagerIds;
				  var empsIds = value==undefined||value==null?[]:value;
	    		  validity = deptManagerIds.length+empsIds.length<1?true:false;
	    		  ngModel.$setValidity("requiredActivityemps", !validity);
	          });
			  return /*!validity ? value : []*/value;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
.directive('requiredTaskpackage', [function () {
	return {
		  require: "ngModel",
		  link: function (scope, element, attr, ngModel) {
			  var customValidator = function (arry) {
				  var validity = null;
				  scope.$watch(function () {
			          return arry;
			      }, function(){
			    	  validity = arry==undefined ? true : arry.length <=0 ? true : false;
			          ngModel.$setValidity("requiredTaskpackage", !validity);
		          });
		          return !validity ? arry : [];
		      };
		      ngModel.$formatters.push(customValidator);
		      ngModel.$parsers.push(customValidator);
		  }
	};
}])
.directive('requiredLecturer', [function () {
	return {
	  require: "ngModel",
	  link: function (scope, element, attr, ngModel) {
		  var customValidator = function (value) {
			  var validity = null;
			  scope.$watch(function () {
		          return value;
		      }, function(){
		    	  validity = value==undefined || value =="" ? true : false;
		          ngModel.$setValidity("requiredLecturer", !validity);
	          });
	          return !validity ? value : undefined;
	      };
	      ngModel.$formatters.push(customValidator);
	      ngModel.$parsers.push(customValidator);
	  }
	};
}])
//添加活动
.controller('AddActivityController', ['$scope', '$http', 'FileUploader', 'dialogService','$timeout', function ($scope, $http, FileUploader, dialogService,$timeout) {
    //$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'添加培训活动'};
    $scope.$emit('cm', {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'添加培训活动'});
    var nowDate = new Date() ;
    $scope.defaultStartTime = nowDate.getFullYear() + "-" + (nowDate.getMonth() + 1) + "-" + nowDate.getDate() + " 9:00:00";
    $scope.defaultEndTime = nowDate.getFullYear() + "-" + (nowDate.getMonth() + 1) + "-" + nowDate.getDate() + " 19:00:00";
    $scope.activity = $scope.formData={
    	needApply : 'Y',
		protocol : 'N',
		isCN : 'N',
		isEN : 'N',
		preTaskInfo :[],
		deptCodes:"",
		employeeGradeStr:"",
		city:"",
		cityEn:"",
		language:""
    };
    $scope.choosedEmpsArr = [];//指定学习人员
    $scope.choosedItemArr = [];//推送部门负责人
    $scope.taskPackage = [];
    $scope.$on("packageHasChanged",function( e, packages){
		e.stopPropagation();
		var currItem, newTaskCoursesOrExamPapers = [];
		
		//20170318 add s
		//$scope.taskPackage = [];
		var oldPackages = $scope.taskPackage.length > 0 ? $scope.taskPackage[0].packages ? $scope.taskPackage[0].packages.concat() : [] : [];
		//console.log(oldPackages);
		$scope.taskPackage = [];
		for(var i in packages){
			for(var j in oldPackages){
				if(packages[i].packageId == oldPackages[j].packageId){
					packages[i].isOld = true;
					//packages[i] = oldPackages[j];
					for(var k in packages[i].taskCoursesOrExamPapers){
						var newItem = packages[i].taskCoursesOrExamPapers[k];
						for(var l in oldPackages[j].taskCoursesOrExamPapers){
							//20170327 modify s
							var oldItem = oldPackages[j].taskCoursesOrExamPapers[l];
							if((newItem.examPaperId != null && oldItem.examPaperId != null && newItem.examPaperId == oldItem.examPaperId)
									|| (newItem.courseId != null && oldItem.courseId != null && newItem.courseId == oldItem.courseId)){
								packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
							}
							 
							/*if(packages[i].taskCoursesOrExamPapers[k].examPaperId == oldPackages[j].taskCoursesOrExamPapers[l].examPaperId
									|| packages[i].taskCoursesOrExamPapers[k].courseId == oldPackages[j].taskCoursesOrExamPapers[l].courseId){
								var sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
								packages[i].taskCoursesOrExamPapers[k].sort = sort;
								//packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
								break;
							}*/
							//20170327 modify e
						}
					}
					break;
				}
			}
		}
		//20170318 add e
		for(var i in packages){
			//20170318 add s
			//20170327 modify s
			/*if(packages[i].isOld){
				for(var j in packages[i].taskCoursesOrExamPapers){
					packages[i].taskCoursesOrExamPapers[j]["preTaskIds"] = "";
					packages[i].taskCoursesOrExamPapers[j]["preTaskNames"] = "";
					packages[i].taskCoursesOrExamPapers[j]["choosedItemArr"] = [];
				}
			}*/
			//20170327 modify e
			//按任务包内部排序
			packages[i].taskCoursesOrExamPapers.sort(function(a,b){
				if(a.sort && b.sort){
					return Number(a.sort) - Number(b.sort);
				}else {
					return 0;
				}
			});
			//20170318 add e
			newTaskCoursesOrExamPapers = newTaskCoursesOrExamPapers.concat(packages[i].taskCoursesOrExamPapers);
		}
		//生成排序、任务编号字段
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			//重置排序
			currItem.sort = i+1;
			currItem.preTaskName = "任务" + currItem.sort;
		}
		//20170327 modify s
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			//console.log(currItem.choosedItemArr,"~~");
			if(currItem.choosedItemArr && currItem.choosedItemArr.length > 0){
				var pretaskSorts = [], pretaskNames = [], preTaskNames = "", preTaskIds = "";
				for(var j = 0; j < currItem.choosedItemArr.length; j++){
					//console.log("j", currItem.choosedItemArr[j]);
					if(currItem.choosedItemArr[j].sort >= (i + 1) || j >= i){
						currItem.choosedItemArr.splice(j, 1);
						j--;
					}else if(currItem.choosedItemArr[j].sort != undefined && currItem.choosedItemArr[j].sort != ""){
						pretaskSorts.push(currItem.choosedItemArr[j].sort);
						pretaskNames.push(currItem.choosedItemArr[j].preTaskName != undefined && currItem.choosedItemArr[j].preTaskName != "" ? currItem.choosedItemArr[j].preTaskName : "任务" + currItem.choosedItemArr[j].sort);
					}
				}
				pretaskSorts.sort(function(a, b){
					return parseInt(a.sort) - parseInt(b.sort);
				});
				pretaskNames.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currItem.preTaskIds = pretaskSorts.join(",");
				currItem.preTaskNames = pretaskNames.join(","); 
			} 
		}
		//20170327 modify e
		//20170318 add s
		/*if(packages && packages.length > 0){
			$scope.taskPackage.push({packages : packages, taskCoursesOrExamPapers : newTaskCoursesOrExamPapers});
		}*/
		$scope.taskPackage.push({packages : packages, taskCoursesOrExamPapers : newTaskCoursesOrExamPapers});
		//20170318 add e
	})
    /*//创建补考日期 html
    $scope.createRetaking = function(item){
    	item.retakingCountArr = [];
    	for(var i =1;i<=item.retakingExamCount;i++){
    		item.retakingCountArr.push(i);
    	}
    }*/
	
	$scope.initActivityTime = function(){
    	if($scope.taskPackage && !$scope.taskPackage.length){
    		return;
    	}
    	var currTaskCoursesOrExamPaper;
    	for(var i in $scope.taskPackage[0].taskCoursesOrExamPapers){
    		currTaskCoursesOrExamPaper = $scope.taskPackage[0].taskCoursesOrExamPapers[i];
    		if(currTaskCoursesOrExamPaper.preTaskIds != "" && currTaskCoursesOrExamPaper.preTaskIds != undefined){
    			currTaskCoursesOrExamPaper["startTime"] = $scope.formData.activityStartTime;
    			currTaskCoursesOrExamPaper["endTime"] = $scope.formData.activityEndTime;
    			if(currTaskCoursesOrExamPaper.retakingInfo && currTaskCoursesOrExamPaper.retakingInfo.length > 0){
                	for(var j = 0; j < currTaskCoursesOrExamPaper.retakingInfo.length; j++){
                		currTaskCoursesOrExamPaper.retakingInfo[j]["startTime"] = $scope.formData.activityStartTime;
                		currTaskCoursesOrExamPaper.retakingInfo[j]["endTime"] = $scope.formData.activityEndTime;
                	}
        		} 
    		}
    	}
    }
    //创建补考日期 html
    $scope.createRetaking = function(item){
    	//20170614 add start
    	var startTime = $scope.formData.activityStartTime != undefined && $scope.formData.activityStartTime != "" ? $scope.formData.activityStartTime : "";
		var endTime = $scope.formData.activityEndTime != undefined && $scope.formData.activityEndTime != "" ? $scope.formData.activityEndTime : "";
		//20170614 add end
    	/*if(item.preTaskIds != "" && item.preTaskIds != undefined && $scope.formData.activityStartTime != undefined && $scope.formData.activityEndTime != undefined && $scope.formData.activityStartTime != "" && $scope.formData.activityEndTime != ""){
    		if(item.retakingExamCount==undefined){
    			item.retakingInfo = []; 
    		}else{
    			item.retakingInfo = []; 
            	for(var i =1;i<=item.retakingExamCount;i++){
            		item.retakingInfo.push({startTime: $scope.formData.activityStartTime, endTime : $scope.formData.activityEndTime});
            	}
    		}
    	}else{
    		if(item.retakingExamCount==undefined&&item.retakingInfo!=undefined){
        		item.retakingInfo = []; 
        	}
        	if(item.retakingExamCount!=undefined&&item.retakingInfo!=undefined){
        		if(item.retakingExamCount >= item.retakingInfo.length){
        			var len = item.retakingInfo.length;
            		for(var i =0;i<item.retakingExamCount-len;i++){
                		item.retakingInfo.push({startTime:'',endTime:''});
                	}
            	}else{
            		var len = item.retakingInfo.length - item.retakingExamCount ;
            		item.retakingInfo.splice(item.retakingExamCount,len);
            	}
        	}
        	if(item.retakingExamCount!=undefined&&item.retakingInfo==undefined){
        		item.retakingInfo = []; 
            	for(var i =1;i<=item.retakingExamCount;i++){
            		item.retakingInfo.push({startTime:'',endTime:''});
            	}
        	}
    		 
    	}*/
		if(item.retakingExamCount == undefined && item.retakingInfo != undefined){
    		item.retakingInfo = []; 
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo != undefined){
    		if(item.retakingExamCount >= item.retakingInfo.length){
    			var len = item.retakingInfo.length;
        		for(var i =0;i<item.retakingExamCount-len;i++){
            		item.retakingInfo.push({startTime:startTime, endTime: endTime});
            	}
        	}else{
        		var len = item.retakingInfo.length - item.retakingExamCount ;
        		item.retakingInfo.splice(item.retakingExamCount, len);
        		for(var i = 0; i < item.retakingInfo.length; i++){
        			item.retakingInfo[i]["startTime"] = startTime;
        			item.retakingInfo[i]["endTime"] = endTime;
            	}
        	}
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo == undefined){
    		item.retakingInfo = []; 
        	for(var i =1;i<=item.retakingExamCount;i++){
        		item.retakingInfo.push({startTime:startTime,endTime: endTime});
        	}
    	}
    }
    $scope.changeCN = function(){
    	if($scope.activity.isCN=='Y'){
    		$scope.activity.isCN = 'N';
    		if($scope.activity.isEN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isCN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    $scope.changeCN = function(){
    	if($scope.activity.isCN=='Y'){
    		$scope.activity.isCN = 'N';
    		if($scope.activity.isEN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isCN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    $scope.changeEN = function(){
    	if($scope.activity.isEN=='Y'){
    		$scope.activity.isEN = 'N';
    		if($scope.activity.isCN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isEN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务"+item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务"+item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    $scope.$on("FormIsValid", function(e, preTaskInfo){
    	e.stopPropagation();
    	$scope.activity.preTaskInfo = preTaskInfo;
    	doSave(); 
	});
    //保存方法
    var doSave=function(){
    	$scope.activityForm.$submitted = true;
    	if($scope.activityForm.$invalid)
		{
    		dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
		 	return;
		}
    	$scope.$emit("isSaving", true);
		$http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/activity/add',
			data : $scope.activity, 
			headers : { 'Content-Type': 'application/json' }
		}).success(function(data) {
			$scope.$emit("isSaving", false);
			if(data.result=="success"){
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/activityList','activityList',null);
		   		},2000);
			}else if(data.result=="protected"){
				dialogService.setContent("活动报名日期已结束或活动已开始！").setShowDialog(true);
			}else if(data.result=="examTimeOut"){
				dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
			}else{
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			}
		})
		.error(function(){
			$scope.$emit("isSaving", false);
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
    }
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    }; 
}])

//编辑活动控制器
.controller('EditActivityController', ['$scope', '$http', '$state', '$stateParams', '$q', '$timeout','dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	//编辑
	$scope.$emit('cm', { pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动' , gcmn: '编辑培训活动'});
	$scope.deferred = $q.defer();
	$scope.taskPackageDeffered = $q.defer();
	if($stateParams.id){
		$scope.$emit('isLoading', true);
	    $http.get("/enterpriseuniversity/services/activity/findDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		//未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	//切换活动中英文
    $scope.changeCN = function(){
    	if($scope.activity.isCN=='Y'){
    		$scope.activity.isCN = 'N';
    		if($scope.activity.isEN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isCN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    $scope.changeEN = function(){
    	if($scope.activity.isEN=='Y'){
    		$scope.activity.isEN = 'N';
    		if($scope.activity.isCN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isEN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
	$scope.taskPackage = [];
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.activity = $scope.formData = data; 
		$scope.activity.language ='Y'; 
		$scope.taskPackage = [];
		var currPackage, currCoursesOrExamPaper, taskCoursesOrExamPapers = [], packages = $scope.activity.packages ? $scope.activity.packages : [];
		for(var i in packages){
			currPackage = packages[i];
			for(var j in currPackage.taskCoursesOrExamPapers){
				currCoursesOrExamPaper = currPackage.taskCoursesOrExamPapers[j];
				currCoursesOrExamPaper["packageId"] = currPackage.packageId;
				//20170318 add s
				//currCoursesOrExamPaper["preTaskNames"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "";
				var nameArr = (currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "").split(",");
				nameArr.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currCoursesOrExamPaper["preTaskNames"] = nameArr.join(",");
				//20170318 add e
				currCoursesOrExamPaper["preTaskIds"] = currCoursesOrExamPaper.pretaskId;
				currCoursesOrExamPaper["preTaskName"] = "任务" + currCoursesOrExamPaper.sort;
				//20170316 add
				//currCoursesOrExamPaper["choosedItemArr"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				var currPretaskArr = [];
				//console.log("currCoursesOrExamPaper.pretaskId", currCoursesOrExamPaper.pretaskId); 
				var currPretaskIds = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				for(var f = 0 ; f < currPretaskIds.length; f++){
					if(currPretaskIds[f] == ""){
						continue;
					}
					currPretaskArr.push({ sort : currPretaskIds[f]});
				}
				//console.log("currPretaskArr", currPretaskArr);
				currCoursesOrExamPaper["choosedItemArr"] = currPretaskArr;
				
				//20170316 add
				
				if(currCoursesOrExamPaper.examId){
					currCoursesOrExamPaper["retakingExamCount"] = currCoursesOrExamPaper.retakingExamTimes;
					currCoursesOrExamPaper["retakingInfo"] = []; 
					for(var m=1; m <= currCoursesOrExamPaper.retakingExamTimes; m++){
						currCoursesOrExamPaper.retakingInfo.push({
							startTime : currCoursesOrExamPaper.retakingExamBeginTimeList[m], 
							endTime : currCoursesOrExamPaper.retakingExamEndTimeList[m]
						});
					}
				}
				if(currCoursesOrExamPaper.courseId){
					currCoursesOrExamPaper.lecturerId = currCoursesOrExamPaper.lecturerId ? Number(currCoursesOrExamPaper.lecturerId) : "";
				}
				taskCoursesOrExamPapers.push(currCoursesOrExamPaper);
			}
			taskCoursesOrExamPapers.sort(function(a,b){
				if(a.sort && b.sort){
					return Number(a.sort) - Number(b.sort);
				}else {
					return 0;
				}
			});
		}
		$scope.taskPackage.push({ packages : packages, taskCoursesOrExamPapers : taskCoursesOrExamPapers });
		$scope.taskPackageDeffered.resolve($scope.taskPackage);
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
		$scope.$emit('isLoading', false);
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	});
	$scope.$on("packageHasChanged",function( e, packages){
		e.stopPropagation();
		var currItem, 
		newTaskCoursesOrExamPapers = [], 
		oldTaskCoursesOrExamPapers = [], 
		//20170318 add s
		oldPackages = $scope.taskPackage.length > 0 ? $scope.taskPackage[0].packages ? $scope.taskPackage[0].packages.concat() : [] : [];
		//oldTaskCoursesOrExamPapers = $scope.taskPackage[0].taskCoursesOrExamPapers.concat();
		//20170318 add e
		
		$scope.taskPackage[0].packages = packages;
		/*for(var i in packages){
			newTaskCoursesOrExamPapers = newTaskCoursesOrExamPapers.concat(packages[i].taskCoursesOrExamPapers);
		}*/
	 
		//20170318 add s
		for(var i = 0 ; i < packages.length; i ++){
			for(var j = 0 ; i < oldPackages.length; j ++ ){
				if(packages[i].packageId == oldPackages[j].packageId){
					packages[i].isOld = true;
					//packages[i] = oldPackages[j];
					for(var k = 0; k < packages[i].taskCoursesOrExamPapers.length; k ++){
						var newItem = packages[i].taskCoursesOrExamPapers[k];
						for(var l = 0; l < oldPackages[j].taskCoursesOrExamPapers.length; l ++){
							//20170327 modify s
							var oldItem = oldPackages[j].taskCoursesOrExamPapers[l];
							if((newItem.examPaperId != null && oldItem.examPaperId != null && newItem.examPaperId == oldItem.examPaperId)
									|| (newItem.courseId != null && oldItem.courseId != null && newItem.courseId == oldItem.courseId)){
								packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
							}
							/*if(packages[i].taskCoursesOrExamPapers[k].examPaperId == oldPackages[j].taskCoursesOrExamPapers[l].examPaperId
									|| packages[i].taskCoursesOrExamPapers[k].courseId == oldPackages[j].taskCoursesOrExamPapers[l].courseId){
								
								console.log(k,packages[i].taskCoursesOrExamPapers[k], l, oldPackages[j].taskCoursesOrExamPapers[l]);
								//packages[i].taskCoursesOrExamPapers[k].sort = new Number(oldPackages[j].taskCoursesOrExamPapers[l].sort);
								//console.log(i,packages[i].taskCoursesOrExamPapers[k].sort, j, oldPackages[j].taskCoursesOrExamPapers[l].sort);
								//packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
								
								break;
							}*/
							//20170327 modify e
						}
					}
					break;
				}
			}
		}
		//排序
		for(var i in packages){
			//20170327 modify s
			/*if(packages[i].isOld){
				for(var j in packages[i].taskCoursesOrExamPapers){
					packages[i].taskCoursesOrExamPapers[j]["preTaskIds"] = "";
					packages[i].taskCoursesOrExamPapers[j]["preTaskNames"] = "";
					packages[i].taskCoursesOrExamPapers[j]["choosedItemArr"] = [];
				}
			}*/
			if(packages[i].isOld){
				oldTaskCoursesOrExamPapers = oldTaskCoursesOrExamPapers.concat(packages[i].taskCoursesOrExamPapers);
			}
			//20170327 modify e
			packages[i].taskCoursesOrExamPapers.sort(function(a, b){
				if(a.sort && b.sort){
					return parseInt(a.sort) - parseInt(b.sort);
				}else {
					return 0;
				}
			});
			newTaskCoursesOrExamPapers = newTaskCoursesOrExamPapers.concat(packages[i].taskCoursesOrExamPapers);
		}
		
		//生成排序、任务编号字段
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			currItem.sort = i + 1;
			currItem.preTaskName = "任务" + currItem.sort;
		}
		//20170327 modify s
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			//console.log(currItem.choosedItemArr,"~~");
			if(currItem.choosedItemArr && currItem.choosedItemArr.length > 0){
				var pretaskSorts = [], pretaskNames = [], preTaskNames = "", preTaskIds = "";
				for(var j = 0; j < currItem.choosedItemArr.length; j++){
					//console.log("j", currItem.choosedItemArr[j]);
					if(currItem.choosedItemArr[j].sort >= (i + 1) || j >= i){
						currItem.choosedItemArr.splice(j, 1);
						j--;
					}else if(currItem.choosedItemArr[j].sort != undefined && currItem.choosedItemArr[j].sort != ""){
						pretaskSorts.push(currItem.choosedItemArr[j].sort);
						pretaskNames.push(currItem.choosedItemArr[j].preTaskName != undefined && currItem.choosedItemArr[j].preTaskName != "" ? currItem.choosedItemArr[j].preTaskName : "任务" + currItem.choosedItemArr[j].sort);
					}
				}
				pretaskSorts.sort(function(a, b){
					return parseInt(a.sort) - parseInt(b.sort);
				});
				pretaskNames.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currItem.preTaskIds = pretaskSorts.join(",");
				currItem.preTaskNames = pretaskNames.join(","); 
			} 
		}
		//20170327 modify e
		//console.log(newTaskCoursesOrExamPapers, "newTaskCoursesOrExamPapers");
		$scope.taskPackage[0].taskCoursesOrExamPapers = newTaskCoursesOrExamPapers;
	})
	$scope.initActivityTime = function(){
    	if($scope.taskPackage && !$scope.taskPackage.length){
    		return;
    	}
    	var currTaskCoursesOrExamPaper;
    	for(var i in $scope.taskPackage[0].taskCoursesOrExamPapers){
    		currTaskCoursesOrExamPaper = $scope.taskPackage[0].taskCoursesOrExamPapers[i];
    		if(currTaskCoursesOrExamPaper.preTaskIds != "" && currTaskCoursesOrExamPaper.preTaskIds != undefined){
    			currTaskCoursesOrExamPaper["startTime"] = $scope.formData.activityStartTime;
    			currTaskCoursesOrExamPaper["endTime"] = $scope.formData.activityEndTime;
    			if(currTaskCoursesOrExamPaper.retakingInfo && currTaskCoursesOrExamPaper.retakingInfo.length > 0){
                	for(var j = 0; j < currTaskCoursesOrExamPaper.retakingInfo.length; j++){
                		currTaskCoursesOrExamPaper.retakingInfo[j]["startTime"] = $scope.formData.activityStartTime;
                		currTaskCoursesOrExamPaper.retakingInfo[j]["endTime"] = $scope.formData.activityEndTime;
                	}
        		} 
    		}
    	}
    }
	//创建补考日期 html
    $scope.createRetaking = function(item){
		/*if(item.preTaskIds != "" && item.preTaskIds != undefined && $scope.formData.activityStartTime != undefined && $scope.formData.activityEndTime != undefined && $scope.formData.activityStartTime != "" && $scope.formData.activityEndTime != ""){
    		if(item.retakingExamCount==undefined){
    			item.retakingInfo = []; 
    		}else{
    			item.retakingInfo = []; 
            	for(var i =1;i<=item.retakingExamCount;i++){
            		item.retakingInfo.push({startTime: $scope.formData.activityStartTime, endTime : $scope.formData.activityEndTime});
            	}
    		}
    	}else{
    		if(item.retakingExamCount==undefined&&item.retakingInfo!=undefined){
        		item.retakingInfo = []; 
        	}
        	if(item.retakingExamCount!=undefined&&item.retakingInfo!=undefined){
        		if(item.retakingExamCount >= item.retakingInfo.length){
        			var len = item.retakingInfo.length;
            		for(var i =0;i<item.retakingExamCount-len;i++){
                		item.retakingInfo.push({startTime:'',endTime:''});
                	}
            	}else{
            		var len = item.retakingInfo.length - item.retakingExamCount ;
            		item.retakingInfo.splice(item.retakingExamCount,len);
            	}
        	}
        	if(item.retakingExamCount!=undefined&&item.retakingInfo==undefined){
        		item.retakingInfo = []; 
            	for(var i =1;i<=item.retakingExamCount;i++){
            		item.retakingInfo.push({startTime:'',endTime:''});
            	}
        	}
    	}*/
    	var startTime = $scope.formData.activityStartTime != undefined && $scope.formData.activityStartTime != "" ? $scope.formData.activityStartTime : "";
		var endTime = $scope.formData.activityEndTime != undefined && $scope.formData.activityEndTime != "" ? $scope.formData.activityEndTime : "";
		if(item.retakingExamCount == undefined && item.retakingInfo != undefined){
    		item.retakingInfo = []; 
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo != undefined){
    		if(item.retakingExamCount >= item.retakingInfo.length){
    			var len = item.retakingInfo.length;
        		for(var i =0;i<item.retakingExamCount-len;i++){
            		item.retakingInfo.push({startTime:startTime, endTime: endTime});
            	}
        	}else{
        		var len = item.retakingInfo.length - item.retakingExamCount ;
        		item.retakingInfo.splice(item.retakingExamCount, len);
        		for(var i = 0; i < item.retakingInfo.length; i++){
        			item.retakingInfo[i]["startTime"] = startTime;
        			item.retakingInfo[i]["endTime"] = endTime;
            	}
        	}
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo == undefined){
    		item.retakingInfo = []; 
        	for(var i =1;i<=item.retakingExamCount;i++){
        		item.retakingInfo.push({startTime:startTime,endTime: endTime});
        	}
    	}
		
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务" + item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务" + item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    $scope.$on("FormIsValid", function(e, preTaskInfo){
    	e.stopPropagation();
    	$scope.activity.preTaskInfo = preTaskInfo;
    	doSave(); 
	});
    var doSave = function ()
	{
	    $scope.activityForm.$submitted = true;
	    if ($scope.activityForm.$invalid) {
	        dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
	        return;
	    }
	    $scope.$emit("isSaving", true);
	    $http(
	    {
	        method : 'POST', 
	        url : '/enterpriseuniversity/services/activity/update', 
	        data : $scope.activity,
	        headers : { 'Content-Type' : 'application/json' }
	    }).success(function (data) 
	    {
	    	$scope.$emit("isSaving", false); 
	        if (data.result == "success")
	        {
	            dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
	            $timeout(function ()
	            {
	                dialogService.sureButten_click();
	                $scope.go('/activity/activityList', 'activityList', null);
	            }, 2000);
	        }
	        else if (data.result == "examTimeOut") {
	            dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
	        }
	        else
	        {
	            dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
	            $timeout(function ()
	            {
	                dialogService.sureButten_click();
	            }, 2000);
	        }
	    }) .error(function ()
	    {
	    	$scope.$emit("isSaving", false);
	        dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
	        $timeout(function ()
	        {
	            dialogService.sureButten_click();
	        }, 2000);
	    });
	}
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };   
}])
//复制活动控制器
.controller('CopyActivityController',['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
    //编辑
    //$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'复制培训活动'};
    $scope.$emit('cm', {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动', gcmn:'复制培训活动'});
    $scope.deferred = $q.defer();
    $scope.taskPackageDeffered = $q.defer();
	if($stateParams.id){
		$scope.$emit('isLoading', true);
	    $http.get("/enterpriseuniversity/services/activity/findDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.taskPackage = [];
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.activity = $scope.formData = data;
		$scope.activity.language='Y';
		$scope.taskPackage = [];
		var currPackage, currCoursesOrExamPaper, taskCoursesOrExamPapers = [], packages = $scope.activity.packages ? $scope.activity.packages : [];
		for(var i in packages){
			currPackage = packages[i];
			for(var j in currPackage.taskCoursesOrExamPapers){
				currCoursesOrExamPaper = currPackage.taskCoursesOrExamPapers[j];
				currCoursesOrExamPaper["packageId"] = currPackage.packageId;
				//20170318 add s
				//currCoursesOrExamPaper["preTaskNames"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "";
				var nameArr = (currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "").split(",");
				nameArr.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currCoursesOrExamPaper["preTaskNames"] = nameArr.join(",");
				//20170318 add e
				currCoursesOrExamPaper["preTaskIds"] = currCoursesOrExamPaper.pretaskId;
				currCoursesOrExamPaper["preTaskName"] = "任务" + currCoursesOrExamPaper.sort;
				
				//20170316 add
				//currCoursesOrExamPaper["choosedItemArr"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				var currPretaskArr = [];
				 
				var currPretaskIds = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				for(var f = 0 ; f < currPretaskIds.length; f++){
					if(currPretaskIds[f] == ""){
						continue;
					}
					currPretaskArr.push({ sort : currPretaskIds[f]});
				}
				currCoursesOrExamPaper["choosedItemArr"] = currPretaskArr;
				
				//20170316 add
				
				if(currCoursesOrExamPaper.examId){
					currCoursesOrExamPaper["retakingExamCount"] = currCoursesOrExamPaper.retakingExamTimes;
					currCoursesOrExamPaper["retakingInfo"] = []; 
					for(var m=1; m <= currCoursesOrExamPaper.retakingExamTimes; m++){
						currCoursesOrExamPaper.retakingInfo.push({
							startTime : currCoursesOrExamPaper.retakingExamBeginTimeList[m], 
							endTime : currCoursesOrExamPaper.retakingExamEndTimeList[m]
						});
					}
				}
				if(currCoursesOrExamPaper.courseId){
					currCoursesOrExamPaper.lecturerId = currCoursesOrExamPaper.lecturerId ? Number(currCoursesOrExamPaper.lecturerId) : "";
				}
				taskCoursesOrExamPapers.push(currCoursesOrExamPaper);
			}
			taskCoursesOrExamPapers.sort(function(a,b){
				if(a.sort && b.sort){
					return Number(a.sort) - Number(b.sort);
				}else {
					return 0;
				}
			});
		}
		$scope.taskPackage.push({ packages : packages, taskCoursesOrExamPapers : taskCoursesOrExamPapers });
		$scope.taskPackageDeffered.resolve($scope.taskPackage);
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
		$scope.$emit('isLoading', false);
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	});
	$scope.$on("packageHasChanged",function( e, packages){
		e.stopPropagation();
		var currItem, newTaskCoursesOrExamPapers = [];
		//20170318 add s
		var oldPackages = $scope.taskPackage.length > 0 ? $scope.taskPackage[0].packages ? $scope.taskPackage[0].packages.concat() : [] : [];
		//20170318 add e
		
		$scope.taskPackage[0].packages = packages;
		
		//20170318 add s
		for(var i in packages){
			for(var j in oldPackages){
				if(packages[i].packageId == oldPackages[j].packageId){
					packages[i].isOld = true;
					//packages[i] = oldPackages[j];
					for(var k in packages[i].taskCoursesOrExamPapers){
						var newItem = packages[i].taskCoursesOrExamPapers[k];
						for(var l in oldPackages[j].taskCoursesOrExamPapers){
							//20170327 modify s
							var oldItem = oldPackages[j].taskCoursesOrExamPapers[l];
							if((newItem.examPaperId != null && oldItem.examPaperId != null && newItem.examPaperId == oldItem.examPaperId)
									|| (newItem.courseId != null && oldItem.courseId != null && newItem.courseId == oldItem.courseId)){
								packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
							}
							/*if(packages[i].taskCoursesOrExamPapers[k].examPaperId == oldPackages[j].taskCoursesOrExamPapers[l].examPaperId
									|| packages[i].taskCoursesOrExamPapers[k].courseId == oldPackages[j].taskCoursesOrExamPapers[l].courseId){
								var sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
								packages[i].taskCoursesOrExamPapers[k].sort = sort;
								//packages[i].taskCoursesOrExamPapers[k].sort = oldPackages[j].taskCoursesOrExamPapers[l].sort;
								
								break;
							}*/
							//20170327 modify e
						}
					}
					break;
				}
			}
		}
		//20170318 add e
		for(var i in packages){
			//按任务包内部排序
			//20170327 modify s
			/*if(packages[i].isOld){
				for(var j in packages[i].taskCoursesOrExamPapers){
					packages[i].taskCoursesOrExamPapers[j]["preTaskIds"] = "";
					packages[i].taskCoursesOrExamPapers[j]["preTaskNames"] = "";
					packages[i].taskCoursesOrExamPapers[j]["choosedItemArr"] = [];
				}
			}*/
			//20170327 modify e
			packages[i].taskCoursesOrExamPapers.sort(function(a,b){
				if(a.sort && b.sort){
					return Number(a.sort) - Number(b.sort);
				}else {
					return 0;
				}
			});
			
			newTaskCoursesOrExamPapers = newTaskCoursesOrExamPapers.concat(packages[i].taskCoursesOrExamPapers);
		}
		//生成排序、任务编号字段
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			currItem.sort = i+1;
			currItem.preTaskName = "任务" + currItem.sort;
		}
		//20170327 modify s
		for(var i = 0; i < newTaskCoursesOrExamPapers.length; i++ ){
			currItem = newTaskCoursesOrExamPapers[i];
			//console.log(currItem.choosedItemArr,"~~");
			if(currItem.choosedItemArr && currItem.choosedItemArr.length > 0){
				var pretaskSorts = [], pretaskNames = [], preTaskNames = "", preTaskIds = "";
				for(var j = 0; j < currItem.choosedItemArr.length; j++){
					//console.log("j", currItem.choosedItemArr[j]);
					if(currItem.choosedItemArr[j].sort >= (i + 1) || j >= i){
						currItem.choosedItemArr.splice(j, 1);
						j--;
					}else if(currItem.choosedItemArr[j].sort != undefined && currItem.choosedItemArr[j].sort != ""){
						pretaskSorts.push(currItem.choosedItemArr[j].sort);
						pretaskNames.push(currItem.choosedItemArr[j].preTaskName != undefined && currItem.choosedItemArr[j].preTaskName != "" ? currItem.choosedItemArr[j].preTaskName : "任务" + currItem.choosedItemArr[j].sort);
					}
				}
				pretaskSorts.sort(function(a, b){
					return parseInt(a.sort) - parseInt(b.sort);
				});
				pretaskNames.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currItem.preTaskIds = pretaskSorts.join(",");
				currItem.preTaskNames = pretaskNames.join(","); 
			} 
		}
		//20170327 modify e
		$scope.taskPackage[0].taskCoursesOrExamPapers = newTaskCoursesOrExamPapers;
	});
	$scope.initActivityTime = function(){
    	if($scope.taskPackage && !$scope.taskPackage.length){
    		return;
    	}
    	var currTaskCoursesOrExamPaper;
    	for(var i in $scope.taskPackage[0].taskCoursesOrExamPapers){
    		currTaskCoursesOrExamPaper = $scope.taskPackage[0].taskCoursesOrExamPapers[i];
    		if(currTaskCoursesOrExamPaper.preTaskIds != "" && currTaskCoursesOrExamPaper.preTaskIds != undefined){
    			currTaskCoursesOrExamPaper["startTime"] = $scope.formData.activityStartTime;
    			currTaskCoursesOrExamPaper["endTime"] = $scope.formData.activityEndTime;
    			if(currTaskCoursesOrExamPaper.retakingInfo && currTaskCoursesOrExamPaper.retakingInfo.length > 0){
                	for(var j = 0; j < currTaskCoursesOrExamPaper.retakingInfo.length; j++){
                		currTaskCoursesOrExamPaper.retakingInfo[j]["startTime"] = $scope.formData.activityStartTime;
                		currTaskCoursesOrExamPaper.retakingInfo[j]["endTime"] = $scope.formData.activityEndTime;
                	}
        		} 
    		}
    	}
    }
	//创建补考日期 html
    $scope.createRetaking = function(item){
    	/*if(item.preTaskIds != "" && item.preTaskIds != undefined && $scope.formData.activityStartTime != undefined && $scope.formData.activityEndTime != undefined && $scope.formData.activityStartTime != "" && $scope.formData.activityEndTime != ""){
    		if(item.retakingExamCount==undefined){
    			item.retakingInfo = []; 
    		}else{
    			item.retakingInfo = []; 
            	for(var i =1;i<=item.retakingExamCount;i++){
            		item.retakingInfo.push({startTime: $scope.formData.activityStartTime, endTime : $scope.formData.activityEndTime});
            	}
    		}
    	}else{
	    	if(item.retakingExamCount==undefined&&item.retakingInfo!=undefined){
	    		item.retakingInfo = []; 
	    	}
	    	if(item.retakingExamCount!=undefined&&item.retakingInfo!=undefined){
	    		if(item.retakingExamCount >= item.retakingInfo.length){
	    			var len = item.retakingInfo.length;
	        		for(var i =0;i<item.retakingExamCount-len;i++){
	            		item.retakingInfo.push({startTime:'',endTime:''});
	            	}
	        	}else{
	        		var len = item.retakingInfo.length - item.retakingExamCount ;
	        		item.retakingInfo.splice(item.retakingExamCount,len);
	        	}
	    	}
	    	if(item.retakingExamCount!=undefined&&item.retakingInfo==undefined){
	    		item.retakingInfo = []; 
	        	for(var i =1;i<=item.retakingExamCount;i++){
	        		item.retakingInfo.push({startTime:'',endTime:''});
	        	}
	    	}
    	}*/
    	var startTime = $scope.formData.activityStartTime != undefined && $scope.formData.activityStartTime != "" ? $scope.formData.activityStartTime : "";
		var endTime = $scope.formData.activityEndTime != undefined && $scope.formData.activityEndTime != "" ? $scope.formData.activityEndTime : "";
		if(item.retakingExamCount == undefined && item.retakingInfo != undefined){
    		item.retakingInfo = []; 
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo != undefined){
    		if(item.retakingExamCount >= item.retakingInfo.length){
    			var len = item.retakingInfo.length;
        		for(var i =0;i<item.retakingExamCount-len;i++){
            		item.retakingInfo.push({startTime:startTime, endTime: endTime});
            	}
        	}else{
        		var len = item.retakingInfo.length - item.retakingExamCount ;
        		item.retakingInfo.splice(item.retakingExamCount, len);
        		for(var i = 0; i < item.retakingInfo.length; i++){
        			item.retakingInfo[i]["startTime"] = startTime;
        			item.retakingInfo[i]["endTime"] = endTime;
            	}
        	}
    	}
    	if(item.retakingExamCount != undefined && item.retakingInfo == undefined){
    		item.retakingInfo = []; 
        	for(var i =1;i<=item.retakingExamCount;i++){
        		item.retakingInfo.push({startTime:startTime,endTime: endTime});
        	}
    	}
    }
    //切换签订培训协议
    $scope.toggleProtocol = function(){
    	$scope.activity.trainFee = undefined;
    	if($scope.activity.protocol=='Y'){
    		$scope.activity.protocol = 'N';
    	}else{
    		$scope.activity.protocol = 'Y';
    	}
    }
    //计算课程费用
    $scope.calculateTotalPrice = function(item){
    	if(item.unitPrice!=undefined&&item.courseTime!=undefined){
    		item.totalPrice = item.unitPrice*item.courseTime/60;
    	}else{
    		item.totalPrice = 0;
    	}
    }
    //切换线下、线上考试
    $scope.toggleOfflineExam = function(item){
    	item.examAddress = '';
    	if(item.offlineExam=='2'){
    		item.offlineExam = '1';
    	}else{
    		item.offlineExam = '2';
    	}
    }
    //向上移动
    $scope.sortUp = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort+1;//后移元素的sort+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index-1].sort;//后移元素的preTaskName编号+1
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	//currItem.sort = index-2;
    	item.sort = item.sort-1;//前移的元素sort-1
    	item.preTaskName = "任务"+item.sort;
    	//清空前置任务数据
    	item.preTaskIds = "";
    	item.preTaskNames = "";
    	item.choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index-1,0,item);
    }
    //向下移动	
    $scope.sortDown = function(item){
    	//$scope.taskPackage = $scope.taskPackageBackup.concat();
    	var index = $scope.taskPackage[0].taskCoursesOrExamPapers.indexOf(item);
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort = $scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort-1;//前移的元素sort-1
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskName = "任务"+$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].sort;
    	//清空前置任务数据
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskIds = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].preTaskNames = "";
    	$scope.taskPackage[0].taskCoursesOrExamPapers[index+1].choosedItemArr = [];
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index,1);
    	item.sort = item.sort+1;//后移元素的sort+1
    	item.preTaskName = "任务"+item.sort;
    	$scope.taskPackage[0].taskCoursesOrExamPapers.splice(index+1,0,item);
    }
    $scope.$on("FormIsValid", function(e, preTaskInfo){
    	e.stopPropagation();
    	$scope.activity.preTaskInfo = preTaskInfo;
    	doSave();  
	});
  //切换活动中英文
    $scope.changeCN = function(){
    	if($scope.activity.isCN=='Y'){
    		$scope.activity.isCN = 'N';
    		if($scope.activity.isEN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isCN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    $scope.changeEN = function(){
    	if($scope.activity.isEN=='Y'){
    		$scope.activity.isEN = 'N';
    		if($scope.activity.isCN=='N'){
    			$scope.activity.language='';
    		}else{
    			$scope.activity.language='Y';
    		}
    	}else{
    		$scope.activity.isEN = 'Y';
    		$scope.activity.language='Y';
    	}
    }
    //保存按钮    
    var doSave=function(){
    	$scope.activityForm.$submitted = true;
    	if($scope.activityForm.$invalid)
		{
    		dialogService.setContent("表单存在不合法字段,请按提示要求调整表单后重试").setShowDialog(true);
		 	return;
		}
    	$scope.$emit("isSaving", true);
    	$http({
			method : 'POST',
			url  : '/enterpriseuniversity/services/activity/add',
			data : $scope.activity, 
			headers : { 'Content-Type': 'application/json' }
		}).success(function(data) {
			$scope.$emit("isSaving", false); 
			if(data.result=="success"){
				dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   			$scope.go('/activity/activityList','activityList',null);
		   		},2000);
			}else if(data.result=="examTimeOut"){
				dialogService.setContent("考试时间超出起止时间间隔！").setShowDialog(true);
			}else if(data.result=="protected"){
				dialogService.setContent("活动报名日期已结束或活动已开始！").setShowDialog(true);
			}else{
				dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
   				$timeout(function(){
		   			dialogService.sureButten_click(); 
		   		},2000);
			}
		})
		.error(function(){
			$scope.$emit("isSaving", false); 
			dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			$timeout(function(){
	   			dialogService.sureButten_click(); 
	   		},2000);
		});
    }
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };
}])
.controller('FormSubmitController',['$scope', '$http', '$timeout', 'dialogService', function($scope, $http, $timeout, dialogService){
	var formValidate = function(){
		var activity = {};
	    angular.copy($scope.activity, activity);
	    activity.preTaskInfo = [];
	    //协议日期
	    if (activity.protocol == "Y")
	    {
	    	console.log("activity.protocolStartTime",activity.protocolStartTime);
	    	console.log("activity.protocolEndTime",activity.protocolEndTime);
	        if (activity.protocolStartTime && activity.protocolEndTime)
	        {
	            activity.protocolStartTime = activity.protocolStartTime.replace(/-/g, "/");
	            activity.protocolEndTime = activity.protocolEndTime.replace(/-/g, "/");
	            if ((new Date(activity.protocolEndTime).getTime() / 1000 - (new Date(activity.protocolStartTime).getTime() / 1000)) < 0) {
	                dialogService.setContent("协议终止日期不能大于协议生效日期").setShowDialog(true);
	                return;
	            }
	        }
	        else if (activity.protocolStartTime != undefined && activity.protocolStartTime != '') {
	            dialogService.setContent("请填写协议终止日期").setShowDialog(true);
	            return;
	        }
	        else {
	            dialogService.setContent("请填写协议生效日期").setShowDialog(true);
	            return;
	        }
	    }
	    
	    //活动时间
	    console.log("activity.activityStartTime",activity.activityStartTime);
    	console.log("activity.activityEndTime",activity.activityEndTime);
	    if (activity.activityStartTime && activity.activityEndTime)
	    {
	        activity.activityStartTime = activity.activityStartTime.replace(/-/g, "/");
	        activity.activityEndTime = activity.activityEndTime.replace(/-/g, "/");
	        if (activity.protocol == "Y")
	        {
	            //判断培训活动时间是否在培训活动协议时间范围内
	            if (new Date(activity.activityStartTime).getTime() / 1000 - (new Date(activity.protocolStartTime).getTime() / 1000 ) < 0 
	            	|| new Date(activity.protocolEndTime).getTime() / 1000 - (new Date(activity.activityEndTime).getTime() / 1000 ) < 0)
	            {
	                dialogService.setContent("培训活动开始、结束时间不在培训协议开始、结束时间范围内").setShowDialog(true);
	                return;
	            }
	        }
	        if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(activity.activityStartTime).getTime() / 1000) < 0) {
	            dialogService.setContent("活动结束时间不能大于活动开始时间").setShowDialog(true);
	            return;
	        }
	    }
	    else if (activity.activityStartTime != undefined && activity.activityStartTime != '') {
	        dialogService.setContent("请填写活动结束时间").setShowDialog(true);
	        return;
	    }
	    else {
	        dialogService.setContent("请填写活动开始时间").setShowDialog(true);
	        return;
	    }
	    
	    //报名时间
	    if (activity.needApply == "Y")
	    {
	    	console.log("activity.applicationStartTime",activity.applicationStartTime);
	    	console.log("activity.applicationDeadline",activity.applicationDeadline);
	        if (activity.applicationStartTime && activity.applicationDeadline)
	        {
	            activity.applicationStartTime = activity.applicationStartTime.replace(/-/g, "/");
	            activity.applicationDeadline = activity.applicationDeadline.replace(/-/g, "/");
	            if (new Date(activity.activityStartTime).getTime() / 1000 - (new Date(activity.applicationDeadline).getTime() / 1000) < 0) {
	                dialogService.setContent("培训活动开始时间须大于活动报名结束时间").setShowDialog(true);
	                return;
	            }
	            if (new Date(activity.applicationDeadline).getTime() / 1000 - (new Date(activity.applicationStartTime).getTime() / 1000) < 0) {
	                dialogService.setContent("报名开始时间不能大于报名结束时间").setShowDialog(true);
	                return;
	            }
	        }
	        else if (activity.applicationStartTime != undefined && activity.applicationStartTime != '') {
	            dialogService.setContent("请填写报名结束时间").setShowDialog(true);
	            return;
	        }
	        else {
	            dialogService.setContent("请填写报名开始时间").setShowDialog(true);
	            return;
	        }
	    }
	    
	    //任务包时间校验
	    var currPackage, taskPackage = [];
	    angular.copy($scope.taskPackage, taskPackage);
	    for (var i = 0; i < taskPackage.length; i++)
	    {
	    	currPackage =  taskPackage[i];
	        for (var j = 0; j < currPackage.taskCoursesOrExamPapers.length; j++)
	        {
	        	var hasPreTask = false;
	            var currCoursesOrExamPaper = currPackage.taskCoursesOrExamPapers[j], taskInfo = {};
	            taskInfo.pretaskId = currCoursesOrExamPaper.preTaskIds == undefined ? "" : currCoursesOrExamPaper.preTaskIds;
	            if(taskInfo.pretaskId != ""){
	            	hasPreTask = true;
	            }
	            console.log("currCoursesOrExamPaper.startTime" + j,currCoursesOrExamPaper.startTime);
		    	console.log("currCoursesOrExamPaper.endTime" + j,currCoursesOrExamPaper.endTime);
		    	if(!currCoursesOrExamPaper.courseId || currCoursesOrExamPaper.courseOnline=='Y'){
		            if (currCoursesOrExamPaper.startTime && currCoursesOrExamPaper.endTime){
		            	taskInfo.startTime = currCoursesOrExamPaper.startTime;
	            		taskInfo.endTime = currCoursesOrExamPaper.endTime;
		                currCoursesOrExamPaper.formattedStartTime = currCoursesOrExamPaper.startTime.replace(/-/g, "/");
		                currCoursesOrExamPaper.formattedEndTime = currCoursesOrExamPaper.endTime.replace(/-/g, "/");
//		                if (currCoursesOrExamPaper.preTaskIds)
//		                {
//		                    var lastPreTaskSort = currCoursesOrExamPaper.preTaskIds.split(",")[currCoursesOrExamPaper.preTaskIds.length - 1];
//		                    for (var index in currPackage.taskCoursesOrExamPapers)
//		                    {
//		                        var indexItem = currPackage.taskCoursesOrExamPapers[index];
//		                        if (indexItem.sort == lastPreTaskSort)
//		                        {
//		                            if (new Date(currCoursesOrExamPaper.startTime).getTime() / 1000 - (new Date(indexItem.formattedEndTime).getTime() / 1000) < 0)
//		                            {
//		                                dialogService.setContent("任务" + (j + 1) + "的开始时间不得早于(前置)任务" + indexItem.sort + "的结束时间").setShowDialog(true);
//		                                return;
//		                            }
//		                        }
//		                    }
//		                }
		                
		                //20170323 add s
		                if (hasPreTask)
		                {
		                	//有前置任务，需要校验任务时间范围在活动开始、结束时间之间
		                	if (new Date(currCoursesOrExamPaper.startTime).getTime() / 1000 - (new Date(activity.activityStartTime).getTime() / 1000) < 0)
                            {
                                dialogService.setContent("任务" + (j + 1) + "的开始时间不在活动时间范围内").setShowDialog(true);
                                return;
                            }
		                	if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.endTime).getTime() / 1000) < 0)
                            {
                                dialogService.setContent("任务" + (j + 1) + "的结束时间不在活动时间范围内").setShowDialog(true);
                                return;
                            }
		                } else {
		                	//无前置任务
				            //判断第一个任务的开始时间是否在培训活动开始时间范围内
				            if (j == 0)
				            {
				                if (new Date(currCoursesOrExamPaper.formattedStartTime).getTime() / 1000 - (new Date(activity.activityStartTime).getTime() / 1000)< 0)
				                {
				                    dialogService.setContent("任务" + (j + 1) + "的开始时间不在培训活动开始时间范围内").setShowDialog(true);
				                    return;
				                }
				            }
				            //判断最后一个任务的结束时间是否在培训活动结束时间范围内
				            else if (j == currPackage.taskCoursesOrExamPapers.length - 1)
				            {
				                if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.formattedEndTime).getTime() / 1000) < 0)
				                {
				                    dialogService.setContent("任务" + (j + 1) + "的结束时间不在培训活动结束时间范围内").setShowDialog(true);
				                    return;
				                }
				            }
		                	//后一个任务开始时间须要在前一个任务结束时间之后
				            /*else {
				            	var preCoursesOrExamPaper = currPackage.taskCoursesOrExamPapers[j-1];
				            	if (new Date(currCoursesOrExamPaper.formattedStartTime).getTime() / 1000 - (new Date(preCoursesOrExamPaper.formattedEndTime).getTime() / 1000)< 0)
				                {
				                    dialogService.setContent("任务" + (j + 1) + "的开始时间须在任务" + j + "的结束时间之后").setShowDialog(true);
				                    return;
				                }
				            }*/
		                	 
		                }
		                //20170323 add e
		                if (new Date(currCoursesOrExamPaper.endTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.startTime).getTime() / 1000) < 0) {
		                    dialogService.setContent("任务" + (j + 1) + "的开始时间不能大于结束时间").setShowDialog(true);
		                    return;
		                }
		                if(currCoursesOrExamPaper.courseId){
		                	if (new Date(currCoursesOrExamPaper.endTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.startTime).getTime() / 1000) < currCoursesOrExamPaper.courseTime * 60) {
		                        dialogService.setContent("开始时间、结束时间间隔不得小于课程时长").setShowDialog(true);
		                        return ;
		                	}
		                }else{
		                	if (new Date(currCoursesOrExamPaper.endTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.startTime).getTime() / 1000) < parseInt(currCoursesOrExamPaper.examTime) * 60) {
		                        dialogService.setContent("任务" + (j + 1) + "的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
		                        return;
		                    }
		                }
		                
		            }
		            else if (currCoursesOrExamPaper.startTime != undefined && currCoursesOrExamPaper.startTime != '') {
		                dialogService.setContent("请填写任务" + (j + 1) + "的结束时间").setShowDialog(true);
		                return;
		            }
		            else {
		                dialogService.setContent("请填写任务" + (j + 1) + "的开始时间").setShowDialog(true);
		                return;
		            }
		            
		            //20170323 modify s
		            /*//判断第一个任务的开始时间是否在培训活动开始时间范围内
		            if (j == 0)
		            {
		                if (new Date(currCoursesOrExamPaper.formattedStartTime).getTime() / 1000 - (new Date(activity.activityStartTime).getTime() / 1000)< 0)
		                {
		                    dialogService.setContent("任务" + (j + 1) + "的开始时间不在培训活动开始时间范围内").setShowDialog(true);
		                    return;
		                }
		            }
		            //判断最后一个任务的结束时间是否在培训活动结束时间范围内
		            if (j == currPackage.taskCoursesOrExamPapers.length - 1)
		            {
		                if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.formattedEndTime).getTime() / 1000) < 0)
		                {
		                    dialogService.setContent("任务" + (j + 1) + "的结束时间不在培训活动结束时间范围内").setShowDialog(true);
		                    return;
		                }
		            }*/
		            //20170323 modify e
		    	}
	            //课程
	            if (currCoursesOrExamPaper.courseId)
	            {
	            	taskInfo.packageId = currCoursesOrExamPaper.packageId ;
	                taskInfo.taskId = currCoursesOrExamPaper.courseId ;
	                taskInfo.sort = currCoursesOrExamPaper.sort ;
	                taskInfo.lecturerId = currCoursesOrExamPaper.lecturerId ;
	                taskInfo.courseAddress = currCoursesOrExamPaper.courseAddress ;
	                taskInfo.courseTime = currCoursesOrExamPaper.courseTime ;
	                taskInfo.unitPrice = currCoursesOrExamPaper.unitPrice ;
	                taskInfo.totalPrice = currCoursesOrExamPaper.totalPrice ;
	            }
	            //试卷
	            if (currCoursesOrExamPaper.examPaperId)
	            {
	            	taskInfo.packageId = currCoursesOrExamPaper.packageId ;
	                taskInfo.taskId = currCoursesOrExamPaper.examPaperId ;
	                taskInfo.sort = currCoursesOrExamPaper.sort ;
	                taskInfo.offlineExam = currCoursesOrExamPaper.offlineExam ;
	                if (currCoursesOrExamPaper.offlineExam == "2")
	                {
	                    //线下考试地址
	                    taskInfo.examAddress = currCoursesOrExamPaper.examAddress == undefined ? "" : currCoursesOrExamPaper.examAddress;
	                }
	                taskInfo.retakingExamTimes = currCoursesOrExamPaper.retakingExamCount ;
	                taskInfo.offlineExam = currCoursesOrExamPaper.offlineExam == undefined ? "1" : currCoursesOrExamPaper.offlineExam;
	                taskInfo.retakingExamBeginTimeList = [];
	                taskInfo.retakingExamEndTimeList = [];
	                var retakingStartTime,retakingEndTime;
	                console.log("currCoursesOrExamPaper.retakingExamCount" ,currCoursesOrExamPaper.retakingExamCount);
			    	console.log("currCoursesOrExamPaper.retakingInfo" ,currCoursesOrExamPaper.retakingInfo);
	                for (var k = 0; k < currCoursesOrExamPaper.retakingExamCount; k++)
	                {
	                    retakingStartTime = currCoursesOrExamPaper.retakingInfo[k].startTime;
	                    retakingEndTime = currCoursesOrExamPaper.retakingInfo[k].endTime;
	                    taskInfo.retakingExamBeginTimeList[k] = retakingStartTime;
	                    taskInfo.retakingExamEndTimeList[k] = retakingEndTime;
	                    if (retakingStartTime != undefined && retakingStartTime != '' && retakingEndTime != undefined && retakingEndTime != '')
	                    {
	                        retakingStartTime = retakingStartTime.replace(/-/g, "/");
	                        retakingEndTime = retakingEndTime.replace(/-/g, "/");
	                        //20170323 add s
			                if (hasPreTask){
			                	if (new Date(retakingStartTime).getTime() / 1000 - (new Date(activity.activityStartTime).getTime() / 1000) < 0)
	                            {
	                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不在活动时间范围内").setShowDialog(true);
	                                return;
	                            }
			                	if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(retakingEndTime).getTime() / 1000) < 0)
	                            {
	                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的结束时间不在活动时间范围内").setShowDialog(true);
	                                return;
	                            }
			                } else {
			                	//无前置任务校验首次补考日期不得早于首次考试结束日期
		                        /*if (k == 0)
		                        {
		                            if (new Date(retakingStartTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.formattedEndTime).getTime() / 1000) < 0)
		                            {
		                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不得早于首次考试的结束时间").setShowDialog(true);
		                                return;
		                            }
		                        }*/
		                        //无前置任务校验补考日期不得早于上次考试结束日期
		                       /* if (k > 0)
		                        {
		                            if (new Date(retakingStartTime).getTime() / 1000 - (new Date(taskInfo.retakingExamEndTimeList[k - 1]).getTime() / 1000) < 0)
		                            {
		                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不能小于补考日期" + k + "的结束时间").setShowDialog(true);
		                                return;
		                            }
		                        }*/
		                        if (k == currCoursesOrExamPaper.retakingExamCount - 1)
		                        {
		                            if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(retakingEndTime).getTime() / 1000) < 0)
		                            {
		                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的结束时间不在培训活动结束时间范围内").setShowDialog(true);
		                                return;
		                            }
		                        }
			                }
			                //20170323 add e
			                
			                //20170323 modify s
			                /*//无前置任务校验首次补考日期不得早于首次考试结束日期
	                        if (k == 0)
	                        {
	                            if (new Date(retakingStartTime).getTime() / 1000 - (new Date(currCoursesOrExamPaper.formattedEndTime).getTime() / 1000) < 0)
	                            {
	                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不得早于首次考试的结束时间").setShowDialog(true);
	                                return;
	                            }
	                        }
	                        //无前置任务校验补考日期不得早于上次考试结束日期
	                        if (k > 0)
	                        {
	                            if (new Date(retakingStartTime).getTime() / 1000 - (new Date(taskInfo.retakingExamEndTimeList[k - 1]).getTime() / 1000) < 0)
	                            {
	                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不能小于补考日期" + k + "的结束时间").setShowDialog(true);
	                                return;
	                            }
	                        }
	                        if (k == currCoursesOrExamPaper.retakingExamCount - 1)
	                        {
	                            if (new Date(activity.activityEndTime).getTime() / 1000 - (new Date(retakingEndTime).getTime() / 1000) < 0)
	                            {
	                                dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的结束时间不在培训活动结束时间范围内").setShowDialog(true);
	                                return;
	                            }
	                        }*/
			                //20170323 modify e
			                
	                        if (new Date(retakingEndTime).getTime() / 1000 - (new Date(retakingStartTime).getTime() / 1000) < 0)
	                        {
	                            dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间不能大于结束时间").setShowDialog(true);
	                            return;
	                        }
	                        if (new Date(retakingEndTime).getTime() / 1000 - (new Date(retakingStartTime).getTime() / 1000) < parseInt(currCoursesOrExamPaper.examTime) * 60)
	                        {
	                            dialogService.setContent("任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始、结束时间间隔不能小于考试时长").setShowDialog(true);
	                            return;
	                        }
	                    }
	                    else if (retakingStartTime != undefined && retakingStartTime != '')
	                    {
	                        dialogService.setContent("请填写任务" + (j + 1) + "的补考日期" + (k + 1) + "的结束时间").setShowDialog(true);
	                        return;
	                    }
	                    else
	                    {
	                        dialogService.setContent("请填写任务" + (j + 1) + "的补考日期" + (k + 1) + "的开始时间").setShowDialog(true);
	                        return;
	                    }
	                }
	            }
	            activity.preTaskInfo[j] = taskInfo ;
	        }
	    }
	    $scope.$emit("FormIsValid", activity.preTaskInfo);
	}
	
	$scope.doFormSave = function(){
		formValidate();
	}
	 
}])
.controller('ViewActivityController', ['$scope', '$http', '$stateParams', '$q', '$timeout','dialogService', function ($scope, $http, $stateParams, $q, $timeout, dialogService) {
	//$scope.$parent.cm = {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动' , gcmn: '查看培训活动'};
	$scope.$emit("cm", {pmc:'pmc-c', pmn: '活动管理 ', cmc:'cmc-cb', cmn: '培训活动' , gcmn: '查看培训活动'});
	$scope.deferred = $q.defer();
	$scope.taskPackageDeffered = $q.defer();
	$scope.taskPackage = [];
	if($stateParams.id){
		$scope.$emit('isLoading', true);
	    $http.get("/enterpriseuniversity/services/activity/findDetails?activityId=" + $stateParams.id).success(function (data) {
	        $scope.deferred.resolve(data);
	    }).error(function(data){
	    	$scope.deferred.reject(data);
	    })
	}else {
		//未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
	}
	$scope.deferred.promise.then(function(data) {  // 成功回调
		$scope.$emit('isLoading', false);
		$scope.activity = $scope.formData = data; 
		$scope.taskPackage = [];
		var currPackage, currCoursesOrExamPaper, taskCoursesOrExamPapers = [], packages = $scope.activity.packages ? $scope.activity.packages : [];
		for(var i in packages){
			currPackage = packages[i];
			for(var j in currPackage.taskCoursesOrExamPapers){
				currCoursesOrExamPaper = currPackage.taskCoursesOrExamPapers[j];
				currCoursesOrExamPaper["packageId"] = currPackage.packageId;
				//20170318 add s
				//currCoursesOrExamPaper["preTaskNames"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "";
				var nameArr = (currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.preName : "").split(",");
				nameArr.sort(function(a,b){
					return Number(a.replace("任务",""))-Number(b.replace("任务",""));
				});
				currCoursesOrExamPaper["preTaskNames"] = nameArr.join(",");
				//20170318 add e
				currCoursesOrExamPaper["preTaskIds"] = currCoursesOrExamPaper.pretaskId;
				currCoursesOrExamPaper["preTaskName"] = "任务" + currCoursesOrExamPaper.sort;
				
				//20170316 add
				//currCoursesOrExamPaper["choosedItemArr"] = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				var currPretaskArr = [];
				 
				var currPretaskIds = currCoursesOrExamPaper.pretaskId ? currCoursesOrExamPaper.pretaskId.split(",") : [];
				
				for(var f = 0 ; f < currPretaskIds.length; f++){
					if(currPretaskIds[f] == ""){
						continue;
					}
					currPretaskArr.push({ sort : currPretaskIds[f]});
				}
				currCoursesOrExamPaper["choosedItemArr"] = currPretaskArr;
				
				//20170316 add
				if(currCoursesOrExamPaper.examId){
					currCoursesOrExamPaper["retakingExamCount"] = currCoursesOrExamPaper.retakingExamTimes;
					currCoursesOrExamPaper["retakingInfo"] = []; 
					for(var m=1; m <= currCoursesOrExamPaper.retakingExamTimes; m++){
						currCoursesOrExamPaper.retakingInfo.push({
							startTime : currCoursesOrExamPaper.retakingExamBeginTimeList[m], 
							endTime : currCoursesOrExamPaper.retakingExamEndTimeList[m]
						});
					}
				}
				if(currCoursesOrExamPaper.courseId){
					currCoursesOrExamPaper.lecturerId = currCoursesOrExamPaper.lecturerId ? Number(currCoursesOrExamPaper.lecturerId) : "";
				}
				taskCoursesOrExamPapers.push(currCoursesOrExamPaper);
			}
			taskCoursesOrExamPapers.sort(function(a,b){
				if(a.sort && b.sort){
					return Number(a.sort) - Number(b.sort);
				}else {
					return 0;
				}
			});
		}
		$scope.taskPackage.push({ packages : packages, taskCoursesOrExamPapers : taskCoursesOrExamPapers });
		$scope.taskPackageDeffered.resolve($scope.taskPackage);
		$scope.activity.activityId = $stateParams.id;
		if($scope.activity.city=='\"null\"'){
			$scope.activity.city = undefined;
		}
	}, function(data) {  // 错误回调
		$scope.$emit('isLoading', false);
	    dialogService.setContent("页面数据初始化异常").setShowDialog(true);
	});
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/activityList','activityList',null);
    };
}])

//选择课活动图片控制器
.controller('ActivtiyImgController', ['$scope', 'FileUploader', 'dialogService','$http', function ($scope, FileUploader, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url : '/enterpriseuniversity/services/file/upload/activityImg',
            autoUpload : true
        }
    );
    //上传课程封面图片过滤器
    imgUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的培训活动封面图片，请先删除旧的培训活动封面图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("仅允许上传一张培训活动封面图片").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件不符合培训活动封面图片格式要求").setShowDialog(true);
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048)
            {
            	dialogService.setContent("上传文件大小不符合要求(大于2M)").setShowDialog(true);
            	return item.size/1024 <= 2048;
            }
            return true;
        }
    });
    //编辑页面培训活动封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.activityImg&&$scope.formData.activityImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.activityImg);
    		}
        }, function(data) {
            
        }); 
    } 
    //修改活动图片
    $scope.buildActivityImg = function(imgUrl){
		$scope.$parent.formData.activityImg = imgUrl;
    }
    //预览框显示/隐藏控制
    $scope.openPreview = false;
    //预览
    $scope.preview = function(){
    	if($scope.$parent.formData.activityImg&&$scope.$parent.formData.activityImg!=""){
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.activityImg;
    	}
    	else
    	{
    		dialogService.setContent("封面图片还未上传完毕,请稍后重试预览").setShowDialog(true);
    	}
    }
    //关闭预览框
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildActivityImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
   		if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("activityImg").value=[];
        $scope.buildActivityImg(""); 
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    // CALLBACKS
    imgUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("解析上传文件失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildActivityImg(response.url);//接收返回的文件地址
        }
    };
}])
//活动英文封面
.controller('ActivtiyImgEnController', ['$scope', 'FileUploader', 'dialogService','$http', function ($scope, FileUploader, dialogService,$http) {
    var imgEnUploader = $scope.imgEnUploader = new FileUploader(
        {
            url : '/enterpriseuniversity/services/file/upload/activityImg',
            autoUpload : true
        }
    );
    //上传课程封面图片过滤器
    imgEnUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currEnFileUrlQueue&&$scope.currEnFileUrlQueue.length>0)
            {
            	dialogService.setContent("要上传新的培训活动封面图片，请先删除旧的培训活动封面图片").setShowDialog(true);
            	return !($scope.currEnFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("仅允许上传一张培训活动封面图片").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件不符合培训活动封面图片格式要求").setShowDialog(true);
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
            if( item.size/1024 > 2048)
            {
            	dialogService.setContent("上传文件大小不符合要求(大于2M)").setShowDialog(true);
            	return item.size/1024 <= 2048;
            }
            return true;
        }
    });
    //编辑页面培训活动封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currEnFileUrlQueue =[]; 
    		if($scope.formData.activityImgEn&&$scope.formData.activityImgEn!="")
    		{
    			$scope.currEnFileUrlQueue.push($scope.formData.activityImgEn);
    		}
        }, function(data) {
            
        }); 
    } 
    //修改活动图片
    $scope.buildActivityImgEn = function(imgUrl){
		$scope.$parent.formData.activityImgEn = imgUrl;
    }
    //预览框显示/隐藏控制
    $scope.openPreviewEn = false;
    //预览
    $scope.previewEn = function(){
    	if($scope.$parent.formData.activityImgEn&&$scope.$parent.formData.activityImgEn!=""){
    		$scope.openPreviewEn = true;
    		$scope.previewImgEnUrl = "/enterpriseuniversity/"+$scope.$parent.formData.activityImgEn;
    	}
    	else
    	{
    		dialogService.setContent("封面图片还未上传完毕,请稍后重试预览").setShowDialog(true);
    	}
    }
    //关闭预览框
    $scope.closePreviewEn = function(){
    	$scope.openPreviewEn = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currEnFileUrlQueue.splice($scope.currEnFileUrlQueue.indexOf(item), 1);
        $scope.buildActivityImgEn("");
    }
    //删除 
    $scope.removeItem = function (item) {
//   		if(item.fileUrl!=null){
//    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
//    	}
        item.remove();
        document.getElementById("activityImgEn").value=[];
        $scope.buildActivityImgEn(""); 
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    // CALLBACKS
    imgEnUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("解析上传文件失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildActivityImgEn(response.url);//接收返回的文件地址
        }
    };
}])
//选择部门控制器
.controller('DeptModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.isBuildOldChoosedItems = false;
    $scope.openModalDialog = false;
	$scope.queryDept = function(){
		//查询部门树数据
		$scope.$emit('isLoading', true);
		$http.get("/enterpriseuniversity/services/orggroups/selectByScope")
			.success(function(response) {
				$scope.$emit('isLoading', false);
				$scope.deptList = response;
				if($scope.isBuildOldChoosedItems&&$scope.itemArr.initCurrChoosedItemArrStatus){
	            	$scope.itemArr.initCurrChoosedItemArrStatus();
	            }
	            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItems){
	            	$scope.itemArr.buildOldChoosedItemArr();
	            }
			})
			.error(function(){
				$scope.$emit('isLoading', false);
				dialogService.setContent("查询部门数据错误").setShowDialog(true);
			});
	}
	//容器及函数
    $scope.itemArr = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	for(var i in $scope.itemArr.currChoosedItemArr)
            	{
                	if($scope.itemArr.currChoosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            } 
            else
            {
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        deleteItem : function (item) {
            for(var i in $scope.itemArr.choosedItemArr){
            	if($scope.itemArr.choosedItemArr[i].code==item.code){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            		break;
            	}
            }
            $scope.buildCode();
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.deptList,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.deptList) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var code = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.deptList,code); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,code){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==code)
        		{
        			if($scope.itemArr.choosedItemArr.length<1)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    					continue;
    				}
    				var isContained = false;
    				for(var j in $scope.itemArr.choosedItemArr)
    				{
    					if($scope.itemArr.choosedItemArr[j].code==LoopNode[i].code)
    					{
    						isContained = true;
    						break;
        				} 
    				}
    				if(!isContained)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    				}
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoop(LoopNode[i].children,code);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,code,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==code)
        		{
        			LoopNode[i].selected = true; 
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.deptList,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,code,'C',parentNodeArr);
            	}
        	}
        	if(loopType=='P'){ 
        		console.log('parentNodeArr', parentNodeArr);
        		for(var j in parentNodeArr){ 
        			if(parentNodeArr[j]!='0'){ 
        				$scope.itemArr.nodeLoopForCollapsed(LoopNode,parentNodeArr[j]);  
        			} 
        		} 
        	} 
        } ,
        //初始化化树的折叠状态 //>
        nodeLoopForCollapsed:function(LoopNode,fatherCode){ 
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fatherCode)
        		{
        			LoopNode[i].collapsed = true;
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForCollapsed(LoopNode[i].children,fatherCode);
            	}
        	}
        },
        //获取selected节点的所有父节点  //输出：parentNodeArr
        nodeLoopForParentNodeFatherCode :function(LoopNode,fathercode,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fathercode)
        		{
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.deptList,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        }
    };
    //编辑页面初始化
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var deptCodeArr = [];
	    	if($scope.formData.deptCodes)
	    	{
	    		var deptCodeArr = $scope.formData.deptCodes.split(",");
	    	}
    		for(var i = 0;i<deptCodeArr.length;i++)
    		{
        		if(deptCodeArr[i]!=""&&$scope.itemArr.oldChooseItemArr.indexOf(deptCodeArr[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(deptCodeArr[i]);
        		}
        	}
	    	$scope.queryDept(); 
        }, function(data) {}); 
    }
    //打开选择部门模态框
    $scope.$parent.$parent.doOpenDeptModal = function(){
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
    	$scope.isBuildOldChoosedItems = true;
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
        //重新查询模态框中的数据
        $scope.queryDept();
    }
    //生成所属部门字段值
    $scope.buildCode = function () {
        var deptCodeStr = "";
        for (var i in $scope.itemArr.choosedItemArr) {
        	deptCodeStr = deptCodeStr + "," + $scope.itemArr.choosedItemArr[i].code;
        }
        $scope.$parent.$parent.formData.deptCodes = deptCodeStr;
    }
    $scope.doSure= function () {
    	$scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildCode();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
//员工等级控制器
.controller('EmployeeGradeModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.openModalDialog = false;
    $scope.employeeGrade=[
      {id:1,name:"P0"},{id:2,name:"P1"},{id:3,name:"P2"},{id:4,name:"P3"},{id:5,name:"P4"},
      {id:6,name:"P5"},{id:7,name:"P6"},{id:8,name:"P7"},{id:9,name:"P8"},{id:10,name:"P9"},
      {id:11,name:"P10"},{id:12,name:"P11"},{id:13,name:"P12"},{id:14,name:"新人"},{id:15,name:"M0"},
      {id:16,name:"M1"},{id:17,name:"M2"},{id:18,name:"M3"},{id:19,name:"M4"},{id:20,name:"M5"},
      {id:21,name:"M6"},{id:22,name:"M7"},{id:23,name:"M8"},{id:24,name:"M9"},{id:25,name:"M10"},
      {id:26,name:"M11"},{id:27,name:"M12"}
    ];
    $scope.$parent.$parent.doOpenEmployeeGradeModal = function(){
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
    	$scope.itemArr.initCurrChoosedItemArrStatus();
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    }
    /*
     * 模态框容器
     * */
    $scope.itemArr = {
    	isChooseAllItem: false,
    	oldChooseItemArr: [],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //选中choosedItemArr中的项           
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) {
            	for(var j in $scope.employeeGrade){
            		 if($scope.employeeGrade[j].name==$scope.itemArr.currChoosedItemArr[i].name){
            			 $scope.employeeGrade[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked) 
            {
                item.checked = undefined;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.name==$scope.itemArr.currChoosedItemArr[i].name)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            } 
            else 
            {
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        //删除操作
        deleteItem : function (item) {
            for(var i in $scope.itemArr.choosedItemArr){
            	if(item.name==$scope.itemArr.choosedItemArr[i].name){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            		break;
            	}
            }
            $scope.buildEmployeeGradeStr();
        },
        //编辑页面数据回显
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
            for(var i in $scope.itemArr.oldChooseItemArr)
            {
            	var empGradeName = $scope.itemArr.oldChooseItemArr[i];
            	for (var j in $scope.employeeGrade) 
            	{
        			if($scope.employeeGrade[j].name==empGradeName)
        			{
        				if($scope.itemArr.choosedItemArr.length<1)
        				{
        					$scope.itemArr.choosedItemArr.push($scope.employeeGrade[j]);
        					continue;
        				}
        				var isContained = false;
        				for(var k in $scope.itemArr.choosedItemArr)
        				{
        					if($scope.itemArr.choosedItemArr[k].name==$scope.employeeGrade[j].name)
        					{
        						isContained = true;
        						break;
            				} 
        				}
        				if(!isContained)
        				{
        					$scope.itemArr.choosedItemArr.push($scope.employeeGrade[j]);
        				}
	            	}
	            }
            }
            $scope.buildEmployeeGradeStr();
            $scope.$emit('isLoading', false);
        },
        //全选
        chooseAllItem : function(){
        	//...
        	if(this.isChooseAllItem){
        		 
        	}else{
        		 
        	}
        }           
    };
    //初始化管理员项
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	var employeeGradeArr = [];
	    	if($scope.formData.employeeGradeStr)
	    	{
	    		var employeeGradeArr = $scope.formData.employeeGradeStr.split(",");
	    	}
    		for(var i = 0; i< employeeGradeArr.length; i++)
    		{
        		if(employeeGradeArr[i]!=""&&$scope.itemArr.oldChooseItemArr.indexOf(employeeGradeArr[i])==-1)
        		{
        			$scope.itemArr.oldChooseItemArr.push(employeeGradeArr[i]);
        		}
        	}
    		$scope.itemArr.buildOldChoosedItemArr();
        }, function(data) {}); 
    }
    $scope.buildEmployeeGradeStr = function () {
        var employeeGradeStr = "";
        for (var i in $scope.itemArr.choosedItemArr) {
        	employeeGradeStr = employeeGradeStr + "," + $scope.itemArr.choosedItemArr[i].name;
        }
        $scope.$parent.$parent.formData.employeeGradeStr = employeeGradeStr;
    }
    $scope.doSure= function () {
    	$scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildEmployeeGradeStr();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//选择任务包控制器
.controller('TaskPackageModalController', ['$scope', '$http', '$state', '$stateParams', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $state, $stateParams , $timeout, dialogService, dialogStatus) {
	$scope.url = "/enterpriseuniversity/services/taskPackage/findActivityTaskList?pageNum=";
	$scope.openModalDialog = false;
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 15,
		perPageOptions: [20, 50, 100, '全部'],
		rememberPerPage: 'perPageItems',
		onChange: function () {
			if(!$scope.openModalDialog){
				$scope.page = {};
				return;
			}
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			if ($scope.packageName != undefined && $scope.packageName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&packageName=" + $scope.packageName
					.replace(/\%/g,"%25").replace(/\#/g,"%23")
					.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
					.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
			}
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
				"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
			$scope.$emit('isLoading', true);
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
			    $scope.page = response;
			    $scope.paginationConf.totalItems = response.count;
			    $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
			    //初始化模态框选中状态
		        if ($scope.itemArr.initCurrChoosedItemArrStatus) 
		        {
		            $scope.itemArr.initCurrChoosedItemArrStatus();
		        } 
		    }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("任务包数据查询异常！").setShowDialog(true);
            });
		}
	};
     //搜索按钮函数
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
     	$scope.paginationConf.itemsPerPage = 20 ;
		$scope.paginationConf.onChange();
	};
	
	$scope.itemArr = {
    	oldChooseItemArr: [],
        currChoosedItemArr: [],
        choosedItemArr :[],
        //选中choosedItemArr中的项           
	    initCurrChoosedItemArrStatus: function () {
	    	$scope.$emit('isLoading', true);
	        for (var i in $scope.itemArr.currChoosedItemArr) {
	        	for(var j in $scope.page.data){
	        		 if($scope.page.data[j].packageId==$scope.itemArr.currChoosedItemArr[i].packageId){
	        			 $scope.page.data[j].checked = true;
	        		 }
	        	}
	        }
	        $scope.$emit('isLoading', false);
	    } 
	    ,
	    //勾选 or 取消勾选操作
	    chooseItem : function (item) {
	    	if (item.checked) 
            {
                item.checked = undefined;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.packageId==$scope.itemArr.currChoosedItemArr[i].packageId)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            } 
            else 
            {
                item.checked = true;
                for(var i in item.taskCoursesOrExamPapers){
                	item.taskCoursesOrExamPapers[i]["packageId"] = item.packageId;
                }
                $scope.itemArr.currChoosedItemArr.push(item);
            }
	    }           
	};
	 
	//同步数据 
	$scope.taskPackageDeffered && $scope.taskPackageDeffered.promise.then(function(taskPackage) {
		//为初始化勾选状态用
		$scope.itemArr.currChoosedItemArr = taskPackage[0].packages.concat();
	}, function() {});  
	
    $scope.$parent.$parent.doOpenTaskPackageModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	//20170327 modify s
    	//$scope.itemArr.currChoosedItemArr = $scope.taskPackage.length > 0 ? $scope.taskPackage[0].packages : [];
    	$scope.itemArr.currChoosedItemArr = $scope.taskPackage.length > 0 ? $scope.taskPackage[0].packages ? $scope.taskPackage[0].packages.concat() : [] : [];
    	//20170327 modify e
        //初始化每页显示条数
        $scope.paginationConf.itemsPerPage = 20;
        //重新查询模态框中的数据
        $scope.paginationConf.currentPage == 1 ? $scope.paginationConf.onChange() : $scope.paginationConf.currentPage = 1 ;
        //$scope.paginationConf.onChange();
    }
	$scope.doSure = function () {
		$scope.$emit("packageHasChanged", $scope.itemArr.currChoosedItemArr.concat());
		$scope.openModalDialog = false;
		dialogStatus.setHasShowedDialog(false);
	} 
	$scope.doClose = function () {
	   $scope.openModalDialog = false;
	   dialogStatus.setHasShowedDialog(false);
	} 
}])
//选择任务包筛选学习人员控制器
.controller('ChooseTaskPackageForEmpModalController', ['$scope','$http','$state','$stateParams','dialogService', function ($scope, $http, $state, $stateParams, dialogService) {
	$scope.url = "/enterpriseuniversity/services/taskPackage/findTaskList?pageNum=";
	$scope.openTaskPackage2ModalDialog = false;
	$scope.paginationConf = {
		currentPage: 1,
		totalItems: 10,
		itemsPerPage: 20,
		pagesLength: 15,
		perPageOptions: [20, 50, 100, '全部'],
		rememberPerPage: 'perPageItems',
		onChange: function () {
			if(!$scope.openTaskPackage2ModalDialog){
				$scope.page = {};
				return;
			}
			$scope.$httpUrl = "";
			$scope.$httpPrams = "";
			if($scope.packageName != undefined && $scope.packageName != "") {
				$scope.$httpPrams = $scope.$httpPrams + "&packageName=" 
					+ $scope.packageName.replace(/\%/g,"%25").replace(/\#/g,"%23")
					.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
					.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
			}
			$scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
		    	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
			$scope.$emit('isLoading', true);
			$http.get($scope.$httpUrl).success(function (response) {
				$scope.$emit('isLoading', false);
		        $scope.page = response;
		        $scope.paginationConf.totalItems = response.count;
		        $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
				if($scope.itemArr.initCurrChoosedItemArrStatus){
					$scope.itemArr.initCurrChoosedItemArrStatus();
				} 
		    }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("任务包数据查询异常！").setShowDialog(true);
            });
		}
	};
	//搜索按钮函数
	$scope.search = function () {
		$scope.paginationConf.currentPage = 1;
		$scope.paginationConf.itemsPerPage = 20;
		$scope.paginationConf.onChange();
	};
	//打开摸态框
	$scope.$parent.$parent.doOpenTaskPackageForEmpModal = function(){
		$scope.itemArr.currChoosedItemArr = $scope.$parent.$parent.tomPackage.packageId==undefined?[]:$scope.itemArr.choosedItemArr.concat(); 
		$scope.packageName = "";
		$scope.openTaskPackage2ModalDialog = true;
		$scope.search();
	}
	$scope.itemArr = {
		choosedItemArr: [],
		currChoosedItemArr: [],
		initCurrChoosedItemArrStatus: function () {
			$scope.$emit('isLoading', true);
			for (var i in $scope.itemArr.currChoosedItemArr) 
			{
				for(var j in $scope.page.data)
			 	{
			 		 if($scope.page.data[j].packageId==$scope.itemArr.currChoosedItemArr[i].packageId)
			 		 {
			 			 $scope.page.data[j].checked = true;
			 		 }
			 	}
			}
			$scope.$emit('isLoading', false);
		},
		//勾选 or 取消勾选操作
	    chooseItem : function (item) {
	         if (item.checked)
	         {
	             item.checked = undefined;
	             $scope.itemArr.currChoosedItemArr = [];
	         } 
	         else
	         {
	        	 for(var j in $scope.page.data)
     			 {
	        		 $scope.page.data[j].checked = undefined;
     			 }
	             item.checked = true;
	             $scope.itemArr.currChoosedItemArr = [];
	             $scope.itemArr.currChoosedItemArr.push(item);
	         }
	     },
	     //删除操作
	     deleteItem : function (item) {
	         item.checked = undefined;
	         for(var i in $scope.itemArr.choosedItemArr)
	         {
	         	if(item.packageId==$scope.itemArr.choosedItemArr[i].packageId)
	         	{
	         		$scope.itemArr.choosedItemArr.splice(i, 1);
	         	}
	         }
	     }
	};
	//确定
	$scope.doSure = function () {
	     $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
	     if($scope.itemArr.choosedItemArr.length==0){
	    	 $scope.$parent.$parent.tomPackage={};
	     }else{
	    	 $scope.$parent.$parent.tomPackage.packageName=$scope.itemArr.choosedItemArr[0].packageName;
	    	 $scope.$parent.$parent.tomPackage.packageId=$scope.itemArr.choosedItemArr[0].packageId;
	     }
	     $scope.openTaskPackage2ModalDialog = false;
	}
	//取消按钮
    $scope.doClose = function () {
        $scope.openTaskPackage2ModalDialog = false;
    } 
}])
//学习人员控制器
.controller('LearnerModalController',['$scope', '$http', 'FileUploader', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, FileUploader, $timeout, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/activity/findEmpPage?pageNum=";
    $scope.openModalDialog = false;
    $scope.tomPackage={};
    //组织架构树数据
    $scope.formData={};
    $scope.getTreeData = function () {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/orggroups/selectEmp").success(function (response) {
            $scope.empTreeData = response;
            $scope.getDefaultAutoSearchFatherNode($scope.empTreeData);
            $scope.$emit('isLoading', false);
            $scope.paginationConf.onChange();
        }).error(function(){
        	$scope.$emit('isLoading', false); 
        	dialogService.setContent("组织架构数据初始化异常").setShowDialog(true);
        });
    }
    $scope.getDefaultAutoSearchFatherNode = function(treeData){
    	for(var i in treeData){
    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
    		if(treeData[i].statuss=="2"&&treeData[i].fathercode==null){
    			$scope.currNode = treeData[i];
    			treeData[i].collapsed = true;
    			treeData[i].selected = 'selected';
    			break;
    		}
    	}
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 7,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
            if ($scope.currNode)
            {
            	$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&code="+$scope.currNode.code;
            }
            else
            {
            	$scope.paginationConf.totalItems = 0 ;
            	$scope.page = {};
            	return;
            }
            //条件查询
            if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
        		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
        			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
        	} 
            if ($scope.tomPackage.packageId != "" && $scope.tomPackage.packageId != undefined&& $scope.tomPackage.taskState != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&packageId=" + $scope.tomPackage.packageId + "&taskState=" + $scope.tomPackage.taskState;
            }
            if($scope.searchOptionCountry !="" && $scope.searchOptionCountry != undefined){
            	$scope.$httpPrams = $scope.$httpPrams + "&country=" +$scope.selectOptionCountry;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
            	$scope.chooseAllItem = false;
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //勾选后未确定  点击分页项  初始勾选状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询响应异常，请重新登录后重试").setShowDialog(true);
            });
        }
    };
    //查询按钮
    $scope.search = function () {
    	if(!$scope.currNode){
    		dialogService.setContent("请先选择公司或者部门后再查询").setShowDialog(true);
    	}else if($scope.selectedOption == ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
    		dialogService.setContent("请选择查询条件").setShowDialog(true);
    		return;
    	}
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    //查询条件下拉菜单
    $scope.searchOption = [
       {name: "---查询条件---", value: ""},
       {name: "员工编号", value: "empcode"},
       {name: "员工姓名", value: "name"},
       {name: "员工性别", value: "sex"},
       {name: "所在城市", value: "cityname"}
    ];
    //选择人员
    $scope.searchOptionCountry = [
                           {name: "---查询语言环境---", value: ""},
                           {name: "中文", value: "ch"},
                           {name: "英文", value: "en"}
                        ];
    //点击树节点      按部门id查询员工列表
	$scope.searchByCondition = function (currNode) {
	    $scope.currNode = currNode;
	    if($scope.currNode.statuss=="1"){
	    	//公司
	    	return ;
	    }
	    $scope.selectedOption = "";
	    $scope.optionValue = "";
	    $scope.selectOptionCountry = "";
	    $scope.chooseAllItem = false;
	    $scope.search();
	}
	/*///用于查询未通过选择的任务包的员工
    $scope.failed =function(){
    	$scope.tomPackage.taskState="N";
    	$scope.search();
    }
    //用于查询已通过选择的任务包的员工
    $scope.pass =function(){
    	$scope.tomPackage.taskState="Y";
    	$scope.search();
    }
    //重置查询条件
    $scope.reset = function(){
    	$scope.tomPackage = {};
    	$scope.search();
    }*/
	$scope.changeSelectedOption = function(){
		if($scope.currNode){
			if($scope.selectedOption==""){
				$scope.optionValue = "";
			}
    		$scope.search();
    	}
    } 
	$scope.changeSelectedTaskState = function(){
    	$scope.search();
    }
    //容器
    $scope.itemArr = {
    	isChooseAllItem: false,
        choosedItemArr: [],
        currChoosedItemArr: [],
        //初始化选中状态
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	for(var j in $scope.page.data)
            	{
            		 if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
            		 {
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.checked)
            {
            	//取消勾选
                item.checked = undefined;
                $scope.chooseAllItem = false;
                for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                	{
                		//从容器中删除取消勾选的项
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else
            {
            	//勾选
                item.checked = true;
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        //模态框中预删除  点击doSure删除生效
        tempDeleteItem : function (item) {
            item.checked = undefined;
            for(var i in $scope.itemArr.currChoosedItemArr)
            {
            	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
            	{
            		$scope.itemArr.currChoosedItemArr.splice(i, 1);
            	}
            }
            //立即从当前页中取消勾选预删除的项
            //其他页取消勾选预删除的项将在分页初始化函数中进行  initCurrChoosedItemArrStatus
            for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].code == item.code)
            	{
            		$scope.page.data[i].checked = undefined;
            		$scope.chooseAllItem = false;
            	}
            }
        },
        //全选
        chooseAllItem : function(){
        	//...
        	if($scope.chooseAllItem){
        		$scope.chooseAllItem = false;
        	}else{
        		$scope.chooseAllItem = true;
        	}
        	if($scope.chooseAllItem){
        		for(var i in $scope.page.data){
        			for(var j in $scope.itemArr.currChoosedItemArr)
                    {
        				var hasContained = false ;
                    	if($scope.page.data[i].code==$scope.itemArr.currChoosedItemArr[j].code)
                    	{
                    		hasContained = true ;
                    		break;
                    	}
                    }
        			if(!hasContained){
        				$scope.page.data[i].checked = true ;
        				$scope.itemArr.currChoosedItemArr.push($scope.page.data[i]);
        			}
        		} 
        	}else{
        		for(var i in $scope.page.data){
        			var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        			for(var j in currChoosedItemArrBak)
                    {
                    	if($scope.page.data[i].code==currChoosedItemArrBak[j].code)
                    	{
                    		$scope.page.data[i].checked = false ;
                    		$scope.itemArr.currChoosedItemArr.splice(j, 1); 
                    	}
                    }
        		} 
        	}
        }
    };
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.choosedItemArr = [];
	    	if(($scope.formData.deptManagers&&$scope.formData.deptManagers.length<1&&$scope.formData.emps&&$scope.formData.emps.length>0) || $scope.module){
	    		$scope.$parent.$parent.isDept = "N";
	    		for(var i in $scope.formData.emps)
	        	{
	        		$scope.itemArr.choosedItemArr.push({"code":$scope.formData.emps[i].code,"name":$scope.formData.emps[i].name,"cityname":$scope.formData.emps[i].cityname,"deptname":$scope.formData.emps[i].deptname});
	        	}
	    	}
        	$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
        	$scope.buildEmpIds();
        }, function(data) {}); 
    }
    //选择学习人员按钮,显示模态框
	$scope.$parent.$parent.doOpenLearnerModal = function () {
		if($scope.$parent.$parent.isDept=='N'){
			$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		}else{
			$scope.itemArr.currChoosedItemArr = [];
			$scope.itemArr.choosedItemArr = [];
		}
        $scope.chooseAllItem = false;
        $scope.selectedOption = ""; 
        $scope.optionValue = "";
        $scope.selectOptionCountry = "";
        $scope.currNode = undefined;
        $scope.paginationConf.totalItems = 0 ;
    	$scope.page = {};
    	$scope.tomPackage={};
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	$scope.$parent.$parent.deleteEmpItem = function(item){
		var choosedItemArrBak = $scope.itemArr.choosedItemArr.concat();
		for(var i in choosedItemArrBak){
			var currItem = choosedItemArrBak[i];
			if(item.code == currItem.code){
				$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(currItem),1);
			}
		}
		$scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr.concat();
		$scope.buildEmpIds();
	}
	//导入员工功能 s
	$scope.isMerge = "N";//默认
	$scope.toggleMergeType = function(mergeType){
		if(mergeType=="Y"){
			$scope.isMerge = 'Y';
		}else{
			$scope.isMerge = 'N';
		}
	}
	var empUploader = $scope.empUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/uploadEmp',
            autoUpload:true
        }
    );
	empUploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("导入文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
		}
	});
	//上传文件回调函数  回调成功设置表单数据 
	empUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件上传失败！").setShowDialog(true);
        }
        else
        {//回调成功
        	console.info('uploadEmp-response.url', response.url); 
        	$scope.getUploadEmpInfo(response.url); 
        }
    }; 
    $scope.getUploadEmpInfo = function(fileUrl){
    	$scope.$emit('isLoading', true);
    	$http.get("/enterpriseuniversity/services/activity/importEmps?filePath="+fileUrl).success(function (response) {
    		$scope.uploadEmps = response.emps!=undefined?response.emps:[];
    		$scope.uploadFailedEmps = response.errorCodes!=undefined?response.errorCodes:[];
    		//考虑之前勾选的项？
    		if($scope.itemArr.currChoosedItemArr.length>0&&$scope.isMerge=="Y"){
    			var currItem = "";
        		var uploadItem = "";
        		var isContainedMark = false;
        		for(var i in $scope.uploadEmps){
        			uploadItem = $scope.uploadEmps[i];
        			isContainedMark = false;
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				currItem = $scope.itemArr.currChoosedItemArr[j];
        				if(currItem.code==uploadItem.code){
        					isContainedMark = true;
            			}
        			}
        			if(!isContainedMark){
        				$scope.itemArr.currChoosedItemArr.push(uploadItem);
        			}
        		}
    		}else{
    			$scope.itemArr.currChoosedItemArr = $scope.uploadEmps.concat();
    		}
    		
    		if($scope.uploadFailedEmps.length>0){
    			var failedEmpCodes = $scope.uploadFailedEmps.join("、");
    			dialogService.setContent("员工数据导入完毕，仍有以下员工编号未成功导入:" + failedEmpCodes).setShowDialog(true);
    		}else{
    			dialogService.setContent("员工数据导入成功！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
    		}
    		
    		//初始化导入的数据的勾选状态
    		if($scope.page.data){
    			for (var j in $scope.page.data) 
                {
    				$scope.page.data[j].checked = false;
                	for(var i in $scope.itemArr.currChoosedItemArr)
                	{
                		if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
                		{
                			$scope.page.data[j].checked = true;
                		}
                	}
                }
    		}
    		$scope.$emit('isLoading', false);
        })
        .error(function(response){
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("查询导入员工数据失败！").setShowDialog(true);
        });
    }
	$scope.buildEmpIds = function(){
		$scope.$parent.$parent.formData.empIds = [];
		if($scope.itemArr.choosedItemArr.length<1){
			$scope.$parent.$parent.formData.empIds = null;
			return;
		}
		for(var i in $scope.itemArr.choosedItemArr)
		{
			$scope.$parent.$parent.formData.empIds.push($scope.itemArr.choosedItemArr[i].code);
		}
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.$parent.$parent.isDept='N';
        $scope.$parent.$parent.choosedEmpsArr = $scope.itemArr.choosedItemArr;//推送员工
        //清空推送部门负责人容器
        $scope.$parent.$parent.choosedItemArr = [];
        $scope.$parent.$parent.formData.codes = $scope.$parent.$parent.formData.codes==undefined?[]:[];
        $scope.$parent.$parent.formData.deptManagerIds = $scope.$parent.$parent.formData.deptManagerIds==undefined?[]:[];
        $scope.$parent.$parent.formData.deptManagers = $scope.$parent.$parent.formData.deptManagers==undefined?[]:[];
        $scope.$parent.$parent.formData.emps = $scope.$parent.$parent.formData.emps==undefined?[]:[];
        $scope.buildEmpIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//部门负责人控制器
.controller('ManagerModalController',['$scope', '$http', 'FileUploader', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, FileUploader, $timeout, dialogService, dialogStatus) {
    $scope.url = "/enterpriseuniversity/services/activity/findEmpPage?pageNum=";
    $scope.openModalDialog = false;
    $scope.tomPackage={};
    //组织架构树数据
    $scope.formData={};
    $scope.getTreeData = function () {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/orggroups/selectEmp").success(function (response) {
            $scope.empTreeData = response;
            $scope.getDefaultAutoSearchFatherNode($scope.empTreeData);
            $scope.$emit('isLoading', false);
            $scope.paginationConf.onChange();
        }).error(function(){
        	$scope.$emit('isLoading', false);
        });
    }
    $scope.getDefaultAutoSearchFatherNode = function(treeData){
    	for(var i in treeData){
    		//选择人员摸态框   组织架构父节点展开 和默认查询父节点下的全部人员数据
    		if(treeData[i].statuss=="2"&&treeData[i].fathercode==null){
    			$scope.currNode = treeData[i];
    			treeData[i].collapsed = true;
    			treeData[i].selected = 'selected';
    			break;
    		}
    	}
    }
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 5,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.chooseAllItem = false;
            if ($scope.currNode)
            {
            	$scope.$httpPrams = $scope.$httpPrams+"&statuss="+$scope.currNode.statuss+"&code="+$scope.currNode.code;
            }
            else
            {
            	$scope.paginationConf.totalItems = 0 ;
            	$scope.page = {};
            	return;
            }
            //条件查询
            if($scope.selectedOption != ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
        		$scope.$httpPrams = $scope.$httpPrams + "&" + $scope.selectedOption + "=" 
        			+ $scope.optionValue.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
        	} 
            if ($scope.tomPackage.packageId != "" && $scope.tomPackage.packageId != undefined && $scope.tomPackage.taskState != undefined && $scope.tomPackage.taskState != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&packageId=" + $scope.tomPackage.packageId + "&taskState=" + $scope.tomPackage.taskState;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage 
            	+ "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) 
            	+ $scope.$httpPrams;
            $scope.$emit('isLoading', true);
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
            	$scope.chooseAllItem = false;
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
                //勾选后未确定  点击分页项  初始勾选状态
                if ($scope.itemArr.initCurrChoosedItemArrStatus) 
                {
                    $scope.itemArr.initCurrChoosedItemArrStatus();
                }
            }).error(function(){
            	$scope.$emit('isLoading', false);
            	dialogService.setContent("数据查询响应异常，请重新登录后重试").setShowDialog(true);
            });
        }
    };
    $scope.search = function () {
    	if(!$scope.currNode){
    		dialogService.setContent("请先选择公司或者部门后再查询").setShowDialog(true);
    	}else if($scope.selectedOption == ""&&$scope.optionValue != undefined && $scope.optionValue != ""){
    		dialogService.setContent("请选择查询条件").setShowDialog(true);
    		return;
    	}
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20 ;
    	$scope.paginationConf.onChange();
    };
    $scope.searchOption = [
           {name: "---查询条件---", value: ""},
           {name: "员工编号", value: "empcode"},
           {name: "员工姓名", value: "name"},
           {name: "员工性别", value: "sex"},
           {name: "所在城市", value: "cityname"}
    ];
    //按部门id查询员工列表
    $scope.searchByCondition = function (currNode) {
        $scope.currNode = currNode;
        $scope.chooseAllItem = false;
        $scope.selectedOption = "";
        $scope.optionValue = "";
        $scope.search();
    }
    //改变查询条件
    $scope.changeSelectedOption = function(){
    	if($scope.currNode){
    		if($scope.selectedOption==""){
				$scope.optionValue = "";
			}
    		$scope.search();
    	}
    }
    $scope.changeSelectedTaskState = function(){
    	$scope.search();
    }
    $scope.itemArr = {
        choosedItemArr: [],
        currChoosedItemArr: [],
        choosedDeptHeaderItemArr: [],
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	for(var j in $scope.page.data)
            	{
            		 if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
            		 {
            			 $scope.page.data[j].checked = true;
            		 }
            	}
            }
            $scope.$emit('isLoading', false);
        },
        //添加部门负责人
        putDeptHeaderItem : function(item){//N
        	var hasContained = false;
        	for(var i in $scope.itemArr.currChoosedItemArr){
        		var currItem = $scope.itemArr.currChoosedItemArr[i];
         		if(item.deptpsncode == currItem.code){
         			hasContained = true;
         			break;
        		}
        	}
        	var hasDeptContained = false;
        	for(var i in $scope.itemArr.choosedDeptHeaderItemArr){
        		var currItem = $scope.itemArr.choosedDeptHeaderItemArr[i];
         		if(item.deptpsncode == currItem.code){
         			hasDeptContained = true;
         			break;
        		}
        	}
        	if(item.deptpsncode==item.code){
            	//部门负责人 to choosedDeptHeaderItemArr
        		if(!hasDeptContained){
        			$scope.itemArr.choosedDeptHeaderItemArr.push(item);
        		}
            	if(!hasContained){
            		$scope.itemArr.currChoosedItemArr.push(item);
            	}
            }else{
            	//非部门负责人 to choosedDeptHeaderItemArr
            	if(!hasDeptContained){
            		$scope.itemArr.choosedDeptHeaderItemArr.push({code:item.deptpsncode,name:item.deptpsnname,deptcode:item.deptcode,deptname:item.deptname,cityname:item.cityname,deptpsncode:item.deptpsncode});
    			}
            	if(!hasContained){
            		$scope.itemArr.currChoosedItemArr.push({code:item.deptpsncode,name:item.deptpsnname,deptcode:item.deptcode,deptname:item.deptname,cityname:item.cityname,deptpsncode:item.deptpsncode});
            	}
            }
        	//放入部门负责人currChoosedItemArr
        	if(!hasContained){
        		//立即选中当前页的部门负责人数据
        		for(var i in $scope.page.data)
                {
                	if($scope.page.data[i].code == item.deptpsncode)
                	{
                		$scope.page.data[i].checked = true;
                	}
                }
			}
        },
        buildDeptHeaderArr:function(){//Y
        	$scope.itemArr.choosedDeptHeaderItemArr = [];
        	for(var i in $scope.itemArr.currChoosedItemArr)
            {
        		var currChoosedItem = $scope.itemArr.currChoosedItemArr[i];
        		var hasDeptContained = false;
            	for(var j in $scope.itemArr.choosedDeptHeaderItemArr){
            		var choosedDeptHeaderItem = $scope.itemArr.choosedDeptHeaderItemArr[j];
             		if(currChoosedItem.deptpsncode == choosedDeptHeaderItem.code){
             			hasDeptContained = true;
             			break;
            		}
            	}
            	if(!hasDeptContained){
            		if(currChoosedItem.deptpsncode == currChoosedItem.code){//部门负责人
            			$scope.itemArr.choosedDeptHeaderItemArr.push(currChoosedItem);
            		}else{//普通员工  手动生成一个部门负责人
            			$scope.itemArr.choosedDeptHeaderItemArr.push(
        					{
        						code:currChoosedItem.deptpsncode,
        						name:currChoosedItem.deptpsnname,
	    						deptcode:currChoosedItem.deptcode,
	    						deptname:currChoosedItem.deptname,
	    						cityname:currChoosedItem.cityname,
	    						deptpsncode:currChoosedItem.deptpsncode});
            		}
            	}
            }
        },
        //删除部门负责人
        deleteDeptHeaderItem:function(item){//N
        	var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        	for(var j in currChoosedItemArrBak){
        		var currItem = currChoosedItemArrBak[j];
        		if(item.code==currItem.deptpsncode)
            	{
        			$scope.itemArr.currChoosedItemArr.splice($scope.itemArr.currChoosedItemArr.indexOf(currItem), 1);
            	}
        	}
        	for(var k in $scope.itemArr.choosedDeptHeaderItemArr){
        		var currItem = $scope.itemArr.choosedDeptHeaderItemArr[k];
         		if(item.code == currItem.code){
         			$scope.itemArr.choosedDeptHeaderItemArr.splice(k, 1); 
         			break;
        		}
        	}
        	for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].deptpsncode == item.code)
            	{
            		$scope.chooseAllItem = false;
            		$scope.page.data[i].checked = undefined;
            	}
            }
        },
        //勾选 or 取消勾选操作
        chooseItem : function (item) {//Y
            if (item.checked)
            {//取消选中
            	/*
                if(item.code!=item.deptpsncode){
                	for(var i in $scope.itemArr.currChoosedItemArr)
                    {
                    	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                    	{
                    		//取消选中
                    		$scope.chooseAllItem = false;
                            item.checked = undefined;
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    		break;
                    	}
                    }
                }else{
                	//删除部门负责人
                    $scope.itemArr.deleteDeptHeaderItem(item);
                }
                */
            	for(var i in $scope.itemArr.currChoosedItemArr)
                {
                	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
                	{
                		//取消选中全选
                		$scope.chooseAllItem = false;
                        item.checked = undefined;
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                		break;
                	}
                }
            	//重新生成部门负责人数组
            	$scope.itemArr.buildDeptHeaderArr();
            } 
            else
            {	//选中
            	item.checked = true;
            	/*
                if(item.code!=item.deptpsncode){
                	$scope.itemArr.currChoosedItemArr.push(item);
                }
                */
            	$scope.itemArr.currChoosedItemArr.push(item);
            	//添加部门负责人
                //$scope.itemArr.putDeptHeaderItem(item);
            	$scope.itemArr.buildDeptHeaderArr();
            }
        },
        //模态框中预删除  点击doSure删除生效
        tempDeleteItem : function (item) {//Y
        	item.checked = undefined;
        	$scope.chooseAllItem = false;
        	for(var i in $scope.itemArr.currChoosedItemArr)
            {
            	if(item.code==$scope.itemArr.currChoosedItemArr[i].code)
            	{
            		$scope.itemArr.currChoosedItemArr.splice(i, 1);
            		break;
            	}
            }
            for(var i in $scope.page.data)
            {
            	if($scope.page.data[i].code == item.code)
            	{
            		$scope.page.data[i].checked = undefined;
            		break;
            	}
            }
            $scope.itemArr.buildDeptHeaderArr();
        },
        //全选
        chooseAllItem : function(){//Y
        	if($scope.chooseAllItem){
        		$scope.chooseAllItem = false;
        	}else{
        		$scope.chooseAllItem = true;
        	}
        	if($scope.chooseAllItem){
        		for(var i in $scope.page.data){
        			var item = $scope.page.data[i];
        			item.checked = true ;
    				for(var j in $scope.itemArr.currChoosedItemArr)
                    {
        				var hasContained = false ;
                    	if(item.code==$scope.itemArr.currChoosedItemArr[j].code)
                    	{
                    		hasContained = true ;
                    		break;
                    	}
                    }
        			if(!hasContained){
        				$scope.itemArr.currChoosedItemArr.push(item);
        			}
    				//添加部门负责人
    				//$scope.itemArr.putDeptHeaderItem(item);
        		}
        		$scope.itemArr.buildDeptHeaderArr();
        	}else{
        		for(var i in $scope.page.data){
        			var item = $scope.page.data[i];
        			item.checked = false ;
        			var currChoosedItemArrBak = $scope.itemArr.currChoosedItemArr.concat();
        			for(var j in currChoosedItemArrBak)
                    {
                    	if(item.code==currChoosedItemArrBak[j].code)
                    	{
                    		//if(item.code!=item.deptpsncode){
                    		$scope.itemArr.currChoosedItemArr.splice($scope.itemArr.currChoosedItemArr.indexOf(currChoosedItemArrBak[j]), 1); 
                    		//}
                			//删除部门负责人
                    		//$scope.itemArr.deleteDeptHeaderItem(currChoosedItemArrBak[j]);
                    	}
                    }
        		} 
        		$scope.itemArr.buildDeptHeaderArr();
        	}
        }
    };
    //接收编辑页面传值
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
	    	$scope.formData = data; 
	    	$scope.itemArr.choosedItemArr = [];
	    	if($scope.formData.deptManagers&&$scope.formData.deptManagers.length>0){
	    		$scope.$parent.$parent.isDept = "Y";
	    		$scope.itemArr.choosedDeptHeaderItemArr = $scope.formData.deptManagers;
	    		$scope.itemArr.choosedItemArr = $scope.formData.emps;
	    		$scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr.concat();
	    	} 
	    	$scope.buildParentFormData();
        }, function(data) {}); 
    }
    //推送部门负责人按钮,显示模态框
	$scope.$parent.$parent.doOpenManagerModal = function () {
		if($scope.$parent.$parent.isDept=='Y'){
			$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		}else{
			$scope.itemArr.choosedDeptHeaderItemArr = [];
			$scope.itemArr.currChoosedItemArr = [];
			$scope.itemArr.choosedItemArr = [];
		}
        $scope.chooseAllItem = false;
        $scope.selectedOption = ""; 
        $scope.optionValue = "";
        $scope.tomPackage = {};
        $scope.currNode = undefined;
        $scope.paginationConf.totalItems = 0 ;
    	$scope.page = {};
        $scope.openModalDialog = true;
        dialogStatus.setHasShowedDialog(true);
        $scope.getTreeData();
    }
	//导入员工功能 s
	$scope.isMerge = "N";//默认
	$scope.toggleMergeType = function(mergeType){
		if(mergeType=="Y"){
			$scope.isMerge = 'Y';
		}else{
			$scope.isMerge = 'N';
		}
	}
	var empUploader = $scope.empUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/uploadEmp',
            autoUpload:true
        }
    );
	empUploader.filters.push({
		name : 'customFilter',
		fn : function(item, options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			if ('|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) == -1) {
				dialogService.setContent("导入文件格式不正确！").setShowDialog(true)
			}
			return '|application|vnd.openxmlformats-officedocument.spreadsheetml.sheet|vnd.ms-excel|'.indexOf(type) !== -1;
		}
	});
	//上传文件回调函数  回调成功设置表单数据 
	empUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件上传失败！").setShowDialog(true);
        }
        else
        {//回调成功
        	console.info('uploadEmp-response.url', response.url); 
        	$scope.getUploadEmpInfo(response.url); 
        }
    }; 
    $scope.getUploadEmpInfo = function(fileUrl){
    	$scope.$emit('isLoading', true);
    	$http.get("/enterpriseuniversity/services/activity/importEmps?filePath="+fileUrl).success(function (response) {
    		$scope.uploadEmps = response.emps!=undefined?response.emps:[];
    		$scope.uploadFailedEmps = response.errorCodes!=undefined?response.errorCodes:[];
    		//考虑之前勾选的项？
    		if($scope.itemArr.currChoosedItemArr.length>0&&$scope.isMerge=="Y"){
    			var currItem = "";
        		var uploadItem = "";
        		var isContainedMark = false;
        		for(var i in $scope.uploadEmps){
        			uploadItem = $scope.uploadEmps[i];
        			isContainedMark = false;
        			for(var j in $scope.itemArr.currChoosedItemArr){
        				currItem = $scope.itemArr.currChoosedItemArr[j];
        				if(currItem.code==uploadItem.code){
        					isContainedMark = true;
            			}
        			}
        			if(!isContainedMark){
        				$scope.itemArr.currChoosedItemArr.push(uploadItem);
        			}
        		}
    		}else{
    			$scope.itemArr.currChoosedItemArr = $scope.uploadEmps.concat();
    		}
    		
    		$scope.itemArr.buildDeptHeaderArr();//生成部门负责人数组
    		
    		if($scope.uploadFailedEmps.length>0){
    			var failedEmpCodes = $scope.uploadFailedEmps.join("、");
    			dialogService.setContent("员工数据导入完毕，仍有以下员工编号未成功导入:" + failedEmpCodes).setShowDialog(true);
    		}else{
    			dialogService.setContent("员工数据导入成功！").setShowSureButton(false).setShowDialog(true);
        		$timeout(function(){
    				dialogService.sureButten_click(); 
    			},2000);
    		}
    		
    		//初始化导入的数据的勾选状态
    		if($scope.page.data){
    			for (var j in $scope.page.data) 
                {
    				$scope.page.data[j].checked = false;
                	for(var i in $scope.itemArr.currChoosedItemArr)
                	{
                		if($scope.page.data[j].code==$scope.itemArr.currChoosedItemArr[i].code)
                		{
                			$scope.page.data[j].checked = true;
                		}
                	}
                }
    		}
    		$scope.$emit('isLoading', false);
        })
        .error(function(response){
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("查询导入员工数据失败！").setShowDialog(true);
        });
    }
    //导入员工功能 e
	$scope.$parent.$parent.deleteDeptHeaderItem = function(item){
		var choosedItemArrBak = $scope.itemArr.choosedItemArr.concat();
		for(var j in choosedItemArrBak)
        {
			var currItem = choosedItemArrBak[j];
        	if(item.code==currItem.deptpsncode)
        	{
        		$scope.itemArr.choosedItemArr.splice($scope.itemArr.choosedItemArr.indexOf(currItem), 1);
        	}
        }
		var choosedDeptHeaderItemArrBak = $scope.itemArr.choosedDeptHeaderItemArr.concat();
		for(var i in choosedDeptHeaderItemArrBak){
			var currItem = choosedDeptHeaderItemArrBak[i];
			if(item.code == currItem.code){
				$scope.itemArr.choosedDeptHeaderItemArr.splice($scope.itemArr.choosedDeptHeaderItemArr.indexOf(currItem), 1);
			}
		}
		$scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr.concat();
		$scope.buildParentFormData();
	}
	$scope.buildParentFormData = function(){
		$scope.$parent.$parent.formData.codes = [];
		$scope.$parent.$parent.formData.deptManagerIds = [];
		for(var i in $scope.itemArr.choosedDeptHeaderItemArr){
			var deptHeaderItem = $scope.itemArr.choosedDeptHeaderItemArr[i]; 
			var empStr = "";
			for(var j in $scope.itemArr.choosedItemArr){
				var empItem = $scope.itemArr.choosedItemArr[j];
				/*if(empItem.code!=empItem.deptpsncode&&empItem.deptpsncode==deptHeaderItem.code){
					empStr = empStr + ","+ empItem.code;
				}*/
				if(empItem.deptpsncode==deptHeaderItem.code){
					empStr = empStr + ","+ empItem.code;
				}
			}
			$scope.$parent.$parent.formData.deptManagerIds.push(deptHeaderItem.code);
			$scope.$parent.$parent.formData.codes.push(empStr);
		}
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.$parent.$parent.choosedItemArr = $scope.itemArr.choosedDeptHeaderItemArr;//推送部门负责人
        $scope.buildParentFormData();
        $scope.$parent.$parent.isDept='Y'; 
        $scope.$parent.$parent.choosedEmpsArr = [];//清空员工容器
        $scope.$parent.$parent.formData.empIds = $scope.$parent.$parent.formData.empIds !=undefined ? []:[]; 
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
//选择前置任务模态框
.controller('PreTaskModelController', ['$scope', '$http', 'dialogService', 'dialogStatus', function ($scope, $http, dialogService, dialogStatus) {
	$scope.openModalDialog = false;
	//$scope.$parent.$parent.formData.preTaskInfo=[];
	$scope.$parent.$parent.choosePretask = function (item,index) {
		$scope.currItem = item;
		$scope.openModalDialog = true;
		dialogStatus.setHasShowedDialog(true);
		$scope.itemArr.choosedItemArr = item.choosedItemArr != undefined ? item.choosedItemArr.concat():[];
		console.log(item.choosedItemArr, "item.choosedItemArr");
		$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
		$scope.page = { data : []};
		var taskCoursesOrExamPapers = $scope.taskPackage[0].taskCoursesOrExamPapers.concat();
		for(var j=0;j< taskCoursesOrExamPapers.length; j++){
			if(j<index){
				$scope.page.data.push(taskCoursesOrExamPapers[j]); 
			}
		}
		$scope.itemArr.initCurrChoosedItemArrStatus();
  	}
	$scope.itemArr = {
        	isChooseAllItem: false,
        	oldChooseItemArr: [],
            choosedItemArr: [],
            currChoosedItemArr: [],
            //选中choosedItemArr中的项           
            initCurrChoosedItemArrStatus: function () {
            	var itemArr = [];
            	var needRebuild = false;
            	for(var j in $scope.page.data){
            		$scope.page.data[j].checked = false;
            		for (var i in $scope.itemArr.currChoosedItemArr) {
            			//alert($scope.itemArr.currChoosedItemArr[i].examPaperId);
		        		 /*if(($scope.page.data[j].courseId&&$scope.page.data[j].courseId==$scope.itemArr.currChoosedItemArr[i].courseId)
		        				 ||($scope.page.data[j].examPaperId&&$scope.page.data[j].examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId)){
		        			 $scope.page.data[j].checked = true;
		        		 }*/
            			//alert($scope.page.data[j].sort+"=====-"+$scope.itemArr.currChoosedItemArr[i]);
            			//console.log($scope.page.data[j].sort, $scope.itemArr.currChoosedItemArr[i].sort);
            			if($scope.page.data[j].sort == $scope.itemArr.currChoosedItemArr[i].sort){
            				//编辑页面数据
            				/*if($scope.itemArr.currChoosedItemArr[i].sort==undefined||$scope.itemArr.currChoosedItemArr[i].preTaskName==undefined){
            					itemArr.push($scope.page.data[j]);
            					needRebuild = true;
            				}*/
            				itemArr.push($scope.page.data[j]);
            				/*if($scope.itemArr.currChoosedItemArr[i].preTaskName == undefined || $scope.itemArr.currChoosedItemArr[i].preTaskName == ""){
            					needRebuild = true;
            				}*/
            				$scope.page.data[j].checked = true;
            			}
            			if($scope.itemArr.currChoosedItemArr[i].preTaskName == undefined || $scope.itemArr.currChoosedItemArr[i].preTaskName == ""){
        					needRebuild = true;
        				}
                	}
            		
                }
            	//编辑页面数据
        		if(needRebuild){
        			$scope.itemArr.currChoosedItemArr = itemArr;
				}
            },
            //勾选 or 取消勾选操作
            chooseItem : function (item,index) {
                if (item.checked) 
                {
                	item.checked = undefined;
                    for(var i in $scope.itemArr.currChoosedItemArr)
                    {
                    	if(item.courseId&&item.courseId==$scope.itemArr.currChoosedItemArr[i].courseId||item.examPaperId&&item.examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId){
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    	}
                    	/*else if(item.examPaperId&&item.examPaperId==$scope.itemArr.currChoosedItemArr[i].examPaperId){
                    		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                    	}*/
                    }
                } 
                else 
                {
                    item.checked = true;
                    //item.preTaskName="任务"+(index+1);
                    //item.sort = 
                    $scope.itemArr.currChoosedItemArr.push(item);
                }
            } ,
            //全选
            chooseAllItem : function(){
            	//...
            	if(this.isChooseAllItem){
            		this.isChooseAllItem = false;
            		
            		
            	}else{
            		this.isChooseAllItem = false;
            	}
            }           
    };
	var buildPreTaskInfo=function(){
		var currItemPreTaskNameArry = [];
		var currItemPreTaskIdArry = [];
		for(var i=0;i<$scope.itemArr.choosedItemArr.length;i++){
			currItemPreTaskNameArry.push($scope.itemArr.choosedItemArr[i].preTaskName);
			/*if($scope.itemArr.choosedItemArr[i].courseId){
				currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].courseId);
			}else if($scope.itemArr.choosedItemArr[i].examPaperId){
				currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].examPaperId);
			}*/
			currItemPreTaskIdArry.push($scope.itemArr.choosedItemArr[i].sort);
		}
		currItemPreTaskNameArry.sort(function(a,b){
			return Number(a.replace("任务",""))-Number(b.replace("任务",""));
		});
		if( $scope.itemArr.choosedItemArr.length>0){
        	$scope.currItem.startTime=$scope.formData.activityStartTime ? $scope.formData.activityStartTime: "";
        	$scope.currItem.endTime= $scope.formData.activityEndTime ? $scope.formData.activityEndTime:"";
        }
		$scope.currItem.preTaskNames = currItemPreTaskNameArry.join(","); 
		$scope.currItem.preTaskIds = currItemPreTaskIdArry.join(","); 
		//20170316 add s 设置补考时间
		if($scope.formData.activityStartTime != undefined && $scope.formData.activityEndTime != undefined && $scope.formData.activityStartTime != "" && $scope.formData.activityEndTime != ""){
			if($scope.currItem.retakingExamCount && $scope.currItem.retakingInfo){
				//console.log("???");
				for(var i in $scope.currItem.retakingInfo){
					$scope.currItem.retakingInfo[i].startTime = $scope.formData.activityStartTime;
					$scope.currItem.retakingInfo[i].endTime = $scope.formData.activityEndTime;
				}
			}
		}
		//20170316 add e
	}
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.currItem.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        buildPreTaskInfo();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}])
.directive('requiredLanguage', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					return value;
				}, function(){
					validity = !value ? false : true;
					ngModel.$setValidity("requiredLanguage", validity);
				});
				return validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])//添加项目名称分类选择上级项目名称分类模态框函数
.controller('RadioProjectClassifyModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
    //项目名称分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/projectClassify/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
            $scope.projectClassifyData = data.children;
            if($scope.isBuildOldChoosedItem&&$scope.itemArr.initCurrChoosedItemArrStatus){
            	$scope.itemArr.initCurrChoosedItemArrStatus();
            }
            if($scope.itemArr.buildOldChoosedItemArr&&!$scope.isBuildOldChoosedItem){
            	$scope.itemArr.buildOldChoosedItemArr();
            }
        }).error(function () {
        	$scope.$emit('isLoading', false);
        	dialogService.setContent("项目名称分类树数据查询异常").setShowDialog(true);
        });
    }
    if($scope.$parent.$parent.deferred)
    {
    	$scope.$parent.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.itemArr.oldChooseItemArr = [];
    		if($scope.formData.parentClassifyId){
    			$scope.itemArr.oldChooseItemArr.push($scope.formData.parentClassifyId);
    		}
        	$scope.searchTreeData();
        }, function(data) {
        	dialogService.setContent("页面数据初始化异常").setShowDialog(true);
            return;
        }); 
    }
    $scope.isBuildOldChoosedItem = false;
    $scope.openModalDialog = false;
    $scope.itemArr = {
		oldChooseItemArr:[],
        choosedItemArr: [],
        currChoosedItemArr: [],
        //勾选 or 取消勾选操作
        chooseItem : function (item) {
            if (item.selected) 
            {
            	$scope.itemArr.currChoosedItemArr = [];
            } 
            else
            {
            	$scope.itemArr.currChoosedItemArr = [];
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.projectClassifyData,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);
            }
            $scope.$emit('isLoading', false);
        },
        buildOldChoosedItemArr : function(){
        	$scope.$emit('isLoading', true);
        	if ($scope.projectClassifyData) 
        	{
	            for(var i in $scope.itemArr.oldChooseItemArr)
	            {
	            	var projectId = $scope.itemArr.oldChooseItemArr[i];
	            	$scope.itemArr.nodeLoop($scope.projectClassifyData,projectId); 
	            }
            }
        	$scope.$emit('isLoading', false);
        },
        nodeLoop:function(LoopNode,projectId){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==projectId)
        		{
        			if($scope.itemArr.choosedItemArr.length<1)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    					continue;
    				}
    				var isContained = false;
    				for(var j in $scope.itemArr.choosedItemArr)
    				{
    					if($scope.itemArr.choosedItemArr[j].projectId==LoopNode[i].code)
    					{
    						isContained = true;
    						break;
        				} 
    				}
    				if(!isContained)
    				{
    					$scope.itemArr.choosedItemArr.push(LoopNode[i]);
    				}
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoop(LoopNode[i].children,projectId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,projectId,loopType,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==projectId)
        		{
        			LoopNode[i].selected = true; 
        			parentNodeArr.push(LoopNode[i].fathercode);
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.projectClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,projectId,'C',parentNodeArr);
            	}
        	}
        	if(loopType=='P'){ 
        		console.log('parentNodeArr', parentNodeArr);
        		for(var j in parentNodeArr){ 
        			if(parentNodeArr[j]!='0'){ 
        				$scope.itemArr.nodeLoopForCollapsed(LoopNode,parentNodeArr[j]);  
        			} 
        		} 
        	}
        } ,
        //初始化化树的折叠状态 //>
        nodeLoopForCollapsed:function(LoopNode,fatherCode){ 
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fatherCode)
        		{
        			LoopNode[i].collapsed = true;
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForCollapsed(LoopNode[i].children,fatherCode);
            	}
        	}
        },
        //获取selected节点的所有父节点  //输出：parentNodeArr
        nodeLoopForParentNodeFatherCode :function(LoopNode,fathercode,parentNodeArr){
        	for(var i in LoopNode)
        	{
        		if(LoopNode[i].code==fathercode)
        		{
        			parentNodeArr.push(LoopNode[i].fathercode); 
        			$scope.itemArr.nodeLoopForParentNodeFatherCode($scope.projectClassifyData,LoopNode[i].fathercode,parentNodeArr);
        			break;
        		}
        		if(LoopNode[i].children){
            		$scope.itemArr.nodeLoopForParentNodeFatherCode(LoopNode[i].children,fathercode,parentNodeArr);//>C 子节点 LoopNode[i].children
            	}
        	}
        }
    };
    //生成projectIds数组
    $scope.buildParentClassifyNameAndId = function(){
    	if($scope.itemArr.choosedItemArr.length>0){
    		$scope.$parent.$parent.formData.parentClassifyId= $scope.itemArr.choosedItemArr[0].code;
    		$scope.$parent.$parent.formData.parentProjectClassifyName= $scope.itemArr.choosedItemArr[0].name;
    	}
    	else
    	{
    		$scope.$parent.$parent.formData.parentClassifyId= 0;
    		$scope.$parent.$parent.formData.parentProjectClassifyName= "";
    	}
    }
    $scope.$parent.$parent.doOpenRadioProjectClassifyModal = function () {
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.isBuildOldChoosedItem = true;
    	$scope.itemArr.currChoosedItemArr = $scope.itemArr.choosedItemArr.concat();
        //重新查询模态框中的数据
        $scope.searchTreeData(); 
    }
    //确定
    $scope.doSure = function () {
        $scope.itemArr.choosedItemArr = $scope.itemArr.currChoosedItemArr.concat();
        $scope.buildParentClassifyNameAndId();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
}]);

