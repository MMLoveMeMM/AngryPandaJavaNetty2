package com.org.handler;

import com.org.pack.Req;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IOClientHandler extends ChannelInboundHandlerAdapter{

    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(ctx, msg);
		//由于对象解码器已经对订购请求应答消息进行了自动解码，
        //因此，SubReqClientHandler接收到的消息已经是解码成功后的订购应答消息。
        System.out.println("Receive server response : [" + msg + "]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
		ctx.flush();
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) {
        //在链路激活的时候循环构造10条订购请求消息，最后一次性地发送给服务端。
		System.out.println("channelActive");
        for (int i = 1; i < 10; i++) {
        	System.out.println("channelActive i : "+i);
        	ctx.channel().write(makeReq(i));
            // ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    private Req makeReq(int i) {
        Req req = new Req();
        req.setAddress("深圳福田上沙十巷"+i+"号");
        req.setIdentityID("430**************");
        req.setId(i);
        return req;
    }
}
