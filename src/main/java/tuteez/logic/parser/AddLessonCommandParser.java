package tuteez.logic.parser;

import static java.util.Objects.requireNonNull;
import static tuteez.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static tuteez.logic.Messages.MESSAGE_INVALID_PERSON_INDEX_FORMAT;
import static tuteez.logic.Messages.MESSAGE_MISSING_LESSON_FIELD_PREFIX;
import static tuteez.logic.Messages.MESSAGE_MISSING_PERSON_INDEX;
import static tuteez.logic.parser.CliSyntax.PREFIX_LESSON;

import java.util.List;

import tuteez.commons.core.index.Index;
import tuteez.logic.commands.AddLessonCommand;
import tuteez.logic.commands.LessonCommand;
import tuteez.logic.parser.exceptions.ParseException;
import tuteez.model.person.lesson.Lesson;

/**
 * Parses input arguments and creates a new AddLessonCommand object
 */
public class AddLessonCommandParser implements Parser<LessonCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddLessonCommand
     * and returns an AddLessonCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddLessonCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_LESSON);

        validateBasicCommandFormat(args);
        validatePrefixExists(argMultimap);

        Index personIndex = parsePersonIndex(argMultimap);

        return createAddLessonCommand(personIndex, argMultimap);
    }

    private void validateBasicCommandFormat(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddLessonCommand.MESSAGE_USAGE));
        }
    }

    private void validatePrefixExists(ArgumentMultimap argMultimap) throws ParseException {
        if (!argMultimap.getValue(PREFIX_LESSON).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MISSING_LESSON_FIELD_PREFIX));
        }
    }

    private Index parsePersonIndex(ArgumentMultimap argMultimap) throws ParseException {
        String preamble = argMultimap.getPreamble().trim();

        if (preamble.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MISSING_PERSON_INDEX));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(preamble);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    String.format(MESSAGE_INVALID_PERSON_INDEX_FORMAT, preamble)));
        }
        return index;
    }

    private AddLessonCommand createAddLessonCommand(Index personIndex, ArgumentMultimap argMultimap)
            throws ParseException {
        List<String> lessonStrings = argMultimap.getAllValues(PREFIX_LESSON);
        List<Lesson> lessons = ParserUtil.parseLessons(lessonStrings);
        return new AddLessonCommand(personIndex, lessons);
    }
}
