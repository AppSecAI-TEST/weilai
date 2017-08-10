package com.chinamobo.ue.common.entity;

import java.util.List;

public class Tree {
	private String code;
	private String name;
	private String nameEn;
	private String fathercode;
	private String statuss;
	private String topname;
	private String orgcode;
	
	
	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getTopname() {
		return topname;
	}

	public void setTopname(String topname) {
		this.topname = topname;
	}

	public String getStatuss() {
		return statuss;
	}

	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}

	private List<Tree> children;

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

	public String getFathercode() {
		return fathercode;
	}

	public void setFathercode(String fathercode) {
		this.fathercode = fathercode;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

}
