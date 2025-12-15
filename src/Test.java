import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Test- und Demonstrationsprogramm für den Bees Algorithm.
 *
 * Zweck:
 * - zeigt die Verwendung des Bees Algorithm mit verschiedenen Zielfunktionen
 * - demonstriert funktionale Übergabe von Funktionen (Function<double[], Double>)
 * - überprüft die Funktionsfähigkeit in 1D- und mehrdimensionalen Suchräumen
 * <p>
 * Aufgabenverteilung im Projekt
 *
 * Daniil Nilov:
 *   - Implementierung der Klassen aufgabe7.BeesParams, aufgabe7.Candidate und aufgabe7.SearchSpace
 *
 * Bogdan Semenenko:
 *   - Implementierung des Bees Algorithmus
 *   - Implementierung des aufgabe7.Test- und Demonstrationsprogramms
 *
 * Loujain Mahfoud:
 *   - Überarbeitung und Ergänzung der Kommentare
 *   - Refactoring und Strukturverbesserungen
 */

public class Test {

    public static void main(String[] args) {

        Random rnd = new Random(42);

        /* ------------------------------------------------------------
         * Beispiel 0: Demonstration der Hilfsklassen
         * ------------------------------------------------------------ */

        double[][] demoBounds = { { -10.0, 10.0 }, { -5.0, 5.0 } };
        SearchSpace space = new SearchSpace(demoBounds, rnd);

        Comparator<Double> maxComparator = (a, b) -> Double.compare(b, a);

        System.out.println("=== Demo: zufällige Punkte im Suchraum ===");
        space.randomPoints(5)
                .forEach(p -> System.out.println(java.util.Arrays.toString(p)));

        double[] x = { 1.0, 2.0 };
        Candidate c = new Candidate(x, 3.14);
        System.out.println("\nDemo aufgabe7.Candidate: " + c);

        BeesParams params = new BeesParams(
                10,   // steps
                20,   // n (scout bees)
                5,    // m (beste Felder)
                2,    // e (exzellente Felder)
                10,   // p (Rekrutierung exzellente Felder)
                5,    // q (Rekrutierung übrige Felder)
                0.5,  // s (Patchgröße relativ zum Suchraum)
                0.9,  // shrink pro Schritt
                3     // r (Anzahl zurückgegebener Lösungen)
        );

        System.out.println("\naufgabe7.BeesParams: steps=" + params.steps() + ", n=" + params.n());

        BeeAlgorithm ba = new BeeAlgorithm();

        /* ------------------------------------------------------------
         * Beispiel 1: Eindimensionale Optimierung
         * f(x) = sin(x), x ∈ [-1800, 1800], Maximierung
         * ------------------------------------------------------------ */

        System.out.println("\n=== Bees Algorithm: f(x) = sin(x) ===");

        double[][] bounds1D = { { -1800.0, 1800.0 } };

        List<double[]> resultsSin = ba.run(
                1,
                arr -> Math.sin(Math.toRadians(arr[0])), // Zielfunktion
                bounds1D,
                maxComparator,                         // Vergleich für Maximum
                params
        );

        resultsSin.forEach(r ->
                System.out.println("x = " + r[0] + "  f(x) = " +
                        Math.sin(Math.toRadians(r[0])))
        );

        /* ------------------------------------------------------------
         * Beispiel 2: Weitere 1D-Funktion
         * f(x) = cos(x), gleiche Suchdomäne
         * ------------------------------------------------------------ */

        System.out.println("\n=== Bees Algorithm: f(x) = cos(x) ===");

        List<double[]> resultsCos = ba.run(
                1,
                arr -> Math.cos(Math.toRadians(arr[0])),
                bounds1D,
                maxComparator,
                params
        );

        resultsCos.forEach(r ->
                System.out.println("x = " + r[0] + "  f(x) = " +
                        Math.cos(Math.toRadians(r[0])))
        );

        /* ------------------------------------------------------------
         * Beispiel 3: Mehrdimensionale Optimierung
         * f(x,y) = sin(x) * cos(y)
         * ------------------------------------------------------------ */

        System.out.println("\n=== Bees Algorithm: f(x,y) = sin(x) * cos(y) ===");

        double[][] bounds2D = {
                { -10.0, 10.0 },
                { -10.0, 10.0 }
        };

        List<double[]> results2D = ba.run(
                2,
                arr -> Math.sin(arr[0]) * Math.cos(arr[1]),
                bounds2D,
                maxComparator,
                params
        );

        results2D.forEach(r ->
                System.out.println("x = (" + r[0] + ", " + r[1] + ")  f(x,y) = " +
                        (Math.sin(r[0]) * Math.cos(r[1])))
        );
    }
}