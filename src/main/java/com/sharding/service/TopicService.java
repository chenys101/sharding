package com.sharding.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sharding.entity.Topic;
import com.sharding.respository.TopicRespository;
import com.sharding.util.Functions;

@Repository
public class TopicService {
	
	static Log log = LogFactory.getLog(TopicService.class);
	
	@Autowired
	private TopicRespository topicRespository;

	public Topic findFromDB(long id) {
		return topicRespository.findFromDB(id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void create(String title, String content, long groupId){
		Topic topic = new Topic();
		topic.setTitle(title);
		topic.setContent(content);
		topic.setCreateTime(Functions.getNow());
		topic.setGroupId(groupId);
		topicRespository.create(topic);
	}
	
	public int count(long id){
		return topicRespository.count(id);
	}
	
	public List<Topic> list(int pageNo, int pageSize){
		return topicRespository.list(pageNo, pageSize);
	}
}
