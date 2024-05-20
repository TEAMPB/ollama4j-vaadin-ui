package de.teampb.soco.dev.ollama4j.ui.service;

import java.util.List;
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmbeddingService {

    @Inject
    private OllamaService ollamaService;

    public List<Double> generateEmbedding(String prompt) {
        ollamaService.getOllamaAPIInstance();

        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        api.setRequestTimeoutSeconds(240);

        try {
            return api.generateEmbeddings(ollamaService.getModel(), prompt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
