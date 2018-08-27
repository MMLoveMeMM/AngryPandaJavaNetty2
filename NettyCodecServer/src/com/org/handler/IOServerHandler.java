package com.org.handler;

import com.org.pack.Req;
import com.org.pack.Resp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class IOServerHandler extends ChannelInboundHandlerAdapter{

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //经过解码器handler ObjectDecoder的解码，
        //SubReqServerHandler接收到的请求消息已经被自动解码为SubscribeReq对象，可以直接使用。
        Req req = (Req) msg;
        System.out.println("user name : "+req.getAddress());
        if (req.getIdentityID().contains("430*")) {
            System.out.println("Service accept client subscribe req : ["
                    + req.toString() + "]");
            //对订购者的用户名进行合法性校验，校验通过后打印订购请求消息，构造订购成功应答消息立即发送给客户端。
            ctx.writeAndFlush(makeResp(req.getId()));
        }
    }

    private Resp makeResp(int id) {
        Resp resp = new Resp();
        resp.setId(id);
        resp.setCode(1);
        resp.setDesc("how to using self-codec in netty");
        return resp;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();// 发生异常，关闭链路
    }
	
}
