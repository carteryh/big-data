package cn.itcast.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import cn.itcast.service.UserService;
import cn.itcast.util.CustomerContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import cn.itcast.entity.User;

@Controller
public class UserController {

	private UserService userService;

	@RequestMapping(value = "/user_login")
	public @ResponseBody String user_login(String uname, String upassword,
			HttpSession session) {

		System.out.println("1>>>>>>>>>>>>>>>>>>   "+upassword);

		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);

		User user = userService.login(uname, upassword);
		if (user instanceof User && user != null) {
			session.setAttribute("user", user);
			// ////////////////////////添加记住我的逻辑///////////////////////////////
			/*
			 * if ("true".equals(rememberMe)) { cookieUtil.setCookie(user,
			 * response); }
			 */
			return "true";

		}
		return "false";
	}

	@RequestMapping(value = "/user_exit")
	public String exit(HttpSession session) {
		// cookieUtil.delCookie(request, response);
		session.removeAttribute("user");
		return "redirect:/login.jsp";
	}

	// 添加用户
	@RequestMapping(value = "user_add")
	public @ResponseBody String userAdd(User user, HttpSession session) {
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user2 = (User) session.getAttribute("user");
		if (user2 instanceof User) {
			if (userService.userAdd(user) != 0) {
				return String.valueOf(userService.userAdd(user));
			}
		}
		return "0";

	}

	// 显示所有用户
	@RequestMapping(value = "user_manage")
	public ModelAndView userManage(HttpSession session) {
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			List<User> users = userService.getUsermange();
			ModelAndView modelAndView = new ModelAndView(
					"view/UserManage/userShow");
			modelAndView.addObject("users", users);
			return modelAndView;
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}

	// 删除用户
	@RequestMapping(value = "user_del")
	public ModelAndView userDel(HttpSession session, int id) {
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user = (User) session.getAttribute("user");
		if (user instanceof User) {
			if (userService.userDel(id) != 0) {
				return new ModelAndView("redirect:user_manage");
			}
		}
		return new ModelAndView("redirect:/timeout.jsp");
	}

	// 通过id查询一条记录
	@RequestMapping(value = "user_get")
	public @ResponseBody String getUserById(HttpSession session, User user) {
		// 转换制定的数据源
		CustomerContextHolder
				.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user2 = (User) session.getAttribute("user");
		if (user2 instanceof User) {
			User user3 = userService.getUserById(user);
			String user_json = JSON.toJSONString(user3);
			return user_json;
		}
		return "redirect:/timeout.jsp";
	}

	// 添加用户
	@RequestMapping(value = "user_edit")
	public @ResponseBody String userEdit(User user, HttpSession session) {
		// 转换制定的数据源
		CustomerContextHolder.setCustomerType(CustomerContextHolder.DATA_SOURCE_DEFAULT);
		User user2 = (User) session.getAttribute("user");
		if (user2 instanceof User) {
			if (userService.userEdit(user) != 0) {
				return String.valueOf(userService.userEdit(user));
			}
		}
		return "0";

	}

	/************************************ Get Set 方法存放处 *******************************/

	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
