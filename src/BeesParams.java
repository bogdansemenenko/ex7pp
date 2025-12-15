/**
 * Parametercontainer für den Bees Algorithm.
 * <p>
 * Enthält alle numerischen Steuerparameter des Algorithmus
 * (Anzahl Bienen, Elite-Plätze, Patch-Größe, Iterationen).
 * <p>
 * STYLE: datenorientiert, immutable.
 *
 * @param steps       Anzahl der Iterationen (t).
 * @param n           n – Gesamtanzahl der Bienen
 *                    m – Anzahl der ausgewählten besten Plätze
 *                    e – Anzahl der Elite-Plätze
 *                    p – Rekrutierte Bienen pro Elite-Platz
 *                    q – Rekrutierte Bienen pro Nicht-Elite-Platz
 * @param s           s – relative Patch-Größe bezogen auf den Suchraum
 *                    shrinkPerStep – Schrumpfung der Patch-Größe pro Iteration
 * @param resultCount Anzahl der besten Ergebnisse, die zurückgegeben werden.
 * @INV Alle Parameter liegen in den im Konstruktor geprüften Wertebereichen.
 */
public record BeesParams(int steps, int n, int m, int e, int p, int q, double s, double shrinkPerStep,
                         int resultCount) {

    /**
     * Erstellt ein validiertes Parameterset für den Bees Algorithm.
     *
     * @PRE Alle Parameter erfüllen die dokumentierten Schranken
     * @POST Objekt ist vollständig initialisiert und unveränderlich
     */
    public BeesParams {

        if (steps <= 0) throw new IllegalArgumentException("steps must be > 0");
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        if (m <= 0 || m >= n) throw new IllegalArgumentException("m must be < n");
        if (e <= 0 || e >= m) throw new IllegalArgumentException("e must be < m");
        if (q <= 0 || q >= p) throw new IllegalArgumentException("need 0 < q < p");
        if (s <= 0.0 || s > 1.0) throw new IllegalArgumentException("s in (0,1]");
        if (shrinkPerStep <= 0.0 || shrinkPerStep > 1.0)
            throw new IllegalArgumentException("shrinkPerStep in (0,1]");
        if (resultCount <= 0) throw new IllegalArgumentException("resultCount > 0");

    }
}