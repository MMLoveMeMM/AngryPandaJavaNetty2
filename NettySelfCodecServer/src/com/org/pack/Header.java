package com.org.pack;

public class Header {

    private int crcCode = 0xadaf0105; //Ψһ��ͨ�ű�־ 
    
    private int length; //����Ϣ�ĳ���  header + body 
    
    private long sessionID;     //�ỰID
    
    private byte type ;        //��Ϣ������
    
    private byte priority;    //��Ϣ�����ȼ�    
    
    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

}