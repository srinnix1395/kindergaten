package com.srinnix.kindergarten.base;

/**
 * Created by anhtu on 5/6/2017.
 */

class Ideone {
    public static void main(String[] args) throws java.lang.Exception {
        char operator[] = new char[9];
        TRY(1, operator);
    }

    public static void TRY(int i, char[] operator) {
        if (i == 8) {
            if (checkResult(operator) == 100) {
                printResult(operator);
            }
            clear(operator);
        } else {
            for (int j = 0; j < 3; j++) {
                switch (j) {
                    case 0: {
                        operator[i] = '+';
                        break;
                    }
                    case 1: {
                        operator[i] = '-';
                        break;
                    }
                    case 2: {
                        operator[i] = '=';
                        break;
                    }
                }
                TRY(i + 1, operator);
            }
        }
    }

    public static void clear(char[] operator) {
        for (int i = 0; i < operator.length; i++) {
            operator[i] = '\0';
        }
    }

    public static int checkResult(char[] operator) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < operator.length; i++) {
            builder.append(String.valueOf(i + 1)).append(operator[i]);
        }
        builder.append("9");

        String s[] = builder.toString().split("\\+");

        int p = 0;
        for (String value : s) {
            if (!value.contains("-")) {
                p += Integer.parseInt(value);
            } else {
                String q[] = value.split("-");
                p += Integer.parseInt(q[0]);
                for (int j = 1; j < q.length; j++) {
                    p -= Integer.parseInt(q[j]);
                }
            }
        }

        return p;
    }

    public static void printResult(char[] operator) {
        for (int i = 0; i < operator.length - 1; i++) {
            System.out.print((i + 1) + " " + operator[i]);
        }
        System.out.println();
    }
}


