package br.com.paulo.chatbot.domain.ai;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class LlmJudgeEvaluationTest {

    @Autowired
    private LlmJudgeService llmJudgeService;

    @MockitoBean
    private ChatLanguageModel chatLanguageModel;

    @Test
    public void testLlmAsAJudgeEvaluatesCorrectly() {
        // Arrange
        String context = "Nossos planos começam em R$ 99 e vão até R$ 299.";
        String question = "Qual o plano mais barato?";
        
        // Simular o Juiz Aprovando
        when(chatLanguageModel.generate(anyString())).thenReturn("APROVADO");
        boolean isApproved = llmJudgeService.evaluateResponse(context, question, "O nosso plano básico custa R$ 99.");
        assertTrue(isApproved, "O juiz deve aprovar uma resposta correta e polida.");
        
        // Simular o Juiz Reprovando (Alucinação)
        when(chatLanguageModel.generate(anyString())).thenReturn("REPROVADO");
        boolean isRejected = llmJudgeService.evaluateResponse(context, question, "O plano é de graça.");
        assertFalse(isRejected, "O juiz deve reprovar uma alucinação.");
    }
}
