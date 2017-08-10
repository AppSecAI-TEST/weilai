/**
 * 
 */
package com.chinamobo.ue.system.entity;

/**
 * 版本: [1.0]
 * 功能说明: 
 *
 * 作者: WChao
 * 创建时间: 2017年6月8日 上午11:00:16
 */
public class TomRollingPictureEmp {
	
	public TomRollingPictureEmp(){
		
	}
	public TomRollingPictureEmp(String pictureId , String empId){
		this.pictureId = pictureId;
		this.empId = empId;
	}
	private String pictureId;
	private String empId;
	public String getPictureId() {
		return pictureId;
	}
	public void setPictureId(String pictureId) {
		this.pictureId = pictureId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}
