package cursor.mapper.example;

import cursor.mapper.annotation.CursorName;
import cursor.mapper.example.ExampleContentProvider.Contract;

public class UserModel {

    @CursorName(Contract.FIRST_NAME)
    private String firstName;
    @CursorName(Contract.EMAIL)
    private String email;
    @CursorName(Contract.SURNAME)
    private String surname;
    @CursorName(Contract.AGE)
    private int age;

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }
}
