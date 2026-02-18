package it.unicam.ids;

import it.unicam.ids.controller.HackathonHandler;
import it.unicam.ids.controller.InvitoHandler;
import it.unicam.ids.controller.IscrizioneHandler;
import it.unicam.ids.controller.Result;
import it.unicam.ids.controller.SegnalazioneHandler;
import it.unicam.ids.controller.SottomissioneHandler;
import it.unicam.ids.controller.SupportoHandler;
import it.unicam.ids.controller.TeamHandler;
import it.unicam.ids.controller.ValutazioneHandler;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.RichiestaSupporto;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Segnalazione;
import it.unicam.ids.model.StatoHackathon;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.SegnalazioneRepository;
import it.unicam.ids.repository.SottomissioneRepository;
import it.unicam.ids.repository.SupportoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.CalendarService;
import it.unicam.ids.service.GoogleCalendarAPI;
import it.unicam.ids.service.ConsoleService;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.InvitoService;
import it.unicam.ids.service.IscrizioneService;
import it.unicam.ids.service.NotificationService;
import it.unicam.ids.service.ObserverSupporto;
import it.unicam.ids.service.SegnalazioneService;
import it.unicam.ids.service.SottomissioneService;
import it.unicam.ids.service.SupportoService;
import it.unicam.ids.service.TeamService;
import it.unicam.ids.service.ValutazioneService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final InvitoRepository invitoRepository;
    private final SottomissioneRepository sottomissioneRepository;
    private final SupportoRepository supportoRepository;
    private final SegnalazioneRepository segnalazioneRepository;

    private final TeamService teamService;
    private final HackathonService hackathonService;
    private final IscrizioneService iscrizioneService;
    private final InvitoService invitoService;
    private final SottomissioneService sottomissioneService;
    private final ValutazioneService valutazioneService;
    private final SupportoService supportoService;
    private final SegnalazioneService segnalazioneService;

    private final TeamHandler teamHandler;
    private final HackathonHandler hackathonHandler;
    private final IscrizioneHandler iscrizioneHandler;
    private final InvitoHandler invitoHandler;
    private final SottomissioneHandler sottomissioneHandler;
    private final ValutazioneHandler valutazioneHandler;
    private final SupportoHandler supportoHandler;
    private final SegnalazioneHandler segnalazioneHandler;

    private final Scanner scanner;

    public Main() {
        // Inizializzazione Repository
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        hackathonRepository = new HackathonRepository();
        invitoRepository = new InvitoRepository();
        sottomissioneRepository = new SottomissioneRepository();
        supportoRepository = new SupportoRepository();
        segnalazioneRepository = new SegnalazioneRepository();

        // Inizializzazione Observer
        ObserverSupporto observerSupporto = new ObserverSupporto();
        observerSupporto.addSubscriber(new NotificationService());
        observerSupporto.addSubscriber(new ConsoleService());

        // Inizializzazione Service
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository, hackathonRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService, teamRepository);
        iscrizioneService = new IscrizioneService(teamRepository, hackathonRepository, utenteRepository);
        invitoService = new InvitoService(utenteRepository, teamRepository, invitoRepository);
        sottomissioneService = new SottomissioneService(sottomissioneRepository, hackathonRepository, utenteRepository);
        valutazioneService = new ValutazioneService(sottomissioneRepository);
        // Google Calendar: usa le credenziali se il file esiste, altrimenti simulazione
        CalendarService calendarService;
        String credentialsPath = "src/main/resources/credentials.json";
        java.io.File credFile = new java.io.File(credentialsPath);
        if (credFile.exists()) {
            GoogleCalendarAPI googleCalendarAPI = new GoogleCalendarAPI(credentialsPath);
            calendarService = new CalendarService(googleCalendarAPI);
            System.out.println("[INFO] Google Calendar configurato con credenziali reali.");
        } else {
            calendarService = new CalendarService();
            System.out.println("[INFO] Google Calendar in modalità simulazione (credentials.json non trovato).");
        }
        supportoService = new SupportoService(supportoRepository, hackathonRepository,
                utenteRepository, teamRepository, calendarService, observerSupporto);
        segnalazioneService = new SegnalazioneService(segnalazioneRepository, hackathonRepository, utenteRepository, teamRepository);

        // Inizializzazione Handler (Controller)
        teamHandler = new TeamHandler(teamService);
        hackathonHandler = new HackathonHandler(hackathonService);
        iscrizioneHandler = new IscrizioneHandler(iscrizioneService);
        invitoHandler = new InvitoHandler(invitoService);
        sottomissioneHandler = new SottomissioneHandler(sottomissioneService);
        valutazioneHandler = new ValutazioneHandler(valutazioneService);
        supportoHandler = new SupportoHandler(supportoService);
        segnalazioneHandler = new SegnalazioneHandler(segnalazioneService);

        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        System.out.println("========================================");
        System.out.println("   SISTEMA GESTIONE HACKATHON - IDS");
        System.out.println("========================================\n");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> creaOrganizzatore();
                case "2" -> creaLeader();
                case "3" -> creaTeam();
                case "4" -> creaHackathon();
                case "5" -> iscriviTeamAHackathon();
                case "6" -> cambiaStatoHackathon();
                case "7" -> inviaRichiestaSupporto();
                case "8" -> prenotaCall();
                case "9" -> confermaPartecipazione();
                case "10" -> visualizzaTeamPartecipanti();
                case "11" -> visualizzaSegnalazioni();
                case "12" -> gestisciSegnalazione();
                case "13" -> eseguiDemo();
                case "0" -> {
                    running = false;
                    System.out.println("\nArrivederci!");
                }
                default -> System.out.println("\nOpzione non valida. Riprova.");
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n--- MENU PRINCIPALE ---");
        System.out.println("1. Crea Organizzatore");
        System.out.println("2. Crea Leader");
        System.out.println("3. Crea Team");
        System.out.println("4. Crea Hackathon");
        System.out.println("5. Iscrivi Team a Hackathon");
        System.out.println("6. Cambia Stato Hackathon");
        System.out.println("7. Invia Richiesta Supporto");
        System.out.println("8. Prenota Call di Supporto");
        System.out.println("9. Conferma Partecipazione a Call");
        System.out.println("10. Visualizza Team Partecipanti");
        System.out.println("11. Visualizza Segnalazioni");
        System.out.println("12. Gestisci Segnalazione");
        System.out.println("13. Esegui Demo automatica");
        System.out.println("0. Esci");
        System.out.print("\nScelta: ");
    }

    private void creaOrganizzatore() {
        System.out.println("\n--- CREA ORGANIZZATORE ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Utente organizzatore = new Utente(nome, cognome, email, password);
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);

        System.out.println("\nOrganizzatore creato con ID: " + organizzatore.getId());
        System.out.println(organizzatore);
    }

    private void creaLeader() {
        System.out.println("\n--- CREA LEADER ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Utente leader = new Utente(nome, cognome, email, password);
        leader = utenteRepository.add(leader);

        System.out.println("\nUtente creato con ID: " + leader.getId() + " (diventerà Leader quando crea un team)");
        System.out.println(leader);
    }

    private void creaTeam() {
        System.out.println("\n--- CREA TEAM ---");
        System.out.print("Nome Team: ");
        String nome = scanner.nextLine();
        System.out.print("ID Leader: ");
        Long leaderId = Long.parseLong(scanner.nextLine());

        Result<Team> result = teamHandler.creaTeam(nome, leaderId);

        if (result.isSuccess()) {
            System.out.println("\nTeam creato con successo!");
            System.out.println(result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void creaHackathon() {
        System.out.println("\n--- CREA HACKATHON ---");
        System.out.print("ID Organizzatore: ");
        Long organizzatoreId = Long.parseLong(scanner.nextLine());
        System.out.print("Nome Hackathon: ");
        String nome = scanner.nextLine();
        System.out.print("Descrizione: ");
        String descrizione = scanner.nextLine();
        System.out.print("Data Inizio (YYYY-MM-DD): ");
        LocalDate dataInizio = LocalDate.parse(scanner.nextLine());
        System.out.print("Data Fine (YYYY-MM-DD): ");
        LocalDate dataFine = LocalDate.parse(scanner.nextLine());
        System.out.print("Scadenza Iscrizioni (YYYY-MM-DD): ");
        LocalDate scadenza = LocalDate.parse(scanner.nextLine());
        System.out.print("Regolamento: ");
        String regolamento = scanner.nextLine();
        System.out.print("Premio: ");
        double premio = Double.parseDouble(scanner.nextLine());
        System.out.print("Max Membri per Team: ");
        int maxMembri = Integer.parseInt(scanner.nextLine());

        Result<Hackathon> result = hackathonHandler.creaHackathonRequest(
                nome, dataInizio, dataFine, descrizione, regolamento, scadenza, maxMembri, premio, organizzatoreId);

        if (result.isSuccess()) {
            System.out.println("\nHackathon creato con successo!");
            System.out.println(result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void iscriviTeamAHackathon() {
        System.out.println("\n--- ISCRIVI TEAM A HACKATHON ---");
        System.out.print("ID Team: ");
        Long teamId = Long.parseLong(scanner.nextLine());
        System.out.print("ID Hackathon: ");
        Long hackathonId = Long.parseLong(scanner.nextLine());

        Result<String> result = iscrizioneHandler.iscriviTeam(teamId, hackathonId);

        if (result.isSuccess()) {
            System.out.println("\n" + result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void cambiaStatoHackathon() {
        System.out.println("\n--- CAMBIA STATO HACKATHON ---");
        System.out.print("ID Hackathon: ");
        Long hackathonId = Long.parseLong(scanner.nextLine());
        System.out.println("Stati disponibili: IN_ISCRIZIONE, IN_CORSO, IN_VALUTAZIONE, CONCLUSO, ANNULLATO");
        System.out.print("Nuovo Stato: ");
        String statoStr = scanner.nextLine().trim().toUpperCase();

        try {
            StatoHackathon nuovoStato = StatoHackathon.valueOf(statoStr);
            Result<String> result = hackathonHandler.cambiaStato(hackathonId, nuovoStato);

            if (result.isSuccess()) {
                System.out.println("\n" + result.getData());
            } else {
                System.out.println("\nErrore: " + result.getErrorMessage());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\nStato non valido. Usa uno tra: IN_ISCRIZIONE, IN_CORSO, IN_VALUTAZIONE, CONCLUSO, ANNULLATO");
        }
    }

    private void inviaRichiestaSupporto() {
        System.out.println("\n--- INVIA RICHIESTA SUPPORTO ---");
        System.out.print("ID Utente (partecipante): ");
        Long utenteId = Long.parseLong(scanner.nextLine());
        System.out.print("ID Hackathon: ");
        Long hackathonId = Long.parseLong(scanner.nextLine());
        System.out.print("Descrizione problema: ");
        String descrizione = scanner.nextLine();

        Result<String> result = supportoHandler.inviaRichiestaSupporto(descrizione, utenteId, hackathonId);

        if (result.isSuccess()) {
            System.out.println("\n" + result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void prenotaCall() {
        System.out.println("\n--- PRENOTA CALL DI SUPPORTO ---");

        // Mostra le richieste di supporto disponibili
        System.out.print("ID Hackathon (per vedere le richieste): ");
        Long hackathonId = Long.parseLong(scanner.nextLine());

        Result<List<RichiestaSupporto>> richiesteResult = supportoHandler.getRichieste(hackathonId);
        if (richiesteResult.isSuccess()) {
            List<RichiestaSupporto> richieste = richiesteResult.getData();
            if (richieste.isEmpty()) {
                System.out.println("\nNessuna richiesta di supporto trovata per questo hackathon.");
                return;
            }
            System.out.println("\nRichieste di supporto:");
            for (RichiestaSupporto r : richieste) {
                System.out.println("  ID: " + r.getId() + " | Descrizione: " + r.getDescrizione()
                        + " | Risolta: " + r.isRisolta());
            }
        } else {
            System.out.println("\nErrore: " + richiesteResult.getErrorMessage());
            return;
        }

        System.out.print("\nID Richiesta da gestire: ");
        Long richiestaId = Long.parseLong(scanner.nextLine());
        System.out.print("Data Call (YYYY-MM-DD): ");
        LocalDate data = LocalDate.parse(scanner.nextLine());
        System.out.print("Ora Inizio Call (HH:mm, es. 10:00): ");
        LocalTime oraInizio = LocalTime.parse(scanner.nextLine().trim(), DateTimeFormatter.ofPattern("H:mm"));
        System.out.print("Ora Fine Call (HH:mm, es. 11:00): ");
        LocalTime oraFine = LocalTime.parse(scanner.nextLine().trim(), DateTimeFormatter.ofPattern("H:mm"));

        Result<String> result = supportoHandler.prenotaCall(richiestaId, data, oraInizio, oraFine);

        if (result.isSuccess()) {
            System.out.println("\n" + result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void confermaPartecipazione() {
        System.out.println("\n--- CONFERMA PARTECIPAZIONE A CALL ---");
        System.out.print("ID Evento Google Calendar: ");
        String eventId = scanner.nextLine().trim();

        Result<String> result = supportoHandler.confermaPartecipazione(eventId);

        if (result.isSuccess()) {
            System.out.println("\n" + result.getData());
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void visualizzaTeamPartecipanti() {
        System.out.println("\n--- VISUALIZZA TEAM PARTECIPANTI ---");
        System.out.print("ID Hackathon: ");
        Long hackathonId = Long.parseLong(scanner.nextLine());

        Result<List<Team>> result = hackathonHandler.getTeams(hackathonId);

        if (result.isSuccess()) {
            List<Team> teams = result.getData();
            if (teams.isEmpty()) {
                System.out.println("\nNessun team iscritto a questo hackathon.");
            } else {
                System.out.println("\nTeam partecipanti (" + teams.size() + "):");
                for (Team t : teams) {
                    System.out.println("  ID: " + t.getId() + " | Nome: " + t.getNome()
                            + " | Leader: " + t.getLeaderId() + " | Membri: " + t.getMembri().size());
                }
            }
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void visualizzaSegnalazioni() {
        System.out.println("\n--- VISUALIZZA SEGNALAZIONI ---");
        System.out.print("ID Hackathon: ");
        Long hackathonId = Long.parseLong(scanner.nextLine());

        Result<List<Segnalazione>> result = segnalazioneHandler.getSegnalazioni(hackathonId);

        if (result.isSuccess()) {
            List<Segnalazione> segnalazioni = result.getData();
            if (segnalazioni.isEmpty()) {
                System.out.println("\nNessuna segnalazione per questo hackathon.");
            } else {
                System.out.println("\nSegnalazioni (" + segnalazioni.size() + "):");
                for (Segnalazione s : segnalazioni) {
                    System.out.println("  ID: " + s.getId() + " | Team: " + s.getTeamId()
                            + " | Descrizione: " + s.getDescrizione()
                            + " | Gestita: " + s.isGestita());
                }
            }
        } else {
            System.out.println("\nErrore: " + result.getErrorMessage());
        }
    }

    private void gestisciSegnalazione() {
        System.out.println("\n--- GESTISCI SEGNALAZIONE ---");
        System.out.print("ID Segnalazione: ");
        Long segnalazioneId = Long.parseLong(scanner.nextLine());

        System.out.println("Azioni disponibili:");
        System.out.println("  1. Archivia segnalazione");
        System.out.println("  2. Squalifica team");
        System.out.print("Scelta: ");
        String azione = scanner.nextLine().trim();

        switch (azione) {
            case "1" -> {
                Result<String> result = segnalazioneHandler.archiviaSegnalazione(segnalazioneId);
                if (result.isSuccess()) {
                    System.out.println("\n" + result.getData());
                } else {
                    System.out.println("\nErrore: " + result.getErrorMessage());
                }
            }
            case "2" -> {
                System.out.print("ID Team da squalificare: ");
                Long teamId = Long.parseLong(scanner.nextLine());
                System.out.print("ID Hackathon: ");
                Long hackathonId = Long.parseLong(scanner.nextLine());

                Result<String> result = segnalazioneHandler.squalificaTeam(segnalazioneId, teamId, hackathonId);
                if (result.isSuccess()) {
                    System.out.println("\n" + result.getData());
                } else {
                    System.out.println("\nErrore: " + result.getErrorMessage());
                }
            }
            default -> System.out.println("\nAzione non valida.");
        }
    }

    private void eseguiDemo() {
        System.out.println("\n========================================");
        System.out.println("        DEMO AUTOMATICA");
        System.out.println("========================================\n");

        // Crea Organizzatore
        System.out.println("1. Creazione Organizzatore...");
        Utente organizzatore = new Utente("Mario", "Rossi", "mario.rossi@email.com", "password123");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.add(organizzatore);
        System.out.println("   Organizzatore creato: " + organizzatore);

        // Crea Leader (utente che diventerà leader)
        System.out.println("\n2. Creazione Utente (futuro Leader)...");
        Utente leader = new Utente("Luigi", "Verdi", "luigi.verdi@email.com", "password456");
        leader = utenteRepository.add(leader);
        System.out.println("   Utente creato: " + leader);

        // Crea Team
        System.out.println("\n3. Creazione Team...");
        Result<Team> teamResult = teamHandler.creaTeam("Team Alpha", leader.getId());
        if (teamResult.isSuccess()) {
            System.out.println("   Team creato: " + teamResult.getData());
        }

        // Crea Hackathon
        System.out.println("\n4. Creazione Hackathon...");
        Result<Hackathon> hackathonResult = hackathonHandler.creaHackathonRequest(
                "Hackathon 2025",
                LocalDate.now().plusMonths(2),
                LocalDate.now().plusMonths(2).plusDays(3),
                "Grande evento di programmazione",
                "Nessun uso di AI, codice originale",
                LocalDate.now().plusMonths(1),
                5,
                10000.0,
                organizzatore.getId()
        );
        if (hackathonResult.isSuccess()) {
            System.out.println("   Hackathon creato: " + hackathonResult.getData());
        }

        // Iscrivi Team a Hackathon
        System.out.println("\n5. Iscrizione Team a Hackathon...");
        if (teamResult.isSuccess() && hackathonResult.isSuccess()) {
            Result<String> iscrizioneResult = iscrizioneHandler.iscriviTeam(
                    teamResult.getData().getId(),
                    hackathonResult.getData().getId()
            );
            if (iscrizioneResult.isSuccess()) {
                System.out.println("   " + iscrizioneResult.getData());
            } else {
                System.out.println("   Errore: " + iscrizioneResult.getErrorMessage());
            }
        }

        System.out.println("\n========================================");
        System.out.println("        DEMO COMPLETATA");
        System.out.println("========================================");
    }
}
