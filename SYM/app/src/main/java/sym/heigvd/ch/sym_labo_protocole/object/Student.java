package sym.heigvd.ch.sym_labo_protocole.object;

public class Student {

    public enum Gender {
        MALE, FEMALE;

        @Override
        public String toString()
        {
            return name().toLowerCase();
        }
    }

    private String firstname;
    private String name;
    private int age;
    private Gender gender;

    public Student(String firstname, String name, int age, Gender gender) {

        this.firstname = firstname;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public static Student verifyFields(String firstname, String name, int age, int gender) {

        if(firstname.equals("")) {

            return null;
        }

        if(name.equals("")) {

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
