package com.org;

import com.org.codec.NettyMessageDecoder;
import com.org.codec.NettyMessageEncoder;
import com.org.handler.IOClientHandler;
import com.org.pack.Header;
import com.org.pack.NettyMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	private final static String HOST_IP = "127.0.0.1";
	private final static int HOST = 8765;
	
    public static void main(String[] args) throws Exception {
        //ONE:
        //1 �̹߳�����
        EventLoopGroup work = new NioEventLoopGroup();
        
        //TWO:
        //3 �����ࡣ���ڰ������Ǵ���NETTY����
        Bootstrap b = new Bootstrap();
        b.group(work)    //�󶨹����߳���
         .channel(NioSocketChannel.class)    //����NIO��ģʽ
         // ��ʼ���󶨷���ͨ��
         .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new NettyMessageDecoder(1024*1024*5, 4, 4));
                sc.pipeline().addLast(new NettyMessageEncoder());
                sc.pipeline().addLast(new IOClientHandler());
            }
         });
        
        ChannelFuture cf =  b.connect(HOST_IP, HOST).syncUninterruptibly();
        
        System.out.println("client start....");
        
        Channel c = cf.channel();
        
        for(int i = 0; i < 50; i ++){
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setSessionID(1001L);
            header.setPriority((byte)1);
            header.setType((byte)1);
            message.setHeader(header);
            message.setBody("������������" + i);
            c.writeAndFlush(message);
        }
        
        //�ͷ�����
        cf.channel().closeFuture().sync();
        work.shutdownGracefully();
    }
}