package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DEC_31;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_JAN_1;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class AddAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_appointmentAcceptedByModel_success() {
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(0))
                .withAppointments(VALID_APPOINTMENT_JAN_1).build();
        AddAppointmentCommand addAppointmentCommand = new AddAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_JAN_1));

        String expectedMessage = String.format(AddAppointmentCommand.MESSAGE_ADD_APPOINTMENT_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddAppointmentCommand addAppointmentCommand = new AddAppointmentCommand(outOfBoundIndex,
                new Appointment(VALID_APPOINTMENT_JAN_1));

        assertCommandFailure(addAppointmentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final AddAppointmentCommand standardCommand = new AddAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_JAN_1));

        // same values -> returns true
        AddAppointmentCommand commandWithSameValues = new AddAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_JAN_1));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddAppointmentCommand(INDEX_SECOND_PERSON,
                new Appointment(VALID_APPOINTMENT_JAN_1))));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddAppointmentCommand(INDEX_FIRST_PERSON,
                new Appointment(VALID_APPOINTMENT_DEC_31))));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Appointment appointment = new Appointment(VALID_APPOINTMENT_JAN_1);
        AddAppointmentCommand addAppointmentCommand = new AddAppointmentCommand(index, appointment);
        String expected = AddAppointmentCommand.class.getCanonicalName() + "{index=" + index + ", appointment="
                + appointment + "}";
        assertEquals(expected, addAppointmentCommand.toString());
    }

}
