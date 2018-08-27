package com.org.marshall;

import java.io.IOException;

import org.jboss.marshalling.Unmarshaller;

import io.netty.buffer.ByteBuf;

public class MarshallingDecoder {
    
    private Unmarshaller unmarshaller;

    public MarshallingDecoder() throws IOException{
        this.unmarshaller = MarshallingCodeCFactory.buildUnMarshalling();
    }
    
    public Object decode(ByteBuf in) throws Exception {
        try {
            //1 首先读取4个长度(实际body内容长度)，因为在编码的时候设置了一个4个字节的站位付，所以这个时候要加上一个4个字节的站位符作为重新计算长度的。
            int bodySize = in.readInt();
            //2 获取实际body的缓冲内容，因为这个readerIndex的索引，是因为前面的
        /*    header.setCrcCode(frame.readInt());        //crcCode ----> 添加通信标记认证逻辑
            header.setLength(frame.readInt());        //length
            header.setSessionID(frame.readLong());    //sessionID
            header.setType(frame.readByte());        //type
            header.setPriority(frame.readByte());    //priority
            这些已经把游标给移动了，所以这个时候就会是现在的索引值.*/
            //slice这个方法
            ByteBuf buf = in.slice(in.readerIndex(), bodySize);
            //3 转换
            ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
            //4 读取操作:
            this.unmarshaller.start(input);
            Object ret = this.unmarshaller.readObject();
            this.unmarshaller.finish();
            //5 读取完毕以后, 更新当前读取起始位置:  因为slice的方法只会读取内容，而不会修改索引值的，所以读完之后要修改索引值的。
            in.readerIndex(in.readerIndex() + bodySize);
            
            return ret;
            
        } finally {
            this.unmarshaller.close();
        }
    }

}