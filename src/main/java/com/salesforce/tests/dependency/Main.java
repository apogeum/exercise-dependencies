package com.salesforce.tests.dependency;

import java.util.Optional;
import java.util.Scanner;

/**
 * The entry point for the Test program
 */
public class Main {


    public static void main(String[] args) {

        RepositoryService service = RepositoryService.empty();

        //read input from stdin
        Scanner scan = new Scanner(System.in);
        CommandParser parser = CommandParser.build();

        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }

            //the END command to stop the program
            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }

            Optional<Command> c = parser.parse(line);

            c.ifPresent(cmd -> cmd.execute(service));


        }

    }
}