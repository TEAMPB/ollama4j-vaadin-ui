package de.teampb.soco.dev.ollama4j.ui.view.chat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.teampb.soco.dev.ollama4j.ui.service.ChatService;
import de.teampb.soco.dev.ollama4j.ui.template.MainLayout;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessageRole;
import jakarta.inject.Inject;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */
@Route(value = "chat/image",layout = MainLayout.class)
@PageTitle("Chat about Images (requires image classification capable model)")
@CdiComponent
public class ImageChatView extends VerticalLayout {

    private static final Logger LOG = LoggerFactory.getLogger(ImageChatView.class);

    private MessageList chat;
    private MessageInput input;
    private List<MessageListItem> chatEntries  = new ArrayList<>();

    private List<byte[]> imagesForMessage = new ArrayList<>();

    @Inject
    private ChatService chatService;

    public ImageChatView() {

        H2 header = new H2("Chat about images");
        
        chat = new MessageList();
        input = new MessageInput();
        chat.setItems(new MessageListItem[0]);
        input.addSubmitListener(this::onSubmit);
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(e -> {
            String fileName = e.getFileName();
            InputStream fileInputStream = buffer.getInputStream(fileName);
            addFileToImages(fileInputStream);
        });

        HorizontalLayout inputHorizontalLayout = new HorizontalLayout(input,upload);
        this.setPadding(true); // Leave some white space
        this.setHeightFull(); // We maximize to window
        this.setWidthFull();
        chat.setSizeFull(); // Chat takes most of the space
        input.setWidthFull(); // Full width only
        chat.setMaxWidth("1200px"); // Limit the width
        //input.setMaxWidth("1100px");
        inputHorizontalLayout.setMaxWidth("1200px");
        add(header, chat, input, upload);
    }

    private void addFileToImages(InputStream fileInputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try{
        int nRead;
        byte[] data = new byte[4];

        while ((nRead = fileInputStream.readNBytes(data, 0, data.length)) != 0) {
            System.out.println("here " + nRead);
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] targetArray = buffer.toByteArray();
        this.imagesForMessage.add(targetArray);
        }
        catch(IOException ioException){
            LOG.error("Could not parse uploaded image!", ioException);
        }
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
        chatService.sendChatWithImages(submitEvent.getValue(), imagesForMessage ,
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
