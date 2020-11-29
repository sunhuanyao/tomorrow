package com.sun.tomorrow.core.tool.parseCore;

import com.sun.tomorrow.core.domain.RegionInfo;
import com.sun.tomorrow.core.domain.TRsource;
import com.sun.tomorrow.core.util.PathUtil;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public abstract class AbstractXmlReader implements TReader<List<RegionInfo>>{

    public static final String DEFAULT_REGION_XML = "tomorrow_region.xml";
    protected TRsource resource;

    public AbstractXmlReader(TRsource resource){
        this.resource = resource;
    }

    /**
     *   获取Document, 相当于文件信息
     * @return
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws org.xml.sax.SAXException
     */



    protected Document getDocument() throws ParserConfigurationException,
            IOException, org.xml.sax.SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        if(this.resource == null){
            return builder.parse(PathUtil.getDefaultPath(DEFAULT_REGION_XML));
        }
        return builder.parse(new FileInputStream(PathUtil.getRelativePath(TReader.MIDDIR + this.resource.getPath())));

    }
}
