package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_DEC_31;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_DATETIME_JAN_1;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUYER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATETIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SELLER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAppointmentCommand;
import seedu.address.logic.commands.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.model.appointment.AppointmentDatetime;

public class EditAppointmentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAppointmentCommand.MESSAGE_USAGE);

    private EditAppointmentCommandParser parser = new EditAppointmentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditAppointmentCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5 " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0 " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1
                + " " + PREFIX_SELLER + INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_BUYER + INDEX_SECOND_PERSON.getOneBased();

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);

        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // only datetime
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_DEC_31;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_DEC_31));
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // only seller
        userInput = targetIndex.getOneBased() + " " + PREFIX_SELLER + INDEX_SECOND_PERSON.getOneBased();
        descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_SECOND_PERSON);
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // only buyer
        userInput = targetIndex.getOneBased() + " " + PREFIX_BUYER + INDEX_SECOND_PERSON.getOneBased();
        descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(INDEX_SECOND_PERSON);
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // datetime and seller
        userInput = targetIndex.getOneBased() + " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1
                + " " + PREFIX_SELLER + INDEX_FIRST_PERSON.getOneBased();
        descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        descriptor.setSellerIndex(INDEX_FIRST_PERSON);
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;

        // datetime
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentDatetime(new AppointmentDatetime(VALID_APPOINTMENT_DATETIME_JAN_1));
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // seller
        userInput = targetIndex.getOneBased() + " " + PREFIX_SELLER + INDEX_SECOND_PERSON.getOneBased();
        descriptor = new EditAppointmentDescriptor();
        descriptor.setSellerIndex(INDEX_SECOND_PERSON);
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // buyer
        userInput = targetIndex.getOneBased() + " " + PREFIX_BUYER + INDEX_FIRST_PERSON.getOneBased();
        descriptor = new EditAppointmentDescriptor();
        descriptor.setBuyerIndex(INDEX_FIRST_PERSON);
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // Multiple datetimes
        String userInput = "1 " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_JAN_1
                + " " + PREFIX_DATETIME + VALID_APPOINTMENT_DATETIME_DEC_31;
        assertParseFailure(parser, userInput,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATETIME));

        // Multiple sellers
        userInput = "1 " + PREFIX_SELLER + "1 " + PREFIX_SELLER + "2";
        assertParseFailure(parser, userInput,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SELLER));

        // Multiple buyers
        userInput = "1 " + PREFIX_BUYER + "1 " + PREFIX_BUYER + "2";
        assertParseFailure(parser, userInput,
                seedu.address.logic.Messages.getErrorMessageForDuplicatePrefixes(PREFIX_BUYER));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid datetime format
        assertParseFailure(parser, "1 " + PREFIX_DATETIME + "invalid-date",
                AppointmentDatetime.MESSAGE_CONSTRAINTS);

        // invalid seller index (negative)
        assertParseFailure(parser, "1 " + PREFIX_SELLER + "-1",
                ParserUtil.MESSAGE_INVALID_INDEX);

        // invalid buyer index (not a number)
        assertParseFailure(parser, "1 " + PREFIX_BUYER + "abc",
                ParserUtil.MESSAGE_INVALID_INDEX);
    }
}
