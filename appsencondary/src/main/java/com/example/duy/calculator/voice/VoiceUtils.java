package com.example.duy.calculator.voice;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * @Author Mr. Duy
 * Created by Duy on 28-Oct-16.
 */
public class VoiceUtils {
    private ArrayList<ItemReplace> mReplace = new ArrayList<>();
    private Context context;

    public VoiceUtils(InputStream is) {
        init(is);
    }

    public String replace(String res) {
        //to lower case input
        res = res.toLowerCase();
        //replace text to number and operator
        for (int i = 0; i < mReplace.size(); i++) {
            res = res.replace(mReplace.get(i).getText(), mReplace.get(i).getMath());
        }
        return res;
    }

    public void init(InputStream is) {
        mReplace.clear();
        mReplace.add(new ItemReplace("không", "0"));
        mReplace.add(new ItemReplace("một", "1"));
        mReplace.add(new ItemReplace("hai", "2"));
        mReplace.add(new ItemReplace("ba", "3"));
        mReplace.add(new ItemReplace("bốn", "4"));
        mReplace.add(new ItemReplace("năm", "5"));
        mReplace.add(new ItemReplace("lăm", "5"));
        mReplace.add(new ItemReplace("sáu", "6"));

        mReplace.add(new ItemReplace("bảy", "7"));
        mReplace.add(new ItemReplace("bẩy", "7"));
        mReplace.add(new ItemReplace("tám", "8"));

        mReplace.add(new ItemReplace("chín", "9"));
        mReplace.add(new ItemReplace("chính", "9"));
        mReplace.add(new ItemReplace("mười", "10"));

        mReplace.add(new ItemReplace("cộng", "+"));
        mReplace.add(new ItemReplace("trừ", "-"));
        mReplace.add(new ItemReplace("chừ", "-"));
        mReplace.add(new ItemReplace("nhân", "*"));
        mReplace.add(new ItemReplace("chia", "/"));

        mReplace.add(new ItemReplace("mũ", "^"));
        mReplace.add(new ItemReplace("mủ", "^"));
        mReplace.add(new ItemReplace("mẫu", "^"));

        mReplace.add(new ItemReplace("giai thừa", "!"));

        mReplace.add(new ItemReplace("căn bậc hai", "sqrt("));
        mReplace.add(new ItemReplace("căn bậc 2", "sqrt("));
        mReplace.add(new ItemReplace("căn bậc 3", "cbrt("));
        mReplace.add(new ItemReplace("căn bậc ba", "cbrt("));
        mReplace.add(new ItemReplace("căn", "sqrt("));
        mReplace.add(new ItemReplace("đóng ngoặc", ")"));
        mReplace.add(new ItemReplace("mở ngoặc", "("));

        mReplace.add(new ItemReplace("giá trị tuyệt đối", "abs("));
        mReplace.add(new ItemReplace("trị tuyệt đối", "abs("));

        mReplace.add(new ItemReplace("sin", "sin("));
        mReplace.add(new ItemReplace("cos", "cos("));
        mReplace.add(new ItemReplace("cos", "cos("));
        mReplace.add(new ItemReplace("tăng", "tan("));
        mReplace.add(new ItemReplace("cô tăng", "cotan("));
        mReplace.add(new ItemReplace("độ", "degree"));

//        try {
//            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            Document document = documentBuilder.parse(is);
//            Node root = document.getDocumentElement();
//            NodeList nodeList = root.getChildNodes();
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                if (nodeList.item(i) instanceof Element) {
//                    Element element = (Element) nodeList.item(i);
//                    NodeList keyNode = element.getElementsByTagName("key");
//                    NodeList replaceNode = element.getElementsByTagName("replace");
//                    String key = keyNode.item(0).getTextContent();
//                    String replace = replaceNode.item(0).getTextContent();
//                    mReplace.add(new ItemReplace(key, replace));
//                }
//            }
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public String toString(String math) {
        for (int i = 0; i < mReplace.size(); i++) {
            math = math.replace(mReplace.get(i).getMath(), mReplace.get(i).getText());
        }
        return math;
    }


}
