package com.poscoict.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.vo.CategoryVo;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public Boolean getCategoryList(String id, Model model) {
		List<CategoryVo> cList = categoryRepository.findAll(id);
		model.addAttribute("cList", cList);
		return true;
	}
	
	public List<CategoryVo> getClist(String id) {
		return categoryRepository.findAll(id);
	}

	public Boolean deleteCategory(Long no) {
		return categoryRepository.delete(no);
	}

	public Boolean addCategory(CategoryVo categoryVo) {
		return categoryRepository.insert(categoryVo);
	}

}
