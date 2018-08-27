package com.org.pack;

import java.io.Serializable;

public class Resp implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int code;
	private String desc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Resp [ID =" + id + ", code =" + code
				+ ", desc=" + desc + "]";
   }
}
