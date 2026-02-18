package it.unicam.ids.config;

import it.unicam.ids.service.CalendarService;
import it.unicam.ids.service.GoogleCalendarAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class CalendarConfig {

    @Bean
    public CalendarService calendarService() {
        String credentialsPath = "src/main/resources/credentials.json";
        File credFile = new File(credentialsPath);
        if (credFile.exists()) {
            GoogleCalendarAPI googleCalendarAPI = new GoogleCalendarAPI(credentialsPath);
            System.out.println("[INFO] Google Calendar configurato con credenziali reali.");
            return new CalendarService(googleCalendarAPI);
        }
        System.out.println("[INFO] Google Calendar in modalità simulazione.");
        return new CalendarService();
    }
}
