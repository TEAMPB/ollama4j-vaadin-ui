package agency.schmecker.dev.ollama4j.ui.template;

import java.util.List;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import agency.schmecker.dev.ollama4j.ui.service.ModelService;
import agency.schmecker.dev.ollama4j.ui.service.OllamaService;
import agency.schmecker.dev.ollama4j.ui.view.chat.ImageChatView;
import agency.schmecker.dev.ollama4j.ui.view.chat.SimpleChatView;
import agency.schmecker.dev.ollama4j.ui.view.generate.SimpleGenerateView;
import agency.schmecker.dev.ollama4j.ui.view.model.ListModelsView;
import agency.schmecker.dev.ollama4j.ui.view.model.PullModelView;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@CssImport("styles/ollama4j-ui.css")
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

        // add chat accordion to drawer menu
        Accordion chatAccordion = new Accordion();
        RouterLink simpleChatRouterLink = new RouterLink("Simple Chat",SimpleChatView.class);
        simpleChatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink imageChatRouterLink = new RouterLink("Image Chat",ImageChatView.class);
        imageChatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        VerticalLayout chatVerticalLayout = new VerticalLayout(simpleChatRouterLink,imageChatRouterLink);
        chatVerticalLayout.setPadding(false);
        chatAccordion.add("Chat",chatVerticalLayout);

        // add generate accordion to drawer menu
        Accordion generateAccordion = new Accordion();
        RouterLink simpleGenerateRouterLink = new RouterLink("Simple generate",SimpleGenerateView.class);
        simpleChatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        generateAccordion.add("Generate",simpleGenerateRouterLink);

        // add models accordion to drawer menu
        Accordion modelAccordion = new Accordion();
        RouterLink listModelsRouterLink = new RouterLink("List models",ListModelsView.class);
        listModelsRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink pullModelRouterLink = new RouterLink("Pull model",PullModelView.class);
        pullModelRouterLink.setHighlightCondition(HighlightConditions.sameLocation());
        VerticalLayout modelVerticalLayout = new VerticalLayout(listModelsRouterLink,pullModelRouterLink);
        modelVerticalLayout.setPadding(false);
        modelAccordion.add("Models",modelVerticalLayout);

        // add ollama4j image
        Image image = new Image("images/ollama4j.jpeg", "ollama4j icon");
        image.setClassName("ollama-image");

        drawer = new VerticalLayout(chatAccordion,generateAccordion,modelAccordion,image);
        addToDrawer(drawer);

    }

    private void createModelDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Model parameters");
        VerticalLayout dialogLayout = new VerticalLayout();

        // add model selector to dialog
        Select<String> modelSelect = new Select<>();
        modelSelect.setLabel("Model");
        modelSelect.setHelperText("Model to send requests to");
        dialogLayout.add(modelSelect);
        dialog.add(dialogLayout);

        List<String> loadedModelNames = modelService.getLoadedModelNames();
        
        // add button to template header layout that opens model selector dialog
        Button openModelDialogButton = new Button("Loading init model", (e) -> {
            modelService.refreshLoadedModels();
                
            if(!modelService.getLoadedModelNames().isEmpty()) {
                modelSelect.setItems(modelService.getLoadedModelNames());
                modelSelect.setValue(ollamaService.getModel());
                dialog.open();
            }
            else {
                Notification notification = new Notification("No models loaded. Please pull model before selecting one.",5000,Position.MIDDLE);
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                e.getSource().setText("Pull model first");
                e.getSource().addThemeVariants(ButtonVariant.LUMO_ERROR);
                notification.open();
            }
        });

        // if no model is loaded, conditionally style button
        if(loadedModelNames.isEmpty()){
            openModelDialogButton.setText("Pull model first");
            openModelDialogButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        }
        // if at least one model is loaded, check if set model is in the list, otherwise take first one
        else{
            if(modelService.getLoadedModelNames().contains(ollamaService.getModel())){
                openModelDialogButton.setText(ollamaService.getModel());
            }
            else{
                String firstModelInList = modelService.getLoadedModelNames().get(0);
                openModelDialogButton.setText(firstModelInList);
                ollamaService.setModel(firstModelInList);
            }
        }

        // style button in header to be a little margined to the right
        openModelDialogButton.setClassName("modelMenu");
        header.add(openModelDialogButton);

        // add save button to model selector dialog
        Button saveButton = new Button("Save",e -> {
            ollamaService.setModel(modelSelect.getValue());
            openModelDialogButton.setText(ollamaService.getModel());
            openModelDialogButton.removeThemeVariants(ButtonVariant.LUMO_ERROR);
            dialog.close();
        });
        dialog.getFooter().add(saveButton);

        // add dialog to header
        addToNavbar(dialog);
    }


    
}
