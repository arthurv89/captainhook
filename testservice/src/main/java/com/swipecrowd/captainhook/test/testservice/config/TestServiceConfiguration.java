package com.swipecrowd.captainhook.test.testservice.config;

import com.swipecrowd.captainhook.framework.server.AbstractServerProperties;
import com.swipecrowd.captainhook.framework.server.ApplicationArguments;
import com.swipecrowd.captainhook.framework.server.DefaultServiceConfiguration;
import com.swipecrowd.captainhook.test.testservice.TestServiceServerProperties;
import com.swipecrowd.captainhook.test.testservice.client.TestServiceJavaClient;
import com.swipecrowd.captainhook.test.testservice.server.activity.helloworld.HelloWorldService;
import com.swipecrowd.captainhook.test.testservice.server.activity.helloworld.ServiceCall;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.EventPublisher;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class TestServiceConfiguration extends DefaultServiceConfiguration {
    @Bean
    public TestServiceServerProperties createTestServiceServerProperties(final ApplicationArguments applicationArguments) {
        return new TestServiceServerProperties(applicationArguments);
    }

    @Bean
    public TestServiceJavaClient createJavaClient(final AbstractServerProperties serverProperties) {
        return new TestServiceJavaClient(serverProperties);
    }

    @Bean
    public HelloWorldService createHelloWorldService(final TestServiceJavaClient testServiceJavaClient,
                                                     final TestServiceServerProperties testServiceServerProperties,
                                                     final ServiceCall serviceCall) {
        return new HelloWorldService(
                testServiceJavaClient,
                testServiceServerProperties,
                serviceCall);
    }

    @Bean
    public ServiceCall createServiceCall(final RateLimiter rateLimiter,
                                         final CircuitBreaker circuitBreaker) {
        return new ServiceCall(circuitBreaker, rateLimiter);
    }

    @Bean
    public CircuitBreaker createCircuitBreaker() {
        return CircuitBreaker.ofDefaults("TestService");
    }

    @Bean
    public EventPublisher createEventHandler(final CircuitBreaker circuitBreaker) {
        return circuitBreaker.getEventPublisher()
            .onError(event -> System.out.println(event));
    }

    @Bean
    TimeoutDuration createTimeoutDuration() {
        return TimeoutDuration.of(Duration.ofSeconds(1));
    }

    @Bean
    LimitRefreshPeriod createLimitRefreshPeriod() {
        return LimitRefreshPeriod.of(Duration.ofSeconds(1));
    }

    @Bean
    LimitForPeriod createLimitForPeriod() {
        return LimitForPeriod.of(1);
    }

    @Bean
    public RateLimiter createRateLimiter(final TimeoutDuration timeoutDuration,
                                         final LimitRefreshPeriod limitRefreshPeriod,
                                         final LimitForPeriod limitForPeriod) {
        final RateLimiterConfig config = RateLimiterConfig.custom()
                .timeoutDuration(timeoutDuration.getDuration())
                .limitRefreshPeriod(limitRefreshPeriod.getDuration())
                .limitForPeriod(limitForPeriod.getPeriod())
                .build();

        return RateLimiter.of("default", config);
    }
}
