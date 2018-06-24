/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.document;


import com.duy.calculator.DLog;

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

}
