package com.org;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
 
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
 
public class HeartBeatServer {
    
	private final static int PORT = 8010;
    
    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap()
            		.group(bossGroup,workerGroup)
            		.channel(NioServerSocketChannel.class)
            		.handler(new LoggingHandler(LogLevel.INFO))
            		.localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                        	/* readerIdleTime�����г�ʱʱ���趨�����channelRead()��������readerIdleTimeʱ��δ��������ᴥ����ʱ�¼�����userEventTrigger()����;
                            writerIdleTimeд���г�ʱʱ���趨�����write()��������writerIdleTimeʱ��δ��������ᴥ����ʱ�¼�����userEventTrigger()����;
                            allIdleTime�������͵Ŀ��г�ʱʱ���趨�����������к�д����;
                            unitʱ�䵥λ������ʱ�����; */
                            ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new IOHeartBeatServerHandler());
                        };
                        
                    }).option(ChannelOption.SO_BACKLOG, 128)   
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
             // �󶨶˿ڣ���ʼ���ս���������
             ChannelFuture future = sbs.bind(PORT).sync();  
             
             System.out.println("Server start listen at " + PORT );
             future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new HeartBeatServer().start();
    }
 
}