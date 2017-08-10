/*
 * 带有复选框的tree
 * */
(function (angular) {
    'use strict';

    angular.module('angularCheckboxtreeview', []).directive('checkboxtreeModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreeModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtree-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';


                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {

                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };

                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {

                                    if (selectedNode.selected) {
                                    	if(scope.itemArr.chooseItem){
                                        	scope.itemArr.chooseItem(selectedNode);
                                        }
                                    	  
                                        selectedNode.selected = undefined;
                                         
                                        //课程分类控制器方法
                                        /*if(scope.tempDeleteItem){
                                            scope.tempDeleteItem(selectedNode);
                                        }*/
                                        //课程分类控制器方法
                                        
                                    } else {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                      
                                        selectedNode.selected = 'selected';
                                        //添加新选中的项
                                        //scope.selectedArr.push(selectedNode.code);

                                        
                                    }
                                   // scope.roleScope = scope.selectedArr.join(",");
                                    //scope[treeId].currentNode = selectedNode;
                                };

                                scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {

                                    if (selectedNode.selected) {
                                    	if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                        //删除之前选中的选项
                                        /*if (scope.selectedArr != undefined && scope.selectedArr.indexOf(selectedNode.nodeId)) {
                                            scope.selectedArr.splice(scope.selectedArr.indexOf(selectedNode.nodeId), 1);
                                        }*/
                                        //课程分类控制器方法
                                       /* if(scope.tempDeleteItem){
                                            scope.tempDeleteItem(selectedNode);
                                        }*/
                                        
                                    } else {
                                    	//课程分类控制器方法
                                        if(scope.itemArr.chooseItem){
                                            scope.itemArr.chooseItem(selectedNode);
                                        }

                                        selectedNode.selected = 'selected';
                                        //添加新选中的项
                                        //scope.selectedArr.push(selectedNode.code);

                                        
                                    }
                                    //scope.roleScope = scope.selectedArr.join(",");
                                };

                            }

                            element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            .directive('checkboxauthoritytreeModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxauthoritytreeModel;
                       
                        //node id
                        var nodeId = attrs.nodeId || 'authorityTypeName';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'typeFunction';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'list';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            //父节点
                            + ' <input type="checkbox" ng-show="node.authorityTypeId==null" ng-click="' + treeId + '.selectParentCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span style="cursor:default;" ng-show="node.authorityTypeId==null">{{node.' + nodeLabel + '}}</span>'
                            //子节点
                            + ' <input type="checkbox" ng-show="node.authorityTypeId!=null" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span data-ng-class="node.selected" ng-show="node.authorityTypeId!=null" data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxauthoritytree-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';


                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };

                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {
                                if(selectedNode.authorityTypeName!=selectedNode.typeFunction){
                                    if (selectedNode.selected) {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                      
                                    } else {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = 'selected';
                                        
                                    }
                                }
                                };

                                scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {

                                    if (selectedNode.selected) {
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = undefined;
                                       
                                    } else {
                                    	
                                        if(scope.itemArr2.chooseItem){
                                            scope.itemArr2.chooseItem(selectedNode);
                                        }
                                        selectedNode.selected = 'selected';
                                      
                                    }
                                    
                                };
                                scope[treeId].selectParentCheckBox = scope[treeId].selectParentCheckBox || function (selectedNode) {
                                	//全选子权限
                                	if(scope.itemArr2.chooseParentItem){
                                        scope.itemArr2.chooseParentItem(selectedNode);
                                    }
                                    
                                };

                            }

                            element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            .directive('checkboxtreetwoModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreetwoModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-if="node.statuss==\'3\'&&role.roleType==\'2\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <input type="checkbox" ng-if="role.roleType==\'1\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected==\'selected\'"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtreetwo-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';

                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                        
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };
                                
                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {  
                                	 //部门级  and  公司节点不支持点击
                                	 if(scope.role.roleType=="2"&&selectedNode.statuss=="2"){
                                		 return;
                                	 }
                            		 if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                                
                                 scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {
                                     if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                             }
                             element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }])
            //培训活动选择所属部门模态框部门树调用
            .directive('checkboxtreedeptModel',
        ['$compile',
            function ($compile) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        //tree id
                        var treeId = attrs.checkboxtreeId;

                        //tree model
                        var treeModel = attrs.checkboxtreedeptModel;

                        //node id
                        var nodeId = attrs.nodeId || 'code';

                        //node label
                        var nodeLabel = attrs.nodeLabel || 'name';

                        //children
                        var nodeChildren = attrs.nodeChildren || 'children';

                        //tree template
                        var template =
                            '<ul>'
                            + ' <li data-ng-repeat="node in ' + treeModel + '">'
                            + ' <i class="collapsed glyphicon glyphicon-plus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="expanded glyphicon glyphicon-minus"'
                            + ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed"'
                            + ' data-ng-click="' + treeId + '.selectNodeHead(node)"></i>'
                            + ' <i class="normal" data-ng-hide="node.' + nodeChildren + '.length"></i> '
                            + ' <input type="checkbox" ng-if="node.statuss==\'3\'" ng-click="' + treeId + '.selectCheckBox(node);" ng-checked="node.selected"/>'
                            + ' <span data-ng-class="node.selected"'
                            + ' data-ng-click="' + treeId + '.selectNodeLabel(node)">{{node.' + nodeLabel + '}}</span>'
                            + ' <div data-ng-hide="!node.collapsed" data-checkboxtree-id="' + treeId + '"'
                            + ' data-checkboxtreedept-model="node.' + nodeChildren + '" data-node-id=' + nodeId
                            + ' data-node-label=' + nodeLabel + ' data-node-children=' + nodeChildren + ' ></div>'
                            + ' </li>' +
                            '</ul>';

                        //check tree id, tree model
                        if (treeId && treeModel) {

                            //root node
                            if (attrs.angularCheckboxtreeview) {
                        
                                scope[treeId] = scope[treeId] || {};

                                scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode) {
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                };
                                
                                scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode) {  
                                	 //部门级  and  公司节点不支持点击
                                	 if(selectedNode.statuss!="3"){
                                		 return;
                                	 }
                            		 if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                                
                                 scope[treeId].selectCheckBox = scope[treeId].selectCheckBox || function (selectedNode) {
                                     if (selectedNode.selected) {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = undefined;
                                     } else {
                                         if(scope.itemArr.chooseItem){
                                             scope.itemArr.chooseItem(selectedNode);
                                         }
                                         selectedNode.selected = 'selected';
                                     }
                                 };
                             }
                             element.html('').append($compile(template)(scope));
                        }
                    }
                };
            }]);
})(angular);