package com.qeema.aps.payment.application.service;

import java.util.regex.Pattern;

public class PaymentValidation {

    private static final String[] MADA_BINS = {
            "446404", "440795", "440647", "421141", "474491", "588845", "968208", "457997", "457865", "468540",
            "468541", "468542", "468543", "417633", "446393", "636120", "968201", "410621", "409201", "403024",
            "458456", "462220", "968205", "455708", "484783", "588848", "455036", "968203", "486094", "486095",
            "486096", "504300", "440533", "489318", "489319", "445564", "968211", "410685", "406996", "432328",
            "428671", "428672", "428673", "968206", "446672", "543357", "434107", "407197", "407395", "42689700",
            "412565", "431361", "604906", "521076", "588850", "968202", "529415", "535825", "543085", "524130",
            "554180", "549760", "968209", "524514", "529741", "537767", "535989", "536023", "513213", "442463",
            "520058", "558563", "605141", "968204", "422817", "422818", "422819", "410834", "428331", "483010",
            "483011", "483012", "589206", "968207", "406136", "419593", "439954", "407520", "530060", "531196",
            "420132", "242030", "22402030"
    };

    private static final String MEEZA_BINS_REGEX = "507803[0-6][0-9]|507808[3-9][0-9]|507809[0-9][0-9]|507810[0-2][0-9]";

    public static class ValidationResult {
        public boolean validity;
        public String message;
        public String cardType;
        public int cardLength;

        public ValidationResult() {
        }

        public ValidationResult(boolean validity, String message) {
            this.validity = validity;
            this.message = message;
        }

        public ValidationResult(boolean validity, String message, String cardType) {
            this.validity = validity;
            this.message = message;
            this.cardType = cardType;
        }

        public ValidationResult(boolean validity, String message, String cardType, int cardLength) {
            this.validity = validity;
            this.message = message;
            this.cardType = cardType;
            this.cardLength = cardLength;
        }
    }

    public static ValidationResult validateCard(String cardNumber) {
        String cardType = "";
        boolean cardValidity = true;
        String message = "";
        int cardLength = 0;

        if (cardNumber != null && !cardNumber.isEmpty()) {
            cardNumber = cardNumber.replace(" ", "").replace("-", "");
            Pattern visaPattern = Pattern.compile("^4[0-9]{0,15}$");
            Pattern mastercardPattern = Pattern.compile("^5[0-5][0-9]{0,16}$|^2[2-7][0-9]{0,16}$");
            Pattern amexPattern = Pattern.compile("^3$|^3[47][0-9]{0,13}$");
            Pattern madaPattern = Pattern.compile("^(" + String.join("|", MADA_BINS) + ")");
            Pattern meezaPattern = Pattern.compile(MEEZA_BINS_REGEX);

            if (madaPattern.matcher(cardNumber).matches()) {
                cardType = "mada";
                cardLength = 16;
            } else if (meezaPattern.matcher(cardNumber).matches()) {
                cardType = "meeza";
                cardLength = 19;
            } else if (visaPattern.matcher(cardNumber).matches()) {
                cardType = "visa";
                cardLength = 16;
            } else if (mastercardPattern.matcher(cardNumber).matches()) {
                cardType = "mastercard";
                cardLength = 16;
            } else if (amexPattern.matcher(cardNumber).matches()) {
                cardType = "amex";
                cardLength = 15;
            } else {
                cardValidity = false;
                message = "Invalid Card";
            }

            if (cardNumber.length() < 15) {
                cardValidity = false;
                message = "Invalid Card Length";
            } else {
                boolean cardValidByAlgorithm = validateCardNumber(cardNumber);
                if (!cardValidByAlgorithm) {
                    cardValidity = false;
                    message = "Invalid Card";
                }
            }
        } else {
            message = "Card Number Empty";
            cardValidity = false;
        }

        return new ValidationResult(cardValidity, message, cardType, cardLength);
    }

    public static boolean validateCardNumber(String cardNumber) {
        Pattern pattern = Pattern.compile("^[0-9]{13,19}$");
        if (!pattern.matcher(cardNumber).matches()) {
            return false;
        }
        return validateCardNumberByAlgorithm(cardNumber);
    }

    private static boolean validateCardNumberByAlgorithm(String cardNumber) {
        int checksum = 0;
        int j = 1;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int calc = Integer.parseInt(String.valueOf(cardNumber.charAt(i))) * j;
            if (calc > 9) {
                checksum += 1;
                calc -= 10;
            }
            checksum += calc;
            j = (j == 1) ? 2 : 1;
        }

        return (checksum % 10) == 0;
    }

    public static ValidationResult validateHolderName(String cardHolderName) {
        boolean validity = true;
        String message = "";

        cardHolderName = cardHolderName.trim();
        if (cardHolderName.length() > 51 || cardHolderName.length() == 0) {
            validity = false;
            message = "Invalid Card Holder Name";
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z- '.]+$");
        if (!pattern.matcher(cardHolderName).matches()) {
            validity = false;
            message = "Invalid Card Holder Name";
        }

        return new ValidationResult(validity, message);
    }

    public static ValidationResult validateCVV(String cardCVV, String cardType) {
        boolean validity = false;
        String message = "Invalid Card CVV";

        if (cardCVV != null && !cardCVV.isEmpty()) {
            cardCVV = cardCVV.trim();
            if (cardType == null || cardType.isEmpty()) {
                if (cardCVV.length() == 3 && !cardCVV.equals("000")) {
                    validity = true;
                    message = "";
                } else if (cardCVV.length() == 4 && "amex".equals(cardType) && !cardCVV.equals("0000")) {
                    validity = true;
                    message = "";
                }
            }
        }

        return new ValidationResult(validity, message);
    }

    public static ValidationResult validateCardExpiry(String cardExpiryMonth, String cardExpiryYear) {
        boolean validity = true;
        String message = "";

        if (cardExpiryMonth == null || cardExpiryMonth.isEmpty()) {
            validity = false;
            message = "Invalid Expiry Month";
        } else if (cardExpiryYear == null || cardExpiryYear.isEmpty()) {
            validity = false;
            message = "Invalid Expiry Year";
        } else if (Integer.parseInt(cardExpiryMonth) <= 0 || Integer.parseInt(cardExpiryMonth) > 12) {
            validity = false;
            message = "Invalid Expiry Month";
        } else {
            int currentYear = Integer.parseInt(String.valueOf(java.time.Year.now()).substring(2));
            int currentMonth = java.time.LocalDate.now().getMonthValue();
            int expiryYear = Integer.parseInt(cardExpiryYear);
            int expiryMonth = Integer.parseInt(cardExpiryMonth);

            if (expiryYear < currentYear || (expiryYear == currentYear && expiryMonth < currentMonth)) {
                validity = false;
                message = "Invalid Expiry Date";
            }
        }

        return new ValidationResult(validity, message);
    }
}