package com.sharding.test;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharding.entity.Topic;
import com.sharding.service.TopicService;
import com.sharding.util.Functions;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class TopicServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private TopicService topicService;

	@Test
	public void findTopicById(){
		long id=39;
		Topic topic = topicService.findFromDB(id);
		System.out.println(topic);
	}
	
	@Test
	public void createTopic(){
		topicService.create("标题党-_-" + Functions.format(), "主题内容", 11);
	}
	
	@Test
	public void count(){
		System.err.println(topicService.count(1));
	}
	
	@Test
	public void testPage(){
		System.err.println(topicService.list(1, 5));
	}
}
