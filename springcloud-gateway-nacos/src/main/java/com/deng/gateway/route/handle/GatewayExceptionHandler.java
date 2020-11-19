package com.deng.gateway.route.handle;

import com.alibaba.fastjson.JSONObject;
import com.deng.gateway.entity.Result;
import com.deng.gateway.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GatewayExceptionHandler extends AbstractExceptionHandler implements ErrorWebExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GatewayExceptionHandler.class);
    private static final String TRACE_ID = "traceId";

    private static final String JHJCN_BUSI_NOT_FOUND = "Unable to find instance for jhjcn-business";

    private static final String JHJCN_BUSI_NOT_FOUND_ZH = "未xxx微服务,请检查服务是否可用";

    private List<HttpMessageReader<?>> messageReaders = Collections.emptyList();


    private List<HttpMessageWriter<?>> messageWriters = Collections.emptyList();


    private List<ViewResolver> viewResolvers = Collections.emptyList();


    private ThreadLocal<Map<String, Object>> exceptionHandlerResult = new ThreadLocal<>();


    public void setMessageReaders(List<HttpMessageReader<?>> messageReaders) {
        Assert.notNull(messageReaders, "'messageReaders' must not be null");
        this.messageReaders = messageReaders;
    }


    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }


    public void setMessageWriters(List<HttpMessageWriter<?>> messageWriters) {
        Assert.notNull(messageWriters, "'messageWriters' must not be null");
        this.messageWriters = messageWriters;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String errorMessage = super.formatMessage(ex);
        if (errorMessage.equals(JHJCN_BUSI_NOT_FOUND)) {
            errorMessage = JHJCN_BUSI_NOT_FOUND_ZH;
        }
        Result errorResult = super.buildErrorMap(errorMessage);
        Map errorMap = BeanUtils.beanToMap(errorResult);
        ServerHttpRequest request = exchange.getRequest();
        String traceId = request.getHeaders().getFirst(TRACE_ID);
        logger.error("GatewayExceptionHandler request info [traceId={}] result error=[{}]", traceId, JSONObject.toJSONString(errorMap));
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        exceptionHandlerResult.set(errorMap);
        ServerRequest newRequest = ServerRequest.create(exchange, this.messageReaders);
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse).route(newRequest)
                .switchIfEmpty(Mono.error(ex))
                .flatMap((handler) -> handler.handle(newRequest))
                .flatMap((response) -> write(exchange, response));

    }


    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> result = exceptionHandlerResult.get();
        exceptionHandlerResult.remove();
        return ServerResponse.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(result));
    }


    private Mono<? extends Void> write(ServerWebExchange exchange,
                                       ServerResponse response) {
        exchange.getResponse().getHeaders()
                .setContentType(response.headers().getContentType());
        return response.writeTo(exchange, new ResponseContext());
    }

    private class ResponseContext implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return GatewayExceptionHandler.this.messageWriters;
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return GatewayExceptionHandler.this.viewResolvers;
        }
    }
}

