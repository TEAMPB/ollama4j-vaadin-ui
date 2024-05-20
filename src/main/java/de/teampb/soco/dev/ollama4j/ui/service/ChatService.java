package de.teampb.soco.dev.ollama4j.ui.service;

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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ChatService implements Serializable {

    private List<OllamaChatMessage> messages = new ArrayList<>();

    @Inject
    private OllamaService ollamaService;

    public String sendChat(String message, OllamaStreamHandler streamHandler){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();
        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(ollamaService.getModel());

        OllamaChatRequestModel ollamaChatRequestModel = builder.withMessages(messages).withMessage(OllamaChatMessageRole.USER, message).build();

        return callOllamaChat(streamHandler, api, ollamaChatRequestModel); 
    }


    public String sendChatWithImages(String message, List<byte[]> images, OllamaStreamHandler streamHandler){
        OllamaAPI api = ollamaService.getOllamaAPIInstance();

        OllamaChatRequestBuilder builder = OllamaChatRequestBuilder.getInstance(ollamaService.getModel());

        OllamaChatRequestModel ollamaChatRequestModel = builder.withMessages(messages).build();

        OllamaChatMessage userMessage = new OllamaChatMessage(OllamaChatMessageRole.USER, message,images);

        ollamaChatRequestModel.getMessages().add(userMessage);

        return callOllamaChat(streamHandler, api, ollamaChatRequestModel);
    }


    
    private String callOllamaChat(OllamaStreamHandler streamHandler, OllamaAPI api,
            OllamaChatRequestModel ollamaChatRequestModel) {
        try {
            OllamaChatResult chat = api.chat(ollamaChatRequestModel,streamHandler);
            messages = chat.getChatHistory();
            return chat.getResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
