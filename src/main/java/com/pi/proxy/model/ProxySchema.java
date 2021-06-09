package com.pi.proxy.model;

import com.google.gson.GsonBuilder;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class ProxySchema implements DeserializationSchema<Proxy> {

    public ProxySchema() {
    }

    @Override
    public Proxy deserialize(byte[] message) {
        String data = new String(message);
        Proxy proxy = new GsonBuilder()
                .create()
                .fromJson(data, Proxy.class);
        return proxy;
    }

    @Override
    public boolean isEndOfStream(Proxy nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Proxy> getProducedType() {
        return TypeExtractor.getForClass(Proxy.class);
    }

}
