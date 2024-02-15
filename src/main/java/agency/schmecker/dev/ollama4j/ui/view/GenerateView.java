package agency.schmecker.dev.ollama4j.ui.view;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.template.MainLayout;

@Route(value = "generate",layout = MainLayout.class)
@PageTitle("Ask / Generate")
@CdiComponent
public class GenerateView extends VerticalLayout{
    
}
