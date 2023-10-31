package sh.miles.dove.collection;

/**
 * Stores a pair of items
 *
 * @param <F> first type
 * @param <S> second type
 */
public record Pair<F, S>(F first, S second) {

    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }
}
