package com.github.emw7.litersofmyblood.bloodyx.comparator;


import java.lang.reflect.Field;
import java.util.Comparator;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public final class SortComparatorFactory {

  //region Constructors
  // prevents instantiation.
  private SortComparatorFactory() {}
  //endregion Constructors

  //region Supporting types
  /**
   * How to comparator must manage null.
   */
  public enum Nullity {
    NULLS_FIRST, // considers null to be less than non-null. When both are null, they are considered equal
    NULLS_LAST, // considers null to be greater than non-null. When both are null, they are considered equal
    NOT_NULL_SAFE
  }
  //endregion Supporting types

  //region API

  /**
   * Returns a comparator for the type {@code T} for the specified {@link Sort}.
   * <p>
   * The built comparator compares one by one the properties specified by {@code sort} in the order
   * returned by {@link Sort#iterator()}<br>
   * The comparator stops at the first comparison that return something not equal to 0. If all
   * comparisons return 0 then the comparator returns 0.<br>
   * If specified sort is either {@code null} or {@code unsorted} then the built comparator is a
   * comparator that return always 0.
   *
   * @param tClass  the class of the compared objects
   * @param sort    the {@link Sort} for which create the comparator
   * @param nullity how to manage (compare)  @code null}
   * @param <T>     the type of compared objects
   * @return a comparator for the type {@code T} for the specified {@link Sort}
   * @throws NullPointerException if {@code nullity} is equal to {@link Nullity#NOT_NULL_SAFE} and
   * an element to be compared is {@code null}
   * @see Nullity
   */
  public static <T> @NonNull Comparator<T> of(@NonNull final Class<T> tClass,
      @Nullable final Sort sort, @NonNull final Nullity nullity) {

    // preferred this form instead of Optional.ofNullable(sort).orElse(Sort.unsorted());
    if (sort == null || sort.isUnsorted()) {
      return wrapForNullity((tLeft, tRight) -> 0, nullity);
    }
    // else...
    // this is the comparator that is used to compare the value of the properties;
    //  note they must be comparable.
    final Comparator<Comparable<Object>> c = wrapForNullity(Comparable::compareTo, nullity);
    return wrapForNullity((left, right) -> {
      for (final Order order : sort) {
        final Comparable<Object> cleft = valueExtractor(left, order.getProperty());
        final Comparable<Object> cright = valueExtractor(right, order.getProperty());
        // reverse order if order is DESCending.
        final int res = c.compare(cleft, cright) * (order.isDescending() ? -1 : 1);
        if (res != 0) {
          return res;
        }
      }
      return 0;
    }, nullity);
  }
  //endregion API

  //region Private methods

  /**
   * Returns a comparator that wraps the specified comparator by managing {@code null} according to
   * {@code nullity}.
   *
   * @param comparator the comparator to be wrapped
   * @param nullity    how to manage (compare) {@code null}
   * @param <T>        the type of the compared objects
   * @return a comparator that wraps the specified comparator by managing {@code null} according to
   * {@code nullability}
   * @see SortComparatorFactory.Nullity
   */
  private static <T> @NonNull Comparator<T> wrapForNullity(@NonNull final Comparator<T> comparator,
      @NonNull final Nullity nullity) {
    return switch (nullity) {
      case NULLS_FIRST -> Comparator.nullsFirst(comparator);
      case NULLS_LAST -> Comparator.nullsLast(comparator);
      case NOT_NULL_SAFE -> comparator;
    };
  }

  private static <T> Comparable<Object> valueExtractor(@NonNull final T t,
      @NonNull final String propertyName) {
    try {
      Field field = t.getClass().getDeclaredField(propertyName);
      if (field.trySetAccessible()) {
        @SuppressWarnings("unchecked") final Comparable<Object> v = (Comparable<Object>) field.get(
            t);
        return v;
      } else {
        return null;
      }
    } catch (NoSuchFieldException | IllegalAccessException | ClassCastException e) {
      return null;
    }
  }
  //endregion Private methods

}
