package com.poscoict.jblog.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.PostVo;

@Repository
public class PostRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public Boolean insert(PostVo postVo) {
		return 1 == sqlSession.insert("post.insert", postVo);
	}

	public List<PostVo> findByCategoryNo(Long categoryNo) {
		return sqlSession.selectList("post.findByCategoryNo", categoryNo);
	}


	public PostVo findByPostNo(Long postNo) {
		return sqlSession.selectOne("post.findByPostNo", postNo);
	}

	public List<PostVo> findById(String id) {
		return sqlSession.selectList("post.findById", id);
	}

	public Long countByCategoryNo(Long categoryNo) {
		return sqlSession.selectOne("post.countByCategoryNo", categoryNo);
	}
	
	
}
