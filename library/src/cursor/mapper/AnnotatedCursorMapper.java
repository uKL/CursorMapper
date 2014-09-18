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

import static android.text.TextUtils.isEmpty;

import static cursor.mapper.log.LogUtils.LOGW;
import static cursor.mapper.log.LogUtils.makeLogTag;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cursor.mapper.annotation.CursorName;
import cursor.mapper.contentvalues.GenericContentValuesWriter;
import cursor.mapper.cursor.CursorExtractor;

public class AnnotatedCursorMapper<S> implements CursorMapper<S> {

    public static final String TAG = makeLogTag(AnnotatedCursorMapper.class);
    public static final int NO_LIMIT = 0;
    private HashMap<Field, String> mAnnotationValues;
    private Class<S> mClazz;
    private CursorExtractor mCursorExtractor;
    private AnnotationCache mAnnotationCache;
    private boolean mIsIgnoringNull = false;

    public AnnotatedCursorMapper(Class<S> clazz) {
        this(new CursorExtractor(), clazz);
    }

    public AnnotatedCursorMapper(CursorExtractor extractor, Class<S> clazz) {
        mCursorExtractor = extractor;
        mClazz = clazz;
        setAnnotationCache(AnnotationCache.getInstance());
        initializeAnnotationValues(getAnnotatedFields(clazz));
    }

    public void setAnnotationCache(AnnotationCache annotationCache) {
        mAnnotationCache = annotationCache;
    }

    @Override
    public ContentValues toContentValues(S model) {
        validateNotNull(model);

        ContentValues contentValues = new ContentValues(mAnnotationValues.size());
        GenericContentValuesWriter writer = new GenericContentValuesWriter(contentValues);

        for (Field field : mAnnotationValues.keySet()) {
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

    @Override
    public List<S> toObjectList(Cursor cursor) {
        return toObjectList(cursor, NO_LIMIT, true);
    }

    @Override
    public List<S> toObjectList(Cursor cursor, int limit, boolean moveToFirst) {
        List<S> objects = new ArrayList<S>();
        int currentItem = 0;

        if (cursor != null && moveToFirst(cursor, moveToFirst)) {

            while (!cursor.isAfterLast() && (limit == NO_LIMIT || currentItem < limit)) {
                objects.add(toObject(cursor));
                cursor.moveToNext();
                currentItem++;
            }
        }

        return objects;

    }

    private void cacheAnnotatedFields(Class<S> clazz, List<Field> annotatedFields) {

        if (mAnnotationCache != null) {
            mAnnotationCache.put(clazz, annotatedFields);
        }
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

        for (Field field : mAnnotationValues.keySet()) {
            String columnName = findColumnName(field);

            if (isRecursive(columnName) || mCursorExtractor.hasColumn(cursor, columnName)) {
                setField(cursor, columnName, model, field);
            }
        }
    }

    private String findColumnName(Field field) {
        String value = mAnnotationValues.get(field);
        return value != null ? value : findColumnNameInAnnotation(field);
    }

    private String findColumnNameInAnnotation(Field field) {
        return field.getAnnotation(CursorName.class).value();
    }

    private List<Field> getAnnotatedFields(Class<S> clazz) {

        if (mAnnotationCache != null && mAnnotationCache.contains(clazz)) {
            return mAnnotationCache.get(clazz);
        } else {
            List<Field> annotatedFields = new ArrayList<Field>();

            for (Field field : clazz.getDeclaredFields()) {

                if (hasMappingAnnotation(field)) {
                    annotatedFields.add(field);
                }
            }

            cacheAnnotatedFields(clazz, annotatedFields);
            return annotatedFields;
        }
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

    private boolean hasInCache(Class<S> clazz) {
        return mAnnotationCache != null && mAnnotationCache.contains(clazz);
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

    private void initializeAnnotationValues(List<Field> annotatedFields) {
        mAnnotationValues = new HashMap<Field, String>();

        for (Field annotatedField : annotatedFields) {
            mAnnotationValues.put(annotatedField, findColumnNameInAnnotation(annotatedField));
        }
    }

    private boolean isIgnored(Object fieldValue) {
        return mIsIgnoringNull && fieldValue == null;
    }

    private boolean isRecursive(String columnName) {
        return isEmpty(columnName);
    }

    private boolean moveToFirst(Cursor cursor, boolean moveToFirst) {
        return moveToFirst ? cursor.moveToFirst() : true;
    }

    private void setField(Cursor cursor, String columnName, S model, Field field) {
        Object extractedValue = mCursorExtractor.extract(cursor, columnName, field);

        if (extractedValue != null) {
            setValue(model, field, extractedValue);
        }
    }

    private void setValue(S model, Field field, Object extractedValue) {

        try {
            field.setAccessible(true);
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
