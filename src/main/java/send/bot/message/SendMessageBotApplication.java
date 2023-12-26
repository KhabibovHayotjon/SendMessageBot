package send.bot.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import send.bot.message.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SendMessageBotApplication {


    public static void main(String[] args) {
        SpringApplication.run(SendMessageBotApplication.class, args);




    }
}
