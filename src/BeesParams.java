public final class BeesParams {
  // t
  public final int steps;

  // n, m, e, p, q
  public final int n;
  public final int m;
  public final int e;
  public final int p;
  public final int q;

  // s (patch size relative to global range), and shrink per step
  public final double s;
  public final double shrinkPerStep;

  // r
  public final int resultCount;

  public BeesParams(int steps, int n, int m, int e, int p, int q,
      double s, double shrinkPerStep, int resultCount) {
    if (steps <= 0)
      throw new IllegalArgumentException("steps must be > 0");
    if (n <= 0)
      throw new IllegalArgumentException("n must be > 0");
    if (m <= 0 || m >= n)
      throw new IllegalArgumentException("m must be < n and > 0");
    if (e <= 0 || e >= m)
      throw new IllegalArgumentException("e must be < m and > 0");
    if (q <= 0 || q >= p)
      throw new IllegalArgumentException("need 0 < q < p");
    if (s <= 0.0 || s > 1.0)
      throw new IllegalArgumentException("s must be in (0,1]");
    if (shrinkPerStep <= 0.0 || shrinkPerStep > 1.0)
      throw new IllegalArgumentException("shrinkPerStep must be in (0,1]");
    if (resultCount <= 0)
      throw new IllegalArgumentException("resultCount must be > 0");

    this.steps = steps;
    this.n = n;
    this.m = m;
    this.e = e;
    this.p = p;
    this.q = q;
    this.s = s;
    this.shrinkPerStep = shrinkPerStep;
    this.resultCount = resultCount;
  }
}
