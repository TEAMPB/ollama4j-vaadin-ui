package agency.schmecker.dev.ollama4j.ui.service;

import java.util.ArrayList;
import java.util.List;

import io.github.amithkoujalgi.ollama4j.core.OllamaAPI;
import io.github.amithkoujalgi.ollama4j.core.OllamaStreamHandler;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessage;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatMessageRole;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatRequestBuilder;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatRequestModel;
import io.github.amithkoujalgi.ollama4j.core.models.chat.OllamaChatResult;
import io.github.amithkoujalgi.ollama4j.core.types.OllamaModelType;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ChatService {

    private List<OllamaChatMessage> messages = new ArrayList<>();

    private String model = OllamaModelType.LLAVA;

    private OllamaAPI api = new OllamaAPI("http://localhost:11434");

    public String sendChat(String message, OllamaStreamHandler asyncHandler){
        api.setRequestTimeoutSeconds(240);
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(model);

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
