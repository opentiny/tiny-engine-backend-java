package com.tinyengine.it.common.utils;

import java.util.List;
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.DataflowAnomalyAnalysis"
})
public final class SqlIdentifierValidator {

    private SqlIdentifierValidator() {
        // Utility class.
    }

    public static void validate(final String identifier) {
        if (!isValidIdentifier(identifier)) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }

    public static String requireValidIdentifier(final String identifier) {
        validate(identifier);
        return identifier;
    }

    public static void validateAll(final List<String> identifiers) {
        if (identifiers == null) {
            return;
        }
        identifiers.forEach(SqlIdentifierValidator::validate);
    }

    public static void validateOrderType(final String orderType) {
        if (!isValidOrderType(orderType)) {
            throw new IllegalArgumentException("Invalid order type: " + orderType);
        }
    }

    public static String requireValidOrderType(final String orderType) {
        validateOrderType(orderType);
        return orderType.toUpperCase(java.util.Locale.ROOT);
    }

    public static boolean isValidIdentifier(final String identifier) {
        boolean valid = identifier != null && !identifier.isEmpty();
        if (valid) {
            valid = isIdentifierStart(identifier.charAt(0));
        }

        for (int index = 1; valid && index < identifier.length(); index++) {
            if (!isIdentifierPart(identifier.charAt(index))) {
                valid = false;
            }
        }
        return valid;
    }

    public static boolean isValidOrderType(final String orderType) {
        return "ASC".equalsIgnoreCase(orderType) || "DESC".equalsIgnoreCase(orderType);
    }

    public static String escapeSqlLiteral(final String value) {
        String escapedValue = null;
        if (value != null) {
            final StringBuilder escaped = new StringBuilder(value.length());
            for (int index = 0; index < value.length(); index++) {
                final char character = value.charAt(index);
                escaped.append(character);
                if (character == '\\' || character == '\'') {
                    escaped.append(character);
                }
            }
            escapedValue = escaped.toString();
        }
        return escapedValue;
    }

    private static boolean isIdentifierStart(final char character) {
        return character == '_'
                || character >= 'A' && character <= 'Z'
                || character >= 'a' && character <= 'z';
    }

    private static boolean isIdentifierPart(final char character) {
        return isIdentifierStart(character) || character >= '0' && character <= '9';
    }
}
