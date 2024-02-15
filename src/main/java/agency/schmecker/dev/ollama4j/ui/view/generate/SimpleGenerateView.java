package agency.schmecker.dev.ollama4j.ui.view.generate;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.template.MainLayout;

@Route(value = "generate/simple",layout = MainLayout.class)
@PageTitle("Ask single question")
@CdiComponent
public class SimpleGenerateView extends VerticalLayout{

    public SimpleGenerateView(){
        TextArea questionTextArea = new TextArea();
        questionTextArea.setWidthFull();
        questionTextArea.setLabel("Question");
        questionTextArea.setPlaceholder("Please enter a question");

        TextArea answerTextArea = new TextArea();
        answerTextArea.setWidthFull();
        answerTextArea.setLabel("Answer");

        Button askButton = new Button("Ask model",e -> {
            answerTextArea.setValue("TestText");
        });



        add(questionTextArea,askButton,answerTextArea);
    }
    
}
