package com.github.emw7.litersofmyblood.bloodyx.comparator;

import com.github.emw7.litersofmyblood.bloodyx.comparator.SortComparatorFactory.Nullity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SortComparatorFactoryTests {

  class Foo {

    private String name;
    private int i;

    private Foo(final String name, final int i) {
      this.name = name;
      this.i = i;
    }
  }

  // A.1
  @Test
  public void givenLeftWithNullName_whenComparingForNameASCAndIDESCAndNullsFirst_thenLeftIsLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo(null, 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // A.2
  @Test
  public void givenLeftWithNullName_whenComparingForNameDESCAndIDESCAndNullsFirst_thenLeftIsLessThan() {
    final Sort sort = Sort.by(Order.desc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo(null, 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // A.3
  @Test
  public void givenLeftWithNullName_whenComparingForNameASCAndIDESCAndNullsLast_thenLeftIsGreaterThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(new Foo(null, 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // B.1
  @Test
  public void givenLeftWithNullNameAndILessThan_whenComparingForIASCAndNullsFirst_thenLeftIsLessThan() {
    final Sort sort = Sort.by(Order.asc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo(null, 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // B.2
  @Test
  public void givenLeftWithNullNameAndILessThan_whenComparingForIDESCAndNullsFirst_thenLeftIsLessThan() {
    final Sort sort = Sort.by(Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo(null, 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // C.1
  @Test
  public void givenLeftNull_whenComparingNullsFirst_thenLeftIsLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(null, new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => 1").isLessThan(0);
  }

  // C.2
  @Test
  public void givenLeftNull_whenComparingNullsLast_thenLeftIsGreaterThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(null, new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => 1").isGreaterThan(0);
  }

  // C.3
  @Test
  public void givenBothNull_whenComparingNullsFirst_thenLeftEqualsTo() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(null, null);
    Assertions.assertThat(res).as("left is less than right => 1").isEqualTo(0);
  }

  // C.4
  @Test
  public void givenBothNull_whenComparingNullsLast_thenLeftEqualsTo() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(null, null);
    Assertions.assertThat(res).as("left is less than right => 1").isEqualTo(0);
  }

  // C.5
  @Test
  public void givenBothNull_whenComparingNotNullSafe_thenNullPointerException() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    Assertions.assertThatThrownBy(() -> comparator.compare(null, null))
        .isInstanceOf(NullPointerException.class);
  }

  // C.6
  @Test
  public void givenLeftNull_whenComparingNotNullSafe_thenNullPointerException() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    Assertions.assertThatThrownBy(() -> comparator.compare(null, new Foo("a", 1)))
        .isInstanceOf(NullPointerException.class);
  }

  // C.7
  @Test
  public void givenRightNull_whenComparingNotNullSafe_thenNullPointerException() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    Assertions.assertThatThrownBy(() -> comparator.compare(new Foo("a", 1), null))
        .isInstanceOf(NullPointerException.class);
  }

  // D.1
  @Test
  public void givenSortNull_whenAnyNullsFirst_thenLeftEqualsTo() {
    final Sort sort = null;
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // D.2
  @Test
  public void givenSortNull_whenAnyNullsLast_thenLeftEqualsTo() {
    final Sort sort = null;
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // D.3
  @Test
  public void givenSortNull_whenAnyNotNullSafe_thenLeftEqualsTo() {
    final Sort sort = null;
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // D.4
  @Test
  public void givenSortUnsorted_whenAnyNullsFirst_thenLeftEqualsTo() {
    final Sort sort = Sort.unsorted();
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // D.5
  @Test
  public void givenSortUnsorted_whenAnyNullsLast_thenLeftEqualsTo() {
    final Sort sort = Sort.unsorted();
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // D.6
  @Test
  public void givenSortUnsorted_whenAnyNotNullSafe_thenLeftEqualsTo() {
    final Sort sort = Sort.unsorted();
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // E.1
  @Test
  public void givenLeftLessThan_whenComparingForNameASCAndNullsLeft_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // E.2
  @Test
  public void givenLeftLessThan_whenComparingForNameASCAndNullsRight_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // E.3
  @Test
  public void givenLeftLessThan_whenComparingForNameASCAndNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // E.4
  @Test
  public void givenLeftLessThan_whenComparingForNameDESCAndNullsLeft_thenLeftGreaterThan() {
    final Sort sort = Sort.by(Order.desc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_FIRST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // E.5
  @Test
  public void givenLeftLessThan_whenComparingForNameDESCAndNullsRight_thenLeftGreaterThan() {
    final Sort sort = Sort.by(Order.desc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NULLS_LAST);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // E.6
  @Test
  public void givenLeftLessThan_whenComparingForNameDESCAndNotNullSafe_thenLeftGreaterThan() {
    final Sort sort = Sort.by(Order.desc("name"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("b", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // F.1
  @Test
  public void givenLeftEqualsTo_whenComparingForNameASCAndIASCNotNullSafe_thenLeftEqualsTo() {
    final Sort sort = Sort.by(Order.asc("name"), Order.asc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("a", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // F.2
  @Test
  public void givenLeftLessThan_whenComparingForNameASCAndIASCNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.asc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // F.3
  @Test
  public void givenLeftGreaterThan_whenComparingForNameASCAndIASCNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.asc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 1), new Foo("a", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // F.4
  @Test
  public void givenLeftEqualsTo_whenComparingForNameASCAndIDESCNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("a", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isEqualTo(0);
  }

  // F.5
  @Test
  public void givenLeftLessThan_whenComparingForNameASCAndIDESCNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 0), new Foo("a", 1));
    Assertions.assertThat(res).as("left is less than right => -1").isGreaterThan(0);
  }

  // F.6
  @Test
  public void givenLeftGreaterThan_whenComparingForNameASCAndIDESCNotNullSafe_thenLeftLessThan() {
    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);
    int res = comparator.compare(new Foo("a", 1), new Foo("a", 0));
    Assertions.assertThat(res).as("left is less than right => -1").isLessThan(0);
  }

  // G.1
  @Test
  public void givenList_whenComparingComparing_thenListIsSorted() {

    final Sort sort = Sort.by(Order.asc("name"), Order.desc("i"));
    final Comparator<Foo> comparator = SortComparatorFactory.of(Foo.class, sort,
        Nullity.NOT_NULL_SAFE);

    //final List<Foo> foos= generateRandomFoos(0,5,10);
    // it is possible to get the output of the this row and put into a List.of(); that's how
    // the List.of()s below have been created; (the row above is to create a random list of Foo).
    //System.out.println(foos.stream().map($ -> "new Foo(\"" + $.name+ "\"," + $.i + ")").collect(Collectors.joining(",")));

    List<Foo> unorderedFoos = List.of(new Foo("foo-005", 1), new Foo("foo-003", 2),
        new Foo("foo-001", 1), new Foo("foo-005", 3), new Foo("foo-002", 4), new Foo("foo-000", 4),
        new Foo("foo-000", 5), new Foo("foo-005", 0), new Foo("foo-002", 4), new Foo("foo-001", 2));
    List<Foo> sortedFoos = unorderedFoos.stream().sorted(comparator).collect(Collectors.toList());
    System.out.println(sortedFoos.stream().map($ -> "new Foo(\"" + $.name + "\"," + $.i + ")")
        .collect(Collectors.joining(",")));

    // I really hope this always passes!
    Assertions.assertThat(sortedFoos).as("").isSortedAccordingTo(comparator);

    List<Foo> expectedFoos = List.of(new Foo("foo-000", 5), new Foo("foo-000", 4),
        new Foo("foo-001", 2), new Foo("foo-001", 1), new Foo("foo-002", 4), new Foo("foo-002", 4),
        new Foo("foo-003", 2), new Foo("foo-005", 3), new Foo("foo-005", 1), new Foo("foo-005", 0));
    // I'm sorry but this the only mean I have found to verify sorted list is sorted correctly according with
    //  expected result.
    Assertions.assertThat(sortedFoos.stream().map($ -> String.format("(%s,%d)",$.name,$.i)).collect(
        Collectors.joining(";"))).as("").isEqualTo(expectedFoos.stream().map($ -> String.format("(%s,%d)",$.name,$.i)).collect(
        Collectors.joining(";")));
  }

  //region Support

  /**
   * Returns a random generated list of {@link Foo}
   *
   * @param imin                 min value that can get i
   * @param imax                 max value that can get i
   * @param howManyFooToGenerate if negative then generates a number of Foos among 0 and
   *                             -howManyFooToGenerate
   * @return a random generated list of {@link Foo}
   */
  private List<Foo> generateRandomFoos(final int imin, int imax, final int howManyFooToGenerate) {
    final int num = (howManyFooToGenerate >= 0) ? howManyFooToGenerate :
        // +2 because 2nd parameter is exclusive.
        ThreadLocalRandom.current().nextInt(0, -howManyFooToGenerate + 2);
    final List<Foo> foos = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      //  if max < Integer.MAX_VALUE we must add 1
      //  because in the row it is used as bound that
      //  is exclusive.
      final int name = ThreadLocalRandom.current().nextInt(imin, imax + 1);
      final int id = ThreadLocalRandom.current().nextInt(imin, imax + 1);
      final Foo foo = new Foo(String.format("foo-%03d", name), id);
      foos.add(foo);
    }
    return foos;
  }
  //endregion Support
}
