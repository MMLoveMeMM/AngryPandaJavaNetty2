package com.org;

import com.org.codec.NettyMessageDecoder;
import com.org.codec.NettyMessageEncoder;
import com.org.handler.IOServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    
    public static void main(String[] args) throws Exception {
        //ONE:
        //1 ���ڽ��ܿͻ������ӵ��̹߳�����
        EventLoopGroup boss = new NioEventLoopGroup();
        //2 ���ڶԽ��ܿͻ������Ӷ�д�������̹߳�����
        EventLoopGroup work = new NioEventLoopGroup();
        
        //TWO:
        //3 �����ࡣ���ڰ������Ǵ���NETTY����
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, work)    //�����������߳���
         .channel(NioServerSocketChannel.class)    //����NIO��ģʽ
         .option(ChannelOption.SO_BACKLOG, 1024)    //����TCP������
         //.option(ChannelOption.SO_SNDBUF, 32*1024)    // ���÷������ݵĻ����С
         .option(ChannelOption.SO_RCVBUF, 32*1024)    // ���ý������ݵĻ����С
         .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)    // ���ñ�������
         .childOption(ChannelOption.SO_SNDBUF, 32*1024)
         // ��ʼ���󶨷���ͨ��
         .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new NettyMessageDecoder(1024*1024*5, 4, 4));
                sc.pipeline().addLast(new NettyMessageEncoder());
                sc.pipeline().addLast(new IOServerHandler());
            }
         });
        
        ChannelFuture cf = b.bind(8765).sync();
        
        System.err.println("Server start... ");
        
        //�ͷ�����
        cf.channel().closeFuture().sync();
        work.shutdownGracefully();
        boss.shutdownGracefully();
    }
}