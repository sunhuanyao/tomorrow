package com.sun.tomorrow.core.tool.parseCore;

import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;
import com.sun.tomorrow.core.util.PathUtil;
import com.sun.tomorrow.core.util.exception.ResourceNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.UrlResource;

public class DefaultXmlRegionReader extends AbstractXmlReader {
    /**
     * xml的有固定的模版样式，可重写该类来进行解析。
     */
    private static final String FIELD_PIECEAREA = "Piecearea";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_RINGS = "rings";




    public DefaultXmlRegionReader(TRsource resource) {
        super(resource);
    }

    /**
     * 解析数据源
     * @return 返回解析的区域信息
     */
    public List<RegionInfo> parseResource(){

        try {
            Document document = getDocument();

            NodeList list = document.getElementsByTagName(FIELD_PIECEAREA);

            List<RegionInfo> regionInfos = new ArrayList<>();
            for(int i = 0 ; i < list.getLength(); ++ i){

                Element element = (Element) list.item(i);
                Element paElement = (Element) element.getParentNode();
                String name = paElement.getAttribute(FIELD_NAME) + " " + element.getAttribute(FIELD_NAME);
                Point[] points = parseMulPoly(element.getAttribute(FIELD_RINGS));
                if(points == null)continue;

                regionInfos.add(new RegionInfo(name, points));
            }
            return regionInfos;
        }catch (Exception e){
            //调试用
//            e.printStackTrace();
//            System.out.println("false:" + e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
    }



    /**
     * 解析经纬度点集
     * @param polyString 经纬度描述串
     * @return  返回解析完的点集
     */
    public Point[] parseMulPoly(String polyString){
        if(polyString == null || "".endsWith(polyString)) return null;
        String[] mulPointStrings = polyString.split(",|;");
        Point[] points = new Point[mulPointStrings.length];
        for(int i = 0 ; i < mulPointStrings.length; ++ i){
            String[] tmp = mulPointStrings[i].split(" ");
//            try {
            points[i] = new Point(Double.parseDouble(tmp[0]), Double.parseDouble(tmp[1]));
//            }catch (Exception e){
//                System.out.println(mulPointStrings[i] + "@");
//                throw e;
//            }

        }
        return points;
    }

    public static void main(String[] args){
//        TReader tReader = new DefaultXmlReader(new TRsource("test.xml"));
        DefaultXmlRegionReader defaultXmlRegionReader = new DefaultXmlRegionReader(new TRsource(null));

//        defaultXmlReader.getDefaultPath();
        defaultXmlRegionReader.parseResource();
//        String str = "123.123-13";
//
//        String[] sts = str.split("\\.|-");
//        for(int i = 0 ; i< sts.length; ++ i){
//            System.out.println(sts[i]);
//        }
    }


}
