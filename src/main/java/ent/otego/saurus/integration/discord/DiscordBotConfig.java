package ent.otego.saurus.integration.discord;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class DiscordBotConfig {

    //@Bean
    public JDA javaDiscordApi(@Value("${discord.bot.token}") String token) {
        return JDABuilder.createDefault(token).build();
    }
}
