package com.org.codec;

import java.io.IOException;

import com.org.marshall.MarshallingDecoder;
import com.org.pack.Header;
import com.org.pack.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * LengthFieldBasedFrameDecoder ��Ϊ�˽�� ���ճ���������
 * @author Alienware
 *
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private MarshallingDecoder marshallingDecoder;
    
    /**
     * �Ǽ�8Ӧ������ΪҪ��CRC�ͳ��ȱ���ռ�ļ����ˡ��Ҳ´�û
     * @param maxFrameLength ��һ�������������ĳ���   1024*1024*5
     * @param lengthFieldOffset �����������Ե�ƫ���� ����˵����message�� �ܳ��ȵ���ʼλ��   4
     * @param lengthFieldLength �����������Եĳ��� ��������ռ�೤  4
     * @throws IOException 
     */
    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.marshallingDecoder = new MarshallingDecoder();
    }
    
    @Override
    protected Object decode(ChannelHandlerContext ctx,  ByteBuf in) throws Exception {
        //1 ���ø���(LengthFieldBasedFrameDecoder)����:
        ByteBuf frame  = (ByteBuf)super.decode(ctx, in);
        
        if(frame == null){
            return null;
        }
        
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());        //crcCode ----> ����ͨ�ű����֤�߼�
        header.setLength(frame.readInt());        //length
        header.setSessionID(frame.readLong());    //sessionID
        header.setType(frame.readByte());        //type
        header.setPriority(frame.readByte());    //priority
        
        message.setHeader(header);
        
        if(frame.readableBytes() > 4) {
            message.setBody(marshallingDecoder.decode(frame));
        }
        return message;
    }

}