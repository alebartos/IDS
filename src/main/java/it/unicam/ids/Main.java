package it.unicam.ids;

import it.unicam.ids.controller.HackathonHandler;
import it.unicam.ids.controller.IscrizioneHandler;
import it.unicam.ids.controller.Result;
import it.unicam.ids.controller.TeamHandler;
import it.unicam.ids.model.Hackathon;
import it.unicam.ids.model.Ruolo;
import it.unicam.ids.model.Team;
import it.unicam.ids.model.Utente;
import it.unicam.ids.repository.HackathonRepository;
import it.unicam.ids.repository.InvitoRepository;
import it.unicam.ids.repository.TeamRepository;
import it.unicam.ids.repository.UtenteRepository;
import it.unicam.ids.service.HackathonService;
import it.unicam.ids.service.IscrizioneService;
import it.unicam.ids.service.TeamService;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Classe principale per l'esecuzione dell'applicazione da console.
 * Permette di testare manualmente le funzionalita' del sistema.
 */
public class Main {

    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final InvitoRepository invitoRepository;

    private final TeamService teamService;
    private final HackathonService hackathonService;
    private final IscrizioneService iscrizioneService;

    private final TeamHandler teamHandler;
    private final HackathonHandler hackathonHandler;
    private final IscrizioneHandler iscrizioneHandler;

    private final Scanner scanner;

    public Main() {
        // Inizializzazione Repository
        utenteRepository = new UtenteRepository();
        teamRepository = new TeamRepository();
        hackathonRepository = new HackathonRepository();
        invitoRepository = new InvitoRepository();

        // Inizializzazione Service
        teamService = new TeamService(teamRepository, invitoRepository, utenteRepository);
        hackathonService = new HackathonService(hackathonRepository, utenteRepository, teamService);
        iscrizioneService = new IscrizioneService(teamRepository, hackathonRepository);

        // Inizializzazione Handler (Controller)
        teamHandler = new TeamHandler(teamService);
        hackathonHandler = new HackathonHandler(hackathonService);
        iscrizioneHandler = new IscrizioneHandler(iscrizioneService);

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
                case "6" -> eseguiDemo();
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
        System.out.println("6. Esegui Demo automatica");
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
        organizzatore = utenteRepository.save(organizzatore);

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
        leader = utenteRepository.save(leader);

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

    private void eseguiDemo() {
        System.out.println("\n========================================");
        System.out.println("        DEMO AUTOMATICA");
        System.out.println("========================================\n");

        // Crea Organizzatore
        System.out.println("1. Creazione Organizzatore...");
        Utente organizzatore = new Utente("Mario", "Rossi", "mario.rossi@email.com", "password123");
        organizzatore.getRuoli().add(Ruolo.ORGANIZZATORE);
        organizzatore = utenteRepository.save(organizzatore);
        System.out.println("   Organizzatore creato: " + organizzatore);

        // Crea Leader (utente che diventerà leader)
        System.out.println("\n2. Creazione Utente (futuro Leader)...");
        Utente leader = new Utente("Luigi", "Verdi", "luigi.verdi@email.com", "password456");
        leader = utenteRepository.save(leader);
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
