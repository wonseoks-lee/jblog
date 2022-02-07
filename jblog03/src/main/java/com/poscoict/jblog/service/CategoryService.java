package com.poscoict.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryVo> getCategoryList(String id) {
		return categoryRepository.findAll(id);
	}

	public Boolean deleteCategory(Long no) {
		return categoryRepository.delete(no);
	}

	public Boolean addCategory(CategoryVo categoryVo) {
		return categoryRepository.insert(categoryVo);
	}

}
