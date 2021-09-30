package cn.itcast.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cn.itcast.dao.UserDao;
import cn.itcast.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component("userDao")
public class UserDaoimpl implements UserDao {

	private JdbcTemplate jdbcTemplate;

	@Override
	public User login(String uname, String upassword) {
		List<User> users = jdbcTemplate.query(
				"SELECT * FROM tb_user WHERE uname = ? and upassword=? ",
				new Object[] { uname, upassword }, new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User();
						user.setId(rs.getInt("id"));
						user.setUname(rs.getString("uname"));
						user.setUpassword(rs.getString("upassword"));
						user.setRole(rs.getString("role"));
						user.setNWOperator(rs.getString("NWOperator"));
						return user;
					}
				});
		return users.size() != 0 ? users.get(0) : null;
	}

	// 用户添加
	public int userAdd(User user) {
		return jdbcTemplate
				.update("insert into tb_user(uname,upassword,role,NWOperator) values(?,?,?,?)",
						new Object[] { user.getUname(), user.getUpassword(),
								user.getRole(), user.getNWOperator() });
	}

	// 用户管理
	public List<User> getUserManage() {
		String sql = "select * from tb_user";
		return jdbcTemplate.query(sql, new RowMapper<User>() {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setUname(rs.getString("uname"));
				user.setUpassword(rs.getString("upassword"));
				user.setRole(rs.getString("role"));
				user.setNWOperator(rs.getString("NWOperator"));
				return user;
			}
		});
	}

	// 用户删除
	public int userDel(int id) {
		return jdbcTemplate.update("delete from tb_user where id=?",
				new Object[] { id });
	}

	// 通过ID查询记录
	public User getUserById(User user) {
		return jdbcTemplate.queryForObject(
				"select * from tb_user where id = ?",
				new Object[] { user.getId() }, new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						User user = new User();
						user.setId(rs.getInt("id"));
						user.setUname(rs.getString("uname"));
						user.setUpassword(rs.getString("upassword"));
						user.setRole(rs.getString("role"));
						user.setNWOperator(rs.getString("NWOperator"));
						return user;
					}

				});
	}

	// 用户修改
	public int userEdit(User user) {
		String sql = "UPDATE tb_user SET uname=?,upassword=?,role=?,NWOperator=? WHERE id=?";
		return jdbcTemplate.update(sql,
				new Object[] { user.getUname(), user.getUpassword(),
						user.getRole(), user.getNWOperator(), user.getId() });
	}

	/******************************** GET SET 方法存放处 *************************************/
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
