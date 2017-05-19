/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.duy.calculator.document;


import com.example.duy.calculator.DLog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Duy on 29-Mar-17.
 */

public class InfoAppUtil {

    /*  public static ArrayList<ItemInfo> readListTranslate(Context context, String filePath, boolean fromAssets)
              throws IOException, UnexpectedElementException, SAXException, ParserConfigurationException {
          ArrayList<ItemInfo> result = new ArrayList<>();

          InputStream inputStream;
          if (fromAssets) {
              inputStream = context.getAssets().open(filePath);
          } else {
              inputStream = new FileInputStream(filePath);
          }
          Node root = XmlRead.getRootNode(inputStream);
          Node child = root.getFirstChild();
          while (child != null) {
              NamedNodeMap attr = child.getAttributes();
              Node name = attr.getNamedItem("name");
              Node link = attr.getNamedItem("link");
              Node image = attr.getNamedItem("image");

              String sName = name.getNodeValue();
              String sLink = link.getNodeValue();
              String imgPath = image.getNodeValue();
              result.add(new ItemInfo(sName, sLink, imgPath));
              child = root.getNextSibling();
          }
          return result;
      }
  */
    public static ArrayList<ItemInfo> readListTranslate(InputStream inputStream) {
        ArrayList<ItemInfo> result = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            DLog.i(nodeList.getLength());
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node node = nodeList.item(index);
                if (node instanceof Element) {
                    String name = ((Element) node).getAttribute("name");
                    String link = ((Element) node).getAttribute("link");
                    String img = ((Element) node).getAttribute("image");
                    String lang = ((Element) node).getAttribute("lang");
                    result.add(new ItemInfo(name, link, img, lang));
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
           // e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<ItemInfo> readListLicense(InputStream inputStream) {
        ArrayList<ItemInfo> result = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            DLog.i(nodeList.getLength());
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node node = nodeList.item(index);
                if (node instanceof Element) {
                    NodeList nameNode = ((Element) node).getElementsByTagName("name");
                    NodeList licNode = ((Element) node).getElementsByTagName("content");
                    String name = nameNode.item(0).getTextContent();
                    String license = licNode.item(0).getTextContent();
                    result.add(new ItemInfo(name, license, ""));
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
