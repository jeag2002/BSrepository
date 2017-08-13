package com.bs.analysis.cashanalysistool.driver.provider;

public class CashAnalysisBean{
	private String data;
	private long timestamp;
	
	public CashAnalysisBean(){
		this.data = "";
		this.timestamp = 0;
	}
	
	public CashAnalysisBean(String data, long timestamp){
		this.data = data;
		this.timestamp = timestamp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}	
}
