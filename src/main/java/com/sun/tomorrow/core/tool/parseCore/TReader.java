package com.sun.tomorrow.core.tool.parseCore;


import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;

import java.util.List;


public interface TReader {
    public static final String MIDDIR = "/src/main/resources/";
    /**
     * 获取 区域信息
     * @return 返回读取后区域列表
     */
    public List<RegionInfo> parseResource();

}
