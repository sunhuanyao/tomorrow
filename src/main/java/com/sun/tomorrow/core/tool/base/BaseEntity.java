package com.sun.tomorrow.core.tool.base;

import java.io.Serializable;

public abstract class BaseEntity {

    /**
     *  < 0 小于 = 0 相等  > 0 大于
     * @return
     */
    public abstract int compareTo(BaseEntity baseEntity);
}
