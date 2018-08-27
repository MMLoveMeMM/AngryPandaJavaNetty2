package com.org;

import com.org.handler.IOServerHandler;
import com.org.marshalling.MarshallingCodeCFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class CodecServer{
	
	private final static int PORT = 8009;

	public void bind(int port) throws Exception {
        // ���÷���˵�NIO�߳���
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    // .handler(new LoggingHandler(LogLevel.INFO)) // �����־�ڸ��������ʱ����Դ�
                    .childHandler(new ChannelInitializer() {
                        @Override
                        public void initChannel(Channel ch) {
                            //ͨ�������ഴ��MarshallingEncoder�������������ӵ�ChannelPipeline.
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            //ͨ�������ഴ��MarshallingEncoder�������������ӵ�ChannelPipeline�С�
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new IOServerHandler());
                        }
                    });

            // �󶨶˿ڣ�ͬ���ȴ��ɹ�
            ChannelFuture f = b.bind(port).sync();

            // �ȴ�����˼����˿ڹر�
            f.channel().closeFuture().sync();
        } finally {
            // �����˳����ͷ��̳߳���Դ
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new CodecServer().bind(PORT);
    }

}