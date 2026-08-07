package br.com.paulo.chatbot.presentation.interceptor;

import br.com.paulo.chatbot.application.service.RateLimitingService;
import br.com.paulo.chatbot.security.TenantContextHolder;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimitingService rateLimitingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = null;
        try {
            tenantId = TenantContextHolder.getCurrentTenantId().toString();
        } catch (Exception e) {
            // Ignore if no tenant context is found, rely on security filters
        }
        
        if (tenantId == null || tenantId.isEmpty()) {
            // Se não houver tenantId, permitimos (pode ser uma rota não autenticada) ou podemos bloquear.
            // O SecurityConfig já garante que /api/chat/** requer autenticação e JWT.
            return true;
        }

        Bucket bucket = rateLimitingService.resolveBucket(tenantId);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Limit exceeded for this tenant. Retry after " + waitForRefill + " seconds.");
            return false;
        }
    }
}
