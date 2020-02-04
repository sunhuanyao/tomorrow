package com.sun.tomorrow.core.tool.parseCore;

import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.jvm.hotspot.ui.DeadlockDetectionPanel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class DefaultXmlReader implements TReader {

    private static final String FIELD_PIECEAREA = "Piecearea";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_RINGS = "rings";

    private TRsource resource;

    private final Class<?> clazz;


    public DefaultXmlReader(TRsource resource, Class<?> clazz) {
        this.resource = resource;
        this.clazz = clazz;
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
            e.printStackTrace();
            System.out.println("false:" + e.getMessage());
        }
        return null;
    }

    /**
     *   获取Document, 相当于文件信息
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws org.xml.sax.SAXException
     */
    private Document getDocument() throws ParserConfigurationException,
            IOException, org.xml.sax.SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new FileInputStream(getSource()));
    }

    /**
     * 获取 文件路径
     * @return            返回文件路径
     */
    private String getSource(){
        URL url = this.clazz.getClassLoader().getResource(this.resource.getPath());
        return url.getPath();
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
        TReader tReader = new DefaultXmlReader(new TRsource("test.xml"), RegionInfo.class);
        tReader.parseResource();

//        String str = "123.123-13";
//
//        String[] sts = str.split("\\.|-");
//        for(int i = 0 ; i< sts.length; ++ i){
//            System.out.println(sts[i]);
//        }
    }


}
