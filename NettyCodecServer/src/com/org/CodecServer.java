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
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    // .handler(new LoggingHandler(LogLevel.INFO)) // 这个日志在跟踪问题的时候可以打开
                    .childHandler(new ChannelInitializer() {
                        @Override
                        public void initChannel(Channel ch) {
                            //通过工厂类创建MarshallingEncoder解码器，并添加到ChannelPipeline.
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            //通过工厂类创建MarshallingEncoder编码器，并添加到ChannelPipeline中。
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new IOServerHandler());
                        }
                    });

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();

            // 等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new CodecServer().bind(PORT);
    }

}
