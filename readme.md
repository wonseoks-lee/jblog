# 블로그 메인

---

```java
@RequestMapping(value = {"", "/{categoryNo}", "/{categoryNo}/{postNo}"}, method = RequestMethod.GET)
	public String blogMain(
			@PathVariable("blogId") String blogId,
			@PathVariable(required=false) Long categoryNo,
			@PathVariable(required=false) Long postNo,
			Model model) {
		blogService.getBlog(blogId, categoryNo, postNo, model);
		return "blog/blog-main";
	}
```

http://ip:8080/ [사용자ID] / [categoryNo] / [postNo] 로 주소를 요청 받는다

PathVariable에 require=false값을 주어 http://ip:8080/ [사용자ID] 로 요청이 들어와도 오류 X

# 로그인

---

```java
@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		
		UserVo authUser = userService.getUser(id, password);
		if(authUser==null) {
			request.setAttribute("result", "fail");
			request.setAttribute("id", id);
			//viewResolver가 작동하는 공간이 아니므로 직접 경로설정해준다 
			request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
			return false;
		}
		
		//session 
		session.setAttribute("authUser", authUser);
		response.sendRedirect(request.getContextPath());
		return false;
	}
```

id, password를 입력하고 로그인버튼을 누르면 user/auth로 요청이 간다.

user/auth으로 요청 시, 맵핑해둔 로그인 인터셉터가 실행되고 getUser 메서드를 통해  입력받은 id password에 맞는 유저가 있다면 session에 authUser란 이름으로 객체를 넣는다

# 로그아웃

---

```java
@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(session != null) {
			session.removeAttribute("authUser");
			session.invalidate();
		}
		response.sendRedirect(request.getContextPath());
		return false;
	}
```

로그아웃버튼 클릭 시, user/logout으로 요청하고 맵핑해둔 로그아웃인터셉터가 실행된다

session에 있는 authUser를 삭제하고, 세션 객체를 삭제한다.

# 회원가입

---

```java
@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(UserVo userVo) {
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
```

userVo에 id password name을 set 하여 join 메서드를 통해 DB에 등록한다

# 블로그 인터셉터

---

```java
		Map<String, Object> user_id = (Map<String, Object>) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		String blogId = null;
		// NullpointException 방지
		if(user_id.get("blogId")!=null) {
			blogId = (String) user_id.get("blogId");
		}
		
```

.get을 통해 바로 blogId에 대입할 경우 NullpointerException이 발생한다

이를 방지하기위해 user_id.get(”blogId”) 가 널이 아닐때만 blogId에 선언한다

```java
if(blogId == null) {
			response.sendRedirect(request.getContextPath());
			return false;
		}
```

blogId가 널이라는 것은 메인url 로 요청이 들어왔다는 뜻이므로 메인으로 리다이렉트한다

```java
BlogVo blogVo = (BlogVo)blogService.viewMain(blogId);
		if(blogVo == null) {
			response.sendRedirect(request.getContextPath());
			return false;
		}
```

blogId로 url 접근시, blogVo 객체를 불러오고 해당하는 아이디의 블로그가 존재하지 않을 시, 메인으로 리다이렉트

```java
request.setAttribute("blogVo", blogVo);
		return true;
```

정상적인 요청시, blogVo객체를 던짐

### 계정 생성
show databases;

use jblog;

create user 'jblog'@'%' identified by 'jblog';

grant all privileges on jblog.* to 'jblog'@'%';

flush privileges;