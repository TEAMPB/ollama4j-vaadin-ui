package agency.schmecker.dev.ollama4j.ui.template;

import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import agency.schmecker.dev.ollama4j.ui.service.ModelService;
import agency.schmecker.dev.ollama4j.ui.service.OllamaService;
import agency.schmecker.dev.ollama4j.ui.view.chat.SimpleChatView;
import agency.schmecker.dev.ollama4j.ui.view.generate.SimpleGenerateView;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@CssImport("styles/my.css")
public class MainLayout extends AppLayout{

    @Inject
    private OllamaService ollamaService;

    @Inject 
    private ModelService modelService;

    HorizontalLayout header;

    VerticalLayout drawer;

    public MainLayout(){
    }

    @PostConstruct
    public void init(){
        createHeader();
        createDrawer();
        createModelDialog();
    }

    private void createHeader() {
        H2 headerTitle = new H2("Ollama4J UI Tester");
        headerTitle.addClassName("header-title");

        header = new HorizontalLayout(new DrawerToggle(),headerTitle);
        header.setClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        addToNavbar(header);
    }

    private void createDrawer() {

        Accordion chatAccordion = new Accordion();
        RouterLink simpleChatRouterLink = new RouterLink("Simple Chat",SimpleChatView.class);
        simpleChatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        chatAccordion.add("Chat",simpleChatRouterLink);

        Accordion generateAccordion = new Accordion();
        RouterLink simpleGenerateRouterLink = new RouterLink("Simple generate",SimpleGenerateView.class);
        simpleChatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        generateAccordion.add("Generate",simpleGenerateRouterLink);

        Accordion modelAccordion = new Accordion();
        RouterLink listModelsRouterLink = new RouterLink("List models",SimpleGenerateView.class);
        listModelsRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        modelAccordion.add("Generate",listModelsRouterLink);
        drawer = new VerticalLayout(chatAccordion,generateAccordion,modelAccordion);
        addToDrawer(drawer);

    }

    private void createModelDialog() {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Model parameters");

        VerticalLayout dialogLayout = new VerticalLayout();

        Select<String> modelSelect = new Select<>();
        modelSelect.setLabel("Model");
        modelSelect.setItems(modelService.getLoadedModelNames());
        modelSelect.setValue(ollamaService.getModel());
        modelSelect.setHelperText("Model to send requests to");
        dialogLayout.add(modelSelect);

        dialog.add(dialogLayout);

        Button openModelDialogButton = new Button(ollamaService.getModel(), (e) -> {
            dialog.open();
        });
        openModelDialogButton.setClassName("modelMenu");
        header.add(openModelDialogButton);

        
        Button saveButton = new Button("Save",e -> {
            ollamaService.setModel(modelSelect.getValue());
            openModelDialogButton.setText(ollamaService.getModel());
            dialog.close();
        });
        dialog.getFooter().add(saveButton);
        addToNavbar(dialog);

    }


    
}
