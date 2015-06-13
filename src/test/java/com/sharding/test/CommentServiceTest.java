package com.sharding.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharding.entity.Comment;
import com.sharding.service.CommentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class CommentServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private CommentService commentService;

	@Test
	public void findCommentById(){
		long id=2, topicId=1;
		Comment comment = commentService.findFromDB(topicId, id);
		System.out.println(comment);
	}
	
	@Test
	public void createComment(){
		for (int i = 1; i < 10; i++) {
			String content = "ÆÀÂÛÄÚÈÝ¹þ¹þ¹þ~~ "+i;
			commentService.create(content, 1);
		}
	}
	
	@Test
	public void count(){
		System.err.println(commentService.count(1, 1));
	}
	
	@Test
	public void testPage(){
		System.err.println(commentService.list(1, 1, 5));
	}
}
