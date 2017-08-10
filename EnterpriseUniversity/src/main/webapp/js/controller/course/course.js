/**
 * Created by Mr.xiang on 2016/3/26.
 */

angular.module('ele.course', ['ele.orgGroup','ele.lecture','ele.admin','ele.courseClassify','ele.gradeDimension', 'summernote'])
//课程列表
.controller('CourseController', ['$scope', '$http', '$timeout','dialogService', function ($scope, $http , $timeout, dialogService) {
    //用于显示面包屑状态
    //$scope.$parent.cm = {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表'};
    $scope.$emit('cm', {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表'});
    //http请求url
    $scope.url = "/enterpriseuniversity/services/course/findPage?pageNum=";
    $scope.online = [
        {name: "线上课程", value: "Y"},
        {name: "线下课程", value: "N"}
    ];
     
    $scope.status = [
        {name: "已上架", value: "Y"},
        {name: "已下架", value: "N"}
    ];
    $http.get("/enterpriseuniversity/services/courseClassify/findHighest").success(function (response) {
        $scope.type = response;
    }).error(function () {
		dialogService.setContent("课程分类下拉菜单初始化错误").setShowDialog(true);
    });
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
            if ($scope.courseName != undefined && $scope.courseName != "") 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseName=" 
            		+ $scope.courseName.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            if ($scope.selectedOnline != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseOnline=" + $scope.selectedOnline;
            }
            if ($scope.selectedStatus != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&status=" + $scope.selectedStatus;
            }
            if ($scope.selectedType != undefined) 
            {
            	$scope.$httpPrams = $scope.$httpPrams + "&courseType=" + $scope.selectedType;
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                var item;
                for(var i in $scope.page.data){
                	item = $scope.page.data[i];
                	if(item && item.courseOnline!='Y'){
                		$scope.page.data[i].signInTwoDimensionCode = item.signInTwoDimensionCode ? item.signInTwoDimensionCode.replace(/\%/g,"%25").replace(/\#/g,"%23")
	            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
	            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D") : "";
                		$scope.page.data[i].gradeTwoDimensionCode = item.gradeTwoDimensionCode ? item.gradeTwoDimensionCode.replace(/\%/g,"%25").replace(/\#/g,"%23")
	            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
	            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D") : "";
                	}
                }
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
    //查询下拉菜单改变后查询列表数据
    $scope.changeSeclectOption = function(){
    	$scope.search();
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(course){
    	$scope.currHighLightRow.courseId = course.courseId; 
    }
    //上/下架按钮
    $scope.doUpdate = function (course) {
    	/*
    	 * setContent 提示内容   必须设置  
    	 * setShowDialog(true) 设置弹出对话框      必须设置为true 
    	 * setHasNextProcess(true)   可选            默认为false  默认没有需要执行的后续代码
    	 * doNextProcess 指定点击确认按钮后需要执行的后续函数                     当setHasNextProcess(true)设置为true时必须设置
    	 * setShowCancelButton(true) 设置是否显示取消按钮    可选      默认为不显示 
    	 * */
    	dialogService.setContent(course.status == "Y"?"确定下架课程?":"确定上架课程?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
			 $http({
			        url: "/enterpriseuniversity/services/course/updateStatus?courseId=" + course.courseId + "&status=" + (course.status == "Y" ? "N" : "Y"),
			        method: "PUT"
			    }).success(function (response) {
			    	//延迟弹框
			    	$timeout(function(){
			    		if(response.result=="inActivity"){
			    			//dialogService.setContent("不能下架活动中的课程").setShowDialog(true);
			    			dialogService.setContent("不能下架活动中关联的课程").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        				},2000);
			    		}else if(response.result=="protected"){
			    			//dialogService.setContent("不能下架轮播图中的课程").setShowDialog(true);
			    			dialogService.setContent("不能下架轮播图中关联的课程").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        				},2000);
			    		}else{
			    			/*dialogService.setContent(course.status == "Y"?"下架成功":"上架成功").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){
			    				$scope.paginationConf.onChange();
		    				}*/
			    			dialogService.setContent(course.status == "Y"?"下架成功!":"上架成功!").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        					$scope.paginationConf.onChange();
	        				},2000);
			    		}
			    	},200);
			    }).error(function(){
			    	$timeout(function(){
			    		//dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
			    		dialogService.setContent("网络异常,请重新登录后再试").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
			    	},200);
			    });
    	};
    }
    /*//下载签到二维码
    $scope.downLoadSignInTwoDimensionCode = function(course){
    	
    }
    //下载评分二维码
    $scope.downLoadGradeTwoDimensionCode = function(course){
    	
    }*/
    //添加课程按钮
    $scope.doAdd = function(type){
    	if(type == "Y"){
    		$scope.go('/course/addCourse','addCourse',null);
    	}else {
    		$scope.go('/course/addOfflineCourse', 'addOfflineCourse', null);
    	}
    }
    //编辑课程按钮
    $scope.doEdit = function(course){
    	if(course.courseOnline == "Y"){
    		$scope.go('/course/editCourse','editCourse',{id:course.courseId});
    	}else {
    		$scope.go('/course/editOfflineCourse','editOfflineCourse',{id:course.courseId});
    	}
    }
    $scope.doCopy = function(course){
    	dialogService.setContent("确定要复制课程【"+course.courseName+"】?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		var courseUrl = "";
    		if(course.courseOnline == "Y"){
    			courseUrl = "/enterpriseuniversity/services/course/copyOnlineCourse?courseId=" + course.courseId
        	}else{
        		courseUrl = "/enterpriseuniversity/services/course/copyCourseOffline?courseId=" + course.courseId;
        	}
			 $http({
			        url: courseUrl,
			        method: "PUT"
			    }).success(function (response) {
			    	//延迟弹框
			    	$timeout(function(){
			    		if(response.result=="success"){
			    			dialogService.setContent("复制成功").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        					$scope.paginationConf.onChange();
	        				},2000);
			    		}else if(response.result=="error"){
			    			dialogService.setContent("复制异常，请联系管理员!").setShowSureButton(false).setShowDialog(true);
	        	    		$timeout(function(){
	        					dialogService.sureButten_click(); 
	        				},2000);
			    		}
			    	},200);
			    }).error(function(){
			    	$timeout(function(){
			    		//dialogService.setContent("网络异常,请重新登录后再试").setShowDialog(true);
			    		dialogService.setContent("网络异常,请重新登录后再试").setShowSureButton(false).setShowDialog(true);
        	    		$timeout(function(){
        					dialogService.sureButten_click(); 
        				},2000);
			    	},200);
			    });
    	};
    }
}])
//课程评论列表模态框控制器
.controller('CourseCommentListController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout ,dialogService, dialogStatus) {
	$scope.openModalDialog = false;
    $scope.url = "/enterpriseuniversity/services/courseComment/findPage?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!$scope.courseId){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&courseId="+$scope.courseId;
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
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
    //弹出评论模态框
    $scope.$parent.$parent.viewCourseComment = function(courseId){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.courseId = courseId;
    	$scope.search(); 
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code;
    	$scope.currHighLightRow.createTime = item.createTime; 
    }
    //禁用评论
    $scope.doDisable = function (comment) {
    	dialogService.setContent(comment.status == "Y"?"确定要禁用当前课程评论吗？":"确定要启用当前课程评论吗？").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true).doNextProcess = function(){ 
	        $http({
	            url: "/enterpriseuniversity/services/courseComment/updateStatus?courseCommentId=" + comment.courseCommentId + "&status=" + (comment.status == "Y" ? "N" : "Y"),
	            method: "PUT"
	        }).success(function (response) {
	        	$timeout(function(){
	        		dialogService.setContent(comment.status == "Y"?"评论已禁用":"评论已启用").setHasNextProcess(true).setShowDialog(true).doNextProcess = function(){ $scope.search();};
	        	},200);
	        }).error(function(response,state){
	        	$timeout(function(){
	        		if(state==401||state==403){
		        		dialogService.setContent(comment.status == "Y"?"用户权限不足,无法禁用当前评论":"用户权限不足,无法启用当前评论").setShowDialog(true);
		        	}else{
		        		dialogService.setContent(comment.status == "Y"?"评论禁用失败,请稍后重试":"评论启用失败,请稍后重试").setShowDialog(true);
		        	}
	        	},200);
	        });
    	}
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
//课程评论列表模态框控制器
.controller('CourseClassesListController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout ,dialogService, dialogStatus) {
	$scope.openModalDialog = false;
	
    $scope.url = "/enterpriseuniversity/services/course/findclasses?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!$scope.courseId){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&courseId="+$scope.courseId;
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
    //搜索按钮函数
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //弹出评论模态框
    $scope.$parent.$parent.viewCourseClasses = function(item){
    	$scope.openModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.courseId = item.courseId;
    	$scope.fliepath = item.signInTwoDimensionCode;
    	$scope.search(); 
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code;
    	$scope.currHighLightRow.createTime = item.createTime; 
    }
   
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
/*
 * 添加线上课程
 * */
.controller('AddCourseController', ['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state , $timeout, dialogService) {
    $scope.$emit("cm", {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表' , gcmn: '添加线上课程'});
    $scope.course = $scope.formData = {
    		//初始化容器及单选按钮选中状态
    		sectionList:[],
    		classifyIds:[],
    		empIds:[],
    		courseOnline:'Y',
			courseDownloadable:'Y',
			isExcellentCourse:'Y',
			status:'Y',
			openComment:'Y',
			openCourse:'N',
			gradeStatus:'Y'
    }; 
    //切换线上/线下课程  执行函数
    $scope.beCourseOnline = function(course){
    	if (course.courseOnline == "N") 
        {
    		$scope.cleanChoosedEmpData();
        }
    	else
    	{
    		$scope.cleanChoosedLectureData();
    	}
    }
    
    //清空维度  执行函数
    $scope.deleteGrade = function(course){
    	if (course.gradeStatus == "N") 
        {
    		$scope.course.courseGradeDimensions="";
    		$scope.course.lecturerGradeDimensions="";
        }
    }
    //提交表单    
    function formSubmit(){
    	$scope.$emit("isSaving", true);
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/course/addCourse',
    		data : /*$.param($scope.course)*/JSON.stringify($scope.course),  
    		headers : { 'Content-Type': 'application/json' }  
    	})
    	.success(function(data) {
    		$scope.$emit("isSaving", false);
    		dialogService.setContent("新增课程成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/course/courseList','courseList',null);
    		},2000);
    	})
    	.error(function(){
    		$scope.$emit("isSaving", false);
    		dialogService.setContent("新增课程失败！").setShowSureButton(false).setShowDialog(true); 
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    		},2000);
    	});
    }
    //保存按钮
    $scope.doSave = function() {
    	 $scope.courseForm.$submitted = true;
		 if($scope.courseForm.$invalid)
	   	 {
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
	   	 }
    	 //线下课程不指定学习人员
		 if($scope.course.courseOnline=="N"||$scope.course.openCourse=="Y")
    	 {
    		 $scope.course.empIds = [];
    		 $scope.course.empNames = [];
    	 }
		 //提交表单  
		 formSubmit(); 
	};
	//监听章节数据回传
    $scope.$on("SUCCESSSECTIONFORMDATAS", function(e, sectionList){
    	e.stopPropagation();
    	$scope.course.sectionList = sectionList;
    })
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList', 'courseList', null);
    };
}])
/*
 * 编辑线上课程控制器
 * */
.controller('EditCourseController', ['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	$scope.$emit("cm", {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表', gcmn: '编辑线上课程'}); 
	$scope.deferred = $q.defer();
    if ($stateParams.id) 
    {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/course/find?courseId=" + $stateParams.id).success(function (data) {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    } 
    else 
    {
        //未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.course = $scope.formData = data; 
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    });  
    //切换线上/线下课程   单选按钮
    $scope.beCourseOnline = function(course){
    	if (course.courseOnline == "N") 
        {
    		//线下课程清空学习人员
    		$scope.cleanChoosedEmpData();
        }
    	else
    	{
    		//线上课程清空讲师数据   需要重新单选讲师
    		$scope.cleanChoosedLectureData();
    	}
    }
    
    //清空维度  执行函数
    $scope.deleteGrade = function(course){
    	if (course.gradeStatus == "N") 
        {
    		$scope.course.courseGradeDimensions="";
    		$scope.course.lecturerGradeDimensions="";
        }
    }
     
    //提交表单   
    $scope.doSave = function() {
		 $scope.courseForm.$submitted = true;
		 if($scope.courseForm.$invalid)
	   	 {
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
			return;
	   	 }
		 //线下课程不指定学习人员
		 if($scope.course.courseOnline=="N"||$scope.course.openCourse=="Y")
    	 {
    		 $scope.course.empIds = [];
    		 $scope.course.empNames = [];
    	 }
    	 //调接口保存表单数据
		 $scope.$emit("isSaving", true);
		 $http({
			 method : 'PUT',
			 url  : '/enterpriseuniversity/services/course/editCourse',
			 data : /*$.param($scope.course)*/JSON.stringify($scope.course),  
			 headers : { 'Content-Type': 'application/json' }  
		 })
		 .success(function(data) {
			 $scope.$emit("isSaving", false);
			 if(data.result=="success"){
				 dialogService.setContent("保存课程成功！").setShowSureButton(false).setShowDialog(true);
				 $timeout(function(){
					 dialogService.sureButten_click(); 
					 $scope.go('/course/courseList','courseList',null);
				 },2000);
			 }
		 })
		 .error(function(){
			 $scope.$emit("isSaving", false);
			 dialogService.setContent("保存课程失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
	//监听章节数据回传
    $scope.$on("SUCCESSSECTIONFORMDATAS", function(e, sectionList){
    	e.stopPropagation();
    	$scope.course.sectionList = sectionList;
    })
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList','courseList',null);
    };
    
}])
/*
 * 添加线下课程
 * */
.controller('AddOfflineCourseController', ['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state , $timeout, dialogService) {
    $scope.$emit("cm", {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表' , gcmn: '添加线下课程'});
    $scope.course = $scope.formData = {
    		//初始化容器及单选按钮选中状态
    		sectionList:[],
    		classifyIds:[],
    		empIds:[],
    		courseOnline:'N',
			courseDownloadable:'Y',
			isExcellentCourse:'Y',
			status:'Y',
			openComment:'Y',
			openCourse:'N',
			gradeStatus:'Y',
			needApply:'N',
			reapeatSign:'N'
    }; 
    //创建授课时间列表
    $scope.createClasses = function(course){
    	var count = course.classesCount, len;
    	if(!count || !course.classesList){
    		course.classesList = [];
    	}
    	len = course.classesList.length;
    	if(count){
    		if(count > len){
        		for(var i =0; i < count - len; i++){
        			course.classesList.push({classes : course.classesList.length + 1, beginTime :'', endTime:''});
            	}
        	}else if(count < len){
        		course.classesList.splice(count, len - count);
        	}
    	} 
    }
    //保存
    $scope.doSave = function() {
    	$scope.courseForm.$submitted = true;
    	$scope.course.courseDetail=CKEDITOR.instances['newsEditor'].getData();
		$scope.course.courseDetailEn=CKEDITOR.instances['newsEditorEn'].getData();
    	if($scope.courseForm.$invalid||!$scope.course.courseDetail||!$scope.course.courseDetailEn)
   	 	{
    		dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
   	 	}
		//提交表单  
    	//formSubmit();
    	$scope.$emit("isSaving", true);
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/course/addOffline',
    		data : /*$.param($scope.course)*/JSON.stringify($scope.course),  
    		headers : { 'Content-Type': 'application/json' }  
    	})
    	.success(function(data) {
    		$scope.$emit("isSaving", false);
    		dialogService.setContent("新增课程成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    			$scope.go('/course/courseList','courseList',null);
    		},2000);
    	})
    	.error(function(){
    		$scope.$emit("isSaving", false);
    		dialogService.setContent("新增课程失败！").setShowSureButton(false).setShowDialog(true); 
    		$timeout(function(){
    			dialogService.sureButten_click(); 
    		},2000);
    	});
	};
	//监听章节数据回传
    $scope.$on("SUCCESSSECTIONFORMDATAS", function(e, sectionList){
    	e.stopPropagation();
    	$scope.course.sectionList = sectionList;
    })
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList', 'courseList', null);
    };
}])
/*
 * 编辑线下课程控制器
 * */
.controller('EditOfflineCourseController', ['$scope', '$http', '$state', '$stateParams', '$q', '$timeout', 'dialogService', function ($scope, $http, $state, $stateParams, $q, $timeout, dialogService) {
	$scope.$emit("cm", {pmc:'pmc-a', pmn: '课程管理 ', cmc:'cmc-d', cmn: '课程列表', gcmn: '编辑线下课程'}); 
	$scope.deferred = $q.defer();
    if ($stateParams.id) 
    {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/course/findOffline?courseId=" + $stateParams.id).success(function (data) {
        	$scope.deferred.resolve(data);
    	}).error(function(data){
    		$scope.deferred.reject(data); 
    	});
    } 
    else 
    {
        //未传参数
		dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.course = $scope.formData = data; 
    	$(document).ready(function () {
        	setTimeout(function(){
        		CKEDITOR.replace('newsEditor'); 
        	 	CKEDITOR.replace('newsEditorEn');
    			CKEDITOR.instances['newsEditor'].setData($scope.course.courseDetail);
        		CKEDITOR.instances['newsEditorEn'].setData($scope.course.courseDetailEn);
    		}, 500);
        });
    	//初始化授课次数
    	$scope.course.classesCount = $scope.course.classesList ? $scope.course.classesList.length : 0;
    	//格式化时间
    	formatClassesList($scope.course.classesList, "yyyy-MM-dd hh:mm:ss");
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
        dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    });
    
    //arr : array<millisecondItem>
    // formatString : yyyy-MM-dd hh:mm:ss
    function formatClassesList (arr, formatString) {
    	if(!arr || !arr.length || !formatString){
    		return;
    	}
    	angular.forEach(arr, function(item, index){
    		if(item.beginTime && !isNaN(Number(item.beginTime))){
    			arr[index]['beginTime'] = new Date(item.beginTime).Format(formatString);
    		}
    		if(item.endTime && !isNaN(Number(item.endTime))){
    			arr[index]['endTime'] = new Date(item.endTime).Format(formatString);
    		}
    	})
    }
    //创建授课时间列表
    $scope.createClasses = function(course){
    	var count = course.classesCount, len;
    	if(!count || !course.classesList){
    		course.classesList = [];
    	}
    	len = course.classesList.length;
    	if(count){
    		if(count > len){
        		for(var i =0; i < count - len; i++){
        			course.classesList.push({classes : course.classesList.length + 1, beginTime :'', endTime:''});
            	}
        	}else if(count < len){
        		course.classesList.splice(count, len - count);
        	}
    	} 
    } 
    //提交表单   
    $scope.doSave = function() {
		 $scope.courseForm.$submitted = true;
		 $scope.course.courseDetail=CKEDITOR.instances['newsEditor'].getData();
		 $scope.course.courseDetailEn=CKEDITOR.instances['newsEditorEn'].getData();
		 if($scope.courseForm.$invalid||!$scope.course.courseDetail||!$scope.course.courseDetailEn)
		 {
			 dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
			 return;
		 }
    	 //调接口保存表单数据
		 $scope.$emit("isSaving", true);
		 $http({
			 method : 'PUT',
			 url  : '/enterpriseuniversity/services/course/editOffline',
			 data : /*$.param($scope.course)*/JSON.stringify($scope.course),  
			 headers : { 'Content-Type': 'application/json' }  
		 })
		 .success(function(data) {
			 $scope.$emit("isSaving", false);
			 if(data.result=="success"){
				 dialogService.setContent("保存课程成功！").setShowSureButton(false).setShowDialog(true);
				 $timeout(function(){
					 dialogService.sureButten_click(); 
					 $scope.go('/course/courseList','courseList',null);
				 },2000);
			 }
		 })
		 .error(function(){
			 $scope.$emit("isSaving", false);
			 dialogService.setContent("保存课程失败！").setShowSureButton(false).setShowDialog(true); 
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
	//监听章节数据回传
    $scope.$on("SUCCESSSECTIONFORMDATAS", function(e, sectionList){
    	e.stopPropagation();
    	$scope.course.sectionList = sectionList;
    })
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/course/courseList','courseList',null);
    };
}])
.controller('OfflineCourseFormCommonSubmitController', ['$scope', '$http', '$state', '$timeout', 'dialogService', function ($scope, $http, $state , $timeout, dialogService) {
	function validtor(){
		//校验...
		var beginTime, endTime, preEndTime;
		if(!$scope.course.classesList){
			return true;
		}
		for(var i = 0; i < $scope.course.classesList.length; i++){
			beginTime = $scope.course.classesList[i].beginTime;
			endTime = $scope.course.classesList[i].endTime;
			if(!beginTime || !endTime){
				return true;
			}
			if(new Date(endTime).getTime() - new Date(beginTime).getTime() <= 0){
				dialogService.setContent("授课日期" + (i + 1) + "的结束时间须晚于开始时间").setShowDialog(true);
                return;
			}
//			if( i > 0){
//				
//				preEndTime = $scope.course.classesList[i-1].endTime;
//				if(new Date(beginTime).getTime() - new Date(preEndTime).getTime() <= 0){
//					dialogService.setContent("授课日期" + (i + 1) + "的开始时间须晚于授课日期"+(i)+"的结束时间").setShowDialog(true);
//	                return;
//				}
//			}
			
		}
		return true;
	}
	$scope.doFormSubmit = function(){
		if(validtor()){
			$scope.doSave();
		}
	}
}])
//表单验证
.directive('requiredCourseimg', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? false : true;
	                  ngModel.$setValidity("requiredCourseimg", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
//英文封面校验
.directive('requiredCourseimgen', [function () {
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? false : true;
	                  ngModel.$setValidity("requiredCourseimgen", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requireString', [function () {//?
	  return {
	      require: "ngModel",
	      link: function (scope, element, attr, ngModel) {
	          var customValidator = function (value) {
	        	  var validity = null;
	        	  scope.$watch(function () {
	                  return value;
	              }, function(){
	            	  validity = (value!=undefined&&value!="") ? (value.split(",").length < 1 ? true : false) : true;
	                  ngModel.$setValidity("requireString", !validity);
	              });
	              return !validity ? value : undefined;
	          };
	          ngModel.$formatters.push(customValidator);
	          ngModel.$parsers.push(customValidator);
	      }
	  };
}])
.directive('requiredLecturers', [function () {
	return {
       require: "ngModel",
       link: function (scope, element, attr, ngModel) {
           var customValidator = function (value) {
        	   var validity = null;
        	   scope.$watch(function () {
                   return value;
               }, function(){
            	   validity = (value!=undefined&&value!="") ? (value.substr(1,value.length-2).split(",").length < 1 ? true : false):true;
                   ngModel.$setValidity("requiredLecturers", !validity);
               });
               return !validity ? value : undefined;
           };
           ngModel.$formatters.push(customValidator);
           ngModel.$parsers.push(customValidator);
        }
    };
}])
//表单验证
.directive('requireNumber', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					return value;
				}, function(){
					validity = parseInt(value) < 1 ? true : false;
					ngModel.$setValidity("requireNumber", !validity);
				});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
//表单验证
.directive('requiredSections', [function () {
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  scope.$watch(function () {
                  return value;
              }, function(){
            	  validity = parseInt(value) < 1 ? true : false;
                  ngModel.$setValidity("requiredSections", !validity);
                  });
                  return !validity ? value : undefined;
              };
              ngModel.$formatters.push(customValidator);
              ngModel.$parsers.push(customValidator);
          }
      };
}])
//验证未上传完毕的课程章节
.directive('sectionLength', [function () {
	return {
		require: "ngModel",
		link: function (scope, element, attr, ngModel) {
			var customValidator = function (value) {
				var validity = null;
				scope.$watch(function () {
					if(scope.formData){
						return value + scope.formData.sectionList.length;
					}else{
						return value ;
					}
				}, function(){
					if(scope.formData && scope.currFileUrlQueue){
						validity = parseInt(value) > (scope.formData.sectionList.length - scope.currFileUrlQueue.length) ? true : false;
					}else if(scope.$parent.formData){
						validity = parseInt(value) > scope.formData.sectionList.length ? true : false;
					}else{
						validity = false;
					}
					ngModel.$setValidity("sectionLength", !validity);
				});
				return value ;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
.directive('requireEmps', [function () {//?
  return {
      require: "ngModel",
      link: function (scope, element, attr, ngModel) {
          var customValidator = function (value) {
        	  var validity = null;
        	  scope.$watch(function () {
        		  if(scope.course)
        		  {
        			  return value + scope.course.courseOnline + scope.course.openCourse;
        		  }
        		  else
        		  {
        			  return value;
        		  }
              }, function(){
            	  validity = parseInt(value) < 1 ? true : false;
            	  if(scope.course&&scope.course.courseOnline=="N"||scope.course&&scope.course.openCourse=="Y"){
            		  validity = false;
            	  }
                  ngModel.$setValidity("requireEmps", !validity);
              });
              return !validity ? value : undefined;
          };
          ngModel.$formatters.push(customValidator);
          ngModel.$parsers.push(customValidator);
      }
  };
}])
// 课程章节文件上传控制器
.controller('CourseSectionFileUploadController', ['$scope','$http', 'FileUploader', '$timeout', 'dialogService', function ($scope,$http, FileUploader , $timeout , dialogService) {
	var uploader = $scope.uploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/course',
            autoUpload:true 
        }
    );
    uploader.filters.push({
        name: 'customFilter',
        fn: function (item, options) {
        	var type = ''; 
        	if(item.type==''){
        		type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
        	}else{
        		type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
        	}
        	var currFileCount = 0;
        	//支持mp4、ppt、pdf、word格式的文件和zip压缩文件
        	if('|mp4|vnd.ms-powerpoint|vnd.openxmlformats-officedocument.presentationml.presentation|pdf|zip|vnd.openxmlformats-officedocument.wordprocessingml.document|msword|x-zip-compressed|'.indexOf(type) == -1)
        	{
        		dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
        		return '|mp4|vnd.ms-powerpoint|vnd.openxmlformats-officedocument.presentationml.presentation|pdf|zip|vnd.openxmlformats-officedocument.wordprocessingml.document|msword|x-zip-compressed|'.indexOf(type) !== -1;
        	}
        	
        	if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0){
        		currFileCount = $scope.currFileUrlQueue.length;
        	}
        	//过滤相同文件
        	for(var i in this.queue){
        		if(this.queue[i]._file.name == item.name
        				&&new Date(this.queue[i]._file.lastModifiedDate).getTime()==new Date(item.lastModifiedDate).getTime()
        				&&this.queue[i]._file.size == item.size
        				&&this.queue[i]._file.type == item.type){
        			console.log("has selected some same files", this.queue[i]._file, item);
        			return false;
        		}
        	}
            return true;
        }
    });
    function buildAndEmitSectionList(){
    	var sectionList = [];
    	for(var i in $scope.currCourseSectionQueue){
    		sectionList.push($scope.currCourseSectionQueue[i].formData);
    	}
		for(var i in uploader.queue){
    		sectionList.push(uploader.queue[i].formData);
    	}
		for(var i in $scope.eurCourseSections){
			sectionList.push($scope.eurCourseSections[i].formData);
		}
    	//向外传递章节数据
    	$scope.$emit("SUCCESSSECTIONFORMDATAS", sectionList);
    }
    var allTypesAndLangs ;
    //初始化章节类型下拉
    (function(arr1, arr2, arr3, arr4, fn){
    	//非中欧章节类型
    	arr1.push({ sectionType : 1, sectionTypeName: "MP4中文", lang : "cn"});
    	arr1.push({ sectionType : 1, sectionTypeName: "MP4英文", lang : "en"});
    	arr1.push({ sectionType : 2, sectionTypeName: "PPT/PDF英文", lang : "en"});
    	arr1.push({ sectionType : 3, sectionTypeName: "PPT/PDF中文", lang : "cn"});
    	arr1.push({ sectionType : 4, sectionTypeName: "H5中文", lang : "cn"});
    	arr1.push({ sectionType : 4, sectionTypeName: "H5英文", lang : "en"});
    	arr1.push({ sectionType : 5, sectionTypeName: "PC中文", lang : "cn"});
    	arr1.push({ sectionType : 5, sectionTypeName: "PC英文", lang : "en"});
    	//中欧章节类型
    	arr2.push({ sectionType : 6, sectionTypeName: "H5中文", lang : "cn"});
    	arr2.push({ sectionType : 6, sectionTypeName: "H5英文", lang : "en"});
    	arr2.push({ sectionType : 7, sectionTypeName: "PC英文", lang : "en"});
    	arr2.push({ sectionType : 7, sectionTypeName: "PC中文", lang : "cn"});
    	//非中欧章节供应商
    	arr3.push({ corpid : 1, corpName : "供应商A"});
    	//中欧章节供应商
    	arr4.push({ corpid : 2, corpName : "中欧"});
    	 
    	fn(); 
    })($scope.comTypesAndLangs = [], $scope.eurTypesAndLangs = [], $scope.comCorps = [], $scope.eurCorps = [], function(){ allTypesAndLangs = $scope.comTypesAndLangs.concat($scope.eurTypesAndLangs);});
    //切换sectionType和lang属性
    $scope.toggleTypeAndLang = function(item){
    	item.formData.sectionType = item.sectionTypeAndLang ? item.sectionTypeAndLang.sectionType : undefined;
    	item.formData.lang = item.sectionTypeAndLang ? item.sectionTypeAndLang.lang : undefined;
    }
    $scope.eurCourseSections = [];
    //添加中欧课件
    $scope.addEurCourseSection = function(){
    	$scope.eurCourseSections.push({
    		formData : {
        		key : new Date().getTime(),
        		corpid : 2 ,
        		cid : null ,
        		tid : null ,
    			sectionSort : null,
        		sectionId : -1,
    			sectionName : null,
    			sectionNameEn : null,
    			sectionType : undefined,
    			lang:null ,
    			sectionAddress : "-",
        	}
    	});
    	buildAndEmitSectionList();
    }
    $scope.currCourseSectionQueue = [];
    //编辑页面数据初始化
    if($scope.deferred)
    {
    	$scope.deferred.promise.then(function(data) {
    		angular.forEach(data.sectionList ? data.sectionList : [], function(item){
    			var currItem, sectionTypeAndLang;
    			item.key = new Date().getTime();
    			if(item.cid && item.tid){
    				item.corpid = 2;
    			}
    			for(var i in allTypesAndLangs){
    				currItem = allTypesAndLangs[i];
    				if(currItem.lang == item.lang && currItem.sectionType == item.sectionType){
    					sectionTypeAndLang = currItem ;
    					break ;
    				}
    			}
    			$scope.currCourseSectionQueue.push({formData : item, sectionTypeAndLang : sectionTypeAndLang});
    		});
        }, function(data) {}); 
    } 
    //删除之前上传的文件集合   currCourseSectionQueue
    $scope.removeCurrItem = function (item) {
        $scope.currCourseSectionQueue.splice($scope.currCourseSectionQueue.indexOf(item), 1);
        buildAndEmitSectionList();
    }
    //删除新上传的中欧章节
    $scope.removeEurCourseSection = function(item){
    	for(var i in $scope.eurCourseSections){
    		if($scope.eurCourseSections[i].formData.key == item.formData.key){
    			$scope.eurCourseSections.splice(i,1);
    			break;
    		}
    	}
    	buildAndEmitSectionList();
    }
    //删除新上传的非中欧章节
    $scope.removeItem = function (item) {
    	//删除文件
    	if(item.formData.fileUrl){
    		//$http.get("/enterpriseuniversity/services/file/deleteFile?path=" + item.formData.fileUrl);
    	}
    	//从queue中删除
        item.remove();
        buildAndEmitSectionList();
    }
    //手动上传
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		item.upload();
    	}
    } 
    //文件进入queue队列后执行
    uploader.onAfterAddingFile = function (fileItem) {
    	//特殊文件类型判断
    	var fileType = fileItem.file.type.slice(fileItem.file.type.lastIndexOf('/') + 1),
    	sectionType;
    	if(fileType == ""){
    		//zip
    		fileType = fileItem.file.name.slice(fileItem.file.name.lastIndexOf('.') + 1);
    	}
    	if(fileType == "XXX"){
    		sectionType = "XXX";
    	}
    	//初始化参数 
    	fileItem.formData = {
    		key : new Date().getTime(),
    		corpid : null,
			sectionSort : null,
    		sectionId :-1,
			sectionSize : null,
			sectionName : null,
			sectionNameEn : null,
			sectionType : sectionType,
			lang : null,
			sectionAddress : null,
			fileUrl : null,
			sectionDuration : null
    	};
    };
    //上传文件回调函数  回调成功设置表单数据 
    uploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        //回调失败处理
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("文件解析异常,获取上传文件地址失败").setShowDialog(true);
        }
        else
        {	 
        	fileItem.formData.sectionSize = response.num ? response.num : 0;
        	fileItem.formData.sectionAddress = response.url ? response.url : "";
        	fileItem.formData.fileUrl = response.fileUrl ? response.fileUrl : "";
    		fileItem.formData.sectionDuration = response.duration ? response.duration : null;//mp4 duration 时长
    		buildAndEmitSectionList();
        }
    }; 
}])
//上传课程封面图片控制器
.controller('CourseImgController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
    var imgUploader = $scope.imgUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/courseImg',
            autoUpload:true
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
            	dialogService.setContent("要上传新的课程封面图片，请先删除旧的课程封面图片").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
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
    //编辑页面课程封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currFileUrlQueue =[]; 
    		if($scope.formData.courseImg&&$scope.formData.courseImg!="")
    		{
    			$scope.currFileUrlQueue.push($scope.formData.courseImg);
    		}
        }, function(data) {}); 
    } 
    //修改课程封面图片
    $scope.buildCourseImg = function(imgUrl){
		$scope.$parent.formData.courseImg = imgUrl;
    }
    //预览
    $scope.openPreview = false;
    $scope.preview = function(){
    	if($scope.$parent.formData.courseImg&&$scope.$parent.formData.courseImg!="")
    	{
    		$scope.openPreview = true;
    		$scope.previewImgUrl = "/enterpriseuniversity/"+$scope.$parent.formData.courseImg;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    $scope.closePreview = function(){
    	$scope.openPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currFileUrlQueue.splice($scope.currFileUrlQueue.indexOf(item), 1);
        $scope.buildCourseImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
    	if(item.fileUrl!=null){
    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
    	}
        item.remove();
        document.getElementById("courseImgFileInput").value=[];
        $scope.buildCourseImg(""); 
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
        	dialogService.setContent("获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildCourseImg(response.url);//接收返回的文件地址
        }
    };
}])
//上传英文课程封面图片控制器
.controller('CourseImgEnController', ['$scope', 'FileUploader', '$timeout', 'dialogService','$http', function ($scope, FileUploader, $timeout, dialogService,$http) {
    var imgEnUploader = $scope.imgEnUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/courseImg',
            autoUpload:true
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
            	dialogService.setContent("要上传新的课程封面图片，请先删除旧的课程封面图片").setShowDialog(true);
            	return !($scope.currEnFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|jpg|png|jpeg|bmp|gif|'.indexOf(type) == -1)
            {
                dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
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
    //编辑页面课程封面图片文件地址
    if($scope.$parent.deferred)
    {
    	$scope.$parent.deferred.promise.then(function(data) {
    		$scope.formData = data; 
    		$scope.currEnFileUrlQueue =[]; 
    		if($scope.formData.courseImgEn&&$scope.formData.courseImgEn!="")
    		{
    			$scope.currEnFileUrlQueue.push($scope.formData.courseImgEn);
    		}
        }, function(data) {}); 
    } 
    //修改课程封面图片
    $scope.buildCourseImg = function(imgUrl){
		$scope.$parent.formData.courseImgEn = imgUrl;
    }
    //预览
    $scope.openEnPreview = false;
    $scope.previewEn = function(){
    	if($scope.$parent.formData.courseImgEn&&$scope.$parent.formData.courseImgEn!="")
    	{
    		$scope.openEnPreview = true;
    		$scope.previewImgEnUrl = "/enterpriseuniversity/"+$scope.$parent.formData.courseImgEn;
    	}
    	else
    	{
    		dialogService.setContent("请先上传再预览").setShowDialog(true);
    	}
    }
    $scope.closeEnPreview = function(){
    	$scope.openEnPreview = false;
    }
    //删除之前上传的文件路径集合
    $scope.removeCurrItem = function (item) {
        $scope.currEnFileUrlQueue.splice($scope.currEnFileUrlQueue.indexOf(item), 1);
        $scope.buildCourseImg("");
    }
    //删除 
    $scope.removeItem = function (item) {
//    	if(item.fileUrl!=null){
//    		$http.get("/enterpriseuniversity/services/file/deleteFile?path="+item.fileUrl);
//    	}
        item.remove();
        document.getElementById("courseImgEnFileInput").value=[];
        $scope.buildCourseImg(""); 
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
        	dialogService.setContent("获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	fileItem.fileUrl=response.url;
        	$scope.buildCourseImg(response.url);//接收返回的文件地址
        }
    };
}])
//课程报名列表模态框控制器
.controller('CourseApplyListController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout ,dialogService, dialogStatus) {
	$scope.openApplyModalDialog = false;
    $scope.url = "/enterpriseuniversity/services/course/findApplyList?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 15,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	if(!$scope.courseId){
        		$scope.page = {};
        		return;
        	}
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "&courseId="+$scope.courseId;
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage +
            	"&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = response.pageSize ;
            });
        }
    };
    //搜索按钮函数
    $scope.search = function () {
    	$scope.paginationConf.currentPage = 1;
    	$scope.paginationConf.itemsPerPage = 20;
    	$scope.paginationConf.onChange();
    };
    //弹出评论模态框
    $scope.$parent.$parent.viewCourseApply = function(courseId){
    	$scope.openApplyModalDialog = true;
    	dialogStatus.setHasShowedDialog(true);
    	$scope.currHighLightRow = {};
    	$scope.courseId = courseId;
    	$scope.search(); 
    }
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(item){
    	$scope.currHighLightRow.code = item.code;
    	$scope.currHighLightRow.createTime = item.createTime; 
    }
    //关闭
    $scope.doCloseApply = function () {
        $scope.openApplyModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    } 
}])
.controller('ApplyUploadController', ['$scope','$http', 'FileUploader','dialogService','$timeout', function ($scope,$http,FileUploader,dialogService,$timeout) {
    var excelUploader = $scope.excelUploader = new FileUploader(
        {
            url: '/enterpriseuniversity/services/file/upload/apply',
            autoUpload:true                           
        }
    );
    //导入成绩过滤器
    excelUploader.filters.push({
        name: 'customFilter',
        fn: function(item , options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            var size = item.size;
            if($scope.currFileUrlQueue&&$scope.currFileUrlQueue.length>0)
            {
            	dialogService.setContent("要导入新的报名，请先删除旧的报名信息").setShowDialog(true);
            	return !($scope.currFileUrlQueue.length>0);
            }
            if(this.queue.length >0)
            {
            	 dialogService.setContent("超出上传文件数量限制").setShowDialog(true);
            	 return this.queue.length < 1;
            }
            if('|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|'.indexOf(type) == -1)
            {
            	dialogService.setContent("上传文件格式不符合要求").setShowDialog(true);
                return '|vnd.ms-excel|vnd.openxmlformats-officedocument.spreadsheetml.sheet|'.indexOf(type) !== -1;
            }
            return true;
        }
    });
    //删除 
    $scope.removeItem = function (item) {
        item.remove();
        document.getElementById("fileInput").value=[];
    }
    $scope.uploadItem = function(item){
    	if(!(item.isReady || item.isUploading || item.isSuccess)){
    		 item.upload();
    	}
    }
    // CALLBACKS
    excelUploader.onSuccessItem = function (fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        if(response.result=="error")
        {
        	fileItem.progress = 0;
        	fileItem.isSuccess = false;
        	fileItem.isError = true;
        	dialogService.setContent("获取上传文件地址失败").setShowDialog(true);
        }
        else
        {
        	$http.get("/enterpriseuniversity/services/course/importApplyResults?courseId="+document.getElementById("courseId").value+"&filePath="+response.url).success(function (response) {
	    		if(response.result=="error"){
	    			dialogService.setContent("导入失败").setShowDialog(true);
	    		}else if(response.result=="ok"){
	    			dialogService.setContent("导入成功").setShowSureButton(false).setShowDialog(true);
	   			 	$timeout(function(){
	   			 		dialogService.sureButten_click(); 
	   			 	$scope.go('/course/courseList','courseList',null);
	   			 	},2000);
	    		}else {
	    			dialogService.setContent(response.result).setShowSureButton(false).setShowDialog(true);
	   			 	$timeout(function(){
	   			 		dialogService.sureButten_click(); 
	   			 	},2000);
	    		}
    		}).error(function(response){
    			dialogService.setContent("服务器响应异常，导入失败").setShowDialog(true);
        	});
        }
    };
}])
//添加项目名称分类选择上级项目名称分类模态框函数
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
