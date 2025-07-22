package org.smoliagin.template.util.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@Component
public class RequestResponseLogger extends OncePerRequestFilter {

    private static final Logger httpLogger =
            LoggerFactory.getLogger(RequestResponseLogger.class.getName());

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String MDC_CORRELATION_ID_KEY = "correlationId";
    private static final String MDC_HTTP_METHOD_KEY = "httpMethod";
    private static final String MDC_HTTP_URI_KEY = "httpUri";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String correlationId = getOrGenerateCorrelationId(request);
        response.addHeader(CORRELATION_ID_HEADER, correlationId);
        String httpMethod = request.getMethod();
        String httpUri = request.getRequestURI();

        MDC.put(MDC_CORRELATION_ID_KEY, correlationId);
        MDC.put(MDC_HTTP_METHOD_KEY, httpMethod);
        MDC.put(MDC_HTTP_URI_KEY, httpUri);

        try {
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

            filterChain.doFilter(wrappedRequest, wrappedResponse);

            Charset requestCharset = Optional.of(wrappedRequest.getCharacterEncoding())
                    .map(Charset::forName)
                    .orElse(StandardCharsets.UTF_8);

            Charset responseCharset = Optional.ofNullable(wrappedResponse.getCharacterEncoding())
                    .map(Charset::forName)
                    .orElse(StandardCharsets.UTF_8);

            String requestBody = new String(
                    wrappedRequest.getContentAsByteArray(),
                    requestCharset
            );

            String responseBody = new String(
                    wrappedResponse.getContentAsByteArray(),
                    responseCharset
            );

            httpLogger.info("Request Body: {}", requestBody);
            httpLogger.info("Response Body: {}", responseBody);

            wrappedResponse.copyBodyToResponse();

        } finally {
            MDC.remove(MDC_CORRELATION_ID_KEY);
            MDC.remove(MDC_HTTP_METHOD_KEY);
            MDC.remove(MDC_HTTP_URI_KEY);
        }
    }

    private String getOrGenerateCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);

        if (correlationId == null || correlationId.isEmpty()) {
            return UUID.randomUUID().toString();
        }
        return correlationId;
    }
}
