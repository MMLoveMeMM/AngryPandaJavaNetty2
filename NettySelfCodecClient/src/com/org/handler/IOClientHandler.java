package com.org.handler;

import com.org.pack.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class IOClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            
            NettyMessage message = (NettyMessage)msg;
            System.err.println("Client: " + message.getBody());
            
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.err.println("----------客户端数据读异常-----------");
        ctx.close();
    }
    
}
