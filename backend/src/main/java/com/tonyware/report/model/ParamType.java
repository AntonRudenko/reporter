package com.tonyware.report.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;

public enum ParamType {
    STRING {
        @Override
        public Object convert(String value) {
            if (StringUtils.isEmpty(value)) return "";
            return value;
        }
    },
    INTEGER {
        @Override
        public Object convert(String value) {
            if (StringUtils.isEmpty(value)) return null;
            return Integer.valueOf(value);
        }
    },
    DOUBLE {
        @Override
        public Object convert(String value) {
            if (StringUtils.isEmpty(value)) return null;
            return Double.valueOf(value);
        }
    },
    DATE {
        @Override
        public Object convert(String value) {
            if (StringUtils.isEmpty(value)) return null;
            return LocalDateTime.parse(value);
        }
    };

    public abstract Object convert(String value);
}
