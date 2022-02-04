package com.poscoict.jblog.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.poscoict.jblog.exception.UserRepositoryException;
import com.poscoict.jblog.vo.UserVo;

@Repository
public class UserRepository {
	
	@Autowired
	private SqlSession sqlSession;
	
	public boolean insert(UserVo vo) {
		return 1 == sqlSession.insert("user.insert", vo);
	}	

	public boolean update(UserVo vo) {
		return 1 == sqlSession.update("user.update", vo);
	}
		
	public UserVo findByNo(Long no) {
		return sqlSession.selectOne("user.findByNo", no);
	}

	public UserVo findByIdAndPassword(String id, String password) throws UserRepositoryException {
		Map<String,String> map = new HashMap<>();
		map.put("id", id);
		map.put("password", password);
		
		return sqlSession.selectOne("user.findByIdAndPassword", map);
	}
}
