import java.util.Arrays;

/**
 * Repräsentiert einen einzelnen Kandidaten im Suchraum des Bees Algorithmus.
 * <p>
 * Ein Kandidat besteht aus:
 * - einem Punkt x im Suchraum
 * - dem zugehörigen Funktionswert f(x)
 * <p>
 * STYLE: datenorientiert, immutable Value-Object.
 *
 * @param x     Punkt im Suchraum (defensiv kopiert).
 * @param value Funktionswert f(x).
 * @INV x != null; Länge von x bleibt konstant
 */
public record Candidate(double[] x, double value) {

    /**
     * Erstellt einen neuen Kandidaten.
     *
     * @param x     Punkt im Suchraum
     * @param value Funktionswert an diesem Punkt
     * @PRE x != null
     * @POST interner Zustand ist unveränderlich (defensive Kopie)
     */
    public Candidate(double[] x, double value) {
        this.x = Arrays.copyOf(x, x.length);
        this.value = value;
    }

    /**
     * Gibt den Punkt im Suchraum zurück.
     *
     * @return Kopie von x
     * @POST Rückgabe ist unabhängig vom internen Zustand
     */
    @Override
    public double[] x() {
        return Arrays.copyOf(x, x.length);
    }

    /**
     * Gibt den Funktionswert f(x) zurück.
     *
     * @return Funktionswert
     */
    @Override
    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return "x=" + Arrays.toString(x) + "  f=" + value;
    }
}