package agency.schmecker.dev.ollama4j.ui.service;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.exceptions.OllamaBaseException;
import io.github.amithkoujalgi.ollama4j.core.models.Model;
import io.github.amithkoujalgi.ollama4j.core.models.ModelDetail;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;

@EJB
@Stateful
public class ModelService implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(ModelService.class);

    @Inject
    private OllamaService ollamaService;

    private List<Model> loadedModels;

    @PostConstruct
    public void init(){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        try {
            loadedModels = api.listModels();
        } catch (OllamaBaseException | IOException | InterruptedException | URISyntaxException e) {
            LOG.error("Models could not be loaded from Ollama server!", e);
        }
    }

    public List<Model> getLoadedModels() {
        return loadedModels;
    }

    public void setLoadedModels(List<Model> loadedModels) {
        this.loadedModels = loadedModels;
    }

    public List<String> getLoadedModelNames() {
        if(getLoadedModels()== null || getLoadedModels().isEmpty()){
            return Collections.emptyList();
        }
        return loadedModels.stream().map(m -> m.getName()).toList();
    }

    public ModelDetail loadModelDetail(Model model) {
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        try {
            return api.getModelDetails(model.getModel());
        } catch (IOException | OllamaBaseException | InterruptedException | URISyntaxException e) {
            LOG.error("Could not load details to model " + model.getModel(), e);
            return null;
        }
        
    }

    public void pullModel(String modelName) {
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        try {
            api.pullModel(modelName);
        } catch (OllamaBaseException | IOException | URISyntaxException | InterruptedException e) {
            LOG.error("Could not load Model " + modelName, e);
        }
    }

}
