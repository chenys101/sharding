package com.sharding.controler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sharding.service.TopicService;


@Controller
public class IndexController {
	
	static Log log = LogFactory.getLog(IndexController.class);

	@Autowired
	private TopicService taskService;


	@RequestMapping("/index")
	public String index() {
		log.error("===============in===========");
		return "index/list";
	}
}
