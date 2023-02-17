package util;

public class ValidaCpf {
    public static boolean validaCpf(String cpf) {
        if (cpf.length() != 11) return false;

        int firstDigit = 0, secondDigit = 0, digit, weight = 10;
        for (int i = 0; i < 9; i++) {
            digit = Integer.parseInt(cpf.substring(i, i + 1));
            firstDigit += digit * (weight--);
        }

        if (firstDigit % 11 < 2) {
            firstDigit = 0;
        } else {
            firstDigit = 11 - (firstDigit % 11);
        }

        weight = 11;
        for (int i = 0; i < 10; i++) {
            digit = Integer.parseInt(cpf.substring(i, i + 1));
            secondDigit += digit * (weight--);
        }

        if (secondDigit % 11 < 2) {
            secondDigit = 0;
        } else {
            secondDigit = 11 - (secondDigit % 11);
        }

        return (cpf.substring(9, 10).equals(String.valueOf(firstDigit)))
                && (cpf.substring(10, 11).equals(String.valueOf(secondDigit)));
    }
}
