package com.chatbotcongtm;

import com.github.messenger4j.Messenger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ChatbotcongtmApplication {

  public static final String PAGE_ACCESS_TOKEN = "EAAP3YhZCU7oIBO5TzoziRTbM7inLi9vC2m2GMkuHgmLZARd8FGuuHFloTtfrHbU1EceHNktJ4ZAZC2mzMqy3nyF4PgRavF0tT98zgFho0qwNmvPFGsp1KTXYSrkcCSFawgK76tFHoCHKjkj7z0A2Sv1QwAkTPde0BSqlJfYSUoDjcNZC4BZCpogdWj6CsN8ogd";
  public static final String APP_SECRET = "95e9bfda3f8aa03609df2eebefb6e03e";
  public static final String VERIFY_TOKEN = "congtm";

  @Bean
  public Messenger messenger() {
    return Messenger.create(PAGE_ACCESS_TOKEN, APP_SECRET, VERIFY_TOKEN);
  }

  public static void main(String[] args) {
    SpringApplication.run(ChatbotcongtmApplication.class, args);
  }

}
