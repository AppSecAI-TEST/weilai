package com.chinamobo.ue.activity.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinamobo.ue.activity.dao.TomProjectClassifyMapper;
import com.chinamobo.ue.activity.entity.TomProjectClassify;
import com.chinamobo.ue.common.entity.PageData;
import com.chinamobo.ue.common.entity.Tree;
import com.chinamobo.ue.common.servise.TreeServise;
import com.chinamobo.ue.system.service.NumberRecordService;
import com.chinamobo.ue.ums.DBContextHolder;
import com.chinamobo.ue.utils.MapManager;

/**
 * 
 * 版本: [1.0]
 * 功能说明: 课程分类service
 *
 * 作者: JCX
 * 创建时间: 2016年3月1日 下午2:04:36
 */
@Service
public class ProjectClassifyService {

	@Autowired
	private TomProjectClassifyMapper projectClassifyMapper;
	@Autowired
	private NumberRecordService numberRecordService;
	
	/**
	 * 
	 * 功能描述：[添加]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:04:54
	 * @param projectClassify
	 */
	@Transactional
	public void addProjectClassify(TomProjectClassify projectClassify) {
		String classifyNumber= numberRecordService.getNumber(MapManager.numberType("XMFL"));
		projectClassify.setProjectNumber(classifyNumber);
		projectClassify.setCreateTime(new Date());
		projectClassify.setUpdateTime(new Date());
		projectClassifyMapper.insertSelective(projectClassify);
	}

	/**
	 * 
	 * 功能描述：[按id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:05:06
	 * @param classifyId
	 * @return
	 */
	public TomProjectClassify selectById(int classifyId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		TomProjectClassify projectClassify=projectClassifyMapper.selectByPrimaryKey(classifyId);
		if(projectClassify.getParentProjectId()==0){
			projectClassify.setParentClassifyName("-");
		}else{
			projectClassify.setParentClassifyName(projectClassifyMapper.selectByPrimaryKey(projectClassify.getParentProjectId()).getProjectName());
		}
		return projectClassify;
	}

	/**
	 * 
	 * 功能描述：[分页查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:05:21
	 * @param pageNum
	 * @param pageSize
	 * @param classifyName
	 * @param classifyId
	 * @return
	 */
	public PageData<TomProjectClassify> selectByPage(int pageNum, int pageSize, String classifyName,int classifyId) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(classifyName!=null){
			classifyName=classifyName.replaceAll("/", "//");
			classifyName=classifyName.replaceAll("%", "/%");
			classifyName=classifyName.replaceAll("_", "/_");
			
		}
		
		TomProjectClassify example=new TomProjectClassify();
		PageData<TomProjectClassify> page=new PageData<TomProjectClassify>();				
		example.setProjectName(classifyName);	
		
		List<TomProjectClassify> list=selectByParentClassifyId(projectClassifyMapper.selectAll(),classifyId);
		//获取符合查询条件的id
		String ids="-1";
		for(TomProjectClassify projectClassify:list){
			ids=ids+","+projectClassify.getProjectId();
		}
		
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("example", example);
		map.put("ids", ids);
		int count =projectClassifyMapper.countByExample(map);
		if(pageSize==-1){
			pageSize=count;
		}
		map.put("startNum", (pageNum-1)*pageSize);
		map.put("endNum", pageSize);// pageNum*
		
		List<TomProjectClassify> pageList =projectClassifyMapper.selectListByPage(map);
		for(TomProjectClassify projectClassify:pageList){
			if(projectClassify.getParentProjectId()!=0){
				projectClassify.setParentClassifyName(projectClassifyMapper.selectByPrimaryKey(projectClassify.getParentProjectId()).getProjectName());
			}else{
				projectClassify.setParentClassifyName("-");
			}
			
		}
		page.setDate(pageList);
		page.setPageNum(pageNum);
		page.setPageSize(pageSize);
		page.setCount(count);
		
		return page;
	}

	/**
	 * 
	 * 功能描述：[更新]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:15
	 * @param projectClassify
	 */
	@Transactional
	public void updateProjectClassify(TomProjectClassify projectClassify) {
		projectClassify.setUpdateTime(new Date());
		projectClassifyMapper.updateByPrimaryKeySelective(projectClassify);
		
	}

	/**
	 * 
	 * 功能描述：[删除]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:30
	 * @param projectClassify
	 */
	public String updateStatus(TomProjectClassify projectClassify) {
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		List<TomProjectClassify> list = projectClassifyMapper.selectAll();
		List<TomProjectClassify> childrenList=new ArrayList<TomProjectClassify>();
		for(int i=0;i<list.size();i++){
			if(isChild(list.get(i),projectClassify.getProjectId())==true){
				childrenList.add(list.get(i));
			}
		}
		if(childrenList.size()>1){
			return "hasChildren";
		}
//		TomProjectClassifyRelation projectClassifyRelation;
//		projectClassifyRelation=new TomProjectClassifyRelation();
//		projectClassifyRelation.setClassifyId(projectClassify.getClassifyId());
//		if(projectClassifyRelationMapper.selectByExample(projectClassifyRelation).size()!=0){
//			return "protected";
//		}else if(childrenList.size()>1){
//			return "hasChildren";
//		}
		projectClassify.setUpdateTime(new Date());
		DBContextHolder.setDbType(DBContextHolder.MASTER);
		projectClassifyMapper.deleteByPrimaryKey(projectClassify.getProjectId());
		return "success";
	}
	
	/**
	 * 
	 * 功能描述：[检查是否是当前单击的分类的子节点]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:06:40
	 * @param projectClassify
	 * @param classifyId
	 * @return
	 */
	public boolean isChild(TomProjectClassify projectClassify,int classifyId){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		if(projectClassify.getParentProjectId()==classifyId||classifyId==projectClassify.getProjectId()){
			return true;
		}else if(projectClassify.getParentProjectId()==0){
			return false;
		}else{
		   TomProjectClassify classify=projectClassifyMapper.getParent(projectClassify.getParentProjectId());
		   if(classify==null){
			   System.out.println(projectClassify.getProjectId()+"的父节点"+projectClassify.getParentProjectId());
			   return false;
		   }else{
			   return isChild(classify, classifyId);
		   }
		}
	}
	
	/**
	 * 
	 * 功能描述：[从根据分类名筛选出的课程分类列表中进行子节点选择]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月1日 下午2:07:11
	 * @param list
	 * @param classifyId
	 * @return
	 */
	public List<TomProjectClassify> selectByParentClassifyId(List<TomProjectClassify> list,int classifyId){
		List<TomProjectClassify> childrenList=new ArrayList<TomProjectClassify>();
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		//扫描子节点和本身
		for(TomProjectClassify projectClassify:list){
			if(isChild(projectClassify, classifyId)||projectClassify.getProjectId()==classifyId){
				childrenList.add(projectClassify);
			}
		}
	
		return childrenList;
	}

	/**
	 * 
	 * 功能描述：[根据父id查询]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月8日 下午3:37:11
	 * @param i
	 * @return
	 */
	public List<TomProjectClassify> selectByParentClassifyId(int i) {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		return projectClassifyMapper.selectByParentId(i);
	}
	
	/**
	 * 
	 * 功能描述：[课程分类树形菜单]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年3月12日 上午10:34:26
	 * @return
	 */
	public Tree getClassifyTree() {
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		Tree root=new Tree();
		root.setCode("0");
		List<Tree> list=projectClassifyMapper.selectClassifyAsTree();
		TreeServise treeServise=new TreeServise();
		Tree tree=treeServise.recursiveTree(root, list);
		return tree;
	}
	
	/**
	 * 
	 * 功能描述：[获取分类的所有父分类id]
	 *
	 * 创建者：JCX
	 * 创建时间: 2016年4月22日 下午2:51:16
	 * @param classifyIds
	 * @return
	 */
	public HashSet<Integer> getFathers(List<Integer> classifyIds){
		DBContextHolder.setDbType(DBContextHolder.SLAVE);
		HashSet<Integer> set=new HashSet<Integer>();
		for(int classifyId:classifyIds){
			while(classifyId!=0){
				set.add(classifyId);
				classifyId=projectClassifyMapper.selectByPrimaryKey(classifyId).getParentProjectId();
			}
		}
		 
		return set;
	}

}
