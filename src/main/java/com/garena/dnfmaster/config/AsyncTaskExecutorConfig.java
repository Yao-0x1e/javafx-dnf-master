package com.garena.dnfmaster.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 实现参考：
 * https://www.cnblogs.com/huanzi-qch/p/11231041.html
 */
@Configuration
@ConfigurationProperties(prefix = "concurrent")
@Data
public class AsyncTaskExecutorConfig {
    private int corePoolSize;
    private int maxPoolSize;
    private String threadNamePrefix;

    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(maxPoolSize);
        asyncTaskExecutor.setCorePoolSize(corePoolSize);
        asyncTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}
