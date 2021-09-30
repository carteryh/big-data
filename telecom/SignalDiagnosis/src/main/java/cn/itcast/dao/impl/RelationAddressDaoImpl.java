package cn.itcast.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import cn.itcast.dao.RelationAddressDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import cn.itcast.entity.NavigationAddress;
import cn.itcast.entity.RelationAddress;
import cn.itcast.entity.User;

@Component("relationAddressDao")
public class RelationAddressDaoImpl implements RelationAddressDao {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Override
	public List<RelationAddress> showAllAddress(User user) {
		
	List<RelationAddress> relationAddresses=jdbcTemplate.query("select  did,name,address,category,iconaddress,credate from relation_address where uid=?;", new Object[]{user.getId()}, new RowMapper<RelationAddress>(){

			@Override
			public RelationAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
				RelationAddress relationAddress=new RelationAddress();
				relationAddress.setDid(rs.getInt("did"));
				relationAddress.setName(rs.getString("name"));
				relationAddress.setAddress(rs.getString("address"));
				relationAddress.setCategory(rs.getInt("category"));
				relationAddress.setIconaddress(rs.getString("iconaddress"));
				relationAddress.setCredate(rs.getString("credate"));
				
				return relationAddress;
			}});
		 return relationAddresses;
	}

	@Override
	public List<NavigationAddress> showAllNavAddresses(User user) {
		List<NavigationAddress> navigationAddresses=jdbcTemplate.query("select  nid,name,uid,credate from navigation_address where uid=?;", new Object[]{user.getId()}, new RowMapper<NavigationAddress>(){

			@Override
			public NavigationAddress mapRow(ResultSet rs, int rowNum) throws SQLException {
				NavigationAddress navigationAddress=new NavigationAddress();
				navigationAddress.setNid(rs.getInt("nid"));
				navigationAddress.setNvaName(rs.getString("name"));
				navigationAddress.setCredate(rs.getString("credate"));
				navigationAddress.setUid(rs.getInt("uid"));
				return navigationAddress;
			}});
		 return navigationAddresses;
	}

	@Override
	public int deleteAdress(User user, RelationAddress relationAddress) {
		int rs=jdbcTemplate.update("delete  from relation_address where did=?", relationAddress.getDid());

		return rs;
	}

	@Override
	public int addAdress(User user, RelationAddress relationAddress) {
		System.out.println(relationAddress.getName()+" "+relationAddress.getAddress()+" "+relationAddress.getCategory());
		
		System.out.println(relationAddress.getName()+" "+relationAddress.getAddress()+" "+relationAddress.getCredate()+" "+relationAddress.getCategory()+" "+user.getId());
		
		int rs=jdbcTemplate.update("insert into relation_address  values(?,?,?,?,?,?,?)", new Object[]{0,relationAddress.getName(),relationAddress.getAddress(),relationAddress.getCredate(),"",relationAddress.getCategory(),user.getId()});

		System.out.println(rs);
		return rs;
	}

	@Override
	public int editRelaAddress(User user, RelationAddress relationAddress) {
		int rs=jdbcTemplate.update("update  relation_address set name=?,address=? where did=?", new Object[]{relationAddress.getName(),relationAddress.getAddress(),relationAddress.getDid()});

		return rs;
	}

	@Override
	public int deleteNaviAdress(User user, NavigationAddress navigationAddress) {

		int rs=jdbcTemplate.update("delete  from navigation_address where nid=?", navigationAddress.getNid());

		if (rs==1) {
			int rs2=jdbcTemplate.update("update  relation_address set category=0 where category=?", navigationAddress.getNid());

			return rs;
		}
		return 0;
	}

}
