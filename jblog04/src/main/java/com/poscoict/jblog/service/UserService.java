package com.poscoict.jblog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.repository.UserRepository;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public void join(UserVo userVo) {
		BlogVo blogVo = new BlogVo();
		CategoryVo categoryVo = new CategoryVo();
		String id = userVo.getId();
		blogVo.setUserId(id);
		categoryVo.setBlogId(id);
		userRepository.insert(userVo);
		blogRepository.insert(blogVo);
		categoryRepository.insertDefault(categoryVo);
	}

	public UserVo getUser(String id, String password) {
		return userRepository.findByIdAndPassword(id, password);
	}
}
