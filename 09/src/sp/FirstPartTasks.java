package sp;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public final class FirstPartTasks {
    private FirstPartTasks() {}

    // Список названий альбомов
    public static List<String> allNames(Stream<Album> albums) {
        return albums.map(Album::getName).collect(Collectors.toList());
    }

    // Список названий альбомов, отсортированный лексикографически по названию
    public static List<String> allNamesSorted(Stream<Album> albums) {
        return albums.map(Album::getName).sorted().collect(Collectors.toList());
    }

    // Список треков, отсортированный лексикографически по названию, включающий все треки альбомов из 'albums'
    public static List<String> allTracksSorted(Stream<Album> albums) {
        return albums.flatMap((alb) -> alb.getTracks().stream()).map(Track::getName).sorted().collect(Collectors.toList());
    }

    // Список альбомов, в которых есть хотя бы один трек с рейтингом более 95, отсортированный по названию
    public static List<Album> sortedFavorites(Stream<Album> s) {
        return s.filter((alb) -> alb.getTracks().stream().anyMatch((tr) -> tr.getRating() > 95))
                        .sorted(Comparator.comparing(Album::getName)).collect(Collectors.toList());
    }

    // Сгруппировать альбомы по артистам
    public static Map<Artist, List<Album>> groupByArtist(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist));
    }

    // Сгруппировать альбомы по артистам (в качестве значения вместо объекта 'Artist' использовать его имя)
    public static Map<Artist, List<String>> groupByArtistMapName(Stream<Album> albums) {
        return albums.collect(Collectors.groupingBy(Album::getArtist, Collectors.mapping(Album::getName, Collectors.toList())));
    }

    // Число повторяющихся альбомов в потоке
    public static long countAlbumDuplicates(Stream<Album> albums) {
        return albums.collect(() -> new HashMap<Album, Integer>(), new BiConsumer<Map<Album, Integer>, Album>() {
                public void accept(Map<Album, Integer> state, Album alb) {
                    if (state.containsKey(alb))
                        state.put(alb, state.get(alb) + 1); // poor java
                    else
                        state.put(alb, 1);
                }
            }, (a, b) -> {throw new UnsupportedOperationException("poor java");})
            .entrySet().stream().filter((ent) -> ent.getValue() >= 2).count();
    }

    // Альбом, в котором максимум рейтинга минимален
    // (если в альбоме нет ни одного трека, считать, что максимум рейтинга в нем --- 0)
    public static Optional<Album> minMaxRating(Stream<Album> stream) {
        ToIntFunction<Album> f = (alb) -> alb.getTracks().stream().map(Track::getRating).max(Comparator.naturalOrder()).orElse(0);
        return stream.min(Comparator.comparingInt(f));
    }

    // Список альбомов, отсортированный по убыванию среднего рейтинга его треков (0, если треков нет)
    public static List<Album> sortByAverageRating(Stream<Album> albums) {
        return albums.sorted(Comparator.comparingDouble((alb) -> {
                    return - alb.getTracks().stream().collect(Collectors.averagingInt(Track::getRating));
                })).collect(Collectors.toList());
    }

    // Произведение всех чисел потока по модулю 'modulo'
    // (все числа от 0 до 10000)
    public static int moduloProduction(IntStream stream, int modulo) {
        return stream.reduce(1, (a, b) -> (a * b % modulo));
    }

    // Вернуть строку, состояющую из конкатенаций переданного массива, и окруженную строками "<", ">"
    // см. тесты
    public static String joinTo(String... strings) {
        return Arrays.stream(strings).collect(Collectors.joining(", ", "<", ">"));
    }

    // Вернуть поток из объектов класса 'cls'
    public static <R> Stream<R> filterIsInstance(Stream<?> s, Class<R> cls) {
        return s.filter(cls::isInstance).map(cls::cast);
    }
}
