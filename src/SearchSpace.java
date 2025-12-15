import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Repräsentiert den Suchraum des Bees Algorithmus.
 *
 * Der Suchraum ist durch globale Intervallgrenzen pro Dimension definiert
 * und stellt Hilfsmethoden zur Erzeugung zufälliger Punkte bereit.
 *
 * STYLE: Hilfsklasse / Utility für stochastische Punktgenerierung.
 *
 * @INV globalBounds != null; für alle Dimensionen gilt low < high
 */


public final class SearchSpace {

    /** Globale Suchgrenzen pro Dimension: [i][0]=low, [i][1]=high */
    private final double[][] globalBounds;

    /** Zufallsgenerator für reproduzierbare Punktwahl */
    private final Random rnd;

    /**
     * Erstellt einen neuen Suchraum.
     *
     * @param globalBounds globale Intervallgrenzen
     * @param rnd          Zufallsgenerator
     *
     * @PRE globalBounds != null; rnd != null
     * @POST interne Kopie der Grenzen wird angelegt
     */
    public SearchSpace(double[][] globalBounds, Random rnd) {
        this.globalBounds = copy2D(globalBounds);
        this.rnd = rnd;
    }

    /** @return Dimension des Suchraums */
    public int dim() {
        return globalBounds.length;
    }

    /** @return untere Grenze der Dimension i */
    public double low(int i) {
        return globalBounds[i][0];
    }

    /** @return obere Grenze der Dimension i */
    public double high(int i) {
        return globalBounds[i][1];
    }

    /** @return Intervallbreite der Dimension i */
    public double range(int i) {
        return high(i) - low(i);
    }

    /**
     * Erzeugt einen Stream zufälliger Punkte im globalen Suchraum.
     *
     * @param count Anzahl der Punkte
     * @return Stream zufälliger Punkte
     */
    public Stream<double[]> randomPoints(int count) {
        return IntStream.range(0, count)
                .mapToObj(k -> randomPointGlobal());
    }

    /**
     * Erzeugt einen zufälligen Punkt im gesamten Suchraum.
     *
     * @return zufälliger Punkt x
     */
    public double[] randomPointGlobal() {
        double[] x = new double[dim()];
        for (int i = 0; i < dim(); i++) {
            x[i] = low(i) + rnd.nextDouble() * range(i);
        }
        return x;
    }

    /**
     * Erzeugt einen zufälligen Punkt innerhalb lokaler Grenzen
     * (z. B. eines Patches).
     *
     * @param localBounds lokale Intervallgrenzen
     * @return zufälliger Punkt innerhalb der lokalen Grenzen
     */
    public double[] randomPointInBounds(double[][] localBounds) {
        double[] x = new double[dim()];
        for (int i = 0; i < dim(); i++) {
            double lo = localBounds[i][0];
            double hi = localBounds[i][1];
            x[i] = lo + rnd.nextDouble() * (hi - lo);
        }
        return x;
    }

    /**
     * Berechnet die lokalen Grenzen (Patch) um ein Zentrum.
     * <p>
     * Die Patchgröße basiert auf einem relativen Anteil des globalen Suchraums
     * und kann pro Iteration schrumpfen.
     *
     * @param center       Zentrum des Patches
     * @param sRel         relative Patchgröße
     * @param shrinkFactor Schrumpffaktor
     * @return lokale Intervallgrenzen
     */
    public double[][] makePatchBounds(double[] center, double sRel, double shrinkFactor) {
        double[][] b = new double[dim()][2];
        for (int i = 0; i < dim(); i++) {
            double half = (range(i) * sRel * shrinkFactor) / 2.0;
            double lo = center[i] - half;
            double hi = center[i] + half;

            // Begrenzung auf globale Suchgrenzen
            lo = Math.max(lo, low(i));
            hi = Math.min(hi, high(i));

            // Sicherheit gegen numerische Degeneration
            if (hi <= lo) {
                double mid = Math.max(Math.min(center[i], high(i)), low(i));
                lo = Math.max(low(i), mid - 1e-12);
                hi = Math.min(high(i), mid + 1e-12);
            }
            b[i][0] = lo;
            b[i][1] = hi;
        }
        return b;
    }

    /** Interne Hilfsmethode zum defensiven Kopieren von 2D-Arrays. */
    private static double[][] copy2D(double[][] a) {
        double[][] c = new double[a.length][2];
        for (int i = 0; i < a.length; i++) {
            c[i][0] = a[i][0];
            c[i][1] = a[i][1];
        }
        return c;
    }
}