package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code SearchAppointmentCommand}.
 */
public class SearchAppointmentCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        AppointmentContainsKeywordsPredicate firstPredicate =
                new AppointmentContainsKeywordsPredicate(Collections.singletonList("first"));
        AppointmentContainsKeywordsPredicate secondPredicate =
                new AppointmentContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchAppointmentCommand searchFirstCommand = new SearchAppointmentCommand(firstPredicate);
        SearchAppointmentCommand searchSecondCommand = new SearchAppointmentCommand(secondPredicate);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchAppointmentCommand searchFirstCommandCopy = new SearchAppointmentCommand(firstPredicate);
        assertTrue(searchFirstCommand.equals(searchFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noAppointmentFound() {
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 0);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate(" ");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredAppointmentList());
    }

    @Test
    public void execute_multipleKeywords_multipleAppointmentsFound() {
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 3);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate("Fiona Elle");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_singleKeyword_multipleAppointmentsFound() {
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 3);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate("Fiona");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void toStringMethod() {
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Arrays.asList("keyword"));
        SearchAppointmentCommand searchAppointmentCommand = new SearchAppointmentCommand(predicate);
        String expected = SearchAppointmentCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, searchAppointmentCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code AppointmentContainsKeywordsPredicate}.
     */
    private AppointmentContainsKeywordsPredicate preparePredicate(String userInput) {
        return new AppointmentContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
