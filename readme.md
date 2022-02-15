# 초기화면
![jblog main gif-min](https://user-images.githubusercontent.com/66767038/154117720-2ffd3e56-d1c3-4a10-bd78-7ed8085ea92b.gif)
---

# 블로그 메인

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

```java
@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(UserVo userVo) {
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
```

userVo에 id password name을 set 하여 join 메서드를 통해 DB에 등록한다

```java
public void join(UserVo userVo) {
		BlogVo blogVo = new BlogVo();
		CategoryVo categoryVo = new CategoryVo();
		String id = userVo.getId();
		blogVo.setUserId(id);
		categoryVo.setBlogId(id);
		userRepository.insert(userVo);
		blogRepository.insert(blogVo);
		categoryRepository.insertDefault(categoryVo);
	}
```

join을 하고 글작성을 할때, 최초에 디폴트 카테고리가 있어야지만 글을 작성할 수 있다

그러므로, 최초 가입시, 블로그생성과 디폴트카테고리생성, 유저아이디 생성 세가지를 insert한다.  

# 블로그 인터셉터

```java
		Map<String, String> user_id = (Map<String, String>) request.getAttribute( HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		// NullpointException 방지
		if(user_id == null) {
			return true;
		}
		
```

uri에서 pathvariable이 null 일 경우를 먼저 처리 해준다. ( 여기서 바로 user_id.get을 했다가 널포인터가 발생했었는데 map을 먼저 널인지를 체크해줌으로서 .get을 해도 널포인트가 발생하지 않았다 )

```java
if(user_id.get("blogId") == null) {
			return true;
}
```

blogId가 널이라는 것은 메인url 로 요청이 들어왔다는 뜻이므로 메인으로 리다이렉트한다

```java
String blogId = user_id.get("blogId");
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

# Validation

```java
@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join(@ModelAttribute UserVo userVo) {
		return "user/join";
	}
```

ModelAttribute를 사용하여 UserVo 객체를 생성하여 join.jsp 로 넘긴다

(이를 안할 시, 객체가 없으므로 form:form태그를 사용할 때  modelAttribute에 적어둔 객체에 접근하지못하여 오류를 발생시킨다)

```java
@RequestMapping(value="/join", method=RequestMethod.POST)
	public String join(@ModelAttribute @Valid UserVo userVo, BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAllAttributes(result.getModel());
			return "user/join";
		}
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
```

Valid의 결과가 BindindResult객체에 저장된다

만약, 이 결과가 에러를 갖고있을 시에 맵핑된 에러종류들을 한번에 다시 get요청으로  join.jsp로 던진다

```jsx
<form:form modelAttribute="userVo" class="join-form" id="join-form" method="post" action="${pageContext.servletContext.contextPath}/user/join">
			<label class="block-label" for="name">이름</label>
			<form:input path="name"/>
			<p style="text-align:left; padding-left:0; color:#f00">
				<form:errors path="name"/>
			</p>
```

controller부터 /join get요청을 통해 받아온 userVo객체를 세팅하고 set을 사용할 수 있다

```java
@NotEmpty
	@Length(min=2, max=8)
	private String id;
	
	@NotEmpty
	private String name;
	// @Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")
	
	@NotEmpty
	@Length(min=4, max=16)
	private String password;
```

UserVo에 원하는 Validation annotation 추가

```
NotEmpty.userVo.id=\uC544\uC774\uB514\uB294 \uBE44\uC5B4 \uC788\uC744\uC218 \uC5C6\uC2B5\uB2C8\uB2E4.
NotEmpty.userVo.name=\uC774\uB984\uC740 \uBE44\uC5B4 \uC788\uC744 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.
Length.userVo.name=\uC774\uB984\uC740 2~8\uC790 \uC774\uC5B4\uC57C \uD569\uB2C8\uB2E4.
NotEmpty.userVo.password=\uBE44\uBC00\uBC88\uD638\uB294 \uBE44\uC5B4 \uC788\uC744 \uC218 \uC5C6\uC2B5\uB2C8\uB2E4.
Length.userVo.password=\uBE44\uBC00\uBC88\uD638\uB294 4~16\uC790 \uC774\uC5B4\uC57C \uD569\uB2C8\uB2E4.
```

UserVo에 설정해둔 Validation annotation에 따른 메시지명 추가

```
# message resource(Internationalization)
spring.messages.always-message-format=true
spring.messages.basename=messages/messages_ko
spring.messages.encoding=UTF-8
```

application.properties 에 message resource추가 
