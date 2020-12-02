package com.example.template;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorTest extends TemplateApplicationTests {

    @Autowired
    @Qualifier("myExecutor")
    private ThreadPoolExecutor executorService;

    @Test
    public void executor() {
        while (true) {
            try {
                if (executorService.getActiveCount() < executorService.getCorePoolSize()) {
                    log.info("当前活动线程：{}，线程池最大线程数：{}", executorService.getActiveCount(), executorService.getCorePoolSize());
                    executorService.submit(() -> {
                        try {
                            int second = 3;
                            log.info("测试我的线程池，并睡眠{}秒",second);
                            TimeUnit.SECONDS.sleep(second);
                        } catch (InterruptedException e) {
                            log.error(e.getMessage(), e);
                        }
                    });
                } else {
                    log.info("睡眠。当前活动线程：{}，线程池最大线程数：{}", executorService.getActiveCount(), executorService.getCorePoolSize());
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
