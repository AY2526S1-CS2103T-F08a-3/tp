package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents an appointment with a Person in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDatetime(String)}
 */
public class Appointment {

    public static final String MESSAGE_CONSTRAINTS =
            "Appointment must take a valid ISO 8601 datetime without seconds and nanoseconds";

    /*
     * The string must be in ISO 8601 format excluding seconds and nanoseconds: YYYY-MM-DDTHH:MM.
     * The regex checks that these fields, and no more, are specified.
     * The isValidDatetime method will also check validity using LocalDateTime.parse.
     */
    public static final String VALIDATION_REGEX = "(\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2})";

    public final LocalDateTime datetime;

    /**
     * Constructs an {@code Appointment}.
     *
     * @param datetimeStr A String that can be parsed into a valid LocalDateTime.
     */
    public Appointment(String datetimeStr) {
        requireNonNull(datetimeStr);
        checkArgument(isValidDatetime(datetimeStr), MESSAGE_CONSTRAINTS);
        datetime = LocalDateTime.parse(datetimeStr);
    }

    /**
     * Returns true if a given string is a valid datetime.
     */
    public static boolean isValidDatetime(String test) {
        try {
            LocalDateTime.parse(test);
        } catch (DateTimeParseException e) {
            return false;
        }
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return datetime.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return datetime.equals(otherAppointment.datetime);
    }

    @Override
    public int hashCode() {
        return datetime.hashCode();
    }

}
