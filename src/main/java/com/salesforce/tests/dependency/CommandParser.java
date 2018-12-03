package com.salesforce.tests.dependency;

import java.util.Optional;

public class CommandParser {
    public static CommandParser build() {
        return new CommandParser();
    }

    public Optional<Command> parse(String line) {
         String[] splitted = line.split(" ");
         if (splitted.length < 1) return Optional.empty();


         switch (splitted[0]){
            case "DEPEND":
                return DependCommand.build(splitted);
            case "INSTALL":
                return InstallCommand.build(splitted);
            case "REMOVE":
                return RemoveCommand.build(splitted);
            case "LIST":
                return ListCommand.build(splitted);
            default:
                return Optional.empty();
        }

    }
}
