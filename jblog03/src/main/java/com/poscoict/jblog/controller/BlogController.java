package com.poscoict.jblog.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.service.BlogService;
import com.poscoict.jblog.service.CategoryService;
import com.poscoict.jblog.service.FileUploadService;
import com.poscoict.jblog.service.PostService;
import com.poscoict.jblog.vo.BlogVo;
import com.poscoict.jblog.vo.CategoryVo;
import com.poscoict.jblog.vo.PostVo;
import com.poscoict.jblog.vo.UserVo;

// asset을 뺴는 정규표현식 
// .* -> 모든 문자열을 빼겠다.
// (?!aseests) -> assets 을 제외하고 모두 들어온다
@Controller
@RequestMapping("/{blogId:(?!assets)(?!images).*}")
public class BlogController {

	@Autowired
	private HttpSession session;

	@Autowired
	private FileUploadService fileUploadService;
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired PostService postService;

	@RequestMapping({"", "/{pathNo1}", "/{pathNo1}/{pathNo2}"})
	public String blogMain(
			@PathVariable("blogId") String blogId,
			@PathVariable("pathNo1") Optional<Long> pathNo1,
			@PathVariable("pathNo2") Optional<Long> pathNo2,
			Model model) {
		List<CategoryVo> cList = categoryService.getCategoryList(blogId);
		List<PostVo> defaultPost = postService.getDefaultPost(blogId);
		BlogVo blogVo = blogService.viewMain(blogId);
		if(blogVo == null) {
			return "redirect:/";
		}
		Long categoryNo = 0L;
		Long postNo = 0L;
		
		
		if(pathNo2.isPresent()) {
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();
			PostVo postVo = postService.getPost(postNo);
			List<PostVo> pList = postService.getPostList(categoryNo);
			model.addAttribute("postVo", postVo);
			model.addAttribute("pList", pList);
		} else if(pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
			List<PostVo> pList = postService.getPostList(categoryNo);
			model.addAttribute("pList", pList);
		}
		
		
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("cList", cList);
		model.addAttribute("defaultPost", defaultPost);
		model.addAttribute("blogId", blogId);
		return "blog/blog-main";
	}

	@RequestMapping({"/admin","/admin/basic"})
	public String adminBasic(@PathVariable("blogId") String blogId, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(blogId);
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(blogId)) {
			return "redirect:/{blogId}";
		}
		return "blog/blog-admin-basic";
	}

	@RequestMapping(value = "admin/update", method = RequestMethod.POST)
	public String update(
			@PathVariable("blogId") String blogId,
			BlogVo blogVo,
			@RequestParam(value="logo-file")MultipartFile multipartFile,
			Model model) {
		String url = fileUploadService.restore(multipartFile);
		blogVo.setLogo(url);
		blogVo.setUserId(blogId);
		blogService.updateBlog(blogVo);
		model.addAttribute("blogVo", blogVo);
		return "redirect:/{blogId}/admin/basic";
	}
	
	@RequestMapping("admin/write")
	public String adminWrite(@PathVariable("blogId") String blogId, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(blogId);
		List<CategoryVo> cList = categoryService.getCategoryList(blogId);
		model.addAttribute("cList", cList);
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(blogId)) {
			return "redirect:/{id}";
		}
		return "blog/blog-admin-write";
	}
	
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("blogId") String blogId, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(blogId);
		List<CategoryVo> cList = postService.getPostCount(categoryService.getCategoryList(blogId));
		model.addAttribute("cList", cList); 
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(blogId)) {
			return "redirect:/{blogId}";
		}
		return "blog/blog-admin-category";
	}
	
	@RequestMapping(value="/admin/category/write", method=RequestMethod.POST)
	public String categoryWrite(@PathVariable("blogId") String blogId, CategoryVo categoryVo) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		categoryVo.setBlogId(authUser.getId());
		categoryService.addCategory(categoryVo);
		
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(blogId)) {
			return "redirect:/{blogId}";
		}
		return "redirect:/{blogId}/admin/category";
	}
	
	@RequestMapping(value="/admin/post/write", method=RequestMethod.POST)
	public String postWrite(@PathVariable("blogId") String blogId, PostVo postVo) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		postService.write(postVo);
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(blogId)) {
			return "redirect:/{blogId}";
		}
		return "redirect:/{blogId}";
	}
	
	@RequestMapping("admin/category/delete/{no}")
	public String delete(
			@PathVariable("blogId") String blogId,
			@PathVariable("no") Long no) {
		categoryService.deleteCategory(no);
		return "redirect:/{blogId}/admin/category";
	}
	
}
