package com.pi.proxy.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Proxy {

    @SerializedName("time")
    private Long time;
    @SerializedName("machine_id")
    private String machineId;

}