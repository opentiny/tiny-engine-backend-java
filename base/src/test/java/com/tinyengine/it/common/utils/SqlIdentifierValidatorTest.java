package com.tinyengine.it.common.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SqlIdentifierValidatorTest {

    @Test
    void validIdentifier() {
        assertDoesNotThrow(() -> SqlIdentifierValidator.validate("name"));
        assertDoesNotThrow(() -> SqlIdentifierValidator.validate("_name"));
        assertDoesNotThrow(() -> SqlIdentifierValidator.validate("Name123"));
        assertDoesNotThrow(() -> SqlIdentifierValidator.validate("created_at"));
    }

    @Test
    void rejectNullIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate(null));
    }

    @Test
    void rejectEmptyIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate(""));
    }

    @Test
    void rejectSqlInjectionInIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("@@version"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("1; DROP TABLE users"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("id OR 1=1"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("(SELECT password FROM t_user)"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("name AS leaked"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("name'"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("name\""));
    }

    @Test
    void rejectSubqueryInIdentifier() {
        assertThrows(IllegalArgumentException.class,
                () -> SqlIdentifierValidator.validate("(SELECT GROUP_CONCAT(table_name) FROM information_schema.tables)"));
    }

    @Test
    void rejectStartingWithDigit() {
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validate("1name"));
    }

    @Test
    void validateAllWithValidList() {
        List<String> validFields = Arrays.asList("id", "name", "created_at");
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateAll(validFields));
    }

    @Test
    void validateAllRejectsInvalidEntry() {
        List<String> fields = Arrays.asList("id", "@@version", "name");
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validateAll(fields));
    }

    @Test
    void validateAllAcceptsNull() {
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateAll(null));
    }

    @Test
    void validateOrderTypeAsc() {
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateOrderType("ASC"));
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateOrderType("asc"));
    }

    @Test
    void validateOrderTypeDesc() {
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateOrderType("DESC"));
        assertDoesNotThrow(() -> SqlIdentifierValidator.validateOrderType("desc"));
    }

    @Test
    void rejectInvalidOrderType() {
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validateOrderType("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validateOrderType("; DROP TABLE"));
        assertThrows(IllegalArgumentException.class, () -> SqlIdentifierValidator.validateOrderType(null));
    }

    @Test
    void rejectSqliPatternsInIdentifier() {
        String[] sqliPayloads = {
                "@@version",
                "@@datadir",
                "SLEEP(5)",
                "BENCHMARK(10000000,SHA1('test'))",
                "LOAD_FILE('/etc/passwd')",
                "INTO OUTFILE '/tmp/shell.php'",
                "UNION SELECT 1,2,3",
                "information_schema.tables",
                "1 OR 1=1",
                "'; DROP TABLE users--",
                "name AND 1=1",
                "id; SELECT SLEEP(5)",
                "COUNT(*)",
                "GROUP_CONCAT(username)"
        };
        for (String payload : sqliPayloads) {
            assertThrows(IllegalArgumentException.class,
                    () -> SqlIdentifierValidator.validate(payload),
                    "Should reject: " + payload);
        }
    }
}
