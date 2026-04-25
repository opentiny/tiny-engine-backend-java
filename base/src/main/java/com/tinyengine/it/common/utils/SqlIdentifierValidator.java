package com.tinyengine.it.common.utils;

import java.util.List;
import java.util.regex.Pattern;

public class SqlIdentifierValidator {

    private static final Pattern IDENTIFIER_PATTERN =
            Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private static final Pattern ORDER_TYPE_PATTERN =
            Pattern.compile("^(ASC|DESC)$", Pattern.CASE_INSENSITIVE);

    private SqlIdentifierValidator() {
    }

    public static void validate(String identifier) {
        if (identifier == null || !IDENTIFIER_PATTERN.matcher(identifier).matches()) {
            throw new IllegalArgumentException("Invalid SQL identifier: " + identifier);
        }
    }

    public static void validateAll(List<String> identifiers) {
        if (identifiers == null) {
            return;
        }
        identifiers.forEach(SqlIdentifierValidator::validate);
    }

    public static void validateOrderType(String orderType) {
        if (orderType == null || !ORDER_TYPE_PATTERN.matcher(orderType).matches()) {
            throw new IllegalArgumentException("Invalid order type: " + orderType);
        }
    }
}
