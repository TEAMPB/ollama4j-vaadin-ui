package agency.schmecker.dev.ollama4j.ui.view.chat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import agency.schmecker.dev.ollama4j.ui.service.ChatService;
import agency.schmecker.dev.ollama4j.ui.template.MainLayout;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessageRole;
import jakarta.inject.Inject;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(value = "chat/simple",layout = MainLayout.class)
@PageTitle("Text only chat")
@CdiComponent
public class SimpleChatView extends VerticalLayout {

   private MessageList chat;
    private MessageInput input;
    private List<MessageListItem> chatEntries  = new ArrayList<>();

    @Inject
    private ChatService chatService;

    public SimpleChatView() {
        
        chat = new MessageList();
        input = new MessageInput();
        chat.setItems(new MessageListItem[0]);
        add(chat, input);
        input.addSubmitListener(this::onSubmit);
        
        this.setHorizontalComponentAlignment(Alignment.CENTER,
        chat, input);
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window
        chat.setSizeFull(); // Chat takes most of the space
        input.setWidthFull(); // Full width only
        chat.setMaxWidth("1200px"); // Limit the width
        input.setMaxWidth("1200px");
    }

    private void onSubmit(MessageInput.SubmitEvent submitEvent) {
        MessageListItem question = new MessageListItem(submitEvent.getValue(), Instant.now(), OllamaChatMessageRole.USER.name());
        question.setUserAbbreviation("US");
        question.setUserColorIndex(1);
        chatEntries.add(question);
        MessageListItem answer = new MessageListItem("Thinking.....", Instant.now(), OllamaChatMessageRole.ASSISTANT.name());
        chatEntries.add(answer);
        answer.setUserAbbreviation("AS");
        answer.setUserColorIndex(2);
        chat.setItems(chatEntries);
        Thread t = new Thread(()-> {
        chatService.sendChat(submitEvent.getValue(), 
                        (s)-> {
                            getUI().ifPresent(ui -> ui.access(
                                () -> {
                                    answer.setText(s);
                                }
                            ));
                        });            
         });
         t.start();
    }

}
