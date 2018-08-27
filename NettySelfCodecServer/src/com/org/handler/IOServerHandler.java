package com.org.handler;

import com.org.pack.Header;
import com.org.pack.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IOServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * ������ͨ�����м����ʱ�� �����ļ�������
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    
        System.err.println("--------ͨ������------------");
    }
    
    /**
     * �����ǵ�ͨ���������ݽ��ж�ȡ��ʱ�� �����ļ�������
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx /*NETTY����������*/, Object msg /*ʵ�ʵĴ�������*/) throws Exception {
        
        NettyMessage requestMessage = (NettyMessage)msg;
        
        System.err.println("Server: " + requestMessage.getBody());

        NettyMessage responseMessage = new NettyMessage();
        Header header = new Header();
        header.setSessionID(2002L);
        header.setPriority((byte)2);
        header.setType((byte)2);
        responseMessage.setHeader(header);
        responseMessage.setBody("������Ӧ����: " + requestMessage.getBody());
        ctx.writeAndFlush(responseMessage);
        
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       System.err.println("--------���ݶ�ȡ���----------");
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.err.println("--------���������ݶ��쳣----------: ");
        cause.printStackTrace();
        ctx.close();
    }
    
}