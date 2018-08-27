package com.org.marshall;

import java.io.IOException;
import org.jboss.marshalling.Marshaller;
import io.netty.buffer.ByteBuf;

public class MarshallingEncoder {
    
    //�հ�ռλ: ����Ԥ������ body�����ݰ����� 
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    
    private Marshaller marshaller;
    
    public MarshallingEncoder() throws IOException {
        this.marshaller = MarshallingCodeCFactory.buildMarshalling();
    }

    public void encode(Object body, ByteBuf out) throws IOException {
        try {
            //����Ҫ֪����ǰ������λ������: ��ʼ����λ��
            int pos = out.writerIndex();
            //ռλд����:
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(body);
            marshaller.finish();
            //�ܳ���(����λ��) - ��ʼ������(��ʼλ��) - Ԥ���ĳ���  = body���ݳ���
            //pos����body����ʼ��λ��
            out.setInt(pos, out.writerIndex() - pos - 4);
            
        } finally {
            marshaller.close();
        }
        
        
    }

}