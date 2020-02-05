package com.sun.tomorrow.core.tool.parseCore;

import com.sun.tomorrow.core.base.Point;
import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;
import com.sun.tomorrow.core.util.exception.ResourceNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class DefaultXmlReader implements TReader {
    /**
     * xml的有固定的模版样式，可重写该类来进行解析。
     */
    private static final String FIELD_PIECEAREA = "Piecearea";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_RINGS = "rings";

    private TRsource resource;


    public DefaultXmlReader(TRsource resource) {
        this.resource = resource;
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
        return builder.parse(new FileInputStream(getRelativePath()));
    }

    /**
     * 获取相对地址文件路径，意在能够根据用户自定义路径进行配置，但必须在resources路径下的位置。
     * @return            返回文件路径
     */
    private String getRelativePath(){
        String nowDir = System.getProperty("user.dir");

//        URL url = this.classLoader.getResource(this.resource.getPath());
//        if(url == null) throw new ResourceNotFoundException(this.resource.getPath());
//        return url.getPath();

        return nowDir + TReader.MIDDIR + this.resource.getPath();
    }

    private String getDefaultPath(){
        return null;
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
        TReader tReader = new DefaultXmlReader(new TRsource("test.xml"));
        tReader.parseResource();

//        String str = "123.123-13";
//
//        String[] sts = str.split("\\.|-");
//        for(int i = 0 ; i< sts.length; ++ i){
//            System.out.println(sts[i]);
//        }
    }


}
