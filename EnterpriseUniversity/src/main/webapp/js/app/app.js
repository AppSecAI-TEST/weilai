
angular.module('eleApp',
    ['angularFileUpload',
        'ui.router',
        'ng.ueditor',
        'menu',
        'ele.course', 
        'ele.courseClassify',
        'ele.lecture',
        'ele.gradeDimension',
        'ele.examPaper',
        'ele.questionBank',
        'ele.exam',
        'ele.examPaper',
        'ele.taskPackage',
        'ele.activity',
        'ele.projectClassify',
        'ele.commodity',
        'ele.statistic',//查询统计
        'ele.reportForms',//报表统计
        'ele.reportAcemon',//报表统计Acemon临时
        'ele.reportEmps',
        'ele.rankStatistic',//排行统计
        'ele.orgGroup',
        'ele.role',
        'ele.admin',
        'ele.message',
        'ele.news',
        'ele.banner',
        'pagination',
        'angularTreeview',
        'angularRadiotreeview',
        'angularCheckboxtreeview'])
        
        				.config(['$httpProvider', function ($httpProvider) {
				        	$httpProvider.interceptors.push(function() {
				                return {
				                    'request': function(response) {
				                    	var baseURl, paramKeyValues, encodedKeyValues = [];
				                    	var splitUrl = function(url){
				                    		baseURl = url.split("?")[0];
				                    		paramKeyValues = url.split("?").length > 1 ? url.split("?")[1].split("&") : [];
				                    	}
				                    	var encodeParamValue = function(paramKeyValues){
				                    		 angular.forEach(paramKeyValues, function(paramKeyValue, index, paramKeyValues){
				                    			 var keyAndValue = paramKeyValue.split("="), key, value;
				                    			 if(keyAndValue.length > 1){
				                    				 encodedKeyValues.push(keyAndValue[0] + "=" + encodeURIComponent(keyAndValue[1]));
				                    			 }
				                    		 });
				                    		 if(encodedKeyValues.length > 0){
				                    			 response.url = baseURl + "?" + encodedKeyValues.join("&");
				                    		 }else{
				                    			 response.url = baseURl;
				                    		 }
				                    	}
				                    	 
				                    	if(response.url.indexOf("?") != -1){
				                    		splitUrl(response.url);
				                    		
				                    		encodeParamValue(paramKeyValues);
				                    		 
				                    		console.log("response.url", response.url);
				                    	}
				                    	
				                        return response;
				                    }
				                };
				            }); 
						}])

        
					    .config(
					    ['$stateProvider', '$urlRouterProvider', '$locationProvider',
					        function ($stateProvider, $urlRouterProvider, $locationProvider) {
					            /*系统页面视图跳转路由配置*/
					            /*
					             * 课程管理  页面路由配置
					             * */
					            //课程库  页面跳转路由
					            $stateProvider.state('courseList', {
					                url: '/courseList',
					                templateUrl: '/enterpriseuniversity/views/course/courseList.html' 
					            }).state('addCourse', {
					                url: '/addCourse',
					                templateUrl: '/enterpriseuniversity/views/course/addCourse.html'
					            }).state('editCourse', {
					                url: '/editCourse/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourse.html' 
					            }).state('addOfflineCourse', {
					                url: '/addOfflineCourse',
					                templateUrl: '/enterpriseuniversity/views/course/addOfflineCourse.html' 
					            }).state('editOfflineCourse', {
					                url: '/editOfflineCourse/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editOfflineCourse.html' 
					            })
					            // 课程分类页面跳转路由
					            .state('courseClassifyList', {
					                url: '/courseClassifyList',
					                templateUrl: '/enterpriseuniversity/views/course/courseClassifyList.html',
					                controller: 'CourseClassifyController'
		
					            }).state('addCourseClassify', {
					                url: '/addCourseClassify',
					                templateUrl: '/enterpriseuniversity/views/course/addCourseClassify.html',
					                controller: 'AddCourseClassifyController'
					            }).state('editCourseClassify', {
					                url: '/editCourseClassify/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourseClassify.html',
					                controller: 'EditCourseClassifyController'
					            })
					            //讲师管理
					            .state('lectureList', {
					                url: '/lectureList',
					                templateUrl: '/enterpriseuniversity/views/course/lectureList.html',
					                controller: 'LectureController'
					            }).state('addLecture', {
					                url: '/addLecture',
					                templateUrl: '/enterpriseuniversity/views/course/addLecture.html',
					                controller: 'AddLectureController'
					            }).state('editLecture', {
					                url: '/editLecture/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editLecture.html',
					                controller: 'EditLectureController'
					            }).state('lectureCourseList', {
					                url: '/lectureCourseList/:id',
					                templateUrl: '/enterpriseuniversity/views/course/lecturerCourseList.html',
					                controller: 'LecturerCourseListController'
					            })
					            //评价管理
					            .state('gradeDimensionList', {
					                url: '/gradeDimensionList',
					                templateUrl: '/enterpriseuniversity/views/course/gradeDimensionList.html',
					                controller: 'GradeDimensionController'
					            }).state('addCourseGradeDimension', {
					                url: '/addCourseGradeDimension',
					                templateUrl: '/enterpriseuniversity/views/course/addCourseGradeDimension.html',
					                controller: 'AddCourseGradeDimensionController'
					            }).state('editCourseGradeDimension', {
					                url: '/editCourseGradeDimension/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editCourseGradeDimension.html',
					                controller: 'EditCourseGradeDimensionController'
					            }).state('addLectureGradeDimension', {
					                url: '/addLectureGradeDimension',
					                templateUrl: '/enterpriseuniversity/views/course/addLectureGradeDimension.html',
					                controller: 'AddLectureGradeDimensionController'
					            }).state('editLectureGradeDimension', {
					                url: '/editLectureGradeDimension/:id',
					                templateUrl: '/enterpriseuniversity/views/course/editLectureGradeDimension.html',
					                controller: 'EditLectureGradeDimensionController'
					            })
					
					            /*
					             * 试卷管理  页面跳转路由配置
					             * */
					            //试卷库
					            .state('examPaperList', {
					                url: '/examPaperList',
					                templateUrl: '/enterpriseuniversity/views/exam/examPaperList.html',
					                controller: 'ExamPaperController'
					            }).state('addExamPaper', {
					                url: '/addExamPaper',
					                templateUrl: '/enterpriseuniversity/views/exam/addExamPaper.html',
					                controller: 'AddExamPaperController'
					            }).state('editExamPaper', {
					                url: '/editExamPaper/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editExamPaper.html',
					                controller: 'EditExamPaperController'
					            })
					            //题库
					            .state('questionBankList', {
					                url: '/questionBankList',
					                templateUrl: '/enterpriseuniversity/views/exam/questionBankList.html',
					                controller: 'QuestionBankController'
					            }).state('addQuestionBank', {
					                url: '/addQuestionBank',
					                templateUrl: '/enterpriseuniversity/views/exam/addQuestionBank.html',
					                controller: 'AddQuestionBankController'
					            }).state('editQuestionBank', {
					                url: '/editQuestionBank/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editQuestionBank.html',
					                controller: 'EditQuestionBankController'					                	
					            }).state('superadditionQuestionBank', {
					                url: '/superadditionQuestionBank/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/superadditionQuestionBank.html',
					                controller: 'SuperadditionQuestionBankController'
					            })
					            //题库分类
					            .state('questionBankClassifyList', {
					                url: '/questionBankClassifyList',
					                templateUrl: '/enterpriseuniversity/views/exam/questionBankClassifyList.html',
					                controller: 'QuestionBankClassifyController'
					            }).state('addQuestionBankClassify', {
					                url: '/addQuestionBankClassify',
					                templateUrl: '/enterpriseuniversity/views/exam/addQuestionBankClassify.html',
					                controller: 'AddQuestionBankClassifyController'
					            }).state('editQuestionBankClassify', {
					                url: '/editQuestionBankClassify/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editQuestionBankClassify.html',
					                controller: 'EditQuestionBankClassifyController'
					            })
					            
					            //考试管理
					            .state('examList', {
					                url: '/examList',
					                templateUrl: '/enterpriseuniversity/views/exam/examList.html',
					                controller: 'ExamController'
					            }).state('addExam', {
					                url: '/addExam',
					                templateUrl: '/enterpriseuniversity/views/exam/addExam.html',
					                controller: 'AddExamController'
					            }).state('editExam', {
					                url: '/editExam/:id',
					                templateUrl: '/enterpriseuniversity/views/exam/editExam.html',
					                controller: 'EditExamController'
					            })
					            /*
					             * 活动管理    页面跳转路由配置
					             * */
					            //任务包
					            .state('taskPackageList', {
					                url: '/taskPackageList',
					                templateUrl: '/enterpriseuniversity/views/activity/taskPackageList.html',
					                controller: 'TaskPackageController'
					            }).state('addTaskPackage', {
					                url: '/addTaskPackage',
					                templateUrl: '/enterpriseuniversity/views/activity/addTaskPackage.html',
					                controller: 'AddTaskPackageController'
					            }).state('editTaskPackage', {
					                url: '/editTaskPackage/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/editTaskPackage.html'/*,
					                controller: 'EditTaskPackageController'*/
					            }).state('detailTaskPackage', {
					                url: '/detailTaskPackage/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/detailTaskPackage.html',
					                controller: 'DetailTaskPackageController' 
					            }) 
					            
					            //培训活动
					            .state('activityList', {
					                url: '/activityList',
					                templateUrl: '/enterpriseuniversity/views/activity/activityList.html'/*,
					                controller: 'ActivityController'*/
					            }).state('addActivity', {
					                url: '/addActivity',
					                templateUrl: '/enterpriseuniversity/views/activity/addActivity.html'/*,
					                controller: 'AddActivityController'*/
					            }).state('editActivity', {
					                url: '/editActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/editActivity.html'/*,
					                controller: 'EditActivityController'*/
					            }).state('detailActivity', {
					                url: '/detailActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/detailActivity.html'/*,
					                controller: 'ViewActivityController' */
					            }).state('copyActivity', {
					                url: '/copyActivity/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/copyActivity.html'/*,
					                controller: 'CopyActivityController' */
					            })
					             // 项目名称分类页面跳转路由
					            .state('projectClassifyList', {
					                url: '/projectClassifyList',
					                templateUrl: '/enterpriseuniversity/views/activity/projectClassifyList.html',
					                controller: 'ProjectClassifyController'
		
					            }).state('addProjectClassify', {
					                url: '/addProjectClassify',
					                templateUrl: '/enterpriseuniversity/views/activity/addProjectClassify.html',
					                controller: 'AddProjectClassifyController'
					            }).state('editProjectClassify', {
					                url: '/editProjectClassify/:id',
					                templateUrl: '/enterpriseuniversity/views/activity/editProjectClassify.html',
					                controller: 'EditProjectClassifyController'
					            })
					            //e币商城
					            .state('commodityList', {
					                url: '/commodityList',
					                templateUrl: '/enterpriseuniversity/views/EBStore/commodityList.html',
					                controller: 'CommodityController'
					            }).state('addCommodity', {
					                url: '/addCommodity',
					                templateUrl: '/enterpriseuniversity/views/EBStore/addCommodity.html',
					                controller: 'AddCommodityController'
					            }).state('editCommodity', {
					                url: '/editCommodity/:id',
					                templateUrl: '/enterpriseuniversity/views/EBStore/editCommodity.html',
					                controller: 'EditCommodityController'
					            })
					            .state('commodityExchangeList', {
					                url: '/commodityExchangeList',
					                templateUrl: '/enterpriseuniversity/views/EBStore/commodityExchangeList.html',
					                controller: 'CommodityExchangeController'
					            }).state('addCommodityExchange', {
					                url: '/addCommodityExchange',
					                templateUrl: '/enterpriseuniversity/views/EBStore/addCommodityExchange.html',
					                controller: 'AddCommodityExchangeController'
					            }).state('editCommodityExchange', {
					                url: '/editCommodityExchange/:id',
					                templateUrl: '/enterpriseuniversity/views/EBStore/editCommodityExchange.html',
					                controller: 'EditCommodityExchangeController'
					            })
					            //查询统计
					            .state('courseStatisticList', {
					                url: '/courseStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/courseStatisticList.html',
					                controller: 'CourseStatisticController'
					            })
					            .state('openCourseStatisticList', {
					                url: '/openCourseStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/openCourseStatisticList.html',
					                controller: 'OpenCourseStatisticController'
					            })
					            .state('offlineCourseStatisticList', {
					                url: '/offlineCourseStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/offlineCourseStatisticList.html',
					                controller: 'OfflineCourseStatisticController'
					            })
					            //查询统计
					            .state('examStatisticList', {
					                url: '/examStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/examStatisticList.html',
					                controller: 'ExamStatisticController'
					            })
					            //查询统计
					            .state('activityStatisticList', {
					                url: '/activityStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/activityStatisticList.html',
					                controller: 'ActivityStatisticController'
					            })
					            //查询统计
					            .state('feeStatisticList', {
					                url: '/feeStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/feeStatisticList.html',
					                controller: 'FeeStatisticController'
					            })
					            .state('statisticChart', {
					                url: '/statisticChart',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/statisticChart.html',
					                controller: 'StatisticChartController'
					            })
					            //查询统计
					            .state('empStatisticList', {
					                url: '/empStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/empStatisticList.html',
					                controller: 'EmpStatisticController'
					            })
					            .state('pingtaiStatisticList', {
					                url: '/pingtaiStatisticList',
					                templateUrl: '/enterpriseuniversity/views/queryStatistic/pingtaiStatisticList.html',
					                controller: 'PingtaiStatisticController'
					            })
					            /*
					             * 报表统计-页面跳转路由配置
					             */
					            //学员学习行为
					            .state('empLearningActionList', {
					                url: '/empLearningActionList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empLearningActionList.html'/*,
					                controller: 'EmpLearningActionController'*/
					            })

					            //部门平台统计
					            .state('orgPingtaiReport', {
					                url: '/orgPingtaiReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/orgPingtaiReport.html',
					                controller: 'orgPingtaiReportController'
					            })
					            //组织部门-学习资源
					            .state('learnResourcesOrgReport', {
					                url: '/learnResourcesOrgReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/learnResourcesOrgReport.html',
					                controller: 'learnResourcesOrgReportController'
					            })
					            //学员-学习资源
					            .state('stuLearnResourcesReport', {
					                url: '/stuLearnResourcesReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/stuLearnResourcesReport.html'/*,
					                controller: 'stuLearnResourcesController'*/
					            })
					            //学员-学习资源（课程）
					            .state('stuLearnResCourseReport', {
					                url: '/stuLearnResCourseReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/stuLearnResCourseReport.html',
					                controller: 'stuLearnResCourseReportController'
					            })
					            //学员-积分
					            .state('studentIntegralReport', {
					                url: '/studentIntegralReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/studentIntegralReport.html',
					                controller: 'studentIntegralReportController'
					            })
					             .state('activityDeptList', {
					                url: '/activityDeptList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/activityDeptList.html',
					                controller: 'ActivityDeptController'
					            })
					             .state('learningDeptExamList', {
					                url: '/learningDeptExamList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/learningDeptExamList.html'
					            })
					            //学习资源-组织部门统计报表（活动）
					            .state('learningDeptCourseList', {
					                url: '/learningDeptCourseList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/learningDeptCourseList.html',
					                controller: 'LearningDeptCourseController'
					            })
					            //学习资源-组织部门统计报表（课程）
					             .state('learningDeptActivityList', {
					                url: '/learningDeptActivityList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/learningDeptActivityList.html',
					                controller: 'LearningDeptActivityController'
					            })
					            //学习资源-组织部门统计报表（考试）
					            .state('learningResourseOrgDeptExam', {
					                url: '/learningResourseOrgDeptExam',
					                templateUrl: '/enterpriseuniversity/views/reportForms/learningResourseOrgDeptExam.html',
					                controller: 'LearningResourseOrgDeptExamController'
					            })
					             .state('empActivityList', {
					                url: '/empActivityList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empActivityList.html',
					                controller: 'EmpActivityController'
					            })
					             .state('empActivityDetailList', {
					                url: '/empActivityDetailList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empActivityDetailList.html',
					                controller: 'EmpActivityDetailController'
					            })
					             .state('empLearningExamList', {
					                url: '/empLearningExamList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empLearningExamList.html',
					                controller: 'EmpLearningExamController'
					            })
					            //学员报表-学习资源详情（课程）
					             .state('empLearningDetailedCourse', {
					                url: '/empLearningDetailedCourse',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empLearningDetailedCourse.html',
					                controller: 'EmpLearningDetailedCourseController'
					            })
					            //问卷-学员统计
					             .state('questionnaireDetailList', {
					                url: '/questionnaireDetailList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/questionnaireDetailList.html',
					                //controller: 'QuestionnaireDetailController'
					            })

					            
					            //学员-课程评论
					            .state('studentCourseCommentReport', {
					                url: '/studentCourseCommentReport',
					                templateUrl: '/enterpriseuniversity/views/reportForms/studentCourseCommentReport.html',
					                controller: 'studentCourseCommentReportController'
					            })
					            //学员-课程活动-排行
					            .state('stuCourseRanking', {
					                url: '/stuCourseRanking',
					                templateUrl: '/enterpriseuniversity/views/reportForms/stuCourseRanking.html',
					                controller: 'stuCourseRankingController'
					            })
					            //部门-活动学习数据
					            .state('orgActivitylearn', {
					                url: '/orgActivitylearn',
					                templateUrl: '/enterpriseuniversity/views/reportForms/orgActivitylearn.html',
					                controller: 'orgActivitylearnController'
					            })

					            .state('empCourseCommentList', {
					                url: '/empCourseCommentList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empCourseCommentList.html',
					                controller: 'EmpCourseCommentController'
					            })
					            .state('empScoreDetaileList', {
					                url: '/empScoreDetaileList',
					                templateUrl: '/enterpriseuniversity/views/reportForms/empScoreDetaileList.html',
					                controller: 'EmpScoreDetaileController'
					            })
					            //排行统计
					            .state('empLearningTimeRankStatistic', {
					                url: '/empLearningTimeRankStatistic',
					                templateUrl: '/enterpriseuniversity/views/rank/empLearningTimeRank.html' 
					            })
					            .state('empLearningScoreRankStatistic', {
					                url: '/empLearningScoreRankStatistic',
					                templateUrl: '/enterpriseuniversity/views/rank/empLearningScoreRank.html' 
					            })
					            .state('empLearningGradeRankStatistic', {
					                url: '/empLearningGradeRankStatistic',
					                templateUrl: '/enterpriseuniversity/views/rank/empLearningGradeRank.html' 
					            })
					            
					            /*
					             * 系统管理  页面跳转路由配置
					             * */
					            //组织架构
					            .state('empList', {
					                url: '/empList',
					                templateUrl: '/enterpriseuniversity/views/system/empList.html',
					                controller: 'OrgGroupController'
					            }).state('empDetails', {
					                url: '/empDetails/:id',
					                templateUrl: '/enterpriseuniversity/views/system/empDetails.html',
					                controller: 'EmpDetailsController'
					            }).state('studentsRecord', {
					                url: '/studentsRecord/:id',
					                templateUrl: '/enterpriseuniversity/views/system/studentsRecord.html',
					                controller: 'StudentsRecordController'
					            }).state('addOrg', {
					                url: '/addOrg',
					                templateUrl: '/enterpriseuniversity/views/system/addOrg.html',
					                controller: 'AddOrgController'
					            }).state('addDept', {
					                url: '/addDept',
					                templateUrl: '/enterpriseuniversity/views/system/addDept.html',
					                controller: 'AddDeptController'
					            }).state('addEmp', {
					                url: '/addEmp',
					                templateUrl: '/enterpriseuniversity/views/system/addEmp.html',
					                controller: 'AddEmpController'
					            }).state('editEmp', {
					            	url: '/editEmp/:code',
					            	templateUrl: '/enterpriseuniversity/views/system/editEmp.html',
					                controller: 'EditEmpController'
					            }).state('editOrg', {
					                url: '/editOrg/:code',
					                templateUrl: '/enterpriseuniversity/views/system/editOrg.html',
					                controller: 'EditOrgController'
					            }).state('editDept', {
					                url: '/editDept/:code',
					                templateUrl: '/enterpriseuniversity/views/system/editDept.html',
					                controller: 'EditDeptController'
					            })
					            //角色管理
					            .state('roleList', {
					                url: '/roleList',
					                templateUrl: '/enterpriseuniversity/views/system/roleList.html',
					                controller: 'RoleController'
					            }).state('addRole', {
					                url: '/addRole',
					                templateUrl: '/enterpriseuniversity/views/system/addRole.html',
					                controller: 'AddRoleController'
					            }).state('editRole', {
					                url: '/editRole/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editRole.html',
					                controller: 'EditRoleController'
					            })
					            //权限管理
					            .state('adminList', {
					                url: '/adminList',
					                templateUrl: '/enterpriseuniversity/views/system/adminList.html',
					                controller: 'AdminController'
					            }).state('addAdmin', {
					                url: '/addAdmin',
					                templateUrl: '/enterpriseuniversity/views/system/addAdmin.html',
					                controller: 'AddAdminController'
					            }).state('editAdmin', {
					                url: '/editAdmin/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editAdmin.html',
					                controller: 'EditAdminController'
					            })
					            //消息管理
					            .state('messageList', {
					                url: '/messageList',
					                templateUrl: '/enterpriseuniversity/views/system/messageList.html',
					                controller: 'MessageController'
					            }).state('addMessage', {
					                url: '/addMessage',
					                templateUrl: '/enterpriseuniversity/views/system/addMessage.html',
					                controller: 'AddMessageController'
					            }).state('viewMessage', {
					                url: '/viewMessage/:id',
					                templateUrl: '/enterpriseuniversity/views/system/viewMessage.html',
					                controller: 'ViewMessageController'
					            }).state('messageSet', {
					                url: '/messageSet',
					                templateUrl: '/enterpriseuniversity/views/system/messageSet.html',
					                controller: 'SystemMessageSetController'
					            }).state('messageDetails', {
					                url: '/messageDetails',
					                templateUrl: '/enterpriseuniversity/views/system/messageDetails.html',
					                controller: 'MessageDetailsController'
					            })
					            //轮播图
					            .state('bannerList', {
					                url: '/bannerList',
					                templateUrl: '/enterpriseuniversity/views/system/bannerList.html',
					            }).state('addBanner', {
					                url: '/addBanner',
					                templateUrl: '/enterpriseuniversity/views/system/addBanner.html',
					            }).state('editBanner', {
					                url: '/editBanner/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editBanner.html',
					            })
					            //资讯管理
					            .state('newsList', {
					                url: '/newsList',
					                templateUrl: '/enterpriseuniversity/views/system/newsList.html',
					                controller: 'NewsController'
					            }).state('addNews', {
					                url: '/addNews',
					                templateUrl: '/enterpriseuniversity/views/system/addNews.html',
					                controller: 'AddNewsController'
					            }).state('editNews', {
					                url: '/editNews/:id',
					                templateUrl: '/enterpriseuniversity/views/system/editNews.html',
					                controller: 'EditNewsController'
					            }) 
					            //欢迎页
					            .state('welcomePage',{
					            	url:'/welcomePage',
					            	templateUrl: '/enterpriseuniversity/views/welcomePage.html',
					            })
					            //无权限提示页面
					            .state('401',{
					            	url:'/noAuthorityPage',
					            	templateUrl: '/enterpriseuniversity/views/401.html',
					            	controller: 'NoAuthorityController'
					            });
					        }])
					        .factory('dialogService',function(){
						    	var dialog = {
					    			content:"",//提示内容
					    			showCancelButton : false,
					    			showSureButton: true,
					    			showDialog : false ,
					    			hasNextProcess : false,
					    			doNextProcess : function (){},
					    			setHasNextProcess : function(status){
					    				dialog.hasNextProcess = status;
					    				return dialog;
					    			},
					    			setShowDialog : function(status){
					    				dialog.showDialog = status;
					    				return dialog;
					    			},
					    			setShowCancelButton : function(status){
					    				dialog.showCancelButton = status;
					    				return dialog;
					    			},
					    			setShowSureButton : function(status){
					    				dialog.showSureButton = status;
					    				return dialog;
					    			},
					    			sureButten_click : function(){
					    				dialog.setShowDialog(false);
					    				dialog.setShowCancelButton(false);
					    				dialog.setShowSureButton(true);
					    				dialog.removeContent();
					    				if(dialog.hasNextProcess){
					    					dialog.doNextProcess();
					    					dialog.setHasNextProcess(false);
					    					dialog.doNextProcess = function (){};
					    				} 
					    			},
					    			cancelButten_click:function(){
					    				dialog.setShowDialog(false);
					    				dialog.setShowCancelButton(false);
					    				dialog.removeContent();
					    				dialog.setHasNextProcess(false);
					    				dialog.doNextProcess = function (){};
					    			},	
					    			setContent : function(msg){
					    				dialog.content = msg;
					    				return dialog;
					    			},
					    			removeContent : function(){
					    				dialog.content = "";
					    			}
						    	}; 
						    	return dialog;
					        })
						    .factory('loadDialog',['$timeout',function($timeout){
						    	var loadDialog = {
					    			showDialog : false ,
					    			loadingItemCount:0,
					    			loadingItems :[],//多个加载项
					    			timeout : null,
					    			loading:function(){
					    				loadDialog.showDialog = true;
					    				return loadDialog;
					    			},
					    			loaded:function(){
					    				if(!loadDialog.showDialog){
					    					return loadDialog;
					    				}
					    				loadDialog.showDialog = false;
					    				return loadDialog;
					    			},
					    			addLoadingItem : function(){
					    				//loadDialog.loadingItems.push(1);
					    				//loadDialog.loading();
					    				//loadDialog.checkLoadingItems();
					    				loadDialog.loadingItemCount ++;
					    				loadDialog.checkLoadingItems();
					    			},
					    			deleteLoadingItem : function(){
					    				//loadDialog.loadingItems.splice(loadDialog.loadingItems.length-1, 1);
					    				//loadDialog.loadingItems.push(-1);
					    				//loadDialog.checkLoadingItems();
					    				loadDialog.loadingItemCount --;
					    				loadDialog.checkLoadingItems();
					    			},
					    			checkLoadingItems:function(){
					    				/*if(loadDialog.loadingItems.length==0){
					    					loadDialog.loaded();
					    				}else{
					    					loadDialog.loading();
					
					    				}*/
					    				//console.log(loadDialog.loadingItemCount);
					    				if(loadDialog.loadingItemCount===0){
					    					loadDialog.timeout = $timeout(function(){
					    						loadDialog.loaded();
					    					},1000);
					    				}else{
					    					if(angular.isDefined(loadDialog.timeout)){
					    						$timeout.cancel(loadDialog.timeout);
					    						loadDialog.timeout = null;
					    					};
			    							loadDialog.loading();
			    						}
					    			}
						    	};
						    	return loadDialog;
						    }])
						    .factory('saveDialog',function(){
						    	var saveDialog = {
					    			showDialog : false ,
					    			saving:function(){
					    				saveDialog.showDialog = true;
					    			},
					    			saved:function(){
					    				saveDialog.showDialog = false;
					    			}
						    	};
						    	return saveDialog;
						    })
						    //控制body元素是否出滚动条
						    .factory('dialogStatus',function(){
						    	var dialogStatus = {
					    			hasShowedDialog : false ,
					    			setHasShowedDialog:function(status){
					    				dialogStatus.hasShowedDialog = status;
					    			} 
					    			//初始化模态框滚动条位置
					    			//另一种解决方案： 在模态框容器上使用ng-if指令（需要在查询条件属性上加$parent.）  采用后者！
					    			/*initDialogScrollPosition : function(elementId){
					    				document.getElementById(elementId).scrollTop = 0;
					    				document.getElementById(elementId).scrollLeft = 0;
					    			}*/
						    	};
						    	return dialogStatus;
						    })
						    .factory('sessionService',['$http', 'dialogService', function($http, dialogService){
						    	var sessionService = {
					    			 user:null,
					    			 logoutUrl:"",
					    			 logined:false,
					    			 login:function(){//获取登录人信息
					    				 $http.get("/enterpriseuniversity/services/backend/sys/getUser").success(function(data){
					    					 sessionService.setUser(data);
					    					 //console.log(data);
					    					 sessionService.logined = true;
					    				 }).error(function(data){
					    					 //...登录超时
					    				 })
					    			 },
					    			 logout:function(){
					    				 $http.get("/enterpriseuniversity/services/backend/sys/logout").success(function(data){
					    					 //退出sso 
//					    					 window.location.reload();
					    					 window.location.href = "/enterpriseuniversity/services/backend/sys/loginpage";
					    					 /*if(data!="error"){
					    						 sessionService.logoutUrl = data;
					    						 console.log("logoutUrl",sessionService.logoutUrl); 
					    					 }*/
					    				 }).error(function(data){
					    					 //...
					    				 })
					    			 },
					    			 setUser:function(user){
					    				 sessionService.user = user;
					    			 },
					    			 clearUser:function(){
					    				 sessionService.user = null;
					    				 sessionService.logined = false;
					    			 }
						    	}; 
						    	return sessionService;
						    }])
						    .factory('cookieService',function($http,dialogService){
						    	var cookieService = {
						    			getCookie:function(name){
						    				var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
						    				if(arr=document.cookie.match(reg))
						    				return unescape(arr[2]);
						    				else
						    				return null;
						    			}
						    	}
						    	return cookieService;
						    })
					        //监听窗口高度、宽度 用于视图自适应
					        .directive('windowResize', ['$window', 'dialogService', 'loadDialog', 'dialogStatus', function ($window, dialogService, loadDialog, dialogStatus) {
							    return function (scope, element) {
							        var w = angular.element($window);
							        scope.getWindowHeightAndWidth = function () {
							            return {
							                'h': w.innerHeight(),
							                'w': w.innerWidth()
							            };
							        };
							        scope.$watch(scope.getWindowHeightAndWidth, function (newValue, oldValue) {
							            scope.windowHeight = newValue.h;
							            scope.windowWidth = newValue.w;
							            //设置对话框margin-top
							            scope.setModalDialogStyle = function () {
							                return {
							                    'margin-top': (newValue.h<=200?0:(newValue.h/2 - 200)<0?0:(newValue.h/2 - 200)) + 'px',
							                    'margin-bottom':'0px'
							                };
							            };
							            scope.setNomalModalDialogStyle = function () {
							                return {
							                    'margin-top': (newValue.h<=200?60:(newValue.h/2 - 100)<0?0:(newValue.h/2 - 100)) + 'px',
							                    'margin-bottom':'0px'
							                };
							            };
							            //当模态框显示时   设置body元素overflow = hidden
							            scope.setBodyStyle = function () {
							            	//确认框、加载、摸态框显示时 body元素overflow = hidden
							            	if(dialogService.showDialog||loadDialog.showDialog||dialogStatus.hasShowedDialog){
							            		return {
								                    'overflow': 'hidden'
								                };
							            	}else{
							            		return {};
							            	}
							            };
							            //设置评分模态框样式？
							            scope.setFullScreenModalDialogStyle = function () {
							            	if(newValue.w<1280){
							            		return {'margin': '0px auto'};
							            	}else{
							            		if(newValue.h<635){
							            			return {
									                    'margin': '0 auto',
									                    'width': (newValue.w-30)+'px'
									                };
							            		}else{
							            			return {
									                    'margin': '10px auto',
									                    'width': (newValue.w-30)+'px'
									                };
							            		}
							            	}
							            };
							            // ？
							            scope.setStyle_01 = function () {
							            	if(newValue.h<635){
							            		return {
							            			'overflow-x': 'hidden',
							            			'max-height': '510px'
						            			}
							            	}else{
							            		return {
							            			'overflow-x': 'hidden',
							            			'max-height': (newValue.h-150)+'px',
						            			}
							            	}
							            }
							            
							            scope.setStyle_02 = function () {
							            	return {
							            		'margin': '30px auto'
					            			}
							            }
							            
							            //emp dialog 左、中、右三块高度设置
							            scope.setStyle_03 = function () {
							            	if(newValue.h>=610){
							            		return {
							            			'height': newValue.h-185+'px'
						            			}
							            	}else{
							            		return {}
							            	}
							            }
							            
							            //设置统计图位置
							            scope.setStyle_04 = function () {
							            	if(newValue.h>=635){
							            		return {'margin': ((newValue.h-610)<0?0:(newValue.h-610))/2+'px auto'}
							            	}else{
							            		return {}
							            	}
							            }
							            
							        }, true);
							        
							        w.bind('resize', function () {
							            scope.$apply();
							        });
							    }
				        	}]);