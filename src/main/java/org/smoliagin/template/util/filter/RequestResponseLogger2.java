//package org.smoliagin.template.util.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import org.springframework.web.util.ContentCachingResponseWrapper;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.Optional;
//import java.util.UUID;
//
//@Component
//public class RequestResponseLogger2 extends OncePerRequestFilter {
//
//    private static final Logger httpLogger =
//            LoggerFactory.getLogger(RequestResponseLogger.class.getName());
//
//    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
//    private static final String MDC_CORRELATION_ID_KEY = "correlationId";
//    private static final String MDC_HTTP_METHOD_KEY = "httpMethod";
//    private static final String MDC_HTTP_URI_KEY = "httpUri";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        try {
//            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
//            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
//            Charset requestCharset = Optional.of(wrappedRequest.getCharacterEncoding())
//                    .map(Charset::forName)
//                    .orElse(StandardCharsets.UTF_8);
//
//            String requestBody = new String(
//                    wrappedRequest.getContentAsByteArray(),
//                    requestCharset
//            );
//            logRequest(wrappedRequest, requestBody, request);
//
//            filterChain.doFilter(wrappedRequest, wrappedResponse);
//
//            Charset responseCharset = Optional.ofNullable(wrappedResponse.getCharacterEncoding())
//                    .map(Charset::forName)
//                    .orElse(StandardCharsets.UTF_8);
//
//            String responseBody = new String(
//                    wrappedResponse.getContentAsByteArray(),
//                    responseCharset
//            );
//            logResponse(wrappedResponse, responseBody, request);
//
////            httpLogger.info("Request Body: {}", requestBody);
////            httpLogger.info("Response Body: {}", responseBody);
//
//            wrappedResponse.copyBodyToResponse();
//
//        } finally {
//            MDC.clear();
//        }
//    }
//
//    private void logRequest(ContentCachingRequestWrapper requestWrapper, String requestBody, HttpServletRequest request){
//        initMdcParam(request);
//        MDC.put("requestBody", requestBody);
//        logger.trace("Http request");
//        MDC.clear();
//    }
//
//    private void logResponse(ContentCachingResponseWrapper responseWrapper, String responseBody, HttpServletRequest request){
//        MDC.clear();
//        initMdcParam(request);
//        MDC.put("responseBody", responseBody);
//        logger.trace("Http response");
//        MDC.clear();
//
//    }
//
//    private void initMdcParam(HttpServletRequest request) {
//        String correlationId = getOrGenerateCorrelationId(request);
//        String httpMethod = request.getMethod();
//        String httpUri = request.getRequestURI();
//        MDC.put(MDC_CORRELATION_ID_KEY, correlationId);
//        MDC.put(MDC_HTTP_METHOD_KEY, httpMethod);
//        MDC.put(MDC_HTTP_URI_KEY, httpUri);
//    }
//
//    private String getOrGenerateCorrelationId(HttpServletRequest request) {
//        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
//
//        if (correlationId == null || correlationId.isEmpty()) {
//            return UUID.randomUUID().toString();
//        }
//        return correlationId;
//    }
//}