package Managers;
import LabWorks.*;
import Commands.*;


import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.io.File;

/**
 * Класс {@code XmlReader} отвечает за десериализацию XML-файла
 * и восстановление объектов {@link LabWork}.
 *
 * <p>Построчно считывает XML-файл, определяет теги и значения,
 * и на их основе создаёт объекты {@link LabWork}, {@link Coordinates},
 * {@link Person} и {@link Location}.</p>
 *
 * <p>Использует флаги состояния для отслеживания текущего уровня вложенности:
 * <ul>
 *     <li>{@code inCoordinates} — внутри блока coordinates</li>
 *     <li>{@code inPerson} — внутри блока author</li>
 *     <li>{@code inLocation} — внутри блока location</li>
 * </ul>
 *
 * <p>При завершении тега {@code </LabWork>} объект добавляется в коллекцию.</p>
 */

public class XmlReader {
    /**
     * Флаг: находится ли парсер внутри блока coordinates.
     */
    private boolean inCoordinates;

    /**
     * Флаг: находится ли парсер внутри блока author.
     */
    private boolean inPerson;

    /**
     * Флаг: находится ли парсер внутри блока location.
     */
    private boolean inLocation;

    /**
     * Текущий создаваемый объект LabWork.
     */
    private LabWork lw = null;

    /**
     * Генератор идентификаторов (инкрементируется при создании нового LabWork).
     */
    private Long id = Long.valueOf(0);

    /**
     * Преобразует XML-экранированные символы обратно в обычные.
     *
     * <p>Заменяет:
     * <ul>
     *     <li>{@code &lt;} → {@code <}</li>
     *     <li>{@code &gt;} → {@code >}</li>
     *     <li>{@code &quot;} → {@code "}</li>
     *     <li>{@code &apos;} → {@code '}</li>
     *     <li>{@code &amp;} → {@code &}</li>
     * </ul>
     *
     * @param s строка с XML-экранированием
     * @return строка с восстановленными символами или {@code null}, если вход был {@code null}
     */
    private String unescapeXml(String s) {
        if (s == null) return null;
        return s.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&");
    }


    /**
     * Считывает XML-файл и заполняет коллекцию {@link LabWork}.
     *
     * <p>Алгоритм работы:
     * <ol>
     *     <li>Построчно считывает файл</li>
     *     <li>Извлекает тег и значение</li>
     *     <li>В зависимости от текущего контекста (флагов) заполняет поля объектов</li>
     *     <li>При закрытии {@code </LabWork>} добавляет объект в коллекцию</li>
     * </ol>
     *
     * <p>Особенности:
     * <ul>
     *     <li>Создаёт объект {@link LabWork} с дефолтными значениями и затем заполняет его</li>
     *     <li>Использует {@link BigDecimal} для безопасного чтения чисел</li>
     *     <li>Поддерживает {@code null}-значения (например, для passportID)</li>
     *     <li>Игнорирует строки без XML-тегов</li>
     *     <li>Все ошибки парсинга внутри строки подавляются (try-catch)</li>
     * </ul>
     *
     * <p>Ошибки:
     * <ul>
     *     <li>{@link IllegalArgumentException} — если файл не найден</li>
     * </ul>
     *
     * @param fileName имя XML-файла
     * @param cm менеджер коллекции, в который добавляются объекты
     * @throws IllegalArgumentException если файл не найден
     */
    public void read(String fileName, CollectionManager cm) {
        File file = new File(fileName);
        Scanner sc;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Файл не найден\n");
        }

        while (sc.hasNextLine()) {

            String line = sc.nextLine().trim();
            if (!line.contains("<")) continue;

            String value = unescapeXml(line.replaceAll("<.*?>", "").trim());
            String tag = line.substring(line.indexOf("<") + 1, line.indexOf(">"));

            if (tag.equals("LabWork")) {
                id += 1;
                ZonedDateTime creationDate = ZonedDateTime.now();
                lw = new LabWork(id, "default", new Coordinates(1, 1f), creationDate, 1L, null, null);
            }

            if (lw == null) continue;

            try {

                if (tag.equals("name") && !inLocation && !inPerson) {
                    if (!value.isEmpty()) lw.setName(value);
                }

                if (tag.equals("minimalPoint")) {
                    if (value.isEmpty()) {
                        lw.setMinimalPoint(null);
                    } else {
                        BigDecimal bd = new BigDecimal(value);
                        lw.setMinimalPoint(bd.longValue());
                    }
                }

                if (tag.equals("difficulty") && !value.isEmpty()) {
                    lw.setDifficulty(Difficulty.valueOf(value));
                }

                if (tag.equals("coordinates")) {
                    inCoordinates = true;
                    lw.setCoordinates(new Coordinates(1, 1f));
                }

                if (tag.equals("/coordinates")) inCoordinates = false;

                if (tag.equals("x") && inCoordinates && !value.isEmpty()) {
                    lw.getCoordinates().setX(Long.valueOf(value));
                }

                if (tag.equals("y") && inCoordinates && !value.isEmpty()) {
                    lw.getCoordinates().setY(Float.valueOf(value));
                }

                if (tag.equals("author")) {
                    inPerson = true;
                    lw.setAuthor(new Person("Unknown", 1.0, null, null, new Location(0, 0f, "Unknown")));
                }

                if (tag.equals("/author")) inPerson = false;

                if (tag.equals("name") && inPerson && !inLocation && !value.isEmpty()) {
                    lw.getAuthor().setName(value);
                }

                if (tag.equals("hairColor") && !value.isEmpty()) {
                    lw.getAuthor().setHairColor(Color.valueOf(value));
                }

                if (tag.equals("height") && !value.isEmpty()) {
                    lw.getAuthor().setHeight(Double.valueOf(value));
                }

                if (tag.equals("passportID")) {
                    lw.getAuthor().setPassportID(value.isEmpty() ? null : value);
                }

                if (tag.equals("location")) {
                    inLocation = true;
                    lw.getAuthor().setLocation(new Location(0, 0f, "Unknown"));
                }

                if (tag.equals("/location")) inLocation = false;

                if (tag.equals("x") && inLocation && !value.isEmpty()) {
                    lw.getAuthor().getLocation().setX(Integer.valueOf(value));
                }

                if (tag.equals("y") && inLocation && !value.isEmpty()) {
                    lw.getAuthor().getLocation().setY(Float.valueOf(value));
                }

                if (tag.equals("name") && inLocation && !value.isEmpty()) {
                    lw.getAuthor().getLocation().setName(value);
                }

                if (tag.equals("/LabWork")) {
                    cm.add(lw);
                    cm.updateNextId(lw.getId());
                    lw = null;
                    inPerson = false;
                    inLocation = false;
                    inCoordinates = false;
                }

            } catch (Exception e) {
            }
        }

        sc.close();
    }
}
