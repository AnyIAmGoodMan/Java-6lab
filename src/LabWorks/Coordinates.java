package LabWorks;

import java.io.Serializable;

/**
 * Класс {@code Coordinates} представляет координаты объекта {@link LabWork}.
 *
 * <p>Содержит две координаты:
 * <ul>
 *     <li>{@code x} — целое число</li>
 *     <li>{@code y} — число с плавающей точкой (не более 352)</li>
 * </ul>
 *
 * <p>Ограничения:
 * <ul>
 *     <li>{@code y} не может быть {@code null}</li>
 *     <li>{@code y} должно быть ≤ 352</li>
 * </ul>
 */
public class Coordinates implements Serializable {

    /**
     * Координата X.
     */
    private long x;

    /**
     * Координата Y.
     * Не может быть {@code null}, максимальное значение — 352.
     */
    private Float y;

    public long getX() {return x;}
    public Float getY() {return y;}

    public void setX(long x) {
        this.x = x;
    }
    public void setY(Float y) {
        if (y == null || y > 352){
            throw new IllegalArgumentException("Неверный формат координаты y\n");
        }else{
            this.y = y;
        }}
    public Coordinates(long x, Float y) {
        this.x = x;
        this.y = y;
    }
}
