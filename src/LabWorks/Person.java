package LabWorks;

import java.io.Serializable;

/**
 * Класс {@code Person} представляет автора лабораторной работы.
 *
 * <p>Содержит информацию о:
 * <ul>
 *     <li>имени</li>
 *     <li>росте</li>
 *     <li>паспортном идентификаторе</li>
 *     <li>цвете волос</li>
 *     <li>местоположении</li>
 * </ul>
 *
 * <p>Класс обеспечивает валидацию данных через сеттеры.</p>
 */

public class Person implements Serializable {
    /**
     * Имя автора.
     * Не может быть {@code null} или пустой строкой.
     */
    private String name;

    /**
     * Рост автора.
     * Должен быть больше 0.
     */
    private double height;

    /**
     * Паспортный идентификатор.
     * Может быть {@code null}, но если задан:
     * <ul>
     *     <li>не может быть пустым</li>
     *     <li>длина не менее 10 символов</li>
     * </ul>
     */
    private String passportID;

    /**
     * Цвет волос.
     * Может быть {@code null}.
     */
    private Color hairColor;

    /**
     * Местоположение автора.
     * Не может быть {@code null}.
     */
    private Location location;

    public String getName(){return name;}
    public double getHeight() {return height;}
    public String getPassportID() {return passportID;}
    public Color getHairColor() {return hairColor;}
    public Location getLocation() {return location;}

    public void setName(String name) {
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Неправильный формат имени");
        }else{
            this.name = name;
    }}
    public void setHeight(double height) {
        if(height <= 0) {
            throw new IllegalArgumentException("Неправильный формат высоты");
        }else{
            this.height = height;
    }}
    public void setPassportID(String passportID) {
        if (passportID == null) {
            this.passportID = null;
        } else if (passportID.trim().isEmpty() || passportID.length() < 10) {
            throw new IllegalArgumentException("Неправильный формат паспортного айди");
        } else {
            this.passportID = passportID;
        }}
    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }
    public void setLocation(Location location) {
        if(location == null) {
            throw new IllegalArgumentException("Неправильный формат локации");
        }else {
            this.location = location;
        }}

    public Person(String name, double height, String passportID, Color hairColor, Location location) {
        this.name = name;
        this.height = height;
        this.passportID = passportID;
        this.hairColor = hairColor;
        this.location = location;
    }
}
