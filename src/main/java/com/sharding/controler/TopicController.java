package com.sharding.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sharding.entity.Topic;
import com.sharding.service.TopicService;


@Controller
@RequestMapping(value = "/topic")
public class TopicController {

	@Autowired
	private TopicService taskService;

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		System.err.println("======================");
		model.addAttribute("topic", new Topic());
		model.addAttribute("action", "create");
		return "topic/topicForm";
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(){
		return "";
	}
}
