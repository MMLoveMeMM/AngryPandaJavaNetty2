package com.org.pack;

import java.io.Serializable;

public class Req implements Serializable{
	/**
     * Ĭ�ϵ����к�ID
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String identityID;

    private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentityID() {
		return identityID;
	}

	public void setIdentityID(String identityID) {
		this.identityID = identityID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public String toString() {
        return "SubscribeReq [ID =" + id + ", identityID =" + identityID
                + ", address=" + address + "]";
    }
}