/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.ws.soap.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.foi.nwtis.vlspoljar.konfiguracije.bp.BP_Konfiguracija;
import org.mycompany.web.data.Address;
import org.mycompany.web.data.Location;
import org.mycompany.web.data.WeatherData;
import org.mycompany.web.listeners.ApplicationListener;

/**
 *
 * @author Branko
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllAddress")
    public java.util.List<Address> getAllAddress() {
        try {
            BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
            Class.forName(baza_konfig.getDriver_database());
            String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
            String sql = "SELECT * FROM address";
            Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            List<Address> addressList = new ArrayList<>();

            while (rs.next()) {
                Address a = new Address(rs.getInt(1), rs.getString(2), new Location(rs.getString(3), rs.getString(4)));
                addressList.add(a);
            }
            return addressList;
        } catch (SQLException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllMeteodataForAddress")
    public java.util.List<WeatherData> getAllMeteodataForAddress(@WebParam(name = "address") final String address) {
        if (address != null && address.length() > 0) {
            try {
                BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
                Class.forName(baza_konfig.getDriver_database());
                String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
                String sql = "SELECT meteodata.temperature, meteodata.pressureSeaLevel, meteodata.humidity, meteodata.windSpeed, meteodata.rainRate, meteodata.snowRate, meteodata.time, address.address  FROM meteodata INNER JOIN address ON meteodata.idAddress=address.idAddress WHERE meteodata.idAddress=(SELECT idAddress FROM address WHERE address.address='" + address + "')";
                Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<WeatherData> wdList = new ArrayList<>();

                while (rs.next()) {
                    WeatherData wd = new WeatherData();
                    wd.setTemperature(rs.getFloat(1));
                    wd.setPressureSeaLevel(rs.getFloat(2));
                    wd.setHumidity(rs.getFloat(3));
                    wd.setWindSpeed(rs.getFloat(4));
                    wd.setRainRate(rs.getFloat(5));
                    wd.setSnowRate(rs.getFloat(6));
                    wd.setDate(rs.getString(7));
                    wd.setAddress(rs.getString(8));
                    wdList.add(wd);
                }
                return wdList;

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getLastMeteodataForAddress")
    public WeatherData getLastMeteodataForAddress(@WebParam(name = "address") final String address) {
        if (address != null && address.length() > 0) {
            try {
                BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
                Class.forName(baza_konfig.getDriver_database());
                String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
                String sql = "SELECT meteodata.temperature, meteodata.pressureSeaLevel, meteodata.humidity, meteodata.windSpeed, meteodata.rainRate, meteodata.snowRate, meteodata.time, address.address FROM meteodata INNER JOIN address ON meteodata.idAddress=address.idAddress WHERE meteodata.idAddress=(SELECT idAddress FROM address WHERE address.address='" + address + "') ORDER BY meteodata.time DESC LIMIT 1";
                Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                WeatherData wd = new WeatherData();

                while (rs.next()) {
                    wd.setTemperature(rs.getFloat(1));
                    wd.setPressureSeaLevel(rs.getFloat(2));
                    wd.setHumidity(rs.getFloat(3));
                    wd.setWindSpeed(rs.getFloat(4));
                    wd.setRainRate(rs.getFloat(5));
                    wd.setSnowRate(rs.getFloat(6));
                    wd.setDate(rs.getString(7));
                    wd.setAddress(rs.getString(8));
                }
                return wd;

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getLastNMeteodataForAddress")
    public java.util.List<WeatherData> getLastNMeteodataForAddress(@WebParam(name = "address") final String address, @WebParam(name = "N") final int N) {
        if (address != null && address.length() > 0 && N > 0) {
            try {
                BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
                Class.forName(baza_konfig.getDriver_database());
                String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
                String sql = "SELECT meteodata.temperature, meteodata.pressureSeaLevel, meteodata.humidity, meteodata.windSpeed, meteodata.rainRate, meteodata.snowRate, meteodata.time, address.address FROM meteodata INNER JOIN address ON meteodata.idAddress=address.idAddress WHERE meteodata.idAddress=(SELECT idAddress FROM address WHERE address.address='" + address + "') ORDER BY meteodata.time DESC LIMIT " + N + "";
                Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<WeatherData> wdList = new ArrayList<>();
                
                while (rs.next()) {
                    WeatherData wd = new WeatherData();
                    wd.setTemperature(rs.getFloat(1));
                    wd.setPressureSeaLevel(rs.getFloat(2));
                    wd.setHumidity(rs.getFloat(3));
                    wd.setWindSpeed(rs.getFloat(4));
                    wd.setRainRate(rs.getFloat(5));
                    wd.setSnowRate(rs.getFloat(6));
                    wd.setDate(rs.getString(7));
                    wd.setAddress(rs.getString(8));
                    wdList.add(wd);
                }
                return wdList;

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAllMeteodataForAddressInInterval")
    public java.util.List<WeatherData> getAllMeteodataForAddressInInterval(@WebParam(name = "address") final String address, @WebParam(name = "from") final String from, @WebParam(name = "to") final String to) {
        if (address != null && address.length() > 0 && from != null && from.length() > 0 && to != null && to.length() > 0) {
            try {
                BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
                Class.forName(baza_konfig.getDriver_database());
                String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
                String sql = "SELECT meteodata.temperature, meteodata.pressureSeaLevel, meteodata.humidity, meteodata.windSpeed, meteodata.rainRate, meteodata.snowRate, meteodata.time, address.address FROM meteodata INNER JOIN address ON meteodata.idAddress=address.idAddress WHERE meteodata.idAddress=(SELECT idAddress FROM address WHERE address.address='" + address + "')AND (meteodata.time BETWEEN '" + from +"' AND '" + to + "') ";
                Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<WeatherData> wdList = new ArrayList<>();
                
                while (rs.next()) {
                    WeatherData wd = new WeatherData();
                    wd.setTemperature(rs.getFloat(1));
                    wd.setPressureSeaLevel(rs.getFloat(2));
                    wd.setHumidity(rs.getFloat(3));
                    wd.setWindSpeed(rs.getFloat(4));
                    wd.setRainRate(rs.getFloat(5));
                    wd.setSnowRate(rs.getFloat(6));
                    wd.setDate(rs.getString(7));
                    wd.setAddress(rs.getString(8));
                    wdList.add(wd);
                }
                return wdList;

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }
}
