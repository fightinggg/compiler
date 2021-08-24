package com.example.client.pava;

import com.example.pava.impl.PavaCodeImpl;
import com.example.pava.impl.PvmImpl;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Pava {
    public static void main(String[] args) {

        CommandLineParser parser = new GnuParser();

        Options options = new Options();
        options.addOption("p", "par", true, "par file");
        options.addOption("d", "debug", false, "debug");

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("could not parse your command line");
            System.exit(-1);
        }


        if (commandLine.hasOption("par")) {
            try (InputStream inputStream = new FileInputStream(commandLine.getOptionValue("par"))) {
                String code = new String(inputStream.readAllBytes());

                PavaCodeImpl pavaCode = new PavaCodeImpl(code);

                int returnValue = new PvmImpl(commandLine.hasOption("debug")).run(pavaCode, null);
                System.out.println("return: " + returnValue);
                System.exit(0);

            } catch (FileNotFoundException e) {
                System.out.println("could not find file " + commandLine.getOptionValue("par"));
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("oh I'm sorry about this, maybe your par code is bad");
                e.printStackTrace();
                System.exit(-1);
            }
        }

        System.out.println("""
                welcome to pava 1.0 ...
                you can run pava code like this :
                $ pava -par code.par
                you can debug pava code like this :
                $ pava -par code.par -debug
                """);
        System.exit(0);
    }
}
