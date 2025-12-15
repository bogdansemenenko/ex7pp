import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class SearchSpace {
  private final double[][] globalBounds;
  private final Random rnd;

  public SearchSpace(double[][] globalBounds, Random rnd) {
    this.globalBounds = copy2D(globalBounds);
    this.rnd = rnd;
  }

  public int dim() {
    return globalBounds.length;
  }

  public double low(int i) {
    return globalBounds[i][0];
  }

  public double high(int i) {
    return globalBounds[i][1];
  }

  public double range(int i) {
    return high(i) - low(i);
  }

  public Stream<double[]> randomPoints(int count) {
    return IntStream.range(0, count).mapToObj(k -> randomPointGlobal());
  }

  public double[] randomPointGlobal() {
    double[] x = new double[dim()];
    for (int i = 0; i < dim(); i++) {
      x[i] = low(i) + rnd.nextDouble() * range(i);
    }
    return x;
  }

  public double[] randomPointInBounds(double[][] localBounds) {
    double[] x = new double[dim()];
    for (int i = 0; i < dim(); i++) {
      double lo = localBounds[i][0];
      double hi = localBounds[i][1];
      x[i] = lo + rnd.nextDouble() * (hi - lo);
    }
    return x;
  }

  public double[][] makePatchBounds(double[] center, double sRel, double shrinkFactor) {
    // patch half-range = (globalRange * sRel * shrinkFactor) / 2
    double[][] b = new double[dim()][2];
    for (int i = 0; i < dim(); i++) {
      double half = (range(i) * sRel * shrinkFactor) / 2.0;
      double lo = center[i] - half;
      double hi = center[i] + half;

      // clamp to global bounds w
      lo = Math.max(lo, low(i));
      hi = Math.min(hi, high(i));

      // safety: if rounding makes lo==hi, widen minimally within global
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

  private static double[][] copy2D(double[][] a) {
    double[][] c = new double[a.length][2];
    for (int i = 0; i < a.length; i++) {
      c[i][0] = a[i][0];
      c[i][1] = a[i][1];
    }
    return c;
  }
}
