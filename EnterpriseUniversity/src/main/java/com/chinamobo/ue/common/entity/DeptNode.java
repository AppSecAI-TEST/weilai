/**
 * 
 */
package com.chinamobo.ue.common.entity;

import java.util.List;

/**
 * 版本: [1.0]
 * 功能说明: 系统组织部门节点实体;
 *
 * 作者: WChao
 * 创建时间: 2017年4月19日 下午1:32:00
 */
public class DeptNode {
	private Integer pkDept;//部门主键;
	private String code;//部门编号;
	private String name;//部门名称;
	private DeptNode parentNode;//上级部门节点;
	private Integer level;//节点级别;
	private List<DeptNode> childrenNodes;//下级部门节点;
	
	public DeptNode(){
		//默认构造器;
	}
	public DeptNode(Integer pkDept, String code, String name ,DeptNode parentNode ,Integer level) {
		super();
		this.pkDept = pkDept;
		this.code = code;
		this.name = name;
		this.parentNode = parentNode;
		this.level = level;
	}
	
	public Integer getPkDept() {
		return pkDept;
	}
	public void setPkDept(Integer pkDept) {
		this.pkDept = pkDept;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public DeptNode getParentNode() {
		return parentNode;
	}
	public void setParentNode(DeptNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public List<DeptNode> getChildrenNodes() {
		return childrenNodes;
	}
	public void setChildrenNodes(List<DeptNode> childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Override
	public String toString() {
		return "DeptNode [pkDept=" + pkDept + ", code=" + code + ", name=" + name + ", parentNode=" + parentNode
				+ ", level=" + level + ", childrenNodes=" + childrenNodes + "]";
	}
}
