package LabWorks;

import java.io.Serializable;

/**
 * Класс {@code Location} представляет местоположение автора.
 *
 * <p>Содержит координаты и необязательное название места.</p>
 *
 * <p>Ограничения:
 * <ul>
 *     <li>{@code x} — не может быть {@code null}</li>
 *     <li>{@code y} — не может быть {@code null}</li>
 *     <li>{@code name} — может быть {@code null}, но если задано, не может быть пустым</li>
 * </ul>
 */
public class Location implements Serializable {

    /**
     * Координата X.
     * Не может быть {@code null}.
     */
    private Integer x;

    /**
     * Координата Y.
     * Не может быть {@code null}.
     */
    private Float y;

    /**
     * Название места.
     * Может быть {@code null}, но не может быть пустой строкой.
     */
    private String name;

    public Integer getX() {return x;}
    public Float getY() {return y;}
    public String getName() {return name;}

    public void setX(Integer x) {
        if (x == null){
            throw new IllegalArgumentException("Неправильный формат координаты x");
        }else{
            this.x = x;
    }}
    public void setY(Float y) {
        if(y == null){
            throw new IllegalArgumentException("Неправильный формат координаты y");
        }else{
            this.y = y;
    }}
    public void setName(String name) {
        if (name != null && name.trim().isEmpty()){
            throw new IllegalArgumentException("Неправильный формат имени");
        }else{
            this.name = name;
    }}

    public Location(Integer x, Float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
}
