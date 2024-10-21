package tuteez.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandHistoryTest {
    private CommandHistory commandHistory;

    @BeforeEach
    public void setUp() {
        commandHistory = new CommandHistory();
    }

    @Test
    public void add_nullCommand_throwsException() {
        // Check that adding a null command throws an exception
        assertThrows(AssertionError.class, () -> commandHistory.add(null));
    }

    @Test
    public void add_correctlyAddsCommands() {
        commandHistory.add("command1");
        commandHistory.add("command2");

        // Check if the commands have been added correctly
        assertEquals("command2", commandHistory.getPreviousCommand());
        assertEquals("command1", commandHistory.getPreviousCommand());

        // Ensure currentIndex is reset to the end of the list after adding commands
        commandHistory.add("command3");
        assertEquals("command3", commandHistory.getPreviousCommand()); // Should return command3
    }
    @Test
    public void getPreviousCommand_correctlyGetsPreviousCommands() {
        commandHistory.add("command1");
        commandHistory.add("command2");
        commandHistory.add("command3");

        // Check if the last command can be retrieved
        assertEquals("command3", commandHistory.getPreviousCommand());
        assertEquals("command2", commandHistory.getPreviousCommand());
        assertEquals("command1", commandHistory.getPreviousCommand());
        assertNull(commandHistory.getPreviousCommand());
    }

    @Test
    public void getNextCommand_correctlyGetsNextCommands() {
        commandHistory.add("command1");
        commandHistory.add("command2");
        assertEquals("command2", commandHistory.getPreviousCommand());
        assertEquals("command1", commandHistory.getPreviousCommand());
        assertEquals("command2", commandHistory.getNextCommand());
        assertEquals("", commandHistory.getNextCommand()); // No more next commands
    }
}
