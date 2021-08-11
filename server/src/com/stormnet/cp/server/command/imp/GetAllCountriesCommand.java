package com.stormnet.cp.server.command.imp;

import com.stormnet.cp.data.Country;
import com.stormnet.cp.server.command.FirstCommand;
import com.stormnet.cp.server.idb.CountryDao;
import com.stormnet.cp.server.idb.xml.XmlCountryDao;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.List;

public class GetAllCountriesCommand implements FirstCommand {


    @Override
    public void process(JSONObject requestData, JSONWriter jsonWriter) {
        CountryDao countryDao = new XmlCountryDao();

        List<Country> allCountries;
        try {
            allCountries =  countryDao.loadAllCountries();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jsonWriter.key("response-data").array();

        for (Country country :allCountries) {

            jsonWriter.object();
            jsonWriter.key("id").value(country.getId());
            jsonWriter.key("name").value(country.getName());
            jsonWriter.key("code").value(country.getCode());
            jsonWriter.endObject();

        }

        jsonWriter.endArray();

    }
}