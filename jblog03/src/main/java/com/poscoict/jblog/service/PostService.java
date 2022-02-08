package com.poscoict.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poscoict.jblog.repository.PostRepository;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public Boolean write(PostVo postVo) {
		return postRepository.insert(postVo);
	}

	public List<CategoryVo> getPostCount(List<CategoryVo> cList) {
		for(CategoryVo vo: cList) {
			vo.setPostCnt(postRepository.countByCategoryNo(vo.getNo()));
		}
		return cList;
	}

	public List<PostVo> getPostList(Long categoryNo) {
		return postRepository.findByCategoryNo(categoryNo);
	}


	public PostVo getPost(Long postNo) {
		return postRepository.findByPostNo(postNo);
	}


	public List<PostVo> getDefaultPost(String id) {
		return postRepository.findById(id);
	}
	
}
