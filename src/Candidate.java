import java.util.Arrays;

public final class Candidate {
  private final double[] x; // immutable copy
  private final double value;

  public Candidate(double[] x, double value) {
    this.x = Arrays.copyOf(x, x.length);
    this.value = value;
  }

  public double[] x() {
    return Arrays.copyOf(x, x.length);
  }

  public double value() {
    return value;
  }

  @Override
  public String toString() {
    return "x=" + Arrays.toString(x) + "  f=" + value;
  }
}
