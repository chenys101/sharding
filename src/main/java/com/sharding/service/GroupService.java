package com.sharding.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sharding.entity.Group;
import com.sharding.respository.GroupRepository;
import com.sharding.util.Functions;

@Repository
public class GroupService {
	
	static Log log = LogFactory.getLog(GroupService.class);
	
	@Autowired
	private GroupRepository groupRepository;

	public Group findFromDB(long id) {
		return groupRepository.findFromDB(id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void create(String name, String intro){
		Group group = new Group();
		group.setName(name);
		group.setIntro(intro);
		group.setCreateTime(Functions.getNow());
		groupRepository.create(group);
	}
	
	public int count(long id){
		return groupRepository.count(id);
	}
	
	public List<Group> list(int pageNo, int pageSize){
		return groupRepository.list(pageNo, pageSize);
	}
}
