package it.unicam.ids;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.PrintWriter;
import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("com.ids"))
                .build();

        Launcher launcher = LauncherFactory.create();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();

        System.out.println("\n========================================");
        System.out.println("         RISULTATI DEI TEST");
        System.out.println("========================================");
        System.out.println("Test trovati:    " + summary.getTestsFoundCount());
        System.out.println("Test avviati:    " + summary.getTestsStartedCount());
        System.out.println("Test superati:   " + summary.getTestsSucceededCount());
        System.out.println("Test falliti:    " + summary.getTestsFailedCount());
        System.out.println("Test ignorati:   " + summary.getTestsSkippedCount());
        System.out.println("Test abortiti:   " + summary.getTestsAbortedCount());
        System.out.println("========================================");

        List<TestExecutionSummary.Failure> failures = summary.getFailures();
        if (!failures.isEmpty()) {
            System.out.println("\nDETTAGLIO FALLIMENTI:");
            System.out.println("----------------------------------------");
            for (TestExecutionSummary.Failure failure : failures) {
                System.out.println("FALLITO: " + failure.getTestIdentifier().getDisplayName());
                System.out.println("  Causa: " + failure.getException().getMessage());
                System.out.println();
            }
        }

        summary.printTo(new PrintWriter(System.out));

        if (summary.getTestsFailedCount() > 0) {
            System.out.println("\nRISULTATO FINALE: ALCUNI TEST SONO FALLITI");
            System.exit(1);
        } else {
            System.out.println("\nRISULTATO FINALE: TUTTI I TEST SONO PASSATI");
        }
    }
}
