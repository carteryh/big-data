package cn.itcast.dao;

import java.util.List;

import cn.itcast.entity.User;

public interface UserDao {
	User login(String uname, String upassword);

	// 用户添加
	public int userAdd(User user);

	// 用户管理
	public List<User> getUserManage();

	// 用户删除
	public int userDel(int id);

	// 通过ID查询记录
	public User getUserById(User user);

	// 用户修改
	public int userEdit(User user);

}
