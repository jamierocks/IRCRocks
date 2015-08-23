package uk.jamierocks.ircrocks.command;

import com.sk89q.intake.CommandCallable;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.Description;
import com.sk89q.intake.context.CommandLocals;

import java.util.List;

public abstract class BaseCommand implements CommandCallable {

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        return null;
    }

    @Override
    public Description getDescription() {
        return null;
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return true;
    }
}
