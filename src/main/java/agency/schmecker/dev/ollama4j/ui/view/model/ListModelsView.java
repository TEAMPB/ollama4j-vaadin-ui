package agency.schmecker.dev.ollama4j.ui.view.model;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

@Route(value = "model/list", layout = MainLayout.class)
@PageTitle("List loaded models")
@CdiComponent
public class ListModelsView extends VerticalLayout {

    private static final Logger LOG = LoggerFactory.getLogger(ListModelsView.class);

    private Grid<Model> grid = new Grid<>(Model.class, false);

    @Inject
    private ModelService modelService;

    @PostConstruct
    public void init() {
        H2 header = new H2("Loaded models");
        grid.addColumn(Model::getModelName).setHeader("Model Name");
        grid.addColumn(Model::getModelVersion).setHeader("Model Version");
        grid.addColumn(this.createDeleteButtonRenderer()).setAutoWidth(true).setFlexGrow(0);

        grid.setItems(modelService.getLoadedModels());

        grid.setWidthFull();

        grid.setItemDetailsRenderer(createModelDetailsRenderer());

        this.add(header, grid);
        this.setHeightFull();
    }

    private void refreshGrid() {
        LOG.info("Refreshing Grid: " + grid);
        modelService.refreshLoadedModels();
        grid.setItems(modelService.getLoadedModels());
    }

    private ComponentRenderer<ModelDeleteButton, Model> createDeleteButtonRenderer() {
        return new ComponentRenderer<>(ModelDeleteButton::new, ModelDeleteButton::setModel);
    }

    private ComponentRenderer<ModelDetailsFormLayout, Model> createModelDetailsRenderer() {
        return new ComponentRenderer<>(ModelDetailsFormLayout::new,
                ModelDetailsFormLayout::setModel);
    }

    private class ModelDeleteButton extends Button {

        private Model m;

        public ModelDeleteButton() {
            this.setIcon(new Icon(VaadinIcon.TRASH));
            this.addThemeVariants(ButtonVariant.LUMO_ERROR);
            this.addClickListener((e) -> {
                ConfirmDialog dialog = new ConfirmDialog();
                dialog.setHeader("Delete model");
                dialog.setText("Do you want to remove the model '" + m.getModel() + "'?");
                dialog.setRejectable(true);
                Button rejectButton = new Button(new Icon(VaadinIcon.THUMBS_DOWN));
                dialog.setRejectButton(rejectButton);

                Button confirmButton = new Button(new Icon(VaadinIcon.THUMBS_UP));
                confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                        ButtonVariant.LUMO_ERROR);
                dialog.setConfirmButton(confirmButton);
                dialog.addConfirmListener(event -> {
                    modelService.deleteModel(m.getModel());
                    Notification notification = Notification.show("Model deleted!");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    refreshGrid();
                });
                dialog.open();
            });
        }

        public void setModel(Model m) {
            this.m = m;
        }
    }

    private class ModelDetailsFormLayout extends FormLayout {
        private final TextArea modelFile = new TextArea("Modelfile");
        private final TextArea parameters = new TextArea("Parameters");
        private final TextArea template = new TextArea("Template");
        private final TextArea details = new TextArea("Details");
        private final TextArea license = new TextArea("License");

        public ModelDetailsFormLayout() {
            Stream.of(modelFile, parameters, template, details, license).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });
        }

        public void setModel(Model model) {
            ModelDetail modelDetail = modelService.loadModelDetail(model);
            if (modelDetail == null) {
                return;
            }
            if (modelDetail.getModelFile() != null) {
                modelFile.setValue(modelDetail.getModelFile());
            }
            if (modelDetail.getParameters() != null) {
                parameters.setValue(modelDetail.getParameters());
            }
            if (modelDetail.getTemplate() != null) {
                template.setValue(modelDetail.getTemplate());
            }
            if (modelDetail.getDetails() != null) {
                details.setValue(modelDetail.getDetails().toString());
            }
            if (modelDetail.getLicense() != null) {
                license.setValue(modelDetail.getLicense());
            }
        }
    }

}
