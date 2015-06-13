package com.sharding.respository;

import java.util.List;

import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sharding.base.Dao;
import com.sharding.entity.Comment;

@Repository
public class CommentRespository {
	
	static Log log = LogFactory.getLog(CommentRespository.class);
	
	@Autowired
	private Dao dao;

	public Comment findFromDB(long topicId,long id) {
		Comment comment = dao.findDb(Comment.class, topicId, id);
		return comment;
	}
	
	public void create(Comment comment){
		long id = dao.generateId(Comment.class, "id");
		String sql = "insert into " + getTableName(comment.getTopicId())
					+" (id,content,create_time,topic_id) values(?,?,?,?)";
		dao.getJdbcTemplate().update(sql, id, comment.getContent(), 
				comment.getCreateTime(), comment.getTopicId());
	}
	
	public int count(long topicId, long id){
		return dao.count(Comment.class,topicId, " id=?",id);
	}
	
	public List<Comment> list(long topicId, int pageNo, int pageSize){
		String sql = "select * from " + getTableName(topicId);
		return dao.page(Comment.class, sql, pageNo, pageSize);
	}
	
	public String getTableName(long topicId){
		return dao.getTableName(Comment.class, topicId);
	}
}
