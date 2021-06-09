package com.pi.proxy.job.alert.result;

public interface AlertResult {

    Boolean shouldAlert();

    String name();

    String machineId();

    String json();

}
