/**
 * Created by Mr.xiang on 2016/3/26.
 */
/**
 * 项目名称分类模块
 */
angular.module('ele.projectClassify', ['ele.admin'])
.controller('ProjectClassifyController', ['$scope', '$http', '$timeout', 'dialogService', function ($scope, $http, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '项目名称管理 ', cmc:'cmc-cc', cmn: '项目名称分类'};
    //项目名称分类树数据
    $scope.searchTreeData = function(){
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/projectClassify/tree").success(function (data) {
        	$scope.$emit('isLoading', false);
            $scope.projectClassifyData = data.children;
        }).error(function () {
        	$scope.$emit('isLoading', false);
            dialogService.setContent("项目名称分类树数据查询异常").setShowDialog(true);
        });
    }
    $scope.searchTreeData();
    //http请求url
    $scope.url = "/enterpriseuniversity/services/projectClassify/findPage?pageNum=";
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 20,
        pagesLength: 9,
        perPageOptions: [20, 50, 100, '全部'],
        rememberPerPage: 'perPageItems',
        onChange: function () {
        	$scope.$httpUrl = "";
        	$scope.$httpPrams = "";
        	$scope.$emit('isLoading', true);
            if ($scope.currNode) {
            	$scope.$httpPrams = $scope.$httpPrams + "&projectId=" + $scope.currNode.code;
            } 
            if ($scope.projectName != undefined && $scope.projectName != "") {
            	$scope.$httpPrams = $scope.$httpPrams + "&projectName=" 
            		+ $scope.projectName.replace(/\%/g,"%25").replace(/\#/g,"%23")
            			.replace(/\+/g,"%2B").replace(/\s/g,"%20").replace(/\//g,"%2F")
            			.replace(/\?/g,"%3F").replace(/\&/g,"%26").replace(/\=/g,"%3D");
            }
            $scope.$httpUrl = $scope.url + $scope.paginationConf.currentPage + "&pageSize=" + ($scope.paginationConf.itemsPerPage == "全部" ? -1 : $scope.paginationConf.itemsPerPage) + $scope.$httpPrams;
            $http.get($scope.$httpUrl).success(function (response) {
            	$scope.$emit('isLoading', false);
                $scope.page = response;
                $scope.paginationConf.totalItems = response.count;
                $scope.paginationConf.itemsPerPage = (response.pageSize == response.count ? "全部" : response.pageSize);
            });
        }
    };
    //点击节点查询
    $scope.searchByCondition = function (currNode) {
        $scope.currNode = currNode;
        $scope.paginationConf.currentPage = 1;
        $scope.paginationConf.itemsPerPage = 20;
        $scope.projectName = "";
        $scope.paginationConf.onChange();
    }
    //查询按钮
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
    //新增
    $scope.doAdd = function(){
    	$scope.go('/activity/addProjectClassify','addProjectClassify',null);
    }
    //编辑
    $scope.doEdit = function(classify){
    	$scope.go('/activity/editProjectClassify','editProjectClassify',{id:classify.projectId});
    }
    //删除项目名称分类
    $scope.doDelete = function(classify) {
    	if(classify.creatorId==$scope.sessionService.user.code){
		dialogService.setContent("确定要删除项目名称分类?").setHasNextProcess(true).setShowCancelButton(true).setShowDialog(true)
		.doNextProcess = function(){
    		$http({
				url : "/enterpriseuniversity/services/projectClassify/delete?projectId="+ classify.projectId,
				method : "delete"
			})
			.success(function(response) {
				$timeout(function(){
					if(response.result=="success"){
						dialogService.setContent("删除项目名称分类成功！").setShowSureButton(false).setShowDialog(true);
	    	    		$timeout(function(){
	    					dialogService.sureButten_click(); 
	    					$scope.currNode  = undefined;
							$scope.projectName = "";
							$scope.search();
							$scope.searchTreeData();
	    				},2000);
					}else if(response.result=="error"){
						dialogService.setContent("删除项目名称分类失败！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="hasChildren"){
						dialogService.setContent("项目名称分类下存在下级项目名称分类，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}else if(response.result=="protected"){
						dialogService.setContent("项目名称分类下存在项目名称，无法删除！").setShowSureButton(false).setShowDialog(true);
			    		$timeout(function(){
							dialogService.sureButten_click(); 
						},2000);
					}
				},200);
			})
			.error(function(){
				dialogService.setContent("网络异常,删除项目名称分类失败！").setShowSureButton(false).setShowDialog(true);
	    		$timeout(function(){
					dialogService.sureButten_click(); 
				},2000);
			});
    	};
    	}else{
    		dialogService.setContent("需要创建人删除！").setShowDialog(true);
    }
		
	}
    //高亮显示选中行
    $scope.currHighLightRow = {};
    $scope.highLightCurrRow = function(classify){
    	$scope.currHighLightRow.projectId = classify.projectId; 
    }
}])
//updated v1
.controller('AddProjectClassifyController',['$http', '$scope', '$timeout', 'dialogService', function( $http, $scope, $timeout, dialogService) {
    $scope.$parent.cm = {pmc:'pmc-c', pmn: '项目名称管理 ', cmc:'cmc-cc', cmn: '项目名称分类', gcmn : '添加项目名称分类'};
    $scope.projectClassify = $scope.formData = {parentClassifyId: 0 , parentProjectClassifyName:""};
    $scope.addProjectClassifySubmit=false;
    $scope.doSave = function() {
    	$scope.projectClassifyForm.$submitted = true;
    	if($scope.projectClassifyForm.$invalid)
   	 	{
    		//dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
   		 	return;
   	 	}
    	$scope.addProjectClassifySubmit=true;
    	$http({
    		method : 'POST',
    		url  : '/enterpriseuniversity/services/projectClassify/add',
    		data : $.param($scope.projectClassify),  
    		headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
    	})
    	.success(function(data) {
    		$scope.addProjectClassifySubmit=false;
    		dialogService.setContent("新增项目名称分类成功！").setShowSureButton(false).setShowDialog(true);
    		$timeout(function(){
				dialogService.sureButten_click(); 
				$scope.go('/activity/projectClassifyList', 'projectClassifyList', null);
			},2000);
    	})
    	.error(function(){
    		$scope.addProjectClassifySubmit=false;
    		dialogService.setContent("新增项目名称分类失败！").setShowSureButton(false).setShowDialog(true); 
			$timeout(function(){
				dialogService.sureButten_click(); 
			},2000);
    	}); 
	};
	//返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/projectClassifyList', 'projectClassifyList', null);
    }
}])
//编辑项目名称分类控制器
.controller('EditProjectClassifyController',['$scope', '$http', '$q', '$stateParams', '$timeout', 'dialogService', function ($scope, $http, $q, $stateParams, $timeout, dialogService) {
	$scope.$parent.cm = {pmc:'pmc-c', pmn: '项目名称管理 ', cmc:'cmc-cc', cmn: '项目名称分类', gcmn : '编辑项目名称分类'};
    $scope.deferred = $q.defer();
    if ($stateParams.id) {
    	$scope.$emit('isLoading', true);
        $http.get("/enterpriseuniversity/services/projectClassify/find?projectId=" + $stateParams.id)
            .success(function (data) {
            	$scope.deferred.resolve(data);
            })
            .error(function(data){
        		$scope.deferred.reject(data); 
        	});
    } else {
    	dialogService.setContent("未获取到页面初始化参数").setShowDialog(true);
    }
    $scope.deferred.promise.then(function(data) {  // 成功回调
    	$scope.$emit('isLoading', false);
    	$scope.projectClassify = $scope.formData = data; 
    	$scope.projectClassify.parentProjectClassifyName=$scope.projectClassify.parentClassifyName;
    	$scope.projectClassify.parentClassifyId=$scope.projectClassify.parentProjectId;
    }, function(data) {  // 错误回调
    	$scope.$emit('isLoading', false);
    	dialogService.setContent("页面数据初始化异常").setShowDialog(true);
    });  
    $scope.editProjectClassifySubmit=false;
    $scope.doSave = function() {
    	$scope.projectClassifyForm.$submitted = true;
		 if($scope.projectClassifyForm.$invalid)
	   	 {
			 //dialogService.setContent("表单填写不完整,请按提示完整填写表单后重试").setShowDialog(true);
	   		 return;
	   	 }
		 $scope.editProjectClassifySubmit=true;
		 $http({
			 method : 'PUT',
			 url  : '/enterpriseuniversity/services/projectClassify/edit',
			 data : $.param($scope.projectClassify),  
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  
		 })
		 .success(function(data) {
			 $scope.editProjectClassifySubmit=false;
			 dialogService.setContent("保存成功！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				 dialogService.sureButten_click(); 
				 $scope.go('/activity/projectClassifyList', 'projectClassifyList', null);
			 },2000);
		 })
		 .error(function(){
			 $scope.editProjectClassifySubmit=false;
			 dialogService.setContent("保存失败！").setShowSureButton(false).setShowDialog(true);
			 $timeout(function(){
				 dialogService.sureButten_click(); 
			 },2000);
		 }); 
	};
    //返回按钮
    $scope.doReturn = function(){
    	$scope.go('/activity/projectClassifyList', 'projectClassifyList', null);
    }
}])
.directive('requireParentclassify',[ function() {
	return {
		require : "ngModel",
		link : function(scope, element, attr, ngModel) {
			var customValidator = function(value) {
				var validity = null;
				scope.$watch(function() {
								return value;
							},
							function() {
								validity = value ? (parseInt(value.length) < 1 ? true : false) : true;
								ngModel.$setValidity("requireParentclassify",!validity);
							});
				return !validity ? value : undefined;
			};
			ngModel.$formatters.push(customValidator);
			ngModel.$parsers.push(customValidator);
		}
	};
}])
.controller('ProjectClassifyModalController', ['$scope', '$http', '$timeout', 'dialogService', 'dialogStatus', function ($scope, $http, $timeout, dialogService, dialogStatus) {
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
        	$scope.itemArr.oldChooseItemArr = $scope.formData.projectIds;
        	$scope.searchTreeData();
        }, function(data) {}); 
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
            	for(var i in $scope.itemArr.currChoosedItemArr)
            	{
                	if($scope.itemArr.currChoosedItemArr[i].code==item.code)
                	{
                		$scope.itemArr.currChoosedItemArr.splice(i, 1);
                	}
                }
            } 
            else
            {
                $scope.itemArr.currChoosedItemArr.push(item);
            }
        },
        deleteItem : function (item) {
            item.selected = undefined;
            for(var i in $scope.itemArr.choosedItemArr){
            	if($scope.itemArr.choosedItemArr[i].code==item.code){
            		$scope.itemArr.choosedItemArr.splice(i, 1);
            	}
            }
            $scope.buildClassifyIds();
        },
        initCurrChoosedItemArrStatus: function () {
        	$scope.$emit('isLoading', true);
            for (var i in $scope.itemArr.currChoosedItemArr) 
            {
            	$scope.itemArr.nodeLoopStatus($scope.projectClassifyData,$scope.itemArr.currChoosedItemArr[i].code,'P',[]);//> P 数据全量   []存selected节点的fathercode
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
            		$scope.itemArr.nodeLoop(LoopNode[i].children,projectId);
            	}
        	}
        } ,
        nodeLoopStatus:function(LoopNode,projectId,loopType,parentNodeArr){//>
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
            		$scope.itemArr.nodeLoopStatus(LoopNode[i].children,projectId,'C',parentNodeArr);//>C 子节点 LoopNode[i].children
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
    $scope.buildClassifyIds = function(){
    	var projectIds = [];
        for (var i in $scope.itemArr.choosedItemArr) 
        {
        	projectIds.push($scope.itemArr.choosedItemArr[i].code);
        }
    	$scope.$parent.$parent.formData.projectIds= projectIds;
    }
    $scope.$parent.$parent.doOpenProjectClassifyModal = function () {
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
        $scope.buildClassifyIds();
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
    //关闭
    $scope.doClose = function () {
        $scope.openModalDialog = false;
        dialogStatus.setHasShowedDialog(false);
    }
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

