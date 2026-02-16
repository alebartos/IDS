package it.unicam.ids;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

/**
 * Classe principale per eseguire tutti i test del progetto.
 * Esegue automaticamente tutti i test presenti nel package it.unicam.ids
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   ESECUZIONE TEST - Progetto IDS");
        System.out.println("========================================\n");

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("it.unicam.ids"))
                .build();

        Launcher launcher = LauncherFactory.create();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        System.out.println("\n========================================");
        System.out.println("           RISULTATI TEST");
        System.out.println("========================================");
        System.out.println("Test eseguiti:  " + summary.getTestsStartedCount());
        System.out.println("Test superati:  " + summary.getTestsSucceededCount());
        System.out.println("Test falliti:   " + summary.getTestsFailedCount());
        System.out.println("Test ignorati:  " + summary.getTestsSkippedCount());
        System.out.println("========================================");

        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\n⚠ TEST FALLITI:");
            summary.getFailures().forEach(failure -> {
                System.out.println("  - " + failure.getTestIdentifier().getDisplayName());
                System.out.println("    Causa: " + failure.getException().getMessage());
            });
            System.out.println("\n❌ BUILD FAILED");
            System.exit(1);
        } else {
            System.out.println("\n✓ TUTTI I TEST SONO PASSATI!");
            System.exit(0);
        }
    }
}
