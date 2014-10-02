package cursor.mapper.cursor.extractor;

import android.database.Cursor;

import cursor.mapper.AnnotatedCursorMapper;

public class RecursiveExtractor implements ColumnExtractor {

    private AnnotatedCursorMapper mAnnotatedCursorMapper;

    public RecursiveExtractor(Class<?> type) {
        mAnnotatedCursorMapper = new AnnotatedCursorMapper(type);
    }

    @Override
    public Object extract(Cursor cursor, int columnIndex) {
        return mAnnotatedCursorMapper.toObject(cursor);
    }
}
