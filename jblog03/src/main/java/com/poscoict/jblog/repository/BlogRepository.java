package com.poscoict.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public Boolean insert(BlogVo blogVo) {
		return 1 == sqlSession.insert("blog.insert", blogVo);
	}

	public BlogVo findByUserId(String userId) {
		return sqlSession.selectOne("blog.findByUserId", userId);
	}

	public Boolean update(BlogVo blogVo) {
		return 1 == sqlSession.update("blog.update", blogVo);
	}
	
	
}
