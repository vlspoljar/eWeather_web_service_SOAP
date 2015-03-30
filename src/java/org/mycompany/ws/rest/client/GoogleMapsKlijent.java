/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.ws.rest.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;
import org.mycompany.web.data.Location;


/**
 * REST web servis za dohvat geolokacijskih podataka
 * 
 * @author Vlatko Špoljarić
 */
public class GoogleMapsKlijent {

    GMRESTHelper helper;
    Client client;

    public GoogleMapsKlijent() {
        client = ClientBuilder.newClient();
    }

    public Location getGeoLocation(String adresa) {
        try {
            WebTarget webResource = client.target(GMRESTHelper.getGM_BASE_URI())
                    .path("maps/api/geocode/json");
            webResource = webResource.queryParam("address",
                    URLEncoder.encode(adresa, "UTF-8"));
            webResource = webResource.queryParam("sensor", "false");

            String odgovor = webResource.request(MediaType.APPLICATION_JSON).get(String.class);
            JSONObject jo = new JSONObject(odgovor);
            JSONObject obj = jo.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            Location loc = new Location(obj.getString("lat"), obj.getString("lng"));

            return loc;

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(WeatherBugKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(WeatherBugKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
