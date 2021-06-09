package com.pi.proxy.job.alert;

import com.pi.proxy.job.alert.result.AlertResult;
import com.pi.proxy.model.Proxy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.util.ArrayList;
import java.util.List;

public class AlertKeyedProcessFunction extends KeyedProcessFunction<String, Proxy, AlertResult> {

    private transient List<DnsStatusChecker> checkers;

    public AlertKeyedProcessFunction() {
    }

    @Override
    public void open(Configuration parameters) {
        ValueState<Integer> dnsStatusCntState = getRuntimeContext().getState(new ValueStateDescriptor<>("dnsStatusCnt", Integer.class));
        checkers = new ArrayList<>();
        checkers.add(new DnsStatusChecker(dnsStatusCntState));
        checkers.add(new DnsStatusChecker(dnsStatusCntState));
        checkers.add(new DnsStatusChecker(dnsStatusCntState));
        checkers.add(new DnsStatusChecker(dnsStatusCntState));
        checkers.add(new DnsStatusChecker(dnsStatusCntState));
    }

    @Override
    public void processElement(Proxy value, Context ctx, Collector<AlertResult> out) throws Exception {
        for (DnsStatusChecker checker : checkers) {
            out.collect(checker.alertResult(value));
        }
    }

    @Override
    public void close() throws Exception {
    }

}
