package agency.schmecker.dev.ollama4j.ui.view.embeddings;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import agency.schmecker.dev.ollama4j.ui.service.EmbeddingService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import jakarta.inject.Inject;

@Route(value = "embeddings/generate", layout = MainLayout.class)
@PageTitle("Generate embedding")
@CdiComponent
public class GenerateEmbeddingsView extends VerticalLayout {

    @Inject
    private EmbeddingService embeddingService;
    private TextField questionTextField = new TextField();
    private TextArea sentQuestionTextArea = new TextArea();
    private TextArea answerTextArea = new TextArea();

    public GenerateEmbeddingsView() {
        H2 header = new H2("Generate embedding example");

        questionTextField.setWidthFull();
        questionTextField.setLabel("Prompt");
        questionTextField.setPlaceholder("Enter a prompt to create an embedding for");

        sentQuestionTextArea.setWidthFull();
        sentQuestionTextArea.setVisible(false);
        sentQuestionTextArea.setLabel("Sent prompt");
        sentQuestionTextArea.setReadOnly(true);

        answerTextArea.setWidthFull();
        answerTextArea.setVisible(false);
        answerTextArea.setLabel("Vector");
        answerTextArea.setReadOnly(true);

        Button askButton = new Button("Generate embedding", e -> sendQuestion());
        askButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(header, questionTextField, askButton,sentQuestionTextArea, answerTextArea);
    }

    private void sendQuestion() {
        sentQuestionTextArea.setVisible(true);
        answerTextArea.setVisible(true);
        sentQuestionTextArea.setValue(questionTextField.getValue());
        questionTextField.clear();
        answerTextArea.setValue(embeddingService.generateEmbedding(sentQuestionTextArea.getValue()).toString());
    }

}
