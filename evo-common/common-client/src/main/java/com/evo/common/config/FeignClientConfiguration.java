package com.evo.common.config;

import feign.Request;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class FeignClientConfiguration {

    @Bean
    public FeignClientInterceptor requestInterceptor() {
        FeignClientInterceptor feignClientInterceptor = new FeignClientInterceptor();
        return feignClientInterceptor;
    }
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                100,  // thời gian chờ ban đầu (ms)
                SECONDS.toMillis(1),  // thời gian chờ tối đa giữa các lần thử
                3  // số lần thử tối đa
        );
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(5000, 10000);
    }
}
