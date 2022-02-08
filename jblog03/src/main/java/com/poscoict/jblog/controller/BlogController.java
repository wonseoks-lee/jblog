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
@RequestMapping("/{id:(?!assets)(?!images).*}")
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
			@PathVariable("id") String id,
			@PathVariable("pathNo1") Optional<Long> pathNo1,
			@PathVariable("pathNo2") Optional<Long> pathNo2,
			Model model) {
		List<CategoryVo> cList = categoryService.getCategoryList(id);
		List<PostVo> defaultPost = postService.getDefaultPost(id);
		BlogVo blogVo = blogService.viewMain(id);
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
		model.addAttribute("id", id);
		return "blog/blog-main";
	}

	@RequestMapping({"/admin","/admin/basic"})
	public String adminBasic(@PathVariable("id") String id, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(id);
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "blog/blog-admin-basic";
	}

	@RequestMapping(value = "admin/update", method = RequestMethod.POST)
	public String update(
			@PathVariable("id") String id,
			BlogVo blogVo,
			@RequestParam(value="logo-file")MultipartFile multipartFile,
			Model model) {
		String url = fileUploadService.restore(multipartFile);
		blogVo.setLogo(url);
		blogVo.setUserId(id);
		blogService.updateBlog(blogVo);
		model.addAttribute("blogVo", blogVo);
		return "redirect:/{id}/admin/basic";
	}
	
	@RequestMapping("admin/write")
	public String adminWrite(@PathVariable("id") String id, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(id);
		List<CategoryVo> cList = categoryService.getCategoryList(id);
		model.addAttribute("cList", cList);
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "blog/blog-admin-write";
	}
	
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id, Model model) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BlogVo blogVo = blogService.viewMain(id);
		List<CategoryVo> cList = postService.getPostCount(categoryService.getCategoryList(id));
		model.addAttribute("cList", cList); 
		model.addAttribute("blogVo", blogVo);
		
		if(blogVo == null) {
			return "redirect:/";
		}
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "blog/blog-admin-category";
	}
	
	@RequestMapping(value="/admin/category/write", method=RequestMethod.POST)
	public String categoryWrite(@PathVariable("id") String id, CategoryVo categoryVo) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		categoryVo.setBlogId(authUser.getId());
		categoryService.addCategory(categoryVo);
		
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "redirect:/{id}/admin/category";
	}
	
	@RequestMapping(value="/admin/post/write", method=RequestMethod.POST)
	public String postWrite(@PathVariable("id") String id, PostVo postVo) {
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		postService.write(postVo);
		
		// 로그인한 상태 or 안한 상태에서 다른 블로그 아이디 url로 접근시 redirect
		if (authUser == null || !authUser.getId().equals(id)) {
			return "redirect:/{id}";
		}
		return "redirect:/{id}";
	}
	
	@RequestMapping("admin/category/delete/{no}")
	public String delete(
			@PathVariable("id") String id,
			@PathVariable("no") Long no) {
		categoryService.deleteCategory(no);
		return "redirect:/{id}/admin/category";
	}
	
}
