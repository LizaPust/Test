package com.stormnet.cp.server.idb.xml;

import com.stormnet.cp.data.Country;
import com.stormnet.cp.server.db.DataBase;
import com.stormnet.cp.server.db.DbTable;
import com.stormnet.cp.server.db.DomUtils;
import com.stormnet.cp.server.idb.CountryDao;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.util.ArrayList;
import java.util.List;

public class XmlCountryDao implements CountryDao {
    @Override
    public List<Country> loadAllCountries() {

        DataBase dataBase = DataBase.getDataBase();
        DbTable countriesTable = dataBase.getDbTable("countries");
        Document countriesDocument = countriesTable.getDbTableDocument();

        List<Country> allCountries = new ArrayList<>();
        NodeList countryNodeList = countriesDocument.getElementsByTagName("country");
        for (int i = 0; i < countryNodeList.getLength(); i++) {

            Element countryTag = (Element) countryNodeList.item(i);

            Integer id = DomUtils.getChildTagIntegerData(countryTag, "id");
            String name = DomUtils.getChildTagData(countryTag, "name");
            String code = DomUtils.getChildTagData(countryTag, "code");

            Country country = new Country(id, name, code);
            allCountries.add(country);
        }

        return allCountries;
    }

    @Override
    public Country loadCountryById(Integer countryId) {

        List<Country> allCountries = loadAllCountries();
        for (Country country : allCountries) {
            if (country.getId().equals(countryId)) {
                return country;
            }
        }

        return null;
    }

    @Override
    public Integer saveCountry(Country country) throws TransformerException {

        DataBase dataBase = DataBase.getDataBase();
        DbTable countriesTable = dataBase.getDbTable("countries");
        Document document = countriesTable.getDbTableDocument();

        Element rootElement = document.getDocumentElement();

        Element countryTag = document.createElement("country");
        rootElement.appendChild(countryTag);

        Integer nextId = dataBase.getNextId("countries");
        String idStr = nextId.toString();

        DomUtils.appendTagToParentTag(document, "id", idStr, countryTag);
        DomUtils.appendTagToParentTag(document, "name", country.getName(), countryTag);
        DomUtils.appendTagToParentTag(document, "code", country.getCode(), countryTag);

        DomUtils.saveDocument(document, countriesTable.getxmltableFilePath());

        return nextId;
    }

    @Override
    public void updateCountry(Country country) {

        DataBase dataBase = DataBase.getDataBase();
        DbTable countriesTable = dataBase.getDbTable("countries");
        Document document = countriesTable.getDbTableDocument();

        Element rootElement = document.getDocumentElement();

        NodeList countryNodeList = document.getElementsByTagName("country");
        for (int i = 0; i < countryNodeList.getLength(); i++) {

            Element countryTag = (Element) countryNodeList.item(i);
            Integer id = DomUtils.getChildTagIntegerData(countryTag, "id");

            if (id.equals(country.getId())) {
                Element nameTag = (Element) countryTag.getElementsByTagName("name").item(0);
                nameTag.setTextContent(country.getName());

                Element codeTag = (Element) countryTag.getElementsByTagName("code").item(0);
                codeTag.setTextContent(country.getCode());

                try {
                    DomUtils.saveDocument(document, countriesTable.getxmltableFilePath());
                } catch (TransformerException e) {
                    throw new RuntimeException(e);
                }

                return;
            }
        }
    }

    @Override
    public void deleteCountry(Integer countryId) {

        DataBase dataBase = DataBase.getDataBase();
        DbTable countriesTable = dataBase.getDbTable("countries");
        Document document = countriesTable.getDbTableDocument();

        Element allCountriesTag = document.getDocumentElement();

        NodeList countryNodeList = allCountriesTag.getElementsByTagName("country");
        for (int i = 0; i < countryNodeList.getLength(); i++) {

            Element countryTag = (Element) countryNodeList.item(i);
            Integer id = DomUtils.getChildTagIntegerData(countryTag, "id");

            if (id.equals(countryId)) {

                allCountriesTag.removeChild(countryTag);
                try {
                    DomUtils.saveDocument(document, countriesTable.getxmltableFilePath());
                } catch (TransformerException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }

    }
}