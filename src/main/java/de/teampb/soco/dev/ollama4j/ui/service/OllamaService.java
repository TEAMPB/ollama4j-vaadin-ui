package de.teampb.soco.dev.ollama4j.ui.service;

import java.io.Serializable;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class OllamaService implements Serializable{
    
    @Inject @ConfigProperty(name="agency.schmecker.dev.ollama4j.ui.ollama.url",defaultValue = "ö")
    private String url;
    @Inject @ConfigProperty(name="agency.schmecker.dev.ollama4j.ui.ollama.defaultmodel",defaultValue = "llama2")
    private String model;
    @Inject @ConfigProperty(name="agency.schmecker.dev.ollama4j.ui.ollama.timeout",defaultValue = "120")
    private  Integer requestTimeout;
    
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public int getRequestTimeout() {
        return requestTimeout;
    }
    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public OllamaAPI getOllamaAPIInstance(){
        OllamaAPI api =  new OllamaAPI(url);
        api.setRequestTimeoutSeconds(requestTimeout);
        return api;
    }
}
