package com.pi.proxy.job.alert.result;

import com.pi.proxy.job.alert.result.AlertResult;

public abstract class PositiveAlertResult implements AlertResult {

    private final String name;
    private final String machineId;

    public PositiveAlertResult(String name, String machineId) {
        this.name = name;
        this.machineId = machineId;
    }

    @Override
    public Boolean shouldAlert() {
        return true;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String machineId() {
        return machineId;
    }

}
