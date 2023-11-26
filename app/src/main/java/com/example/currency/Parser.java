package com.example.currency;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Parser {
    private static final int MAX_CURRENCIES = 38;

    public static String getCurrencyRatesBaseUsd(InputStream stream) throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            DocumentBuilderFactory xmlDocFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlDocBuilder = xmlDocFactory.newDocumentBuilder();
            Document doc = xmlDocBuilder.parse(stream);

            NodeList rateNodes = doc.getElementsByTagName("item");

            int currenciesToShow = Math.min(MAX_CURRENCIES, rateNodes.getLength());

            for (int i = 0; i < currenciesToShow; ++i) {
                Element rateNode = (Element) rateNodes.item(i);
                String currencyName = rateNode.getElementsByTagName("targetCurrency").item(0).getFirstChild().getNodeValue();
                String rate = rateNode.getElementsByTagName("exchangeRate").item(0).getFirstChild().getNodeValue();
                result.append(String.format("Currency name: %s, rate %s \n", currencyName, rate));
            }
        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}