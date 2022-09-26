package com.whatsapptextextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends DateValidatorUsingDateTimeFormatter {

    private static String documentsDirectory = "/home/dante/Documents/16B_D1j2-Vp0VLBA2z8zVg_waWzFRZGOo";
    private static String specificPerson = "Nickolas";
    private static String outputFileName = specificPerson + "WhatsAppText.txt";

    public static void main(String[] args) {

        int lines = (int) countLinesInTheExportFile(documentsDirectory);
        int iterations = 1;
        int messages = 1;

        Integer messageLine[] = {};

        createFile(outputFileName);

        for (int i = 0; i < lines; i++) {
            if (showsIsAValidWhatsAppMessage(readLine(i, documentsDirectory)))
                messageLine = addX(messageLine.length + 1, messageLine, i);
        }

        do {
            String text = readLine(iterations, documentsDirectory);
            String messageOwner = extractNameorPhoneNumber(text);

            if (messageOwner.equals(specificPerson)) {
                for (int i = iterations; i < messageLine[findIndex(messageLine, iterations) + 1]; i++) {
                    String ownerText = readLine(i, documentsDirectory);
                    appendTextToExistingFile(ownerText + "\n");

                }

                messages++;
            }
            iterations++;

        } while (iterations < lines);

        int NoOfMessages = messages - 1;

        print("TOTAL MESSAGES FROM " + specificPerson + " IN THIS EXPORT FILE IS: " + NoOfMessages);

    }

    private static void print(String text) {
        System.out.println(text);
    }

    private static boolean isDateValid(String date) {
        date = date.replaceAll("/", "-");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        format.setLenient(false);
        try {
            format.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private static boolean isTimeValid(String value) {
        try {
            String[] time = value.split(":");
            return Integer.parseInt(time[0]) < 24 && Integer.parseInt(time[1]) < 60;
        } catch (Exception e) {
            return false;
        }
    }

    private static long countLinesInTheExportFile(String fileName) {

        Path path = Paths.get(fileName);

        long lines = 0;
        try {

            // much slower, this task better with sequence access
            // lines = Files.lines(path).parallel().count();

            lines = Files.lines(path).count();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    private static String readLine(int line, String fileName) {
        FileReader tempFileReader = null;
        BufferedReader tempBufferedReader = null;
        try {
            tempFileReader = new FileReader(fileName);
            tempBufferedReader = new BufferedReader(tempFileReader);
        } catch (Exception e) {
        }
        String returnStr = "ERROR";
        for (int i = 0; i < line - 1; i++) {
            try {
                tempBufferedReader.readLine();
            } catch (Exception e) {
            }
        }
        try {
            returnStr = tempBufferedReader.readLine();
        } catch (Exception e) {
        }

        return returnStr;
    }

    private static boolean showsIsAValidWhatsAppMessage(String text) {
        if (text.length() > 16) {
            if (isDateValid(extractDate(text)) && isTimeValid(extractTime(text))) {
                return true;
            }
        }

        return false;
    }

    private static String extractDate(String text) {
        return text.substring(0, 10);
    }

    private static String extractTime(String text) {
        return text.substring(12, 17);
    }

    private static String extractNameorPhoneNumber(String text) {
        int endIndex = 20 + specificPerson.length();
        if (text.length() > endIndex) {
            return text.substring(20, endIndex);
        }
        return "";
    }

    private static Integer[] addX(int n, Integer arr[], int x) {

        // create a new ArrayList
        List<Integer> arrlist = new ArrayList<Integer>(
                Arrays.asList(arr));

        // Add the new element
        arrlist.add(x);

        // Convert the Arraylist to array
        arr = arrlist.toArray(arr);

        // return the array
        return arr;
    }

    private static int findIndex(Integer[] messageLine, int t) {

        // if array is Null
        if (messageLine == null) {
            return -1;
        }

        // find length of array
        int len = messageLine.length;
        int i = 0;

        // traverse in the array
        while (i < len) {

            // if the i-th element is t
            // then return the index
            if (messageLine[i] == t) {
                return i;
            } else {
                i = i + 1;
            }
        }
        return -1;
    }

    private static void appendTextToExistingFile(String text) {
        try {
            String filename = outputFileName;
            FileWriter fw = new FileWriter(filename, true); // the true will append the new data
            fw.write(text);// appends the string to the file
            fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }

    }

    private static void createFile(String filename) {

        // create a file object for the current location
        File file = new File("JavaFile.java");

        try {

            // create a new file with name specified
            // by the file object
            boolean value = file.createNewFile();
            if (value) {
                System.out.println("New Java File is created.");
            } else {
                System.out.println("The file already exists.");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
