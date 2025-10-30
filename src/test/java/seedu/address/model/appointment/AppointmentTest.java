package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.FIONA_ELLE_PAST;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.DANIEL_EDITED;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.FIONA_DANIEL;
import static seedu.address.testutil.TypicalPersons.FIONA_DANIEL_EDITED;
import static seedu.address.testutil.TypicalPersons.FIONA_EDITED;
import static seedu.address.testutil.TypicalPersons.FIONA_EDITED_DANIEL;
import static seedu.address.testutil.TypicalPersons.FIONA_EDITED_NOBUYER;
import static seedu.address.testutil.TypicalPersons.FIONA_ELLE_1;
import static seedu.address.testutil.TypicalPersons.FIONA_NOBUYER;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import org.junit.jupiter.api.Test;

public class AppointmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Appointment(null, null, null));
    }

    @Test
    public void isPersonSeller() {
        Appointment appointment = FIONA_ELLE_1;
        assertTrue(appointment.isPersonSeller(FIONA));

        // null is false
        assertFalse(appointment.isPersonSeller(null));

        // Elle is the buyer, not the seller
        assertFalse(appointment.isPersonSeller(ELLE));

        // George is not involved in this appointment
        assertFalse(appointment.isPersonSeller(GEORGE));
    }

    @Test
    public void isPersonBuyer() {
        Appointment appointment = FIONA_ELLE_1;
        assertTrue(appointment.isPersonBuyer(ELLE));

        // null is false
        assertFalse(appointment.isPersonBuyer(null));

        // Fiona is the seller, not the buyer
        assertFalse(appointment.isPersonBuyer(FIONA));

        // George is not involved in this appointment
        assertFalse(appointment.isPersonBuyer(GEORGE));
    }

    @Test
    public void updatedWithEditedPerson_personIsSeller_returnsAppointment() {
        // Test with buyer
        Appointment appointment = FIONA_DANIEL;
        Appointment newAppointment = appointment.updatedWithEditedPerson(FIONA, FIONA_EDITED);
        assertEquals(FIONA_EDITED_DANIEL, newAppointment);
        assertEquals(FIONA_DANIEL, appointment);

        // Test without buyer
        appointment = FIONA_NOBUYER;
        newAppointment = appointment.updatedWithEditedPerson(FIONA, FIONA_EDITED);
        assertEquals(FIONA_EDITED_NOBUYER, newAppointment);
        assertEquals(FIONA_NOBUYER, appointment);
    }

    @Test
    public void updatedWithEditedPerson_personIsBuyer_returnsAppointment() {
        Appointment appointment = FIONA_DANIEL;
        Appointment newAppointment = appointment.updatedWithEditedPerson(DANIEL, DANIEL_EDITED);
        assertEquals(FIONA_DANIEL_EDITED, newAppointment);
        assertEquals(FIONA_DANIEL, appointment);
    }

    @Test
    public void updatedWithEditedPerson_personIsNotParticipant_returnsSameAppointment() {
        Appointment appointment = FIONA_ELLE_1;
        Appointment newAppointment = appointment.updatedWithEditedPerson(DANIEL, DANIEL_EDITED);
        assertEquals(FIONA_ELLE_1, newAppointment);
        assertEquals(FIONA_ELLE_1, appointment);
    }

    @Test
    public void equals() {
        AppointmentDatetime appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        Appointment appointment = new Appointment(appointmentDatetime, BENSON, ALICE);

        // same values -> returns true
        appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        assertTrue(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));

        // same object -> returns true
        assertTrue(appointment.equals(appointment));

        // null -> returns false
        assertFalse(appointment.equals(null));

        // different types -> returns false
        assertFalse(appointment.equals(5.0f));

        // different datetime -> returns false
        appointmentDatetime = new AppointmentDatetime("2025-02-01T00:00");
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));

        // different seller -> returns false
        appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, CARL, ALICE)));

        // different buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, CARL)));

        // no buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON)));

        // appointment with no buyer
        appointment = new Appointment(appointmentDatetime, BENSON);

        // same values -> returns true
        assertTrue(appointment.equals(new Appointment(appointmentDatetime, BENSON)));

        // different values -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, ALICE)));

        // compare to appointment with buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));


    }

    @Test
    public void containsKeyword() {
        Appointment appointment = FIONA_ELLE_PAST; // FIONA (seller) and ELLE (buyer), date is 1 month ago

        // Seller name matching (Fiona Kunz)
        assertTrue(appointment.containsKeyword("Fiona"));
        assertTrue(appointment.containsKeyword("Kunz"));

        // Buyer name matching (Elle Meyer)
        assertTrue(appointment.containsKeyword("Elle"));
        assertTrue(appointment.containsKeyword("Meyer"));

        // Seller details matching (from FIONA)
        assertTrue(appointment.containsKeyword("9482427")); // Fiona's phone
        assertTrue(appointment.containsKeyword("lydia@example.com")); // Fiona's email
        assertTrue(appointment.containsKeyword("tokyo")); // part of "little tokyo" address
        assertTrue(appointment.containsKeyword("HDB_J")); // Fiona's property type
        assertTrue(appointment.containsKeyword("seller")); // Fiona's role

        // Buyer details matching (from ELLE)
        assertTrue(appointment.containsKeyword("9482224")); // Elle's phone
        assertTrue(appointment.containsKeyword("werner@example.com")); // Elle's email
        assertTrue(appointment.containsKeyword("michegan")); // part of "michegan ave" address
        assertTrue(appointment.containsKeyword("HDB_5")); // Elle's property type
        assertTrue(appointment.containsKeyword("buyer")); // Elle's role

        // Appointment datetime matching - use the actual datetime from the appointment
        String appointmentDatetimeString = appointment.appointmentDatetime.toString();
        assertTrue(appointment.containsKeyword(appointmentDatetimeString));
        // Partial datetime should not match
        assertFalse(appointment.containsKeyword(appointmentDatetimeString.substring(0, 10))); // Date only
        assertFalse(appointment.containsKeyword("12:00")); // Time only

        // Case insensitive matching
        assertTrue(appointment.containsKeyword("FIONA"));
        assertTrue(appointment.containsKeyword("elle"));
        assertTrue(appointment.containsKeyword("TOKYO"));

        // Non-matching keywords
        assertFalse(appointment.containsKeyword("Charlie"));
        assertFalse(appointment.containsKeyword("nonexistent"));
        assertFalse(appointment.containsKeyword("2024"));

        // Partial word matching should not work (StringUtil requires word boundaries)
        assertFalse(appointment.containsKeyword("Fio"));
        assertFalse(appointment.containsKeyword("Mey"));
    }

}
