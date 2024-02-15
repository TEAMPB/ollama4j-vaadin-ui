package agency.schmecker.dev.ollama4j.ui.view.model;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.template.MainLayout;

@Route(value = "model/list",layout = MainLayout.class)
@PageTitle("List loaded models")
@CdiComponent
public class ListModelsView extends VerticalLayout{
    
}
