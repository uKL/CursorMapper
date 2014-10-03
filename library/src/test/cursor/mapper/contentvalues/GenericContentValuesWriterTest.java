package cursor.mapper.contentvalues;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;

import android.content.ContentValues;

import org.fest.assertions.api.ANDROID;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.api.android.content.ContentValuesEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import cursor.mapper.models.SomeEnum;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GenericContentValuesWriterTest {

    public static class NotSupportedClass {

    }

    ContentValues contentValues;
    GenericContentValuesWriter objectUnderTest;

    @Before
    public void setUp() throws Exception {
        contentValues = new ContentValues();
        objectUnderTest = new GenericContentValuesWriter(contentValues);
    }

    @Test
    public void shouldPutAllItemsAndNotOverwrite() throws Exception {
        // given
        ContentValues values = new ContentValues();

        // when
        objectUnderTest.put("column1", "value");
        objectUnderTest.put("column2", 2);
        objectUnderTest.put("column3", 3L);
        objectUnderTest.put("column4", 5.25f);
        objectUnderTest.put("column5", true);
        objectUnderTest.put("column6", false);

        // then
        ANDROID.assertThat(contentValues).hasSize(6);
    }

    @Test
    public void shouldPutBooleanValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, Boolean.TRUE);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, Boolean.TRUE));
    }

    @Test
    public void shouldPutByteArrayValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, "some-string".getBytes());

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, "some-string".getBytes()));
    }

    @Test
    public void shouldPutByteValue() throws Exception {
        // given
        String columnName = "valid-column-name";
        Byte savedByte = new Byte((byte) 6);

        // when
        objectUnderTest.put(columnName, savedByte);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, savedByte));
    }

    @Test
    public void shouldPutContentValues() throws Exception {
        // given
        String columnName = "valid-column-name";
        ContentValues values = new ContentValues();
        values.put(columnName, "value");

        // when
        objectUnderTest.put(columnName, values);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, "value"));
    }

    @Test
    public void shouldPutDoubleValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, 6d);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, 6d));
    }

    @Test
    public void shouldPutEnumValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, SomeEnum.TEST_VALUE);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, SomeEnum.TEST_VALUE.name()));
    }

    @Test
    public void shouldPutFloatValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, 1.2f);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, 1.2f));
    }

    @Test
    public void shouldPutIntegerValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, 1);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, 1));
    }

    @Test
    public void shouldPutLongValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, 12345L);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, 12345L));
    }

    @Test
    public void shouldPutNullValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, null);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, null));
    }

    @Test
    public void shouldPutShortValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, (short) 5);

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, (short) 5));
    }

    @Test
    public void shouldPutStringValue() throws Exception {
        // given
        String columnName = "valid-column-name";

        // when
        objectUnderTest.put(columnName, "some-string");

        // then
        ANDROID.assertThat(contentValues).contains(ContentValuesEntry.entry(columnName, "some-string"));
    }

    @Test
    public void shouldThrowIllegalIllegalArgumentForUnknownType() throws Exception {
        // given
        Object savedObject = new NotSupportedClass();
        String columnName = "any-name";

        // when
        catchException(objectUnderTest).put(columnName, savedObject);

        // then
        Assertions.assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessage(
                "Unable to put " + savedObject.getClass().getName() + " in ContentValues.");
    }

    @Test
    public void shouldValidateEmptyColumnName() throws Exception {
        // given
        Object savedObject = new Object();
        String columnName = "";

        // when
        catchException(objectUnderTest).put(columnName, savedObject);

        // then
        Assertions.assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessage("Column name is " +
                "required");
    }

    @Test
    public void shouldValidateNullColumnName() throws Exception {
        // given
        Object savedObject = new Object();
        String columnName = null;

        // when
        catchException(objectUnderTest).put(columnName, savedObject);

        // then
        Assertions.assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessage("Column name is required");
    }
}