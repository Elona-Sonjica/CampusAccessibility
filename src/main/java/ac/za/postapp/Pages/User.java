package ac.za.postapp.Pages;

public class User {
    private String name;
    private String surname;
    private String studentNumber;
    private int age;
    private String gender;
    private String email;

    // Constructor
    public User(String name, String surname, String studentNumber, int age, String gender, String email) {
        this.name = name;
        this.surname = surname;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.email = email;
    }

    // ===== Getters and Setters =====
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
