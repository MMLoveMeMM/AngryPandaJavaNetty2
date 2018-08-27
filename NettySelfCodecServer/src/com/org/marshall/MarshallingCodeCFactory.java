package com.org.marshall;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

/**
 * Marshalling����
 * @author��alienware��
 * @since 2014-12-16
 */
public final class MarshallingCodeCFactory {

    /**
     * ����Jboss Marshalling������MarshallingDecoder
     * @return MarshallingDecoder
     * @throws IOException 
     */
    public static Marshaller buildMarshalling() throws IOException {
        //����ͨ��Marshalling������ľ�ͨ������ȡMarshallingʵ������ ����serial��ʶ��������java���л���������
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        //������MarshallingConfiguration���������˰汾��Ϊ5 
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }

    /**
     * ����Jboss Marshalling������MarshallingEncoder
     * @return MarshallingEncoder
     * @throws IOException 
     */
    public static Unmarshaller buildUnMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(configuration);
        return unmarshaller;
    }
}