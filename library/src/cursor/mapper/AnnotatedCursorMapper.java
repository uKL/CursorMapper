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

package cursor.mapper;

import static cursor.mapper.log.LogUtils.LOGW;
import static cursor.mapper.log.LogUtils.makeLogTag;

import android.content.ContentValues;
import android.database.Cursor;

import cursor.mapper.annotation.CursorName;
import cursor.mapper.contentvalues.GenericContentValuesWriter;
import cursor.mapper.cursor.CursorExtractor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotatedCursorMapper<S> implements CursorMapper<S> {
    public static final String TAG = makeLogTag(AnnotatedCursorMapper.class);

    private Class<S> mClazz;
    private CursorExtractor mCursorExtractor;
    private boolean mIsIgnoringNull = false;

    public AnnotatedCursorMapper(Class<S> clazz) {
        this(new CursorExtractor(), clazz);
    }

    public AnnotatedCursorMapper(CursorExtractor extractor, Class<S> clazz) {
        mCursorExtractor = extractor;
        mClazz = clazz;
    }

    @Override
    public ContentValues toContentValues(S model) {
        validateNotNull(model);
        List<Field> annotatedFields = getAnnotatedFields();

        ContentValues contentValues = new ContentValues(annotatedFields.size());
        GenericContentValuesWriter writer = new GenericContentValuesWriter(contentValues);

        for (Field field : annotatedFields) {
            Object fieldValue = getFieldValue(model, field);

            if (!isIgnored(fieldValue)) {
                String columnName = findColumnName(field);
                writer.put(columnName, fieldValue);
            }
        }

        return contentValues;
    }

    @Override
    public S toObject(Cursor cursor) {
        validateNotNull(cursor);
        S instance = createInstance();

        if (instance == null) {
            return null;
        }

        fillWithData(instance, cursor);
        return instance;
    }

    private S createInstance() {
        try {
            return (S) mClazz.newInstance();
        } catch (InstantiationException e) {
            LOGW(TAG, "Unable to instantiate model: " + mClazz);
            return null;
        } catch (IllegalAccessException e) {
            LOGW(TAG, "Unable to instantiate model: " + mClazz);
            return null;
        }
    }

    private void fillWithData(S model, Cursor cursor) {
        List<Field> annotatedFields = getAnnotatedFields();

        for (Field field : annotatedFields) {
            String columnName = findColumnName(field);

            if (mCursorExtractor.hasColumn(cursor, columnName)) {
                setField(cursor, columnName, model, field);
            }
        }
    }

    private String findColumnName(Field field) {
        return field.getAnnotation(CursorName.class).value();
    }

    private List<Field> getAnnotatedFields() {
        List<Field> annotatedFields = new ArrayList<Field>();
        Field[] declaredFields = mClazz.getDeclaredFields();

        for (Field field : declaredFields) {

            if (hasMappingAnnotation(field)) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    private Object getFieldValue(S model, Field field) {
        try {
            return field.get(model);
        } catch (IllegalArgumentException e) {
            LOGW(TAG, "Unable to get field value");
            return null;
        } catch (IllegalAccessException e) {
            LOGW(TAG, "Unable to get field value");
            return null;
        }
    }

    private boolean hasMappingAnnotation(Field field) {
        Annotation[] annotations = field.getDeclaredAnnotations();

        for (Annotation annotation : annotations) {
            if (annotation instanceof CursorName) {
                return true;
            }
        }

        return false;
    }

    private boolean isIgnored(Object fieldValue) {
        return mIsIgnoringNull && fieldValue == null;
    }

    private void setField(Cursor cursor, String columnName, S model, Field field) {
        Object extractedValue = mCursorExtractor.extract(cursor, columnName, field.getType());

        if (extractedValue != null) {
            setValue(model, field, extractedValue);
        }
    }

    private void setValue(S model, Field field, Object extractedValue) {
        try {
            field.set(model, extractedValue);
        } catch (IllegalArgumentException e) {
            LOGW(TAG, "Unable to set field: " + field);
        } catch (IllegalAccessException e) {
            LOGW(TAG, "Unable to set field: " + field);
        }
    }

    private void validateNotNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

}
