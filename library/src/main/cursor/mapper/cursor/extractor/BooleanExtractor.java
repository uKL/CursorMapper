/*
 * Copyright (C) 2014 Paweł Urban<pawel.urban@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cursor.mapper.cursor.extractor;

import android.database.Cursor;

public class BooleanExtractor implements ColumnExtractor {

    @Override
    public Boolean extract(Class<?> targetType, Cursor cursor, int columnIndex) {
        String cursorString = cursor.getString(columnIndex);

        if (isNumeric(cursorString)) {
            return Integer.parseInt(cursorString) == 1;
        } else {
            return Boolean.parseBoolean(cursorString);
        }
    }

    private boolean isNumeric(String cursorString) {

        try {
            Double.parseDouble(cursorString);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
