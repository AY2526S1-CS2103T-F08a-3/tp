package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELLER;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDatetime;
import seedu.address.model.person.Person;

/**
 * Edits the details of an existing appointment in the address book.
 */
public class EditAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "eap";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the appointment identified "
            + "by the index number used in the displayed appointment list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DATETIME + "DATETIME] "
            + "[" + PREFIX_SELLER + "SELLER_INDEX] "
            + "[" + PREFIX_BUYER + "BUYER_INDEX]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATETIME + "2025-01-15T14:00 "
            + PREFIX_SELLER + "2";

    public static final String MESSAGE_EDIT_APPOINTMENT_SUCCESS = "Edited Appointment: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book.";
    public static final String MESSAGE_INVALID_SELLER_ROLE = "The person assigned as seller must have a seller role.";
    public static final String MESSAGE_INVALID_BUYER_ROLE = "The person assigned as buyer must have a buyer role.";
    public static final String MESSAGE_SAME_PERSON = "The same person cannot be both buyer and seller.";

    private final Index index;
    private final EditAppointmentDescriptor editAppointmentDescriptor;

    /**
     * @param index of the appointment in the filtered appointment list to edit
     * @param editAppointmentDescriptor details to edit the appointment with
     */
    public EditAppointmentCommand(Index index, EditAppointmentDescriptor editAppointmentDescriptor) {
        requireNonNull(index);
        requireNonNull(editAppointmentDescriptor);

        this.index = index;
        this.editAppointmentDescriptor = new EditAppointmentDescriptor(editAppointmentDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // TODO: Change to getFilteredAppointmentList() when filtered appointment infrastructure is added
        List<Appointment> lastShownList = model.getAppointmentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        Appointment appointmentToEdit = lastShownList.get(index.getZeroBased());
        Appointment editedAppointment = createEditedAppointment(appointmentToEdit, editAppointmentDescriptor, model);

        if (!appointmentToEdit.equals(editedAppointment) && model.hasAppointment(editedAppointment)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        model.setAppointment(appointmentToEdit, editedAppointment);
        return new CommandResult(String.format(MESSAGE_EDIT_APPOINTMENT_SUCCESS,
                Messages.format(editedAppointment)));
    }

    /**
     * Creates and returns an {@code Appointment} with the details of {@code appointmentToEdit}
     * edited with {@code editAppointmentDescriptor}.
     */
    private static Appointment createEditedAppointment(Appointment appointmentToEdit,
            EditAppointmentDescriptor editAppointmentDescriptor, Model model) throws CommandException {
        assert appointmentToEdit != null;

        AppointmentDatetime updatedDatetime = editAppointmentDescriptor.getAppointmentDatetime()
                .orElse(appointmentToEdit.getAppointmentDatetime());

        Person updatedSeller;
        if (editAppointmentDescriptor.getSellerIndex().isPresent()) {
            Index sellerIndex = editAppointmentDescriptor.getSellerIndex().get();
            List<Person> personList = model.getFilteredPersonList();

            if (sellerIndex.getZeroBased() >= personList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            updatedSeller = personList.get(sellerIndex.getZeroBased());

            if (!updatedSeller.getRole().isSeller()) {
                throw new CommandException(MESSAGE_INVALID_SELLER_ROLE);
            }
        } else {
            updatedSeller = appointmentToEdit.getSeller();
        }

        Person updatedBuyer;
        if (editAppointmentDescriptor.getBuyerIndex().isPresent()) {
            Index buyerIndex = editAppointmentDescriptor.getBuyerIndex().get();
            List<Person> personList = model.getFilteredPersonList();

            if (buyerIndex.getZeroBased() >= personList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }

            updatedBuyer = personList.get(buyerIndex.getZeroBased());

            if (!updatedBuyer.getRole().isBuyer()) {
                throw new CommandException(MESSAGE_INVALID_BUYER_ROLE);
            }
        } else {
            updatedBuyer = appointmentToEdit.getBuyer();
        }

        if (updatedSeller.equals(updatedBuyer)) {
            throw new CommandException(MESSAGE_SAME_PERSON);
        }

        return new Appointment(updatedDatetime, updatedSeller, updatedBuyer);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditAppointmentCommand)) {
            return false;
        }

        EditAppointmentCommand otherEditAppointmentCommand = (EditAppointmentCommand) other;
        return index.equals(otherEditAppointmentCommand.index)
                && editAppointmentDescriptor.equals(otherEditAppointmentCommand.editAppointmentDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editAppointmentDescriptor", editAppointmentDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment.
     */
    public static class EditAppointmentDescriptor {
        private AppointmentDatetime appointmentDatetime;
        private Index sellerIndex;
        private Index buyerIndex;

        public EditAppointmentDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {
            setAppointmentDatetime(toCopy.appointmentDatetime);
            setSellerIndex(toCopy.sellerIndex);
            setBuyerIndex(toCopy.buyerIndex);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(appointmentDatetime, sellerIndex, buyerIndex);
        }

        public void setAppointmentDatetime(AppointmentDatetime appointmentDatetime) {
            this.appointmentDatetime = appointmentDatetime;
        }

        public Optional<AppointmentDatetime> getAppointmentDatetime() {
            return Optional.ofNullable(appointmentDatetime);
        }

        public void setSellerIndex(Index sellerIndex) {
            this.sellerIndex = sellerIndex;
        }

        public Optional<Index> getSellerIndex() {
            return Optional.ofNullable(sellerIndex);
        }

        public void setBuyerIndex(Index buyerIndex) {
            this.buyerIndex = buyerIndex;
        }

        public Optional<Index> getBuyerIndex() {
            return Optional.ofNullable(buyerIndex);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditAppointmentDescriptor)) {
                return false;
            }

            EditAppointmentDescriptor otherEditAppointmentDescriptor = (EditAppointmentDescriptor) other;
            return Objects.equals(appointmentDatetime, otherEditAppointmentDescriptor.appointmentDatetime)
                    && Objects.equals(sellerIndex, otherEditAppointmentDescriptor.sellerIndex)
                    && Objects.equals(buyerIndex, otherEditAppointmentDescriptor.buyerIndex);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("appointmentDatetime", appointmentDatetime)
                    .add("sellerIndex", sellerIndex)
                    .add("buyerIndex", buyerIndex)
                    .toString();
        }
    }
}
