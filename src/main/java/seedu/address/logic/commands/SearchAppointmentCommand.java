package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;

/**
 * Finds and lists all appointments in address book whose details contain any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class SearchAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "sap";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all appointments whose details contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Searches across seller name, buyer name, seller address and appointment datetime.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " alice (finds all appointments with seller or buyer named alice)\n"
            + "  " + COMMAND_WORD + " 2025-01-01T12:00 (finds all appointments on January 1st, 2025)\n"
            + "  " + COMMAND_WORD + " bishan (finds all appointments at addresses containing bishan)\n";

    private final AppointmentContainsKeywordsPredicate predicate;

    public SearchAppointmentCommand(AppointmentContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredAppointmentList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, model.getFilteredAppointmentList().size()),
                false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchAppointmentCommand)) {
            return false;
        }

        SearchAppointmentCommand otherSearchAppointmentCommand = (SearchAppointmentCommand) other;
        return predicate.equals(otherSearchAppointmentCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
