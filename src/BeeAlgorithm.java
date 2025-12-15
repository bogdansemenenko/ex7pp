import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementierung des Bees Algorithmus als Metaheuristik zur Optimierung.
 *
 * Der Algorithmus kombiniert:
 * - globale Zufallssuche (Scout Bees)
 * - lokale Suche um die besten Lösungen (Patches)
 * - schrittweise Verkleinerung der Suchregionen
 *
 * STYLE:
 * - funktional: Bewertung der Kandidaten über Function<double[], Double>,
 *   Selektion und Sortierung über Streams
 * - imperativ: Iterationen, Patch-Verarbeitung, Zufallsgenerierung
 */
public final class BeeAlgorithm {

    /**
     * Führt den Bees Algorithmus für eine gegebene Zielfunktion aus.
     *
     * @param a          Dimension des Suchraums
     * @param f          Zielfunktion f(x)
     * @param bounds     globale Suchgrenzen
     * @param comparator Vergleichsfunktion (Minimum oder Maximum)
     * @param params     Algorithmusparameter
     *
     * @return Liste der besten gefundenen Lösungen (nur x-Werte)
     *
     * @PRE  a == bounds.length
     * @PRE  f != null; bounds != null; comparator != null; params != null
     */
    public List<double[]> run(
            int a,
            Function<double[], Double> f,
            double[][] bounds,
            Comparator<Double> comparator,
            BeesParams params) {

        if (a != bounds.length) {
            throw new IllegalArgumentException("Dimension a stimmt nicht mit bounds überein");
        }

        Random rnd = new Random(123);
        SearchSpace space = new SearchSpace(bounds, rnd);

        // Initiale Scout Bees (globale Zufallssuche)
        List<Candidate> population =
                space.randomPoints(params.n())
                        .map(x -> new Candidate(x, f.apply(x)))
                        .collect(Collectors.toList());

        double shrink = 1.0;

        for (int step = 0; step < params.steps(); step++) {

            // Bewertung und Sortierung der aktuellen Population
            population = population.stream()
                    .sorted((c1, c2) -> comparator.compare(c1.value(), c2.value()))
                    .collect(Collectors.toList());

            // Auswahl der besten m Kandidaten (Patch-Zentren)
            List<Candidate> best = population.stream()
                    .limit(params.m())
                    .collect(Collectors.toList());

            List<Candidate> nextPopulation = new ArrayList<>();

            // Lokale Suche um exzellente Patches
            for (int i = 0; i < params.e(); i++) {
                Candidate center = best.get(i);
                double[][] patchBounds =
                        space.makePatchBounds(center.x(), params.s(), shrink);

                space.randomPoints(params.p())
                        .map(x -> space.randomPointInBounds(patchBounds))
                        .map(x -> new Candidate(x, f.apply(x)))
                        .max((c1, c2) -> comparator.compare(c1.value(), c2.value()))
                        .ifPresent(nextPopulation::add);
            }

            // Lokale Suche um übrige gute Patches
            for (int i = params.e(); i < params.m(); i++) {
                Candidate center = best.get(i);
                double[][] patchBounds =
                        space.makePatchBounds(center.x(), params.s(), shrink);

                space.randomPoints(params.q())
                        .map(x -> space.randomPointInBounds(patchBounds))
                        .map(x -> new Candidate(x, f.apply(x)))
                        .max((c1, c2) -> comparator.compare(c1.value(), c2.value()))
                        .ifPresent(nextPopulation::add);
            }

            // Globale Suche für verbleibende Scout Bees
            int scoutsLeft = params.n() - nextPopulation.size();
            if (scoutsLeft > 0) {
                nextPopulation.addAll(
                        space.randomPoints(scoutsLeft)
                                .map(x -> new Candidate(x, f.apply(x)))
                                .collect(Collectors.toList()));
            }

            population = nextPopulation;
            shrink *= params.shrinkPerStep();
        }

        // Rückgabe der besten r Lösungen
        return population.stream()
                .sorted((c1, c2) -> comparator.compare(c1.value(), c2.value()))
                .limit(params.resultCount())
                .map(Candidate::x)
                .collect(Collectors.toList());
    }
}