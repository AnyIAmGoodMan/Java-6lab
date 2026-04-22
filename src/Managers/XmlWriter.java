package Managers;
import LabWorks.*;
import Commands.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * Класс {@code XmlWriter} отвечает за сериализацию коллекции {@link LabWork}
 * в XML-файл.
 *
 * <p>Использует данные из {@link CollectionManager}, формирует XML-структуру
 * и записывает её в файл, указанный в менеджере коллекции.</p>
 *
 * <p>Поддерживает обработку {@code null}-полей и корректное экранирование
 * специальных XML-символов.</p>
 */

public class XmlWriter {
    CollectionManager manager;

    public XmlWriter(CollectionManager manager) {
        this.manager = manager;
    }


    /**
     * Экранирует специальные XML-символы в строке.
     *
     * <p>Заменяет символы:
     * <ul>
     *     <li>{@code &} → {@code &amp;}</li>
     *     <li>{@code <} → {@code &lt;}</li>
     *     <li>{@code >} → {@code &gt;}</li>
     *     <li>{@code "} → {@code &quot;}</li>
     *     <li>{@code '} → {@code &apos;}</li>
     * </ul>
     *
     * <p>Если строка {@code null}, возвращается пустая строка.</p>
     *
     * @param s исходная строка
     * @return строка, безопасная для вставки в XML
     */
    private String escapeXml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }


    /**
     * Записывает коллекцию {@link LabWork} в XML-файл.
     *
     * <p>Файл берётся из {@link CollectionManager#getFileName()}.</p>
     *
     * <p>Формирует XML следующей структуры:
     * <pre>
     * {@code
     * <LabWorks>
     *     <LabWork>
     *         <name>...</name>
     *         <minimalPoint>...</minimalPoint>
     *         <difficulty>...</difficulty>
     *         <coordinates>
     *             <x>...</x>
     *             <y>...</y>
     *         </coordinates>
     *         <author>
     *             <name>...</name>
     *             <height>...</height>
     *             <passportID>...</passportID>
     *             <hairColor>...</hairColor>
     *             <location>
     *                 <x>...</x>
     *                 <y>...</y>
     *                 <name>...</name>
     *             </location>
     *         </author>
     *     </LabWork>
     * </LabWorks>
     * }
     * </pre>
     *
     * <p>Особенности:
     * <ul>
     *     <li>Поля со значением {@code null} не записываются (кроме строк — там пустая строка)</li>
     *     <li>Строковые значения экранируются методом {@link #escapeXml(String)}</li>
     *     <li>Проверяется существование файла и права на запись</li>
     * </ul>
     *
     * <p>Ошибки:
     * <ul>
     *     <li>{@link IOException} — ошибка записи в файл</li>
     *     <li>{@link IllegalArgumentException} — файл не существует или нет прав на запись</li>
     * </ul>
     */
    public void write() throws IOException {
        String fileName = manager.getFileName();
        File file = new File(fileName);

        if (!file.exists()) {
            throw new IOException("Файл не существует");
        }

        if (!file.canWrite()) {
            throw new IOException("Нет прав на запись");
        }

        try (FileWriter writer = new FileWriter(fileName)) {

            writer.write("<LabWorks>\n");

            for (LabWork labWork : manager.getCollection()) {

                writer.write("<LabWork>\n");

                writer.write("<name>" + escapeXml(labWork.getName()) + "</name>\n");

                if (labWork.getMinimalPoint() != null) {
                    writer.write("<minimalPoint>" + labWork.getMinimalPoint() + "</minimalPoint>\n");
                }

                if (labWork.getDifficulty() != null) {
                    writer.write("<difficulty>" + labWork.getDifficulty() + "</difficulty>\n");
                }

                writer.write("<coordinates>\n");
                writer.write("<x>" + labWork.getCoordinates().getX() + "</x>\n");
                writer.write("<y>" + labWork.getCoordinates().getY() + "</y>\n");
                writer.write("</coordinates>\n");

                if (labWork.getAuthor() != null) {

                    writer.write("<author>\n");

                    writer.write("<name>" + escapeXml(labWork.getAuthor().getName()) + "</name>\n");
                    writer.write("<height>" + labWork.getAuthor().getHeight() + "</height>\n");

                    if (labWork.getAuthor().getPassportID() != null) {
                        writer.write("<passportID>" + escapeXml(labWork.getAuthor().getPassportID()) + "</passportID>\n");
                    }

                    if (labWork.getAuthor().getHairColor() != null) {
                        writer.write("<hairColor>" + labWork.getAuthor().getHairColor() + "</hairColor>\n");
                    }

                    writer.write("<location>\n");
                    writer.write("<x>" + labWork.getAuthor().getLocation().getX() + "</x>\n");
                    writer.write("<y>" + labWork.getAuthor().getLocation().getY() + "</y>\n");

                    if (labWork.getAuthor().getLocation().getName() != null) {
                        writer.write("<name>" + escapeXml(labWork.getAuthor().getLocation().getName()) + "</name>\n");
                    }

                    writer.write("</location>\n");

                    writer.write("</author>\n");
                }

                writer.write("</LabWork>\n");
            }

            writer.write("</LabWorks>\n");
        }
    }
}
