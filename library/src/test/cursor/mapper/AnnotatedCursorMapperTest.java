package cursor.mapper;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;

import static org.fest.assertions.api.ANDROID.assertThat;

import android.content.ContentValues;

import org.fest.assertions.Assertions;
import org.fest.assertions.api.android.content.ContentValuesEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import cursor.mapper.models.ExtendedFlatMappedModel;
import cursor.mapper.models.FlatMappedModel;
import cursor.mapper.models.FlatMappedModelWithPrivateFields;
import cursor.mapper.models.ModelNotAnnotated;
import cursor.mapper.models.NestedMappedModel;

@RunWith(RobolectricTestRunner.class)
public class AnnotatedCursorMapperTest {

    FlatMappedModel flatModel = new FlatMappedModel();
    AnnotatedCursorMapper objectUnderTest;
    ExtendedFlatMappedModel extendedFlatModel = new ExtendedFlatMappedModel();
    NestedMappedModel nestedModel = new NestedMappedModel();

    @Before
    public void setUp() throws Exception {
        objectUnderTest = new AnnotatedCursorMapper(FlatMappedModel.class);
    }

    @Test
    public void shouldNotAllowNullModel() throws Exception {
        // given
        FlatMappedModel model = null;

        // when
        catchException(objectUnderTest).toContentValues(model);

        // then
        Assertions.assertThat(caughtException()).isInstanceOf(IllegalArgumentException.class).hasMessage("Model cannot be null");
    }

    @Test
    public void shouldReturnEmptyContentValuesIfModelDoesntHaveAnnotations() throws Exception {
        // given
        objectUnderTest = new AnnotatedCursorMapper(ModelNotAnnotated.class);

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(new ModelNotAnnotated());

        // then
        assertThat(contentValues).isEmpty();
    }

    @Test
    public void shouldSerializeAllValues() throws Exception {
        // when
        ContentValues contentValues = objectUnderTest.toContentValues(flatModel);

        // then
        assertThatContentValuesContainAllFieldsFromModel(contentValues, flatModel, false);
    }

    @Test
    public void shouldSerializeAllValuesInFlatAndNestedModel() throws Exception {
        // given
        objectUnderTest = new AnnotatedCursorMapper(NestedMappedModel.class);

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(nestedModel);

        // then
        assertThatContentValuesContainAllFieldsFromModel(contentValues, nestedModel.nested, false);
        assertThat(contentValues).contains(ContentValuesEntry.entry("secondInt", nestedModel.aSecondInt));
    }

    @Test
    public void shouldSerializeAllValuesInInheritanceTree() throws Exception {
        // given
        objectUnderTest = new AnnotatedCursorMapper(ExtendedFlatMappedModel.class);

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(extendedFlatModel);

        // then
        assertThatContentValuesContainAllFieldsFromModel(contentValues, extendedFlatModel, false);
        assertThat(contentValues).contains(ContentValuesEntry.entry("addtitionalValue", extendedFlatModel.aAdditionalString));
    }

    @Test
    public void shouldSerializeAllValuesWithForcedSetting() throws Exception {
        // given
        objectUnderTest.setIgnoringNull(false);

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(flatModel);

        // then
        assertThatContentValuesContainAllFieldsFromModel(contentValues, flatModel, false);
    }

    @Test
    public void shouldSerializeAllValuesWithoutNull() throws Exception {
        // given
        objectUnderTest.setIgnoringNull(true);

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(flatModel);

        // then
        assertThatContentValuesContainAllFieldsFromModel(contentValues, flatModel, true);
    }

    @Test
    public void shouldSerializePrivateField() throws Exception {
        // given
        objectUnderTest = new AnnotatedCursorMapper(FlatMappedModelWithPrivateFields.class);
        FlatMappedModelWithPrivateFields model = new FlatMappedModelWithPrivateFields();

        // when
        ContentValues contentValues = objectUnderTest.toContentValues(model);

        // then
        assertThat(contentValues).contains(ContentValuesEntry.entry("aInt", model.getPrivateFieldValue()));
    }

    private void assertThatContentValuesContainAllFieldsFromModel(ContentValues contentValues, FlatMappedModel model,
            boolean ignoreNull) {
        assertThat(contentValues).contains(ContentValuesEntry.entry("aInt", model.aInt));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aLong", model.aLong));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aShort", model.aShort));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aByte", model.aByte));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aFloat", model.aFloat));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aDouble", model.aDouble));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aBoolean", model.aBoolean));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aString", model.aString));

        if (!ignoreNull) {
            assertThat(contentValues).contains(ContentValuesEntry.entry("aNullString", model.aNullString));
        }

        assertThat(contentValues).contains(ContentValuesEntry.entry("aEnum", model.aEnum.name()));
        assertThat(contentValues).contains(ContentValuesEntry.entry("aByteArray", model.aByteArray));
    }
}