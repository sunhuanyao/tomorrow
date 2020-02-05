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
    /**
     * 数据源文件的读取类。该类有不同的读取方式。根据type决定。
     */
    private TReader tReader;
    /**
     * 地图信息文件名 : 默认放在当前项目的resources下面 才能正确识别
     */
    private String sourceName;

    /**
     * 区域信息空间索引工厂类
     */
    private RTreeFactory<RegionInfo> regionInfoRTreeFactory;

    public ApplicationRegionFactory(){
        this(new DefaultXmlReader(null), null);
    }

    public ApplicationRegionFactory(String sourceName){
        this(new DefaultXmlReader(new TRsource(sourceName)), sourceName);
    }

    public ApplicationRegionFactory(TReader tReader, String sourceName){

        this.sourceName = sourceName;

        this.tReader = tReader;

        solve();
    }

    /**
     * 1.读取数据源;
     * 2.预初始化数据区域空间索引工厂类；
     * 3.初始化区域信息。
     */
    public void solve(){

        List<RegionInfo> regionInfos = this.tReader.parseResource();

        RTreeFactory<RegionInfo> regionInfoRTreeFactory = new RTreeFactory<>();
        regionInfoRTreeFactory.init();

        for(int i = 0 ; i < regionInfos.size(); ++ i) {
            regionInfoRTreeFactory.add(regionInfos.get(i), i + 1);
        }

        this.regionInfoRTreeFactory = regionInfoRTreeFactory;

    }

    /**
     * 通过经纬度点得到对应的区域信息，并返回。
     * @param point    给定的经纬度点
     * @return         对应的区域信息
     */
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

    /**
     * 测试入口
     * @param args --
     */
    public static void main(String[] args){
        ApplicationRegionFactory applicationRegionFactory = new ApplicationRegionFactory("tomorrow_region.xml");
        List<RegionInfo> regionInfos = applicationRegionFactory.getRegionInfo(new Point(116.318319,40.603389));
        for(RegionInfo regionInfo: regionInfos){
            System.out.println(regionInfo.getName());
        }
    }




}
