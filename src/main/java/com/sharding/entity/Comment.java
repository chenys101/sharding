package com.sharding.entity;

import java.util.Date;

import com.sharding.base.DBColumn;
import com.sharding.base.Entity;
import com.sharding.base.Transient;

@Entity(tableName="sd_comment",split="Mod:topicId:10",idAllocSize=10)
public class Comment {
	
	private long id;
	private String content;//��������
	private int status;//״̬
	@DBColumn(name="create_time")
	private Date createTime;//����ʱ��
	@DBColumn(name="topic_id")
	private long topicId;//С��ID
	
	@Transient
	private Topic topic;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("Topic{")
		.append("id").append(":").append(id).append(", ")
		.append("content").append(":").append(content)
		.append("status").append(":").append(status).append(", ")
		.append("createTime").append(":").append(createTime).append(", ")
		.append("topicId").append(":").append(topicId)
		.append("}");
		return buf.toString();
	}
}