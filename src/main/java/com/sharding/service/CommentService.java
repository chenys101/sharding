package com.sharding.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sharding.entity.Comment;
import com.sharding.entity.Topic;
import com.sharding.respository.CommentRespository;
import com.sharding.util.Functions;

@Repository
public class CommentService {
	
	static Log log = LogFactory.getLog(CommentService.class);
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private CommentRespository commentRespository;

	public Comment findFromDB(long topicId, long id) {
		return commentRespository.findFromDB(topicId, id);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void create(String content, long topicId){
		Topic topic = topicService.findFromDB(topicId);
		if (null != topic) {
			Comment comment = new Comment();
			comment.setContent(content);
			comment.setTopicId(topicId);
			comment.setCreateTime(Functions.getNow());
			commentRespository.create(comment);
		}
	}
	
	public int count(long topicId, long id){
		return commentRespository.count(topicId, id);
	}
	
	public List<Comment> list(long topicId, int pageNo, int pageSize){
		return commentRespository.list(topicId, pageNo, pageSize);
	}
}
