package com.poscoict.jblog.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.repository.BlogRepository;
import com.poscoict.jblog.repository.CategoryRepository;
import com.poscoict.jblog.repository.PostRepository;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Service
public class BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	public Boolean addBlog(BlogVo blogVo) {
		return blogRepository.insert(blogVo);
	}
	
	public BlogVo viewMain(String blogId) {
		
//		BlogVo blogVo = blogRepository.findByUserId(blogId);
		//model.addAttribute("blogVo",blogVo);
		return blogRepository.findByUserId(blogId);
	}

	public Boolean updateBlog(String blogId, BlogVo blogVo, MultipartFile multipartFile, Model model) {
		String url = fileUploadService.restore(multipartFile);
		blogVo.setLogo(url);
		blogVo.setUserId(blogId);
		blogRepository.update(blogVo);
		model.addAttribute("blogVo", blogVo);
		return true;
	}
	
	public Boolean getBlog(String blogId, Long categoryNo,Long postNo,Model model) {
		PostVo postVo = null;
		List<PostVo> pList = null;
		List<CategoryVo> cList = categoryRepository.findAll(blogId);
		Map<String, Object> map = new HashMap<>();
		
		// 카테고리 리스트의 가장 첫번째 No를 이용해서 defaultPost 구하기 
		BlogVo blogVo = blogRepository.findByUserId(blogId);
		if(blogVo == null) {
			return false;
		}
		
		
		// categoryno나 postno가 없다면 default 뿌리기, 최초 카테고리 접근 시 pList가 null 상태 
		pList = postRepository.findByCategoryNo(cList.get(0).getNo());
		postVo = pList.isEmpty()? null : pList.get(0);
		
		
		if(postNo != null) {
			// 요청한 categoryno postno에 따른 postList, postVo 보내기
			pList = postRepository.findByCategoryNo(categoryNo);
			postVo = postRepository.findByPostNo(postNo);
		} else if(categoryNo != null) {
			// 가장 최신글 postVo에 넣기, 카테고리를 눌러서 접근했을 경우 최초에 포스트가 없는 경우도 따져줘야한다.
			pList = postRepository.findByCategoryNo(categoryNo);
			postVo = pList.isEmpty()? null : pList.get(0);
		}
		
		map.put("postVo", postVo);
		map.put("pList", pList);
		map.put("blogVo", blogVo);
		map.put("cList", cList);
		map.put("blogId", blogId);
		
		model.addAllAttributes(map);
		
		return true;
	}
	
}
