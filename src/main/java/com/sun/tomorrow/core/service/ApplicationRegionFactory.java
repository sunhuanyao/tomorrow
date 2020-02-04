package com.sun.tomorrow.core.service;

import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.container.RTreeFactory;
import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;
import com.sun.tomorrow.core.tool.parseCore.DefaultXmlReader;
import com.sun.tomorrow.core.tool.parseCore.TReader;
import com.sun.tomorrow.core.util.PolygonUtil;

import java.util.*;

public class ApplicationRegionFactory implements RegionFactory{

    private static final String DEFAULT_TYPE = "xml";

    private TReader tReader;
    //xml or other
    private final String type;

    //地图信息文件-----
    private String sourceName;

    private RTreeFactory<RegionInfo> regionInfoRTreeFactory;


    private ClassLoader classLoader;

    public ApplicationRegionFactory(String sourceName){
        this(DEFAULT_TYPE, sourceName);
    }


    public ApplicationRegionFactory(String type, String sourceName){
        if(type == null) {
            this.type = DEFAULT_TYPE;
        }else{
            this.type = type;
        }
        this.sourceName = sourceName;
//        if(loader != null){
//            this.classLoader = loader;
//        }else{
//            this.classLoader = Thread.currentThread().getContextClassLoader();
//        }
        switch (this.type){
            case TReader.xmlReader:
                this.tReader = new DefaultXmlReader(new TRsource(this.sourceName));
                break;
            default:
                break;
        }
        solve();
    }

    public void solve(){

        List<RegionInfo> regionInfos = this.tReader.parseResource();

        RTreeFactory<RegionInfo> regionInfoRTreeFactory = new RTreeFactory<>();
        regionInfoRTreeFactory.init();

        for(int i = 0 ; i < regionInfos.size(); ++ i) {
            regionInfoRTreeFactory.add(regionInfos.get(i), i + 1);
        }

        this.regionInfoRTreeFactory = regionInfoRTreeFactory;

    }

    public List<RegionInfo> getRegionInfo(Point point){
        List<RegionInfo> regionInfos = this.regionInfoRTreeFactory.queryLevel(point);

        List<RegionInfo> res = new ArrayList<>();
        for(RegionInfo regionInfo: regionInfos){
            if(PolygonUtil.IsPtInPoly(point, regionInfo.getPoints())){
                res.add(regionInfo);
            }
        }
        return res;

    }

    public static void main(String[] args){
        ApplicationRegionFactory applicationRegionFactory = new ApplicationRegionFactory("tomorrow_region.xml");
        List<RegionInfo> regionInfos = applicationRegionFactory.getRegionInfo(new Point(116.318319,40.603389));
        for(RegionInfo regionInfo: regionInfos){
            System.out.println(regionInfo.getName());
        }
    }




}
