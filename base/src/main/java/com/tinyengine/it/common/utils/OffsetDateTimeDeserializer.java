/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

/**
 * The type offset date time Deserializer.
 *
 * @since 2025-06-19
 */
public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    private static final DateTimeFormatter ISO_8601_FORMATTER;

    public OffsetDateTimeDeserializer() {
    }

    /**
     * To Deserialize.
     *
     * @param jsonParser the jsonParser
     * @param deserializationContext the deserializationContext
     * @return OffsetDateTime yhe OffsetDateTime
     */
    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (StringUtils.isEmpty(jsonParser.getText())) {
            return null;
        } else {
            TemporalAccessor temporalAccessor = ISO_8601_FORMATTER.parseBest(jsonParser.getText(), ZonedDateTime::from, LocalDateTime::from);
            if (temporalAccessor instanceof ZonedDateTime) {
                return ((ZonedDateTime) temporalAccessor).toOffsetDateTime();
            } else {
                return temporalAccessor instanceof LocalDateTime ? ((LocalDateTime) temporalAccessor).atOffset(ZoneOffset.UTC) : null;
            }
        }

    }

    static {
        ISO_8601_FORMATTER = (new DateTimeFormatterBuilder().parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE).optionalStart()
            .appendLiteral('T').optionalEnd().optionalStart().appendLiteral(' ').optionalEnd()
            .append(DateTimeFormatter.ISO_LOCAL_TIME).optionalStart().appendOffsetId().optionalEnd().toFormatter());
    }
}
