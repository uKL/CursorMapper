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

import static timber.log.Timber.v;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ColumnExtractorFactory {

    private static class SingletonHolder {

        public static final ColumnExtractorFactory INSTANCE = new ColumnExtractorFactory();
    }

    private Map<Class<?>, ColumnExtractor> mExtractors = new HashMap<Class<?>, ColumnExtractor>();

    public ColumnExtractorFactory() {
        addExtractor(String.class, new StringExtractor());

        LongExtractor longExtractor = new LongExtractor();
        addExtractor(long.class, longExtractor);
        addExtractor(Long.class, longExtractor);

        DoubleExtractor doubleExtractor = new DoubleExtractor();
        addExtractor(Double.class, doubleExtractor);
        addExtractor(double.class, doubleExtractor);

        addExtractor(byte[].class, new BlobExtractor());

        FloatExtractor floatExtractor = new FloatExtractor();
        addExtractor(float.class, floatExtractor);
        addExtractor(Float.class, floatExtractor);

        IntExtractor intExtractor = new IntExtractor();
        addExtractor(int.class, intExtractor);
        addExtractor(Integer.class, intExtractor);

        ShortExtractor shortExtractor = new ShortExtractor();
        addExtractor(short.class, shortExtractor);
        addExtractor(Short.class, shortExtractor);

        BooleanExtractor booleanExtractor = new BooleanExtractor();
        addExtractor(boolean.class, booleanExtractor);
        addExtractor(Boolean.class, booleanExtractor);
    }

    public static ColumnExtractorFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addExtractor(Class<?> type, ColumnExtractor extractor) {
        v("Adding extractor for type: " + type);
        mExtractors.put(type, extractor);
    }

    public ColumnExtractor get(Field fieldType) {
        Class<?> type = fieldType.getType();
        return mExtractors.containsKey(type) ? mExtractors.get(type) : new RecursiveExtractor(type);
    }
}
