package sym.heigvd.ch.sym_labo_protocole.object;

/**
 * Class representing a student.
 * It's the object we choose to use in (de)serialization.
 */
public class Student {

    public enum Gender {
        MALE, FEMALE;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    private String firstname;   // The firstname of the student
    private String name;        // The name of the student
    private int age;            // The age of the student
    private Gender gender;      // The gender of the student

    /**
     * Public student constructor.
     * @param firstname The firstname of the new student
     * @param name The name of the new student
     * @param age The age of the new student
     * @param gender The gender of the new student
     */
    public Student(String firstname, String name, int age, Gender gender) {

        this.firstname = firstname;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    /**
     * Static method use to verify fields before the creation of student.
     * @param firstname The firstname to check
     * @param name The name to check
     * @param age The age to check
     * @param gender The gender to check
     * @return A new student if fields are correct, null otherwise
     */
    public static Student verifyFields(String firstname, String name, int age, int gender) {

        if(firstname.equals("")) {

            return null;
        }

        if(age < 0) {
            return null;
        }

        if(name.equals("")) {

            return null;
        }

        if(gender >= Gender.values().length || gender < 0) {

            return null;
        }

        return new Student(firstname, name, age, Gender.values()[gender]);
    }

    @Override
    public String toString() {

        return firstname + " " + name + ", " + age + " years, " + gender.toString();
    }
}
