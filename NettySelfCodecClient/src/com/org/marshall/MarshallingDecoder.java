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
            //1 ���ȶ�ȡ4������(ʵ��body���ݳ���)����Ϊ�ڱ����ʱ��������һ��4���ֽڵ�վλ�����������ʱ��Ҫ����һ��4���ֽڵ�վλ����Ϊ���¼��㳤�ȵġ�
            int bodySize = in.readInt();
            //2 ��ȡʵ��body�Ļ������ݣ���Ϊ���readerIndex������������Ϊǰ���
        /*    header.setCrcCode(frame.readInt());        //crcCode ----> ����ͨ�ű����֤�߼�
            header.setLength(frame.readInt());        //length
            header.setSessionID(frame.readLong());    //sessionID
            header.setType(frame.readByte());        //type
            header.setPriority(frame.readByte());    //priority
            ��Щ�Ѿ����α���ƶ��ˣ��������ʱ��ͻ������ڵ�����ֵ.*/
            //slice�������
            ByteBuf buf = in.slice(in.readerIndex(), bodySize);
            //3 ת��
            ChannelBufferByteInput input = new ChannelBufferByteInput(buf);
            //4 ��ȡ����:
            this.unmarshaller.start(input);
            Object ret = this.unmarshaller.readObject();
            this.unmarshaller.finish();
            //5 ��ȡ����Ժ�, ���µ�ǰ��ȡ��ʼλ��:  ��Ϊslice�ķ���ֻ���ȡ���ݣ��������޸�����ֵ�ģ����Զ���֮��Ҫ�޸�����ֵ�ġ�
            in.readerIndex(in.readerIndex() + bodySize);
            
            return ret;
            
        } finally {
            this.unmarshaller.close();
        }
    }

}