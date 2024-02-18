package agency.schmecker.dev.ollama4j.ui.view.model;

import java.util.stream.Stream;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.ModelService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import io.github.amithkoujalgi.ollama4j.core.models.Model;
import io.github.amithkoujalgi.ollama4j.core.models.ModelDetail;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route(value = "model/list",layout = MainLayout.class)
@PageTitle("List loaded models")
@CdiComponent
public class ListModelsView extends VerticalLayout{

    private Grid<Model> grid = new Grid<>(Model.class,false);

    @Inject
    private ModelService modelService;

    @PostConstruct
    public void init(){
        H2 header = new H2("Loaded models");
        grid.addColumn(Model::getModelName).setHeader("Model Name");
        grid.addColumn(Model::getModelVersion).setHeader("Model Version");

        grid.setItems(modelService.getLoadedModels());

        grid.setWidthFull();

        grid.setItemDetailsRenderer(createModelDetailsRenderer());

        this.add(header, grid);
        this.setHeightFull();
    }

    private ComponentRenderer<ModelDetailsFormLayout, Model> createModelDetailsRenderer(){
        return new ComponentRenderer<>(ModelDetailsFormLayout::new,ModelDetailsFormLayout::setModel);
    }

    private class ModelDetailsFormLayout extends FormLayout {
        private final TextArea modelFile = new TextArea("Modelfile");
        private final TextArea parameters = new TextArea("Parameters");
        private final TextArea template = new TextArea("Template");
        private final TextArea details = new TextArea("Details");
        private final TextArea license = new TextArea("License");

        public ModelDetailsFormLayout(){
                Stream.of(modelFile, parameters, template, details, license).forEach(field -> {
                            field.setReadOnly(true);
                            add(field);
                        });
            }

        public void setModel(Model model) {
            ModelDetail modelDetail = modelService.loadModelDetail(model);
            modelFile.setValue(modelDetail.getModelFile());
            parameters.setValue(modelDetail.getParameters());
            template.setValue(modelDetail.getTemplate());
            details.setValue(modelDetail.getDetails().toString());
            license.setValue(modelDetail.getLicense());
        }
    }
    
}
