package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_DEC_31;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_JAN_1;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SIXTH_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDatetime;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditAppointmentCommand.
 */
public class EditAppointmentCommandTest {

    private static final Index INDEX_FIRST_APPOINTMENT = Index.fromZeroBased(0);
    private static final Index INDEX_SECOND_APPOINTMENT = Index.fromZeroBased(1);

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        // Add some appointments to the model for testing
        Person seller = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Person buyer = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Appointment appointment = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1), seller, buyer);
        model.addAppointment(appointment);
    }

    @Test
    public void execute_allFieldsSpecified_success() {
        Person newSeller = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Person newBuyer = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));
        descriptor.setSellerIndex(INDEX_THIRD_PERSON);
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        Appointment editedAppointment = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31), newSeller, newBuyer);

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(model.getAppointmentList().get(0), editedAppointment);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyDatetimeSpecified_success() {
        Appointment appointmentToEdit = model.getAppointmentList().get(INDEX_FIRST_APPOINTMENT.getZeroBased());

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        Appointment editedAppointment = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31),
                appointmentToEdit.getSeller(),
                appointmentToEdit.getBuyer().get());

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlySellerSpecified_success() {
        Appointment appointmentToEdit = model.getAppointmentList().get(INDEX_FIRST_APPOINTMENT.getZeroBased());
        Person newSeller = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_THIRD_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        Appointment editedAppointment = new Appointment(
                appointmentToEdit.getAppointmentDatetime(),
                newSeller,
                appointmentToEdit.getBuyer().get());

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyBuyerSpecified_success() {
        Appointment appointmentToEdit = model.getAppointmentList().get(INDEX_FIRST_APPOINTMENT.getZeroBased());
        Person newBuyer = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        Appointment editedAppointment = new Appointment(
                appointmentToEdit.getAppointmentDatetime(),
                appointmentToEdit.getSeller(),
                newBuyer);

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAppointmentIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getAppointmentList().size());
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editAppointmentCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSellerIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(outOfBoundIndex);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidBuyerIndex_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(outOfBoundIndex);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidSellerRole_failure() {
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, EditAppointmentCommand.MESSAGE_INVALID_SELLER_ROLE);
    }

    @Test
    public void execute_invalidBuyerRole_failure() {
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(INDEX_THIRD_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, EditAppointmentCommand.MESSAGE_INVALID_BUYER_ROLE);
    }

    @Test
    public void execute_appointmentWithoutBuyer_success() {
        Person sellerWithoutBuyer = model.getFilteredPersonList().get(INDEX_SIXTH_PERSON.getZeroBased());
        Appointment appointmentWithoutBuyer = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1), sellerWithoutBuyer);
        model.addAppointment(appointmentWithoutBuyer);

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));

        Index indexOfAppointmentWithoutBuyer = Index.fromZeroBased(model.getAppointmentList().size() - 1);
        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(indexOfAppointmentWithoutBuyer,
                descriptor);

        Appointment editedAppointment = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31), sellerWithoutBuyer);

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        Appointment targetAppointment = expectedModel.getAppointmentList()
                .get(indexOfAppointmentWithoutBuyer.getZeroBased());
        expectedModel.setAppointment(targetAppointment, editedAppointment);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateAppointment_failure() {
        // Add a second appointment
        Person seller2 = model.getFilteredPersonList().get(INDEX_THIRD_PERSON.getZeroBased());
        Person buyer2 = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Appointment appointment2 = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31), seller2, buyer2);
        model.addAppointment(appointment2);

        // Try to edit first appointment to be the same as second
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));
        descriptor.setSellerIndex(INDEX_THIRD_PERSON);
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, EditAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT);
    }

    @Test
    public void equals() {
        EditAppointmentDescriptor descriptor1 = new EditAppointmentDescriptor();
        descriptor1.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));

        EditAppointmentDescriptor descriptor2 = new EditAppointmentDescriptor();
        descriptor2.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));

        final EditAppointmentCommand standardCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor1);

        // same values -> returns true
        EditAppointmentDescriptor copyDescriptor = new EditAppointmentDescriptor(descriptor1);
        EditAppointmentCommand commandWithSameValues = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT,
                copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditAppointmentCommand(INDEX_SECOND_APPOINTMENT, descriptor1)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor2)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(index, descriptor);
        String expected = EditAppointmentCommand.class.getCanonicalName() + "{index=" + index
                + ", editAppointmentDescriptor=" + descriptor + "}";
        assertEquals(expected, editAppointmentCommand.toString());
    }
}
