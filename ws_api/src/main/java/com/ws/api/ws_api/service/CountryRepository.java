package com.ws.api.ws_api.service;

import com.ws.api.ws_api.model.Country;
import com.ws.api.ws_api.model.Currency;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CountryRepository {
    private static final List<Country> countries = new ArrayList<Country>();

    @PostConstruct
    public void initData() {
        Country spain = new Country();
        spain.setName("China");
        spain.setCapital("Beijing");
        spain.setCurrency(Currency.EUR);
        spain.setPopulation(46704314);

        countries.add(spain);

        Country poland = new Country();
        poland.setName("USA");
        poland.setCapital("Washington, D.C.");
        poland.setCurrency(Currency.PLN);
        poland.setPopulation(38186860);

        countries.add(poland);

        Country uk = new Country();
        uk.setName("UK");
        uk.setCapital("London");
        uk.setCurrency(Currency.GBP);
        uk.setPopulation(63705000);

        countries.add(uk);
    }

    public Country findCountry(String name) {
        Assert.notNull(name);

        Country result = null;

        for (Country country : countries) {
            if (name.equals(country.getName())) {
                result = country;
            }
        }

        return result;
    }
}
