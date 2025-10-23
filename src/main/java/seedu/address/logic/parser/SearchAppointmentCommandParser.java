package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.SearchAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new SearchAppointmentCommand object
 */
public class SearchAppointmentCommandParser implements Parser<SearchAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchAppointmentCommand
     * and returns a SearchAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchAppointmentCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchAppointmentCommand.MESSAGE_USAGE));
        }

        String[] appointmentKeywords = trimmedArgs.split("\\s+");

        return new SearchAppointmentCommand(
            new AppointmentContainsKeywordsPredicate(Arrays.asList(appointmentKeywords)));
    }
}
