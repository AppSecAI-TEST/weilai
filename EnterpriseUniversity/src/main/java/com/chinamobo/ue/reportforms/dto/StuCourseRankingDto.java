package com.chinamobo.ue.reportforms.dto;

public class StuCourseRankingDto {
		
	private String code;
	
	private String name;//人员
	
	private Double Time;//学习时长
	
	private Double exchangeNumber;//学分
	
	private String courseName;//课程名称
	
	private Double score;//课程评分
	
	private String courseNumber;

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

	public Double getTime() {
		/*if(Time.equals(0.0)){
			return 0.0;
		}else{
			return Time/(1000*60*60);
		}*/
		return Time;
	}

	public void setTime(Double time) {
		Time = time;
	}

	public Double getExchangeNumber() {
		return exchangeNumber;
	}

	public void setExchangeNumber(Double exchangeNumber) {
		this.exchangeNumber = exchangeNumber;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	
	
	
}
