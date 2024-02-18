package agency.schmecker.dev.ollama4j.ui.view.model;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.ModelService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import io.github.amithkoujalgi.ollama4j.core.models.Model;
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
        grid.addColumn(Model::getModelName).setHeader("Model Name");
        grid.addColumn(Model::getModelVersion).setHeader("Model Version");

        grid.setItems(modelService.getLoadedModels());

        grid.setWidthFull();

        this.add(grid);
        this.setHeightFull();
    }
    
}
