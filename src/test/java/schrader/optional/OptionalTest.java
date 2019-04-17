package schrader.optional;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OptionalTest {

    /**
     * empty, of, ofNullable, isPresent
     */

    @Test
    public void when_optional_is_empty_then_is_present_returns_false() {
        Optional<String> o = Optional.empty();
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public void when_optional_is_present_then_is_present_returns_true() {
        Optional<String> o = Optional.of("test");
        assertThat(o.isPresent()).isTrue();
    }

    @Test
    public void when_optional_of_with_null_then_throws_null_pointer_exception() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Optional.of(null));
    }

    @Test
    public void when_optional_of_nullable_with_null_then_creates_empty_optional() {
        Optional<String> o = Optional.ofNullable(null);
        assertThat(o.isPresent()).isFalse();
    }

    @Test
    public void when_optional_of_nullable_with_non_null_then_creates_non_empty_optional() {
        Optional<String> o = Optional.ofNullable("test");
        assertThat(o.isPresent()).isTrue();
    }

    /**
     * get, orElse, orElseGet, orElseThrow
     */

    @Test
    public void when_optional_is_empty_then_get_throws_no_such_element_exception() {
        Optional<String> o = Optional.empty();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> o.get());
    }

    @Test
    public void when_optional_is_empty_then_or_else_returns_default_value() {
        Optional<String> o = Optional.empty();
        String s = o.orElse("default");
        assertThat(s).isEqualTo("default");
    }

    @Test
    public void when_optional_is_empty_then_or_else_get_returns_supplied_value() {
        Optional<String> o = Optional.empty();
        Supplier<String> supplier = () -> {
            return "default";
        };
        String s = o.orElseGet(supplier);
        assertThat(s).isEqualTo("default"); // supplier function, lazy evaluated
    }

    @Test
    public void when_optional_is_empty_then_or_else_throws_custom_exception() {
        Optional<String> o = Optional.empty();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> o.orElseThrow(IllegalArgumentException::new));
    }

    /**
     * ifPresent
     */

    @Test
    public void when_optional_is_present_then_lambda_expression_is_executed() {
        Optional<String> o = Optional.of("test");
        o.ifPresent(s -> assertThat(s).isEqualTo("test")); // conditional action
    }

    /**
     * filter
     */

    @Test
    public void when_predicate_is_true_then_filter_returns_optional() {
        Optional<Integer> o = Optional.of(1);
        Optional<Integer> o2 = o.filter(n -> n == 1);
        assertThat(o2.isPresent()).isTrue();
    }

    @Test
    public void when_predicate_is_false_then_filter_returns_empty_optional() {
        Optional<Integer> o = Optional.of(1);
        Optional<Integer> o2 = o.filter(n -> n == 2);
        assertThat(o2.isPresent()).isFalse();
    }

    /**
     * map, flatMap
     */

    @Test
    public void when_optional_is_present_then_map_returns_mapped_optional() {
        Optional<String> o = Optional.of("test");
        Optional<String> o2 = o.map(String::toUpperCase);
        assertThat(o2).isEqualTo(Optional.of("TEST"));
    }

    @Test
    public void when_optional_is_empty_then_map_returns_empty_optional() {
        Optional<String> o = Optional.empty();
        Optional<String> o2 = o.map(String::toUpperCase);
        assertThat(o2).isEqualTo(Optional.empty());
    }

    @Test
    public void when_optional_is_present_then_flatmap_returns_mapped_value() {
        Optional<Person> o = Optional.of(new Person("Harry"));
        String name = o.flatMap(Person::getName /* returns an Optional */).orElse("unknown");
        assertThat(name).isEqualTo("Harry");
    }

    @Test
    public void when_optional_is_empty_then_flatmap_returns_default_value() {
        Optional<Person> o = Optional.of(new Person(null));
        String name = o.flatMap(Person::getName /* returns an Optional */).orElse("unknown");
        assertThat(name).isEqualTo("unknown");
    }

    /**
     * convert empty string to empty optional
     */

    @Test
    public void when_string_is_empty_then_filter_returns_empty_optional() {
        String s = "";
        // convert empty string to empty optional; 'filter' returns empty optional if predicate evaluates to 'false'
        Optional<String> o = Optional.ofNullable(s).filter(val -> !val.isEmpty());
        assertThat(o.isPresent()).isFalse();
    }

    /**
     * Java 9: ofNullable, ifPresentOrElse, stream, or
     */

    @Test
    public void when_optional_of_nullable_with_null_then_is_empty_returns_true() {
        Optional<String> o = Optional.ofNullable(null);
        assertThat(o.isEmpty()).isTrue();
    }

    @Test
    public void when_optional_of_nullable_with_non_null_then_is_present_returns_true() {
        Optional<String> o = Optional.ofNullable("any");
        assertThat(o.isPresent()).isTrue();
    }

    @Test
    public void when_optional_is_present_then_present_branch_is_executed() {
        Optional<String> o = Optional.of("any");
        o.ifPresentOrElse(s -> assertThat(s).isEqualTo("any"), () -> assertThat(true).isFalse());
    }

    @Test
    public void when_optional_is_present_then_else_branch_is_executed() {
        Optional<String> o = Optional.ofNullable(null);
        o.ifPresentOrElse(s -> assertThat(s).isEqualTo("any"), () -> assertThat(true).isTrue());
    }

    @Test
    public void stream() {
        List<Optional<String>> l = Arrays.asList(
                Optional.ofNullable("one"),
                Optional.empty(),
                Optional.of("two"),
                Optional.ofNullable(null),
                Optional.of("three"));
        String[] a = l.stream()
                .flatMap(Optional::stream)
                .toArray(String[]::new);
        assertThat(a).isEqualTo(new String[]{"one", "two", "three"});
    }

    @Test
    public void or() {
    }

    /**
     * Java 11:  isEmpty()
     */

    @Test
    public void when_optional_is_empty_then_is_empty_returns_true() {
        Optional<String> o = Optional.empty();
        assertThat(o.isEmpty()).isTrue();
    }

    class Person {
        private String name;

        Person(String name) {
            this.name = name;
        }

        Optional<String> getName() {
            return Optional.ofNullable(name);
        }
    }
}
