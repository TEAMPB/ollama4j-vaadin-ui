package agency.schmecker.dev.ollama4j.ui.service;

import java.io.Serializable;
import java.util.Map;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.OllamaStreamHandler;
import io.github.amithkoujalgi.ollama4j.core.utils.Options;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GenerateService implements Serializable{

    @Inject
    private OllamaService ollamaService;
    
    public void sendPrompt(String message, OllamaStreamHandler streamHandler){
        ollamaService.getOllamaAPIInstance();

        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        api.setRequestTimeoutSeconds(240);

        try{
            api.generate(ollamaService.getModel(), message, new Options(Map.of()), streamHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
    

}
