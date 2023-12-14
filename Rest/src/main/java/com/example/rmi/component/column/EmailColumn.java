package com.example.rmi.component.column;

import com.example.rmi.component.Column;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailColumn extends Column {
    // Regular expression for validating email addresses
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public EmailColumn(String name) {
        super(name);
        this.type = ColumnType.EMAIL.name();
    }

    @Override
    public boolean validate(String value) {
        if (value == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(value);
        return matcher.matches();
    }
}