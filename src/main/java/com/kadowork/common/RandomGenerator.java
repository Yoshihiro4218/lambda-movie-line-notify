package com.kadowork.common;

import java.security.*;
import java.util.*;

public class RandomGenerator {
    private SecureRandom secureRandom;
    private static final String alphabetUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String alphabetULowerCase = alphabetUpperCase.toLowerCase();
    private static final String number = "0123456789";

    public UUID generateUUID() {
        return UUID.randomUUID();
    }

    public String generateStringAndNumber() {
        return generateStringAndNumber(0);
    }

    public String generateStringAndNumber(int size) {
        if (size <= 0) {
            size = 1;
        }
        secureRandom = new SecureRandom();
        String target = alphabetUpperCase + alphabetULowerCase + number;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(target.charAt(secureRandom.nextInt(target.length())));
        }
        return sb.toString();
    }

    public String generateNumber() {
        return generateNumber(0);
    }

    public List<String> generateNumbers(int count) {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            numbers.add(generateNumber());
        }
        return numbers;
    }

    public String generateNumber(int digit) {
        if (digit <= 0) {
            digit = 1;
        }
        secureRandom = new SecureRandom();
        String target = number;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            sb.append(target.charAt(secureRandom.nextInt(target.length())));
        }
        return sb.toString();
    }

    public int randomInt(int max) {
        secureRandom = new SecureRandom();
        return secureRandom.nextInt(max + 1);
    }

    public List<Integer> randomIntegers(int max, int count) {
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            integers.add(randomInt(max));
        }
        return integers;
    }
}
