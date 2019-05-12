package com.keniang;

import java.util.stream.IntStream;

public class NumberMain {
    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100).forEach(n -> {
            String temp = "";
            if (n % 3 == 0) {
                temp += "Fizz";
            }

            if (n % 5 == 0) {
                temp += "Buzz";
            }

            if (temp.isEmpty()) {
                temp += n;
            }

            System.out.println(temp);
        });
    }
}
