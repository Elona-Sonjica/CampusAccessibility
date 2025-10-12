package ac.za.postapp.Pages;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private int id;
    private String name;
    private String surname;
    private String studentNumber;
    private int age;
    private String gender;
    private String email;
    private String passwordHash;

    //  preferences
    private String deviceType;
    private boolean avoidStairs;
    private boolean preferRamps;
    private Integer minPathWidthCm;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {}

    public User(String name, String surname, String studentNumber, int age,
                String gender, String email) {
        this.name = name;
        this.surname = surname;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.deviceType = "None";
        this.avoidStairs = false;
        this.preferRamps = false;
        this.minPathWidthCm = 90;
        this.createdAt = LocalDateTime.now();
    }

    public User(String name, String surname, String studentNumber, int age,
                String gender, String email, String deviceType,
                boolean avoidStairs, boolean preferRamps, Integer minPathWidthCm) {
        this.name = name;
        this.surname = surname;
        this.studentNumber = studentNumber;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.deviceType = deviceType;
        this.avoidStairs = avoidStairs;
        this.preferRamps = preferRamps;
        this.minPathWidthCm = minPathWidthCm;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public boolean isAvoidStairs() { return avoidStairs; }
    public void setAvoidStairs(boolean avoidStairs) { this.avoidStairs = avoidStairs; }

    public boolean isPreferRamps() { return preferRamps; }
    public void setPreferRamps(boolean preferRamps) { this.preferRamps = preferRamps; }

    public Integer getMinPathWidthCm() { return minPathWidthCm; }
    public void setMinPathWidthCm(Integer minPathWidthCm) { this.minPathWidthCm = minPathWidthCm; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}