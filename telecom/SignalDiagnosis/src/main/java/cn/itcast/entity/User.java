package cn.itcast.entity;

public class User {
	
	private int id;
	private String uname;
	private String upassword;
	private String role;
	private String NWOperator;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	public String getNWOperator() {
		return NWOperator;
	}
	public void setNWOperator(String nWOperator) {
		NWOperator = nWOperator;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", uname=" + uname + ", upassword="
				+ upassword + ", role=" + role + ", NWOperator=" + NWOperator
				+ "]";
	}
	
	
}
