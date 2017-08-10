package com.chinamobo.ue.course.dao;

import java.util.List;
import java.util.Map;

import com.chinamobo.ue.course.entity.TomCourseCnEu;

public interface TomCourseCnEuMapper {
    int insert(TomCourseCnEu record);

    int insertSelective(TomCourseCnEu record);
    
    TomCourseCnEu selectCourseLearn(Map<Object,Object> map); 
    List<TomCourseCnEu>selectCourseBycode(Map<Object,Object> map);
    
    int updateSerlective(TomCourseCnEu record);
    
/* * ����������[�����Աcode���½�id��ѯ]
 *
 * �����ߣ�cjx
 * ����ʱ��: 2017��3��1�� ����11:28:00
 * @param map2
 * @return
 */
	int selectByexample(Map<Object, Object> map2);
}