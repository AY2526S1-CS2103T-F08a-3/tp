package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;

/**
 * Json-friendly version of {@link Appointment}.
 */
class JsonAdaptedAppointment {

    private final String appointmentDateTime;

    /**
     * Constructs a {@code JsonAdaptedTag} with the given {@code appointmentDateTime}.
     */
    @JsonCreator
    public JsonAdaptedAppointment(String appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    /**
     * Converts a given {@code Appointment} into this class for Json use.
     */
    public JsonAdaptedAppointment(Appointment source) {
        appointmentDateTime = source.datetime.toString();
    }

    @JsonValue
    public String getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Converts this Json-friendly adapted appointment object into the model's {@code Appointment} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tag.
     */
    public Appointment toModelType() throws IllegalValueException {
        if (!Appointment.isValidDatetime(appointmentDateTime)) {
            throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
        }
        return new Appointment(appointmentDateTime);
    }

}
