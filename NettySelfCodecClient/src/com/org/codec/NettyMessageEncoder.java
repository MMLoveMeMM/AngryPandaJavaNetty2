package com.org.codec;

import java.io.IOException;

import com.org.marshall.MarshallingEncoder;
import com.org.pack.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class NettyMessageEncoder  extends MessageToByteEncoder<NettyMessage> {

    private MarshallingEncoder marshallingEncoder;
    
    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = new MarshallingEncoder();
    }
    
    
    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage message, ByteBuf sendBuf) throws Exception {
        if(message == null || message.getHeader() == null){
            throw new Exception("编码失败,没有数据信息!");
        }
        
        //Head:
        sendBuf.writeInt(message.getHeader().getCrcCode());
        sendBuf.writeInt(message.getHeader().getLength());
        sendBuf.writeLong(message.getHeader().getSessionID());
        sendBuf.writeByte(message.getHeader().getType());
        sendBuf.writeByte(message.getHeader().getPriority());
        
        //Body:
        Object body = message.getBody();
        //如果不为空 说明: 有数据
        if(body != null){
            //使用MarshallingEncoder
            this.marshallingEncoder.encode(body, sendBuf);
        } else {
            //如果没有数据 则进行补位 为了方便后续的 decoder操作
            sendBuf.writeInt(0);
        }        
        //最后我们要获取整个数据包的总长度 也就是 header +  body 进行对 header length的设置        
        // TODO:  解释： 在这里必须要-8个字节，4是起始的位置
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);        
    }

}