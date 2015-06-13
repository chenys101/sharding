package com.sharding.entity;

import java.util.Date;

import com.sharding.base.DBColumn;
import com.sharding.base.Entity;

@Entity(tableName="sd_group",idAllocSize=10)
public class Group {
	
	private long id;
	private String name;//小组名称
	private String intro;//小组说明
	@DBColumn(name="create_time")
	private Date createTime;//小组创建时间
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Topic{")
		.append("id").append(":").append(id).append(", ")
		.append("name").append(":").append(name).append(", ")
		.append("intro").append(":").append(intro).append(", ")
		.append("createTime").append(":").append(createTime)
		.append("}");
		return buf.toString();
	}
}