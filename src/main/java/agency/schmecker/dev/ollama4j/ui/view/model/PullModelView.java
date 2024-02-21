package agency.schmecker.dev.ollama4j.ui.view.model;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.ModelService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Route(value = "model/pull",layout = MainLayout.class)
@PageTitle("Pull model")
@CdiComponent
public class PullModelView extends VerticalLayout{

    @Inject
    private ModelService modelService;

    private TextField modelName = new TextField("Model name");
    private TextField modelVersion = new TextField("Model version");

    @PostConstruct
    public void init(){
        H2 header = new H2("Pull model");
        Button pullButton = new Button("Pull model", e -> pullModel());
        pullButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        FormLayout formLayout = new FormLayout(modelName,modelVersion);
        formLayout.setResponsiveSteps(new ResponsiveStep("0",1),new ResponsiveStep("750px",2));
        this.add(header,formLayout,pullButton);
    }

    private void pullModel() {
        modelService.pullModel(modelName.getValue()+":"+modelVersion.getValue());
        Notification notification = Notification
            .show("Model pull successful!");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}
