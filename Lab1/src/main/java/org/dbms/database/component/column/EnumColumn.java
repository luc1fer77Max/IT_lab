package org.dbms.database.component.column;

import java.util.HashSet;
import java.util.Set;
import org.dbms.database.component.Column;

public class EnumColumn extends Column {

    public EnumColumn(String name) {
        super(name);
        this.type = ColumnType.ENUM.name(); // Assuming ENUM is a valid ColumnType
    }

    @Override
    public boolean validate(String data) {
        if (data == null || data.isEmpty()) {
            return true;
        }

        String[] items = data.split(",");
        Set<String> uniqueItems = new HashSet<>();

        for (String item : items) {
            String trimmedItem = item.trim();

            // Check if the trimmed item contains spaces
            if (trimmedItem.contains(" ")) {
                return false; // Invalid data, contains space within an item
            }

            // Add to set for uniqueness check, in lower case
            if (!uniqueItems.add(trimmedItem.toLowerCase())) {
                return false; // Duplicate found, validation fails
            }
        }

        // All items are unique and valid, validation passes
        return true;
    }
}