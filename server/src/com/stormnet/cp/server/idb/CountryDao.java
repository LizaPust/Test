package com.stormnet.cp.server.idb;

import com.stormnet.cp.data.Country;

import javax.xml.transform.TransformerException;
import java.util.List;

public interface CountryDao {

    List<Country> loadAllCountries();

    Country loadCountryById(Integer countryId);

    Integer saveCountry(Country country) throws TransformerException;

    void updateCountry(Country country);

    void deleteCountry(Integer countryId);
}
