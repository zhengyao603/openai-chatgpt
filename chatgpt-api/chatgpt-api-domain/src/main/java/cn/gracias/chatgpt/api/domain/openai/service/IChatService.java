package cn.gracias.chatgpt.api.domain.openai.service;

import cn.gracias.chatgpt.api.domain.openai.model.aggregate.ChatProcessAggregate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

@Service
public interface IChatService {

    ResponseBodyEmitter completions(ChatProcessAggregate chatProcess);
}
