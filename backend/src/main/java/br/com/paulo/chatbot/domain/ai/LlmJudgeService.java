package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LlmJudgeService {

    private final ChatLanguageModel chatLanguageModel;

    public LlmJudgeService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    private static final String JUDGE_PROMPT_TEMPLATE = 
        "Você é um juiz de qualidade de Chatbots.\n" +
        "Avalie a resposta fornecida pelo assistente considerando a pergunta do usuário e o contexto recuperado.\n" +
        "Contexto:\n{{context}}\n\n" +
        "Pergunta do usuário:\n{{question}}\n\n" +
        "Resposta do assistente:\n{{answer}}\n\n" +
        "A resposta é educada, correta de acordo com o contexto e não possui alucinações?\n" +
        "Responda EXATAMENTE apenas com 'APROVADO' ou 'REPROVADO'.";

    public boolean evaluateResponse(String context, String question, String answer) {
        PromptTemplate promptTemplate = PromptTemplate.from(JUDGE_PROMPT_TEMPLATE);
        
        Map<String, Object> variables = new HashMap<>();
        variables.put("context", context != null ? context : "Nenhum contexto.");
        variables.put("question", question);
        variables.put("answer", answer);

        String prompt = promptTemplate.apply(variables).text();
        
        String result = chatLanguageModel.generate(prompt).trim().toUpperCase();
        
        return result.contains("APROVADO");
    }
}
