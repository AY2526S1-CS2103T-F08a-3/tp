package seedu.address.testutil;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDatetime;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {

    public static final String DEFAULT_APPOINTMENT_DATETIME = "2024-12-31 14:30";
    private AppointmentDatetime appointmentDatetime;
    private Person seller;
    private Person buyer;

    /**
     * Creates an {@code AppointmentBuilder} with the default details.
     */
    public AppointmentBuilder() {
        appointmentDatetime = new AppointmentDatetime(DEFAULT_APPOINTMENT_DATETIME);
        seller = new PersonBuilder().withName("Default Seller").withRole("seller").build();
        buyer = new PersonBuilder().withName("Default Buyer").withRole("buyer").build();
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code appointmentToCopy}.
     */
    public AppointmentBuilder(Appointment appointmentToCopy) {
        appointmentDatetime = appointmentToCopy.getAppointmentDatetime();
        seller = appointmentToCopy.getSeller();
        buyer = appointmentToCopy.getBuyer();
    }

    /**
     * Sets the {@code AppointmentDatetime} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withAppointmentDatetime(String appointmentDatetime) {
        this.appointmentDatetime = new AppointmentDatetime(appointmentDatetime);
        return this;
    }

    /**
     * Sets the seller {@code Person} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withSeller(Person seller) {
        this.seller = seller;
        return this;
    }

    /**
     * Sets the seller {@code Person} of the {@code Appointment} that we are building using a name.
     */
    public AppointmentBuilder withSellerName(String sellerName) {
        this.seller = new PersonBuilder().withName(sellerName).withRole("seller").build();
        return this;
    }

    /**
     * Sets the buyer {@code Person} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withBuyer(Person buyer) {
        this.buyer = buyer;
        return this;
    }

    /**
     * Sets the buyer {@code Person} of the {@code Appointment} that we are building using a name.
     */
    public AppointmentBuilder withBuyerName(String buyerName) {
        this.buyer = new PersonBuilder().withName(buyerName).withRole("buyer").build();
        return this;
    }

    /**
     * Sets the seller's address of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withSellerAddress(String address, String addressType) {
        this.seller = new PersonBuilder(this.seller).withAddress(address, addressType).build();
        return this;
    }

    /**
     * Sets the buyer's address of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withBuyerAddress(String address, String addressType) {
        this.buyer = new PersonBuilder(this.buyer).withAddress(address, addressType).build();
        return this;
    }

    public Appointment build() {
        return new Appointment(appointmentDatetime, seller, buyer);
    }

}
