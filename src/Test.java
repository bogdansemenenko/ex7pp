import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.IntStream;

public class Test {

  public static void main(String[] args) {
    Random rnd = new Random(42);

    // === Пример 1: SearchSpace и randomPoints ===
    double[][] bounds = { { -10.0, 10.0 }, { -5.0, 5.0 } };
    SearchSpace space = new SearchSpace(bounds, rnd);

    System.out.println("=== Random points ===");
    space.randomPoints(5)
        .forEach(p -> System.out.println(java.util.Arrays.toString(p)));

    // === Пример 2: Candidate ===
    double[] x = { 1.0, 2.0 };
    Candidate c = new Candidate(x, 3.14);
    System.out.println("Candidate: " + c);

    // === Пример 3: BeesParams ===
    BeesParams params = new BeesParams(
        10, 20, 5, 2, 10, 5, 0.5, 0.9, 3);
    System.out.println("BeesParams: steps=" + params.steps + ", n=" + params.n);

    System.out.println("=====BeeAlgorithm=====");
    BeeAlgorithm ba = new BeeAlgorithm();
    double[][] testBounds = { { -1800.0, 1800.0 } };
    var results = ba.run(
        1,
        (arr) -> Math.sin(Math.toRadians(arr[0])), // f(x)
        testBounds,
        Double::compare, // c - сравнение для максимума
        params);

    System.out.println("=== BeeAlgorithm results ===");
    results.forEach(System.out::println);
  }
}

class BeeAlgorithm {

  /**
   * Простейшая заглушка, которая возвращает случайные точки в пределах bounds
   */
  public List<double[]> run(int a, java.util.function.Function<double[], Double> f,
      double[][] bounds,
      java.util.Comparator<Double> c,
      BeesParams params) {

    Random rnd = new Random(123);
    SearchSpace space = new SearchSpace(bounds, rnd);

    return IntStream.range(0, params.resultCount)
        .mapToObj(i -> space.randomPointGlobal())
        .collect(Collectors.toList());
  }
}
