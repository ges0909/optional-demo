package schrader.optional;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OptionalTest {

    /**
     * empty(), of(), ofNullable(), isPresent
     */

    @Test
    public void when_optional_is_empty_then_is_present_returns_false() {
        Optional<String> s = Optional.empty();
        assertThat(s.isPresent()).isFalse(); // Java 11 adds 'isEmpty()'
    }

    @Test
    public void when_optional_is_present_then_is_present_returns_true() {
        Optional<String> s = Optional.of("test");
        assertThat(s.isPresent()).isTrue();
    }

    @Test
    public void when_optional_of_null_then_throws_null_pointer_exception() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Optional.of(null));
    }

    @Test
    public void when_optional_of_nullable_with_null_then_creates_empty_optional() {
        Optional<String> s = Optional.ofNullable(null);
        assertThat(s.isPresent()).isFalse();
    }

    @Test
    public void when_optional_of_nullable_with_non_null_then_creates_non_empty_optional() {
        Optional<String> s = Optional.ofNullable("test");
        assertThat(s.isPresent()).isTrue();
    }

    /**
     * get, orElse, orElseGet, orElseThrow
     */

    @Test
    public void when_optional_is_empty_then_get_throws_no_such_element_exception() {
        Optional<String> s = Optional.empty();
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> s.get());
    }

    @Test
    public void when_optional_is_empty_then_or_else_returns_default_value() {
        Optional<String> s = Optional.empty();
        String v = s.orElse("");
        assertThat(v).isEmpty();
    }

    @Test
    public void when_optional_is_empty_then_or_else_get_returns_supplied_value() {
        Optional<String> s = Optional.empty();
        String v = s.orElseGet(String::new);
        assertThat(v).isEmpty(); // supplier function, lazy evaluated
    }

    @Test
    public void when_optional_is_empty_then_or_else_throws_custom_exception() {
        Optional<String> s = Optional.empty();
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> s.orElseThrow(IllegalArgumentException::new));
    }

    /**
     * ifPresent
     */

    @Test
    public void when_optional_is_present_then_lambda_expression_is_executed() {
        Optional<String> s = Optional.of("test");
        s.ifPresent(v -> assertThat(v).isEqualTo("test")); // conditional action
    }

    /**
     * filter
     */

    @Test
    public void when_predicate_is_true_then_filter_returns_optional() {
        Optional<Integer> i = Optional.of(1);
        Optional<Integer> i2 = i.filter(n -> n == 1);
        assertThat(i2.isPresent()).isTrue();
    }

    @Test
    public void when_predicate_is_false_then_filter_returns_empty_optional() {
        Optional<Integer> i = Optional.of(1);
        Optional<Integer> i2 = i.filter(n -> n == 2);
        assertThat(i2.isPresent()).isFalse();
    }

    /**
     * map, flatMap
     */

    @Test
    public void when_optional_is_present_then_map_returns_mapped_optional() {
        Optional<String> s = Optional.of("test");
        Optional<String> s2 = s.map(String::toUpperCase);
        assertThat(s2).isEqualTo(Optional.of("TEST"));
    }

    @Test
    public void when_optional_is_empty_then_map_returns_empty_optional() {
        Optional<String> s = Optional.ofNullable(null);
        Optional<String> s2 = s.map(String::toUpperCase);
        assertThat(s2).isEqualTo(Optional.empty());
    }

    @Test
    public void when_optional_is_present_then_flatmap_returns_mapped_value() {
        Optional<Person> person = Optional.of(new Person("Harry"));
        String name = person.flatMap(Person::getName /* returns an Optional */).orElse("unknown");
        assertThat(name).isEqualTo("Harry");
    }

    @Test
    public void when_optional_is_present_then_flatmap_returns_default_value() {
        Optional<Person> person = Optional.of(new Person(null));
        String name = person.flatMap(Person::getName /* returns an Optional */).orElse("unknown");
        assertThat(name).isEqualTo("unknown");
    }

    /**
     * convert empty string to empty optional
     */

    @Test
    public void when_string_is_empty_then_filter_returns_empty_optional() {
        String s = "";
        // convert empty string to empty optional
        // filter is not applied if optional is empty
        Optional<String> o = Optional.ofNullable(s).filter(val -> !val.isEmpty());
        assertThat(o.isPresent()).isFalse();
    }

    /**
     * Java 9
     */

    @Test
    public void stream() { // Java 9
    }

    @Test
    public void or() { // Java 9
    }

    @Test
    public void ifPresentOrElse() { // requires Java 9
        String v = "any";
        /* Optional<String> value = */
        Optional.of(v);
        // value.ifPresentOrElse(s -> System.out.println("found: " + s), () ->
        // System.out.println("not found"));
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
