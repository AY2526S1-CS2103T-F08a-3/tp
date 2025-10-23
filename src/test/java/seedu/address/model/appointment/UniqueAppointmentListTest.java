package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.FIONA_DANIEL;
import static seedu.address.testutil.TypicalPersons.FIONA_ELLE_1;
import static seedu.address.testutil.TypicalPersons.FIONA_ELLE_2;
import static seedu.address.testutil.TypicalPersons.GEORGE_BENSON;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

public class UniqueAppointmentListTest {

    private final UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();

    @Test
    public void contains_nullAppointmentThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.contains(null));
    }

    @Test
    public void contains_appointmentNotInList_returnsFalse() {
        assertFalse(uniqueAppointmentList.contains(FIONA_DANIEL));
    }

    @Test
    public void contains_appointmentInList_returnsTrue() {
        uniqueAppointmentList.add(FIONA_DANIEL);
        assertTrue(uniqueAppointmentList.contains(FIONA_DANIEL));
    }

    @Test
    public void add_nullAppointment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.add(null));
    }

    @Test
    public void add_duplicateAppointment_throwsDuplicateAppointmentException() {
        uniqueAppointmentList.add(FIONA_DANIEL);
        assertThrows(DuplicateAppointmentException.class, () -> uniqueAppointmentList.add(FIONA_DANIEL));
    }

    @Test
    public void setAppointment_nullTarget_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueAppointmentList.setAppointment(null, FIONA_DANIEL));
    }

    @Test
    public void setAppointment_nullEditedAppointment_throwsNullPointerException() {
        uniqueAppointmentList.add(FIONA_DANIEL);
        assertThrows(NullPointerException.class, () ->
                uniqueAppointmentList.setAppointment(FIONA_DANIEL, null));
    }

    @Test
    public void setAppointment_targetNotFound_throwsAppointmentNotFoundException() {
        uniqueAppointmentList.add(FIONA_ELLE_1);
        assertThrows(AppointmentNotFoundException.class, () ->
                uniqueAppointmentList.setAppointment(FIONA_DANIEL, FIONA_ELLE_2));
    }

    @Test
    public void setAppointment_duplicateAppointment_throwsDuplicateAppointmentException() {
        uniqueAppointmentList.add(FIONA_ELLE_1);
        uniqueAppointmentList.add(GEORGE_BENSON);
        assertThrows(DuplicateAppointmentException.class, () ->
                uniqueAppointmentList.setAppointment(FIONA_ELLE_1, GEORGE_BENSON));
    }

    @Test
    public void setAppointment_sameAppointment_success() {
        uniqueAppointmentList.add(FIONA_DANIEL);
        uniqueAppointmentList.setAppointment(FIONA_DANIEL, FIONA_DANIEL);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(FIONA_DANIEL);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void setAppointment_replacesAppointment_success() {
        uniqueAppointmentList.add(FIONA_ELLE_1);
        uniqueAppointmentList.setAppointment(FIONA_ELLE_1, FIONA_ELLE_2);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(FIONA_ELLE_2);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void set_uniqueAppointmentList_replacesOwnListWithProvidedUniqueAppointmentList() {
        uniqueAppointmentList.add(FIONA_ELLE_1);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(FIONA_ELLE_2);
        uniqueAppointmentList.setAppointments(expectedUniqueAppointmentList);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void set_nonUniqueAppointmentList_failure() {
        uniqueAppointmentList.add(FIONA_ELLE_1);
        List<Appointment> expectedAppointmentList = new ArrayList<>();
        expectedAppointmentList.add(FIONA_ELLE_2);
        expectedAppointmentList.add(FIONA_ELLE_2);
        assertThrows(DuplicateAppointmentException.class, () ->
                uniqueAppointmentList.setAppointments(expectedAppointmentList));
    }

    @Test
    public void remove_nullAppointment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.remove(null));
    }

    @Test
    public void remove_appointmentDoesNotExist_throwsAppointmentNotFoundException() {
        assertThrows(AppointmentNotFoundException.class, () -> uniqueAppointmentList.remove(FIONA_DANIEL));
    }

    @Test
    public void remove_existingAppointment_removesAppointment() {
        uniqueAppointmentList.add(FIONA_DANIEL);
        uniqueAppointmentList.remove(FIONA_DANIEL);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueAppointmentList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueAppointmentList.asUnmodifiableObservableList().toString(), uniqueAppointmentList.toString());
    }
}
