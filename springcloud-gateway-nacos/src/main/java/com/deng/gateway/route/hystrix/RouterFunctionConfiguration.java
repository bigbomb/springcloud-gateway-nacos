//package com.deng.gateway.route.hystrix;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.server.RequestPredicates;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.RouterFunctions;
//
//@Configuration
//public class RouterFunctionConfiguration {
//    @Autowired
//    private HystrixFallbackHandler hystrixFallbackHandler;
//
//    @Bean
//    public RouterFunction routerGetFunction() {
//        return RouterFunctions.route(
//            RequestPredicates.GET("/fallback")
//                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
//    }
//
//    @Bean
//    public RouterFunction routerPostFunction() {
//        return RouterFunctions.route(
//            RequestPredicates.POST("/fallback")
//                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), hystrixFallbackHandler);
//    }
//
//}