package cursor.mapper.example;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class ExampleContentProvider extends ContentProvider {

    public static class Contract {

        public static final String FIRST_NAME = "first_name";
        public static final String SURNAME = "surname";
        public static final String EMAIL = "email";
        public static final String AGE = "age";
    }

    public static final Uri QUERY = Uri.parse("content://cursor.mapper.example/query");

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{Contract.FIRST_NAME, Contract.SURNAME, Contract.EMAIL,
                Contract.AGE});
        matrixCursor.addRow(new Object[]{"John", "Doe", "john.doe@gmail.com", 18});
        return matrixCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
