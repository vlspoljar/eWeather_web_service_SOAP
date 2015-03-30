package org.mycompany.web.listeners;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.vlspoljar.konfiguracije.Konfiguracija;
import org.foi.nwtis.vlspoljar.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.vlspoljar.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.vlspoljar.konfiguracije.bp.BP_Konfiguracija;
import org.mycompany.web.threads.MeteodataThread;

/**
 *
 * @author
 */
public class ApplicationListener implements ServletContextListener {

    public static javax.servlet.ServletContext context;
    Thread mt;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            context = sce.getServletContext();
            String path = context.getRealPath("/") + "WEB-INF" + System.getProperty("file.separator");
            String datoteka = context.getInitParameter("configuration");

            BP_Konfiguracija bp_konfig = new BP_Konfiguracija(path + datoteka);
            if (bp_konfig == null) {
                System.out.println("Error in configuration!");
                return;
            }
            Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(path + datoteka);
            if (konfig == null) {
                System.out.println("Error in configuration!");
            }

            context.setAttribute("BP_Konfig", bp_konfig);
            context.setAttribute("Konfig", konfig);
            
            mt = new MeteodataThread();
            mt.start();
            
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ApplicationListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (mt != null && mt.isAlive() && mt.isInterrupted()) {
            mt.interrupt();
        }
    }

}
