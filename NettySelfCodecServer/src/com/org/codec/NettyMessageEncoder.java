package com.org.codec;

import java.io.IOException;

import com.org.marshall.MarshallingEncoder;
import com.org.pack.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder  extends MessageToByteEncoder<NettyMessage> {

    private MarshallingEncoder marshallingEncoder;
    
    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }
    
    
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage message, ByteBuf sendBuf) throws Exception {
        if(message == null || message.getHeader() == null){
            throw new Exception("����ʧ��,û��������Ϣ!");
        }
        
        //Head:
        sendBuf.writeInt(message.getHeader().getCrcCode());
        sendBuf.writeInt(message.getHeader().getLength());
        sendBuf.writeLong(message.getHeader().getSessionID());
        sendBuf.writeByte(message.getHeader().getType());
        sendBuf.writeByte(message.getHeader().getPriority());
        
        //Body:
        Object body = message.getBody();
        //�����Ϊ�� ˵��: ������
        if(body != null){
            //ʹ��MarshallingEncoder
            this.marshallingEncoder.encode(body, sendBuf);
        } else {
            //���û������ ����в�λ Ϊ�˷�������� decoder����
            sendBuf.writeInt(0);
        }        
        //�������Ҫ��ȡ�������ݰ����ܳ��� Ҳ���� header +  body ���ж� header length������        
        // TODO:  ���ͣ� ���������Ҫ-8���ֽڣ�4����ʼ��λ��
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);        
    }

}