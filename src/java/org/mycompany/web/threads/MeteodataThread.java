/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.web.threads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.vlspoljar.konfiguracije.Konfiguracija;
import org.foi.nwtis.vlspoljar.konfiguracije.bp.BP_Konfiguracija;
import org.mycompany.web.data.WeatherData;
import org.mycompany.web.listeners.ApplicationListener;
import org.mycompany.ws.rest.client.WeatherBugKlijent;

/**
 *
 * @author Branko
 */
public class MeteodataThread extends Thread {

    Konfiguracija konfig;
    
    public MeteodataThread() {
        konfig = (Konfiguracija) ApplicationListener.context.getAttribute("Konfig");
    } 

    @Override
    public void interrupt() {
        System.out.println("STOP!");
        super.interrupt();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Getting meteodata...");
            try {
                long pocetak = System.nanoTime();
                
                BP_Konfiguracija baza_konfig = (BP_Konfiguracija) ApplicationListener.context.getAttribute("BP_Konfig");
                Class.forName(baza_konfig.getDriver_database());
                String connUrl = baza_konfig.getServer_database() + baza_konfig.getUser_database();
                Connection conn = DriverManager.getConnection(connUrl, baza_konfig.getUser_username(), baza_konfig.getUser_password());
                Statement stmt = conn.createStatement();
                
                String sql = "SELECT * FROM address";
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    int idAddress = rs.getInt("idAddress");
                    String address = rs.getString("address");
                    String latitude = rs.getString("latitude");
                    String longitude = rs.getString("longitude");
                    String cKey = konfig.dajPostavku("weatherBug.cKey");
                    String sKey = konfig.dajPostavku("weatherBug.sKey");
                    WeatherBugKlijent wbk = new WeatherBugKlijent(cKey, sKey);
                    WeatherData wd = wbk.getRealTimeWeather(latitude, longitude);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
                    Date date = new Date();
                    String time = sdf.format(date);

                    String sqlInsert = "INSERT INTO meteodata (idAddress, temperature, pressureSeaLevel, humidity, windSpeed, rainRate, snowRate, time) VALUES ('" + idAddress + "', " + wd.getTemperature() + ", " + wd.getPressureSeaLevel() + ", " + wd.getHumidity() + ", " + wd.getWindSpeed() + ", " + wd.getRainRate() + ", " + wd.getSnowRate() + ", '" + time + "')";
                    Statement stmtInsert = conn.createStatement();
                    int rsInsert = stmtInsert.executeUpdate(sqlInsert);
                    if (rsInsert != 0) {
                        System.out.println("Meteodata added for address: " + address);
                    }
                }

                try {
                    long spavanje = Integer.parseInt(konfig.dajPostavku("mt.interval"))*1000 - (System.nanoTime() - pocetak) / 1000000;
                    if (spavanje < 0) {
                        spavanje = 0;
                    }
                    Thread.sleep(spavanje);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MeteodataThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MeteodataThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MeteodataThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        super.start();
    }
     
}
