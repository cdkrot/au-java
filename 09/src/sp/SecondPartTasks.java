package sp;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class SecondPartTasks {

    private SecondPartTasks() {}

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        return paths.stream().filter((name) -> {
                try (Scanner sc = new Scanner(new File(name))) {
                    return sc.findInLine(sequence.toString()) != null;
                } catch (IOException ex) {
                    return false;
                }
            }).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать, какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        int numIter = (int)1e6;
        int seed = 228;
        
        int res = (int)Stream.generate(new Supplier<Boolean>() {
                Random rnd = new Random(seed);

                public Boolean get() {
                    double x = rnd.nextDouble() - 0.5;
                    double y = rnd.nextDouble() - 0.5;
                    return x * x + y * y <= 0.25;
                }
            }).limit(numIter).filter((x) -> x).count();

        return (double)res / (double)numIter;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> map) {
        ToIntFunction<Map.Entry<String, List<String>>> toint = (ent) -> {
            return ent.getValue().stream().collect(Collectors.summingInt((elem) -> elem.length()));
        };
        
        return map.entrySet().stream().max(Comparator.comparingInt(toint)).get().getKey();
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().flatMap((map) -> map.entrySet().stream())
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }
}
