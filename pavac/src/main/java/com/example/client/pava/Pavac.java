package com.example.client.pava;

import com.example.lang.cpp.Cpp;
import com.example.pava.impl.PavaCodeImpl;
import com.example.pava.impl.PavaDefaultThreeAddressCode;
import com.example.pava.impl.PvmImpl;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Pavac {
    public static void main(String[] args) {

        CommandLineParser parser = new GnuParser();

        Options options = new Options();
        options.addOption("p", "pava", true, "pava file");
        options.addOption("d", "debug", false, "debug");
        options.addOption("o", "output", true, "output");

        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("could not parse your command line");
            System.exit(-1);
        }


        if (commandLine.hasOption("pava")) {
            try (InputStream inputStream = new FileInputStream(commandLine.getOptionValue("pava"))) {

                boolean target = new File("target").mkdir();

                String code = new String(inputStream.readAllBytes());

                System.out.println(code);
                String parCode = Cpp.parse(code, "pava").stream().map(Object::toString).collect(Collectors.joining("\n"));

                try (OutputStream outputStream = new FileOutputStream(commandLine.getOptionValue("output"))) {
                    outputStream.write(parCode.getBytes(StandardCharsets.UTF_8));
                }

                System.out.println("success!");

            } catch (FileNotFoundException e) {
                System.out.println("could not find file " + commandLine.getOptionValue("par"));
                System.exit(-1);
            } catch (Exception e) {
                System.out.println("oh I'm sorry about this, maybe your pava code is bad");
                e.printStackTrace();
                System.exit(-1);
            }
        }

        System.out.println("""
                welcome to pavac 1.0 ...
                you can compile pavac code like this :
                $ java -jar pavac.jar -pava code.pava
                you can debug pava code like this :
                $ java -jar pavac.jar -pava code.pava -debug
                """);
        System.exit(0);
    }
}
