package it.unicam.ids.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Classe che gestisce l'autenticazione OAuth 2.0 e la connessione
 * con le Google Calendar API.
 */
public class GoogleCalendarAPI {

    private static final String APPLICATION_NAME = "IDS Hackathon Calendar";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    private final String credentialsFilePath;

    /**
     * Costruttore che accetta il percorso del file credentials.json.
     * @param credentialsFilePath percorso al file credentials.json di Google
     */
    public GoogleCalendarAPI(String credentialsFilePath) {
        this.credentialsFilePath = credentialsFilePath;
    }

    /**
     * Crea le credenziali di autorizzazione OAuth 2.0.
     * Al primo avvio apre il browser per l'autenticazione,
     * poi salva il token in locale per i successivi utilizzi.
     */
    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        File credentialsFile = new File(credentialsFilePath);
        if (!credentialsFile.exists()) {
            throw new IOException(
                    "File credentials.json non trovato in: " + credentialsFilePath +
                    "\nScaricalo dalla Google Cloud Console (APIs & Services > Credentials).");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsFile)));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Costruisce e restituisce il client Google Calendar autenticato.
     * @return istanza di com.google.api.services.calendar.Calendar pronta all'uso
     */
    public Calendar getCalendarService() throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(httpTransport);

        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
