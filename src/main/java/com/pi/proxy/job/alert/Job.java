package com.pi.proxy.job.alert;

import com.pi.proxy.model.Proxy;
import com.pi.proxy.model.ProxySchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Job {

    public static void mockData() {
        Properties kafkaProperties = new Properties();
        kafkaProperties.put("bootstrap.servers", "localhost:9092");
        kafkaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(kafkaProperties);
        String jsonTemplate = "{\"time\": 1, \"machine_id\": \"$random\"}";
        new Thread(() -> {
            while (true) {
                for (int i = 0; i < 1000; i++) {
                    ProducerRecord<String, String> record = new ProducerRecord<>("hello3", jsonTemplate.replace("$random", Integer.toString(i)));
                    producer.send(record);
                    try {
                        // if send fast, then fail fast
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        //
        mockData();
        // start flink
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "hello");
        FlinkKafkaConsumer<Proxy> consumer = new FlinkKafkaConsumer<>("hello3", new ProxySchema(), properties);
        consumer.setStartFromGroupOffsets();
        // flink
        Configuration configuration = new Configuration();
        configuration.setString("taskmanager.memory.managed.size", "512m");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(1, configuration);
        EmbeddedRocksDBStateBackend backend = new EmbeddedRocksDBStateBackend();
        env.setStateBackend(backend);
        env.enableCheckpointing(60000);
        env
                .addSource(consumer)
                .keyBy(Proxy::getMachineId)
                .process(new AlertKeyedProcessFunction())
                .addSink(new AlertSinkFunction());
        env.execute("hello");
    }

}
