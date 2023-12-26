package send.bot.message.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${bot.name}")
    String botName;

    @Value("${bot.token}")
    String toke;
    @Value("${bot.owner1}")
    Long ownerId;
    @Value("${bot.owner2}")
    Long ownerId2;
}
