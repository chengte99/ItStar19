package com.chengte99.itstar19.model;

import org.json.JSONException;
import org.json.JSONObject;

public class AppConfig {
    private int feedback = 0;
    private String log_id;
    private String sess_id;
    private String domain_url;
    private String appUpDate_url;
    private String app;
    private String web_url;
    private String isDev;
    private String check_link;

    public AppConfig(JSONObject object) {
        try {
            feedback = object.getInt("feedback");
            log_id = object.getString("log_id");
            sess_id = object.getString("sess_id");
            domain_url = object.getString("domain_url");
            appUpDate_url = object.getString("appUpDate_url");
            app = object.getString("app");
            web_url = object.getString("web_url");
            isDev = object.getString("isDev");
            check_link = object.getString("check_link");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getAppUpDate_url() {
        return appUpDate_url;
    }

    public void setAppUpDate_url(String appUpDate_url) {
        this.appUpDate_url = appUpDate_url;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public String getLog_id() {
        return log_id;
    }

    public void setLog_id(String log_id) {
        this.log_id = log_id;
    }

    public String getSess_id() {
        return sess_id;
    }

    public void setSess_id(String sess_id) {
        this.sess_id = sess_id;
    }

    public String getDomain_url() {
        return domain_url;
    }

    public void setDomain_url(String domain_url) {
        this.domain_url = domain_url;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getIsDev() {
        return isDev;
    }

    public void setIsDev(String isDev) {
        this.isDev = isDev;
    }

    public String getCheck_link() {
        return check_link;
    }

    public void setCheck_link(String check_link) {
        this.check_link = check_link;
    }
}
