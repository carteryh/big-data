package cn.itcast.entity;

public class TableCounts {
	private int id;
	private int NWQulity_count;
	private int Signal_Strength_count;
	private int DataConnection_count;
	private int dateTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNWQulity_count() {
		return NWQulity_count;
	}
	public void setNWQulity_count(int nWQulity_count) {
		NWQulity_count = nWQulity_count;
	}
	public int getSignal_Strength_count() {
		return Signal_Strength_count;
	}
	public void setSignal_Strength_count(int signal_Strength_count) {
		Signal_Strength_count = signal_Strength_count;
	}
	public int getDataConnection_count() {
		return DataConnection_count;
	}
	public void setDataConnection_count(int dataConnection_count) {
		DataConnection_count = dataConnection_count;
	}
	public int getDateTime() {
		return dateTime;
	}
	public void setDateTime(int dateTime) {
		this.dateTime = dateTime;
	}
	
	
}
