package LabWorks;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс {@code LabWork} представляет элемент коллекции.
 *
 * <p>Содержит информацию о лабораторной работе:
 * <ul>
 *     <li>уникальный идентификатор</li>
 *     <li>название</li>
 *     <li>координаты</li>
 *     <li>дата создания</li>
 *     <li>минимальный балл</li>
 *     <li>сложность</li>
 *     <li>автор</li>
 * </ul>
 *
 * <p>Класс реализует интерфейс {@link Comparable} и поддерживает
 * сортировку по полю {@code minimalPoint}.</p>
 */
public class LabWork implements Comparable<LabWork>, Serializable {

    /**
     * Уникальный идентификатор.
     * Не может быть {@code null}, должен быть больше 0.
     */
    private Long id;

    /**
     * Название работы.
     * Не может быть {@code null} или пустым.
     */
    private String name;

    /**
     * Координаты работы.
     * Не могут быть {@code null}.
     */
    private Coordinates coordinates;

    /**
     * Дата создания.
     * Генерируется автоматически и не изменяется.
     */
    private final ZonedDateTime creationDate;

    /**
     * Минимальный балл.
     * Может быть {@code null}, но если задан — должен быть больше 0.
     */
    private Long minimalPoint;

    /**
     * Сложность работы.
     * Может быть {@code null}.
     */
    private Difficulty difficulty;

    /**
     * Автор работы.
     * Может быть {@code null}.
     */
    private Person author;

    public Long getId(){return id;}
    public String getName(){return name;}
    public Coordinates getCoordinates(){return coordinates;}
    public java.time.ZonedDateTime getCreationDate(){return creationDate;}
    public Long getMinimalPoint(){return minimalPoint;}
    public Difficulty getDifficulty(){return difficulty;}
    public Person getAuthor(){return author;}

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("Неправильный формат имени");
        }else{
            this.name = name;
    }}
    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null){
            throw new IllegalArgumentException("Неправильный формат координат");
        }else{
            this.coordinates = coordinates;
    }}
    public void setMinimalPoint(Long minimalPoint) {
        if (minimalPoint != null && minimalPoint <= 0){
            throw new IllegalArgumentException("Неправильный формат ввода минимального балла");
        }else{
            this.minimalPoint = minimalPoint;
    }}
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public void setAuthor(Person author) {
        this.author = author;
    }


    /**
     * Сравнивает текущий объект с другим {@link LabWork}.
     *
     * <p>Сравнение происходит по:
     * <ol>
     *     <li>{@code minimalPoint}</li>
     *     <li>при равенстве — по {@code id}</li>
     * </ol>
     *
     * <p>Правила:
     * <ul>
     *     <li>{@code null} считается меньше любого значения</li>
     *     <li>если оба {@code minimalPoint == null} → сравнение по id</li>
     * </ul>
     *
     * @param lw объект для сравнения
     * @return отрицательное, ноль или положительное число
     */
    public int compareTo(LabWork lw) {
        if (this.minimalPoint == null && lw.getMinimalPoint() == null)
            return this.id.compareTo(lw.getId());
        if (this.minimalPoint == null) return -1;
        if (lw.getMinimalPoint() == null) return 1;
        int res = this.minimalPoint.compareTo(lw.getMinimalPoint());
        if (res == 0) {
            return this.id.compareTo(lw.getId());
        }
        return res;
    }

    public LabWork(Long id, String name, Coordinates coordinates, ZonedDateTime ZonedDateTime, Long minimalPoint, Difficulty difficulty, Person author) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = java.time.ZonedDateTime.now();
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }

    /**
     * Возвращает строковое представление объекта.
     *
     * @return строка с полями объекта
     */

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return "LabWork {" +
                "id = " + id +
                ", name = " + name +
                ", coordinates = (" + coordinates.getX() + ", " + coordinates.getY() + ")" +
                ", creationDate = " + creationDate.format(formatter) +
                ", minimalPoint = " + minimalPoint +
                ", difficulty = " + difficulty +
                ", author = " + (author == null ? "null" :
                ", name = " + author.getName() +
                        ", height = " + author.getHeight() +
                        ", passportID = " + author.getPassportID() +
                        ", hairColor = " + author.getHairColor() +
                        ", location = (" +
                        author.getLocation().getX() + ", " +
                        author.getLocation().getY() + ", " +
                        author.getLocation().getName() + ")"
        ) + "}";
    }
}
