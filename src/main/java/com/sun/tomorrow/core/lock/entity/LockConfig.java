package com.sun.tomorrow.core.lock.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LockConfig {

    private String host;
    private String port;
    private Integer sessionTimeOut;

}
