package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_DEC_31;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_JAN_1;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENTS;
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
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDatetime;
import seedu.address.model.person.Person;
import seedu.address.model.person.Role;
import seedu.address.testutil.PersonBuilder;

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
        Appointment appointmentToEdit = model.getFilteredAppointmentList()
                .get(INDEX_FIRST_APPOINTMENT.getZeroBased());

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
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyDatetimeSpecified_success() {
        Appointment appointmentToEdit = model.getFilteredAppointmentList()
                .get(INDEX_FIRST_APPOINTMENT.getZeroBased());

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
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlySellerSpecified_success() {
        Appointment appointmentToEdit = model.getFilteredAppointmentList()
                .get(INDEX_FIRST_APPOINTMENT.getZeroBased());
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
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyBuyerSpecified_success() {
        Appointment appointmentToEdit = model.getFilteredAppointmentList()
                .get(INDEX_FIRST_APPOINTMENT.getZeroBased());
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
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        expectedModel.setAppointment(appointmentToEdit, editedAppointment);
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAppointmentIndex_failure() {
        Index outOfBoundIndex = Index.fromZeroBased(model.getFilteredAppointmentList().size());
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
    public void execute_sameSellerBuyer_failure() throws CommandException {
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);

        // update first person (Alice) from buyer to seller
        Person alice = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.setPerson(alice, new PersonBuilder(alice).withRole(Role.SELLER).build());

        // attempt to update the appointment's seller to Alice (who is also the buyer)
        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand, model, EditAppointmentCommand.MESSAGE_SAME_SELLER_BUYER);
    }

    @Test
    public void execute_appointmentWithoutBuyer_success() {
        Person sellerWithoutBuyer = model.getFilteredPersonList().get(INDEX_SIXTH_PERSON.getZeroBased());
        Appointment appointmentWithoutBuyer = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1), sellerWithoutBuyer);
        model.addAppointment(appointmentWithoutBuyer);
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));

        int indexOfNewAppointment = model.getFilteredAppointmentList().indexOf(appointmentWithoutBuyer);
        assertTrue(indexOfNewAppointment >= 0);
        Index indexOfAppointmentWithoutBuyer = Index.fromZeroBased(indexOfNewAppointment);
        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(indexOfAppointmentWithoutBuyer,
                descriptor);

        Appointment editedAppointment = new Appointment(
                new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31), sellerWithoutBuyer);

        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        expectedModel.setAppointment(appointmentWithoutBuyer, editedAppointment);
        expectedModel.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);

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

    @Test
    public void editAppointmentDescriptor_equals() {
        EditAppointmentDescriptor descriptor1 = new EditAppointmentDescriptor();
        descriptor1.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor1.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor1.setBuyerIndex(INDEX_SECOND_PERSON);

        EditAppointmentDescriptor descriptor2 = new EditAppointmentDescriptor();
        descriptor2.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor2.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor2.setBuyerIndex(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(descriptor1.equals(descriptor1));

        // same values -> returns true
        assertTrue(descriptor1.equals(descriptor2));

        // different types -> returns false
        assertFalse(descriptor1.equals(1));

        // null -> returns false
        assertFalse(descriptor1.equals(null));

        // different datetime -> returns false
        EditAppointmentDescriptor descriptor3 = new EditAppointmentDescriptor();
        descriptor3.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));
        descriptor3.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor3.setBuyerIndex(INDEX_SECOND_PERSON);
        assertFalse(descriptor1.equals(descriptor3));

        // different seller index -> returns false
        EditAppointmentDescriptor descriptor4 = new EditAppointmentDescriptor();
        descriptor4.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor4.setSellerIndex(INDEX_THIRD_PERSON);
        descriptor4.setBuyerIndex(INDEX_SECOND_PERSON);
        assertFalse(descriptor1.equals(descriptor4));

        // different buyer index -> returns false
        EditAppointmentDescriptor descriptor5 = new EditAppointmentDescriptor();
        descriptor5.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor5.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor5.setBuyerIndex(INDEX_THIRD_PERSON);
        assertFalse(descriptor1.equals(descriptor5));
    }

    @Test
    public void editAppointmentDescriptor_toStringMethod() {
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);

        String expected = EditAppointmentDescriptor.class.getCanonicalName()
                + "{appointmentDatetime=" + descriptor.getAppointmentDatetime().get()
                + ", sellerIndex=" + INDEX_FIRST_PERSON
                + ", buyerIndex=" + INDEX_SECOND_PERSON + "}";
        assertEquals(expected, descriptor.toString());
    }

    @Test
    public void editAppointmentDescriptor_isAnyFieldEdited() {
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();

        // no fields set -> returns false
        assertFalse(descriptor.isAnyFieldEdited());

        // datetime set -> returns true
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        assertTrue(descriptor.isAnyFieldEdited());

        // reset and test seller index
        descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);
        assertTrue(descriptor.isAnyFieldEdited());

        // reset and test buyer index
        descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);
        assertTrue(descriptor.isAnyFieldEdited());
    }
}
