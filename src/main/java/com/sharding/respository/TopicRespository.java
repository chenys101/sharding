package com.sharding.respository;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.sharding.base.Dao;
import com.sharding.entity.Topic;

@Repository
public class TopicRespository {
	
	static Log log = LogFactory.getLog(TopicRespository.class);
	
	@Autowired
	private Dao dao;

	public Topic findFromDB(long id) {
		Topic topic = null;
		try {
			topic = dao.findDb(Topic.class, id);
		} catch (DataAccessException e) {
			log.error("Can not find by id:"+id);
		}
		return topic;
	}
	
	public void create(Topic topic){
		dao.create(topic);
	}
	
	public int count(long id){
		return dao.count(Topic.class," id=?",id);
	}
	
	public List<Topic> list(int pageNo, int pageSize){
		String sql = "select * from " + getTableName();
		return dao.page(Topic.class, sql, pageNo, pageSize);
	}
	
	public String getTableName(){
		return dao.getTableName(Topic.class);
	}
}
