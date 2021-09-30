package cn.itcast.service;

import java.util.List;

import javax.annotation.Resource;

import cn.itcast.dao.UserDao;
import org.springframework.stereotype.Component;

import cn.itcast.entity.User;
@Component("userService")
public class UserService {

	private UserDao userDao;

	public User login(String uname, String upassword) {
		return userDao.login(uname, upassword);
	}
	
	//用户添加
	public int userAdd(User user) {
		return userDao.userAdd(user);
	}
	//用户管理
	public List<User> getUsermange() {
		return userDao.getUserManage();
	}
	
	//用户删除
	public int userDel(int id) {
		return userDao.userDel(id);
	}
	
	//通过ID查询一条记录
	public User getUserById(User user) {
		return userDao.getUserById(user);
	}
	//用户修改
	public int userEdit(User user) {
		return userDao.userEdit(user);
	}
	
/*********************************GET SET 方法存放处***********************************/
@Resource
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}


}
