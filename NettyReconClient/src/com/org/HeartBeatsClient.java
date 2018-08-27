package com.org;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
 
import java.util.concurrent.TimeUnit;
/*
 * 这个重连比较和状态机管制的重新连接的区别
 * */
public class HeartBeatsClient {
	
	private final static String HOST_IP = "127.0.0.1";
	private final static int PORT = 8011;
    
    protected final HashedWheelTimer timer = new HashedWheelTimer();
    
    private Bootstrap boot;
    
    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();
 
    public void connect(int port, String host) throws Exception {
        
        EventLoopGroup group = new NioEventLoopGroup();  
        
        boot = new Bootstrap();
        boot.group(group)
        .channel(NioSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO));
            
        final ConnectionWatchdog watchdog = new ConnectionWatchdog(boot, timer, port,host, true) {
        		/*
        		 * 将所有用到的Handler保存起来
        		 * 用的时候复用之
        		 * */
                public ChannelHandler[] handlers() {
                    return new ChannelHandler[] {
                            this,
                            new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS),
                            idleStateTrigger,
                            new StringDecoder(),
                            new StringEncoder(),
                            new IOHeartBeatClientHandler()
                    };
                }
            };
            
            ChannelFuture future;
            //进行连接
            try {
                synchronized (boot) {
                    boot.handler(new ChannelInitializer<Channel>() {
 
                        //初始化channel
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(watchdog.handlers());
                        }
                    });
 
                    future = boot.connect(host,port);
                }
 
                // 以下代码在synchronized同步块外面是安全的
                future.sync();
            } catch (Throwable t) {
                throw new Exception("connects to  fails", t);
            }
    }
 
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new HeartBeatsClient().connect(PORT, HOST_IP);
    }
 
}
