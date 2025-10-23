package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchAppointmentCommand;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;

/**
 * Contains unit tests for SearchAppointmentCommandParser.
 */
public class SearchAppointmentCommandParserTest {

    private SearchAppointmentCommandParser parser = new SearchAppointmentCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SearchAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSearchAppointmentCommand() {
        // no leading and trailing whitespaces
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedSearchAppointmentCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_singleKeyword_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(Arrays.asList("Fiona")));
        assertParseSuccess(parser, "Fiona", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_multipleKeywords_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob", "Charlie")));
        assertParseSuccess(parser, "Alice Bob Charlie", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_dateTimeKeyword_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("2025-01-01T12:00")));
        assertParseSuccess(parser, "2025-01-01T12:00", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_mixedKeywords_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "2025-01-01", "tokyo")));
        assertParseSuccess(parser, "Alice 2025-01-01 tokyo", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_specialCharacters_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Block", "123,", "Street")));
        assertParseSuccess(parser, "Block 123, Street", expectedSearchAppointmentCommand);
    }
}
