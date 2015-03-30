/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.ws.rest.client;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import org.mycompany.web.data.WeatherData;

/**
 * Klasa za dohvat meteopodataka za određenu adresu putem WeatherBug klijenta
 * (dohvaćeni meteopodaci: temperatura, tlak, vlažnost, brzina vjetra)
 *
 * @author Vlatko Špoljarić
 */
public class WeatherBugKlijent {

    String customerKey;
    String secretKey;
    WBRESTHelper helper;
    Client client;

    public WeatherBugKlijent(String customerKey, String secretKey) {
        this.customerKey = customerKey;
        this.secretKey = secretKey;
        helper = new WBRESTHelper(customerKey, secretKey);
        helper.autenticiraj();
        client = ClientBuilder.newClient();
    }

    /**
     * Metoda koja za parametre prima latitude i longitude za određenu adresu te
     * za nju vraća meteopodatke, ukoliko postoje
     *
     * @param latitude
     * @param longitude
     * @return WeatherData/null
     */
    public WeatherData getRealTimeWeather(String latitude, String longitude) {
        WebTarget webResource = client.target(WBRESTHelper.getWB_BASE_URI())
                .path("data/observations/v1/current");
        webResource = webResource.queryParam("location", latitude + "," + longitude);
        webResource = webResource.queryParam("locationtype", "latitudelongitude");
        webResource = webResource.queryParam("units", "metric");
        webResource = webResource.queryParam("cultureinfo", "en-en");
        webResource = webResource.queryParam("verbose", "true");
        webResource = webResource.queryParam("access_token", helper.getToken());

        String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
        try {
            JSONObject jo = new JSONObject(odgovor);
            WeatherData wd = new WeatherData();
            try {
                wd.setTemperature(((Double) jo.getDouble("temperature")).floatValue());
            } catch (Exception e) {
                wd.setTemperature(((Double) 0.0).floatValue());
                System.err.println("Parameter temperature does not exist!");
            }
            try {
                wd.setPressureSeaLevel(((Double) jo.getDouble("pressureSeaLevel")).floatValue());
            } catch (Exception e) {
                wd.setPressureSeaLevel(((Double) 0.0).floatValue());
                System.err.println("Parameter pressureSeaLevel does not exist!");
            }
            try {
                wd.setHumidity(((Double) jo.getDouble("humidity")).floatValue());
            } catch (Exception e) {
                wd.setHumidity(((Double) 0.0).floatValue());
                System.err.println("Parameter humidity does not exist!");
            }
            try {
                wd.setWindSpeed(((Double) jo.getDouble("windSpeed")).floatValue());
            } catch (Exception e) {
                wd.setWindSpeed(((Double) 0.0).floatValue());
                System.err.println("Parameter windSpeed does not exist!");
            }
            try {
                wd.setRainRate(((Double) jo.getDouble("rainRate")).floatValue());
            } catch (Exception e) {
                wd.setRainRate(((Double) 0.0).floatValue());
                System.err.println("Parameter rainRate does not exist!");
            }
            try {
                wd.setSnowRate(((Double) jo.getDouble("snowRate")).floatValue());
            } catch (Exception e) {
                wd.setSnowRate(((Double) 0.0).floatValue());
                System.err.println("Parameter snowRate does not exist!");
            }
            return wd;
        } catch (JSONException ex) {
            Logger.getLogger(WeatherBugKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
