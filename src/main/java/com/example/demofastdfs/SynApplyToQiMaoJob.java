package com.example.demofastdfs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class SynApplyToQiMaoJob {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    @Scheduled(cron = "0 0/1 * * * ?")
    public void toQiMaoJob() {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    log.info("当前线程：【{}】，当前循环：【{}】，当前系统时间：【{}】", executor.toString(), finalI + 1, new Date());
                }
            });
        }
    }
}
