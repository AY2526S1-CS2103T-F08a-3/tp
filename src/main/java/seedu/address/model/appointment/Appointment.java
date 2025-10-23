package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.person.Person;

/**
 * Represents an appointment in the address book.
 * Guarantees: immutable; details are present not null; field values are validated.
 *
 */
public class Appointment implements Comparable<Appointment> {

    public final AppointmentDatetime appointmentDatetime;
    public final Person seller;
    public final Person buyer;

    /**
     * Constructs an {@code Appointment} with a buyer.
     *
     * @param appointmentDatetime An AppointmentDatetime object representing the datetime of the appointment.
     * @param seller A Person object representing the seller.
     * @param buyer A Person object representing the buyer.
     */
    public Appointment(AppointmentDatetime appointmentDatetime, Person seller, Person buyer) {
        requireNonNull(appointmentDatetime);
        requireNonNull(seller);
        requireNonNull(buyer);
        this.appointmentDatetime = appointmentDatetime;
        this.seller = seller;
        this.buyer = buyer;
    }

    /**
     * Constructs an {@code Appointment} without a buyer.
     *
     * @param appointmentDatetime An AppointmentDatetime object representing the datetime of the appointment.
     * @param seller A Person object representing the seller.
     */
    public Appointment(AppointmentDatetime appointmentDatetime, Person seller) {
        requireNonNull(appointmentDatetime);
        requireNonNull(seller);
        this.appointmentDatetime = appointmentDatetime;
        this.seller = seller;
        this.buyer = null;
    }

    public AppointmentDatetime getAppointmentDatetime() {
        return appointmentDatetime;
    }

    public Person getSeller() {
        return seller;
    }

    public Optional<Person> getBuyer() {
        return Optional.ofNullable(buyer);
    }

    /**
     * Returns true if any field of this appointment contains the given keyword (case-insensitive).
     * Searches across seller details, buyer details, and appointment datetime.
     */
    public boolean containsKeyword(String keyword) {
        return seller.containsKeyword(keyword)
                || buyer.containsKeyword(keyword)
                || StringUtil.containsWordIgnoreCase(appointmentDatetime.toString(), keyword);
    }

    @Override
    public String toString() {
        return appointmentDatetime.toString();
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
        return appointmentDatetime.equals(otherAppointment.appointmentDatetime)
                && seller.equals(otherAppointment.seller)
                && Objects.equals(buyer, otherAppointment.buyer);
    }

    @Override
    public int compareTo(Appointment other) {
        return appointmentDatetime.compareTo(other.getAppointmentDatetime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentDatetime, seller, buyer);
    }

}
