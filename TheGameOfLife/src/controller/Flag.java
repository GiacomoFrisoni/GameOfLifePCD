package controller;

public class Flag {
	
	private boolean status;
	
	public Flag() {
		this.status = false;
	}
	
	public synchronized void setOn() {
		this.status = true;
	}
	
	public synchronized void setOff() {
		this.status = false;
	}
	
	public synchronized boolean isOn() {
		return this.status;
	}
	
}
