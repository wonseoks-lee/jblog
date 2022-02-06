package com.poscoict.jblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.vo.BlogVo;

@Service
public class BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	
	public Boolean addBlog(BlogVo blogVo) {
		return blogRepository.insert(blogVo);
	}
	
	public BlogVo viewMain(String userId) {
		return blogRepository.findByUserId(userId);
	}
	
	
}
