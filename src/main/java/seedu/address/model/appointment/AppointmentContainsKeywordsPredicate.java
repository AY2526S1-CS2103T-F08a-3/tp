package seedu.address.model.appointment;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that an {@code Appointment}'s details match any of the keywords given.
 * Searches across seller name, buyer name, seller address, and appointment datetime.
 */
public class AppointmentContainsKeywordsPredicate implements Predicate<Appointment> {
    private final List<String> keywords;

    public AppointmentContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Appointment appointment) {
        return keywords.stream()
                .anyMatch(keyword -> matchesAnyField(appointment, keyword));
    }

    /**
     * Checks if the keyword matches any field of the appointment.
     */
    private boolean matchesAnyField(Appointment appointment, String keyword) {
        return appointment.containsKeyword(keyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentContainsKeywordsPredicate)) {
            return false;
        }

        AppointmentContainsKeywordsPredicate otherPredicate = (AppointmentContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
