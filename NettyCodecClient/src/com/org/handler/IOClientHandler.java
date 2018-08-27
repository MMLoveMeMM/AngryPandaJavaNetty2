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
		//���ڶ���������Ѿ��Զ�������Ӧ����Ϣ�������Զ����룬
        //��ˣ�SubReqClientHandler���յ�����Ϣ�Ѿ��ǽ���ɹ���Ķ���Ӧ����Ϣ��
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
        //����·�����ʱ��ѭ������10������������Ϣ�����һ���Եط��͸�����ˡ�
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
        req.setAddress("���ڸ�����ɳʮ��"+i+"��");
        req.setIdentityID("430**************");
        req.setId(i);
        return req;
    }
}