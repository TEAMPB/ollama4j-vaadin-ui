package agency.schmecker.dev.ollama4j.ui.view.generate;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.GenerateService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import jakarta.inject.Inject;

@Route(value = "generate/simple",layout = MainLayout.class)
@PageTitle("Ask single question")
@CdiComponent
public class SimpleGenerateView extends VerticalLayout{

    @Inject
    private GenerateService generateService;

    public SimpleGenerateView(){
        TextArea questionTextArea = new TextArea();
        questionTextArea.setWidthFull();
        questionTextArea.setLabel("Question");
        questionTextArea.setPlaceholder("Please enter a question");

        TextArea answerTextArea = new TextArea();
        answerTextArea.setWidthFull();
        answerTextArea.setLabel("Answer");
        answerTextArea.setReadOnly(true);

        Button askButton = new Button("Ask model",e -> {
            answerTextArea.setValue("Thinking...");
            Thread t = new Thread(()-> {
                generateService.sendPrompt(questionTextArea.getValue(), 
                                (s)-> {
                                    getUI().ifPresent(ui -> ui.access(
                                        () -> {
                                            answerTextArea.setValue(s);
                                        }
                                    ));
                                });            
                 });
                 t.start();
        });



        add(questionTextArea,askButton,answerTextArea);
    }
    
}
