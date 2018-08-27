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
        //����������handler ObjectDecoder�Ľ��룬
        //SubReqServerHandler���յ���������Ϣ�Ѿ����Զ�����ΪSubscribeReq���󣬿���ֱ��ʹ�á�
        Req req = (Req) msg;
        System.out.println("user name : "+req.getAddress());
        if (req.getIdentityID().contains("430*")) {
            System.out.println("Service accept client subscribe req : ["
                    + req.toString() + "]");
            //�Զ����ߵ��û������кϷ���У�飬У��ͨ�����ӡ����������Ϣ�����충���ɹ�Ӧ����Ϣ�������͸��ͻ��ˡ�
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
        ctx.close();// �����쳣���ر���·
    }
	
}
