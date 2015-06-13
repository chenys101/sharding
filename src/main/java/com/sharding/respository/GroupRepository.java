package com.sharding.respository;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sharding.base.Dao;
import com.sharding.entity.Group;

@Repository
public class GroupRepository {
	
	static Log log = LogFactory.getLog(GroupRepository.class);
	
	@Autowired
	private Dao dao;

	public Group findFromDB(long id) {
		Group group = dao.findDb(Group.class, id);
		return group;
	}
	
	public void create(Group group){
		long id = dao.generateId(Group.class, "id");
		String sql = "insert into " + getTableName()
					+" (id,name,intro,create_time) values(?,?,?,?)";
		dao.getJdbcTemplate().update(sql, id, group.getName(), group.getIntro()
				, group.getCreateTime());
	}
	
	public int count(long id){
		return dao.count(Group.class," id=?",id);
	}
	
	public List<Group> list(int pageNo, int pageSize){
		String sql = "select * from " + getTableName();
		return dao.page(Group.class, sql, pageNo, pageSize);
	}
	
	public String getTableName(){
		return dao.getTableName(Group.class);
	}
}
