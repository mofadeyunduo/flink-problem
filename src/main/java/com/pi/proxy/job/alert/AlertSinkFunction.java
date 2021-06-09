package com.pi.proxy.job.alert;

import com.pi.proxy.job.alert.result.AlertResult;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import redis.clients.jedis.Jedis;

public class AlertSinkFunction extends RichSinkFunction<AlertResult> {

    private transient Jedis jedis;

    public AlertSinkFunction() {
    }

    @Override
    public void open(Configuration parameters) {
        jedis = new Jedis();
    }

    @Override
    public void invoke(AlertResult value, Context context) throws InterruptedException {
        jedis.hset("alert:" + value.name(), value.machineId(), value.json());
    }

    @Override
    public void close() throws Exception {
        if (jedis != null) {
            jedis.close();
        }
    }

}
