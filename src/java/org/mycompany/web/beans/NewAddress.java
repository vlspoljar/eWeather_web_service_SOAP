/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.web.beans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.foi.nwtis.vlspoljar.konfiguracije.bp.BP_Konfiguracija;
import org.mycompany.web.listeners.ApplicationListener;

/**
 *
 * @author
 */
@ManagedBean
@RequestScoped
public class NewAddress {

    public String address;
    public String latitude;
    public String longitude;
    static Statement stmt;

    public NewAddress() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    static void spajanje() {
        try {
            BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
            Class.forName(baza_konfig.getDriver_database());
            String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
            Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());
            stmt = conn.createStatement();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(NewAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveAddress() {
        spajanje();
        try {
            if (address != null && address != "" && latitude != null && latitude != "" && longitude != null && longitude != "")
            stmt.executeUpdate("INSERT INTO address (address, latitude, longitude) VALUES ('" + address + "', '" + latitude + "', '" + longitude + "')");
        } catch (SQLException ex) {
            Logger.getLogger(NewAddress.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
