package com.pi.proxy.job.alert;

import com.google.gson.Gson;
import com.pi.proxy.job.alert.result.AlertResult;
import com.pi.proxy.job.alert.result.PositiveAlertResult;
import com.pi.proxy.model.Proxy;
import org.apache.flink.api.common.state.ValueState;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DnsStatusChecker {

    private transient final ValueState<Integer> valueState;

    public DnsStatusChecker(ValueState<Integer> valueState) {
        // even if not used
        this.valueState = valueState;
    }

    public AlertResult alertResult(Proxy p) throws Exception {
        return new PositiveAlertResult("dns_status", p.getMachineId()) {
            @Override
            public String json() {
                Map<String, Object> m = new HashMap<>();
                m.put("timestamp", new Date().getTime() / 1000);
                return new Gson().toJson(m);
            }
        };
    }

}
