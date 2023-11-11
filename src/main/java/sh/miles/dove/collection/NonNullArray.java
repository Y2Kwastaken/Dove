package sh.miles.dove.collection;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * An array like data type which prevents null elements from occurring
 *
 * @param <E> the type of contents
 */
@SuppressWarnings("unchecked")
public class NonNullArray<E> implements Iterable<E> {

    private final Supplier<E> onNullElement;
    private final Object[] array;

    public NonNullArray(int size, Supplier<E> onNullElement) {
        this.array = new Object[size];
        this.onNullElement = onNullElement;
        fillAllNonNull();
    }

    /**
     * Inserts an element in the array
     *
     * @param index the index to insert at
     * @param item  the item to insert
     * @return the previous item
     */
    @NotNull
    public E insert(final int index, @NotNull final E item) {
        Preconditions.checkArgument(index < array.length && index >= 0, "The provided index must be between 0 and %d".formatted(array.length));
        final E previous = (E) array[index];
        array[index] = item;
        return previous;
    }

    /**
     * Inserts an element in the array
     *
     * @param index the index
     * @param item  the possible item
     * @return the previous item
     */
    @NotNull
    public E insertNullable(final int index, @Nullable final E item) {
        Preconditions.checkArgument(index < array.length && index >= 0, "The provided index must be between 0 and %d".formatted(array.length));
        final E previous = (E) array[index];
        array[index] = item == null ? onNullElement.get() : item;
        return previous;
    }

    /**
     * @param index the index to get the item from
     * @return the item at that index
     */
    @NotNull
    public E get(final int index) {
        Preconditions.checkArgument(index < array.length && index >= 0, "The provided index must be between 0 and %d".formatted(array.length));
        return (E) array[index];
    }

    public int length() {
        return array.length;
    }

    private void fillAllNonNull() {
        for (int i = 0; i < array.length; i++) {
            array[i] = onNullElement.get();
        }
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return (Iterator<E>) Arrays.stream(this.array).iterator();
    }
}
