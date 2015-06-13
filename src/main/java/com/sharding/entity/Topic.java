package com.sharding.entity;

import java.util.Date;

import com.sharding.base.DBColumn;
import com.sharding.base.Entity;
import com.sharding.base.Transient;

@Entity(tableName="sd_topic",idAllocSize=10)
public class Topic {
	
	private long id;
	private String title;//标题
	private String content;//说明
	private int status;//状态
	@DBColumn(name="create_time")
	private Date createTime;//主题创建时间
	@DBColumn(name="group_id")
	private long groupId;//小组ID
	
	@Transient
	private Group group;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Topic{")
		.append("id").append(":").append(id).append(", ")
		.append("title").append(":").append(title).append(", ")
		.append("content").append(":").append(content).append(", ")
		.append("status").append(":").append(status).append(", ")
		.append("createTime").append(":").append(createTime).append(", ")
		.append("groupId").append(":").append(groupId)
		.append("}");
		return buf.toString();
	}
}