package agency.schmecker.dev.ollama4j.ui.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.OllamaStreamHandler;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessage;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessageRole;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatRequestBuilder;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatRequestModel;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatResult;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;

@EJB
@Stateful
public class ChatService implements Serializable {

    private List<OllamaChatMessage> messages = new ArrayList<>();

    @Inject
    private OllamaService ollamaService;

    public String sendChat(String message, OllamaStreamHandler asyncHandler){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        api.setRequestTimeoutSeconds(240);
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(ollamaService.getModel());

        OllamaChatRequestModel ollamaChatRequestModel = builder.withMessages(messages).withMessage(OllamaChatMessageRole.USER, message).build();

        try {
            OllamaChatResult chat = api.chat(ollamaChatRequestModel,asyncHandler);
            messages = chat.getChatHistory();
            return chat.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }

}
