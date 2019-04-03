package schrader.optional;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class OptionalTest {

    @Test
    public void whenEmptyThenFalse() {
        Optional<String> empty = Optional.empty();
        assertThat(empty.isPresent()).isFalse();
    }

    @Test
    public void whenPresentThenTrue() {
        Optional<String> value = Optional.of("any");
        assertThat(value.isPresent()).isTrue();
    }

    @Test
    public void whenNullThenFalse() {
        Optional<String> value = Optional.ofNullable(null);
        assertThat(value.isPresent()).isFalse();
    }

    @Test
    public void whenNotThenPresent() {
        Optional<String> value = Optional.ofNullable("any");
        assertThat(value.get()).isEqualTo("any");
    }

    @Test
    public void whenNullThenException() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Optional.of(null));
    }

    @Test
    public void get_2() {
        String v = null;
        Optional<String> value = Optional.ofNullable(v);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> value.get());
    }

    @Test
    public void orElseGet() {
        String v = null;
        String v2 = Optional.ofNullable(v).orElseGet(String::new);
        assertThat(v2.isEmpty()).isTrue();
    }

    @Test
    public void orElse() {
        String v = null;
        String v2 = Optional.ofNullable(v).orElse("");
        assertThat(v2.isEmpty()).isTrue();
    }

    @Test
    public void orElseThrow() {
        String v = null;
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> Optional.ofNullable(v).orElseThrow(IllegalArgumentException::new));
    }

    @Test
    public void ifPresent() {
        String v = "any";
        Optional<String> value = Optional.of(v);
        value.ifPresent(s -> assertThat(s).isEqualTo(v)); // conditional action
    }

    @Test
    public void whenFilteredThenTrue() {
        Optional<Integer> value = Optional.of(1).filter(n -> n == 1);
        assertThat(value.isPresent()).isTrue();
    }

    @Test
    public void whenNotFilteredThenFalse() {
        Optional<Integer> value = Optional.of(1).filter(n -> n == 2);
        assertThat(value.isPresent()).isFalse();
    }

    @Test
    public void map() {
    }

    @Test
    public void flatMap() {
    }

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
}
