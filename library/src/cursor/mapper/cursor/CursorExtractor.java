/*
 * Copyright (C) 2013 Paweł Urban<pawel.urban@allegro.pl>
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

package cursor.mapper.cursor;

import static cursor.mapper.log.LogUtils.LOGW;
import static cursor.mapper.log.LogUtils.makeLogTag;

import android.database.Cursor;

import java.lang.reflect.Field;

import cursor.mapper.cursor.extractor.ColumnExtractor;
import cursor.mapper.cursor.extractor.ColumnExtractorFactory;

public class CursorExtractor {

    public static final String TAG = makeLogTag(CursorExtractor.class);
    private ColumnExtractorFactory mColumnExtractorFactory;

    public CursorExtractor() {
        this(ColumnExtractorFactory.getInstance());
    }

    public CursorExtractor(ColumnExtractorFactory factory) {
        mColumnExtractorFactory = factory;
    }

    public Object extract(Cursor cursor, String columnName, Field fieldType) {
        int columnIndex = cursor.getColumnIndex(columnName);

        ColumnExtractor columnExtractor = mColumnExtractorFactory.get(fieldType);

        if (columnExtractor == null) {
            LOGW(TAG, "Unsupported column type: " + fieldType);
            return null;
        }

        return columnExtractor.extract(cursor, columnIndex);
    }

    public boolean hasColumn(Cursor cursor, String columnName) {
        try {
            cursor.getColumnIndexOrThrow(columnName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
