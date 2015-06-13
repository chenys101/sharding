package com.sharding.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sharding.entity.Group;
import com.sharding.service.GroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class GroupServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private GroupService groupService;

	@Test
	public void findGroupById(){
		long id=11;
		Group group = groupService.findFromDB(id);
		System.out.println(group.getName());
	}
	
	@Test
	public void createGroup(){
		for (int i = 1; i < 10; i++) {
			String name = "group test "+i;
			String intro = "group intro "+i;
			groupService.create(name, intro);
		}
	}
	
	@Test
	public void count(){
		System.err.println(groupService.count(11));
	}
	
	@Test
	public void testPage(){
		System.err.println(groupService.list(1, 5));
	}
}
