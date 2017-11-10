package com.dbs.escaperoom;

import com.dbs.escaperoom.monitoring.HeartbeatMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class MainroomApplication implements CommandLineRunner{


	public static void main(String[] args) {
		//starts a different thread anyway
		SpringApplication.run(MainroomApplication.class, args);
	}

	@Autowired
	private HeartbeatMonitor monitor;

	@Override
	public void run(String... strings) throws  Exception{
		monitor.start();
		System.out.println("waarom doe je het neit");
	}

	@Bean
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("escaperoom-");
		executor.initialize();
		return executor;
	}

}
