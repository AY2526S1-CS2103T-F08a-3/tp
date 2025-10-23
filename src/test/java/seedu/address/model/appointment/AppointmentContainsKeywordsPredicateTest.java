package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AppointmentContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        AppointmentContainsKeywordsPredicate firstPredicate =
            new AppointmentContainsKeywordsPredicate(firstPredicateKeywordList);
        AppointmentContainsKeywordsPredicate secondPredicate =
            new AppointmentContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        AppointmentContainsKeywordsPredicate firstPredicateCopy =
            new AppointmentContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_appointmentContainsKeywords_returnsTrue() {
        Appointment testAppointment = new Appointment(
            new AppointmentDatetime("2025-01-01T12:00"), ALICE, BOB);

        // Single keyword - delegates to Appointment.containsKeyword()
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(testAppointment));

        // Multiple keywords - should return true if ANY keyword matches
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("Alice", "NonExistent"));
        assertTrue(predicate.test(testAppointment));

        // All keywords match
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(testAppointment));
    }

    @Test
    public void test_appointmentDoesNotContainKeywords_returnsFalse() {
        Appointment testAppointment = new Appointment(
            new AppointmentDatetime("2025-01-01T12:00"), ALICE, BOB);

        // Empty keyword list - should always return false
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(testAppointment));

        // No matching keywords - delegates to Appointment.containsKeyword()
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("NonExistent"));
        assertFalse(predicate.test(testAppointment));

        // Multiple non-matching keywords - should return false if NONE match
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("NonExistent", "NotFound"));
        assertFalse(predicate.test(testAppointment));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(keywords);

        String expected = AppointmentContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
