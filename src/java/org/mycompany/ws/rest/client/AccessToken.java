/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mycompany.ws.rest.client;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dkermek
 */
@XmlRootElement(name = "access_token")
public class AccessToken {

    private String token;
    private String refresh_token = "";
    private String token_type = "";
    private long expires_in = 0;

    public String getToken() {
        System.out.println("token:"+token);
        return token;
    }

    @XmlElement
    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    @XmlElement
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    @XmlElement
    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public long getExpires_in() {
        return expires_in;
    }

    @XmlElement
    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

}
