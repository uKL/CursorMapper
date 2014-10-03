package cursor.mapper.example;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cursor.mapper.AnnotatedCursorMapper;

public class ExampleActivity extends Activity {

    @InjectView(R.id.first_name)
    TextView firstName;
    @InjectView(R.id.surname)
    TextView surname;
    @InjectView(R.id.email)
    TextView email;
    @InjectView(R.id.age)
    TextView age;
    @InjectView(R.id.dumped_values)
    TextView dumpedValues;
    AnnotatedCursorMapper<UserModel> mapper;
    private UserModel loadedUserModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);
        mapper = new AnnotatedCursorMapper(UserModel.class);
        bindExampleCursor();
    }

    @OnClick(R.id.create_content_values)
    public void onDumpRequested() {

        if (loadedUserModel != null) {
            ContentValues contentValues = mapper.toContentValues(loadedUserModel);
            dumpedValues.setText(contentValues.toString());
            saveToProvider(contentValues);
        }
    }

    private void saveToProvider(ContentValues contentValues) {
        getContentResolver().insert(ExampleContentProvider.QUERY, contentValues);
    }

    private void bindExampleCursor() {
        Cursor cursor = queryCursor();
        cursor.moveToFirst();
        loadedUserModel = mapper.toObject(cursor);
        bindUserModel(loadedUserModel);
        cursor.close();
    }

    private void bindUserModel(UserModel userModel) {
        firstName.setText(userModel.getFirstName());
        surname.setText(userModel.getSurname());
        email.setText(userModel.getEmail());
        age.setText(String.format("User is %s years old", userModel.getAge()));
    }

    private Cursor queryCursor() {
        return getContentResolver().query(ExampleContentProvider.QUERY, null, null, null, null);
    }
}
