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

package cursor.mapper.contentvalues;

import android.content.ContentValues;

public class GenericContentValuesWriter {

    private ContentValues mValues;

    public GenericContentValuesWriter(ContentValues contentValues) {
        mValues = contentValues;
    }

    public void put(String columnName, Object object) {
        validateColumnName(columnName);

        if (object == null) {
            mValues.putNull(columnName);
        } else if (object instanceof Boolean) {
            mValues.put(columnName, (Boolean) object);
        } else if (object instanceof Byte) {
            mValues.put(columnName, (Byte) object);
        } else if (object instanceof Double) {
            mValues.put(columnName, (Double) object);
        } else if (object instanceof Float) {
            mValues.put(columnName, (Float) object);
        } else if (object instanceof Integer) {
            mValues.put(columnName, (Integer) object);
        } else if (object instanceof Long) {
            mValues.put(columnName, (Long) object);
        } else if (object instanceof Short) {
            mValues.put(columnName, (Short) object);
        } else if (object instanceof String) {
            mValues.put(columnName, (String) object);
        } else if (object instanceof byte[]) {
            mValues.put(columnName, (byte[]) object);
        } else if (object instanceof ContentValues) {
            mValues.putAll((ContentValues) object);
        } else if (object instanceof Enum) {
            mValues.put(columnName, ((Enum) object).name());
        } else {
            throw new IllegalArgumentException("Unable to put " + object.getClass().getName() + " in ContentValues.");
        }
    }

    private void validateColumnName(String columnName) {

        if (columnName == null || columnName.length() == 0) {
            throw new IllegalArgumentException("Column name is required");
        }
    }
}
