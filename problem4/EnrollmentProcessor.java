package com.availity.edi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class EnrollmentProcessor {


    public static void main(String[] args) {
        Logger.info("EnrollmentProcessor application started.");
        try {
          String inputFile = "input.csv";

            processEnrollments(inputFile);
        } catch (IOException e) {
            Logger.error("Error occurred while processing enrollments:  "+ e.getMessage());
        }
        Logger.info("EnrollmentProcessor application finished.");
    }



    private static void processEnrollments(String inputFile) throws IOException {
        Logger.info("Reading enrollment file...");
        Map<String, List<Enrollee>> enrolleesByCompany = getEnrolleesByCompany(inputFile);

        for (Map.Entry<String, List<Enrollee>> entry : enrolleesByCompany.entrySet()) {
            List<Enrollee> sortedEnrollees = sortEnrollees(entry.getValue());
            writeEnrolleesToFile(entry.getKey(), sortedEnrollees);
        }
    }

    private static Map<String, List<Enrollee>> getEnrolleesByCompany(String inputFile) throws IOException {
        return Files.lines(Paths.get(  inputFile))
                .skip(1) // Skip header
                .map(Enrollee::new)
                .collect(Collectors.groupingBy(Enrollee::getInsuranceCompany));
    }

    private static List<Enrollee> sortEnrollees(List<Enrollee> enrollees) {
        return enrollees.stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Enrollee::getUserId)
                        .thenComparing(Enrollee::getVersion).reversed())))
                .stream()
                .sorted(Comparator.comparing(Enrollee::getLastName).thenComparing(Enrollee::getFirstName))
                .collect(Collectors.toList());
    }

    private static void writeEnrolleesToFile(String companyName, List<Enrollee> enrollees) throws IOException {
        String outputFile = companyName + ".csv";
        Logger.info("Writing sorted enrollees to file:  "+ outputFile);
        deleteDuplicates(enrollees);
        Files.write(Paths.get(outputFile), (Iterable<String>) enrollees.stream().map(Enrollee::toCsv)::iterator);
    }

    private static void deleteDuplicates(List<Enrollee> enrollees) {
        Iterator<Enrollee> iterator = enrollees.iterator();
        Enrollee prevEnrollee = null;
        while (iterator.hasNext()) {
            Enrollee currentEnrollee = iterator.next();
            if (prevEnrollee != null && prevEnrollee.getUserId().equals(currentEnrollee.getUserId())) {
                iterator.remove();
            }
            prevEnrollee = currentEnrollee;
        }
    }


}
  class Logger {
    public static void info(String s) {
        System.out.println("[INFO] " + s);
    }

    public static void error(String message) {
        System.err.println("[ERROR] " + message);
    }
}



class Enrollee {
    private final String userId;
    private final String firstName;
    private final String lastName;
    private final int version;
    private final String insuranceCompany;

    Enrollee(String line) {
        String[] fields = line.split(",");
        this.userId = fields[0];
        this.firstName = fields[1];
        this.lastName = fields[2];
        this.version = Integer.parseInt(fields[3]);
        this.insuranceCompany = fields[4];
    }

    String getUserId() {
        return userId;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    int getVersion() {
        return version;
    }

    String getInsuranceCompany() {
        return insuranceCompany;
    }

    String toCsv() {
        return String.join(",", userId, firstName, lastName, String.valueOf(version), insuranceCompany);
    }
}
