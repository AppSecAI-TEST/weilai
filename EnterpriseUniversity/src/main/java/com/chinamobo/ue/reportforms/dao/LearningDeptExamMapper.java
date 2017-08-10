package com.chinamobo.ue.reportforms.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.activity.entity.TomActivity;
import com.chinamobo.ue.exam.entity.TomExamScore;
import com.chinamobo.ue.reportforms.dto.LearningDeptExamDto;

public interface LearningDeptExamMapper {
	List<LearningDeptExamDto> learningDeptExamList(Map<Object,Object> map);
	int learningDeptExamCount(Map<Object,Object> map);
}
