package com.org;

import com.org.handler.IOClientHandler;
import com.org.marshalling.MarshallingCodeCFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CodecClient {
	private final static int PORT = 8009;
	private final static String HOST_IP = "127.0.0.1";
	
	public void connect(int port, String host) throws Exception {
        // ���ÿͻ���NIO�߳���
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer() {
                        @Override
                        public void initChannel(Channel ch)
                                throws Exception {
                        	ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            ch.pipeline().addLast(new IOClientHandler());
                        }
                    });

            // �����첽���Ӳ���
            ChannelFuture f = b.connect(host, port).sync();

            // �ȴ��ͻ�����·�ر�
            f.channel().closeFuture().sync();
        } finally {
            // �����˳����ͷ�NIO�߳���
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new CodecClient().connect(PORT, HOST_IP);
    }
    
}