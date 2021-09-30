
package cn.itcast.telecom.landmark;

import cn.itcast.telecom.entity.LandMark;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LandMarkUtil {
    public LandMarkUtil() {
    }

    public static List<LandMark> parseLandMark() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = null;
        document = reader.read(new File("/export/landMark.xml"));
        Element root = document.getRootElement();
        List list = root.elements("resourceitem");
        List<LandMark> landMarkBeans = new ArrayList();
        Iterator i = list.iterator();

        while(i.hasNext()) {
            Element landMark = (Element)i.next();
            LandMark bean = new LandMark();
            bean.setLowerLeftLon(Double.valueOf(landMark.element("lowerLeftLon").getText()));
            bean.setLowerLeftLat(Double.valueOf(landMark.element("lowerLeftLat").getText()));
            bean.setTopRightLon(Double.valueOf(landMark.element("topRightLon").getText()));
            bean.setTopRightLat(Double.valueOf(landMark.element("topRightLat").getText()));
            bean.setCategory(landMark.element("category").getText());
            bean.setLandMarkName(landMark.element("landMarkName").getText());
            landMarkBeans.add(bean);
        }

        return landMarkBeans;
    }

    public static void main(String[] args) {
        LandMarkUtil var1 = new LandMarkUtil();

        try {
            parseLandMark();
        } catch (DocumentException var3) {
            var3.printStackTrace();
        }

    }
}
