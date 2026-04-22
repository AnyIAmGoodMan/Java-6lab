package Managers;
import LabWorks.*;

import java.util.ArrayDeque;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Класс {@code CollectionManager} управляет коллекцией объектов {@link LabWork}.
 *
 * <p>Предоставляет методы для:
 * <ul>
 *     <li>добавления и удаления элементов</li>
 *     <li>фильтрации и подсчёта</li>
 *     <li>получения информации о коллекции</li>
 *     <li>генерации уникальных идентификаторов</li>
 * </ul>
 *
 * <p>Коллекция хранится в виде {@link ArrayDeque}.</p>
 */

public class CollectionManager {

    /**
     * Проверка, изменена ли коллекция.
     */
    private boolean isModified = false;
    /**
     * Имя файла, связанного с коллекцией (для сохранения/загрузки).
     */
    private String fileName;

    /**
     * Дата создания коллекции.
     */
    public final ZonedDateTime creationDate = ZonedDateTime.now();

    /**
     * Основная коллекция объектов LabWork.
     */
    private ArrayDeque<LabWork> collection = new ArrayDeque<>();

    /**
     * Следующий доступный идентификатор.
     */
    private long nextId = 1;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * Генерирует новый уникальный идентификатор.
     *
     * @return уникальный id
     */
    public long generateId() {
        return nextId++;
    }


    public void setModified(boolean modified) {
        this.isModified = modified;
    }

    public boolean getModified() {
        return isModified;
    }
    /**
     * Обновляет значение следующего id (используется при загрузке из файла).
     *
     * @param id id существующего элемента
     */
    public void updateNextId(long id) {
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public ZonedDateTime getTime(){
        return creationDate;
    }
    public void add(LabWork labWork) {
        collection.add(labWork);
    }

    /**
     * Удаляет элемент по его id.
     *
     * <p>Проходит по всей коллекции и формирует новую без удаляемого элемента.</p>
     *
     * @param id идентификатор элемента
     * @return сообщение о результате операции
     */
    public String removeById(long id) {
        ArrayDeque<LabWork> filler = new ArrayDeque<>();
        int n = collection.size();
        for (int i = 0; i < n; i++) {
            LabWork l = collection.getFirst();
            if (l.getId() != id) {
                filler.add(l);
            }
            collection.removeFirst();
        }
        collection = filler;
        if (collection.size() == n) {
            return "Элемента с таким айди нет\n";
        }else{
            return "Элемент с таким айди удален\n";
        }
    }

    /**
     * Очищает коллекцию.
     */
    public void clear() {
        collection.clear();
    }

    /**
     * Возвращает размер коллекции.
     *
     * @return количество элементов
     */
    public int size() {
        return collection.size();
    }

    /**
     * Возвращает текущую коллекцию.
     *
     * @return коллекция LabWork
     */
    public ArrayDeque<LabWork> getCollection() {
        return collection;
    }

    /**
     * Удаляет и возвращает первый элемент коллекции.
     *
     * @return первый элемент или {@code null}, если коллекция пуста
     */
    public LabWork removeHead() {
        return collection.poll();
    }

    /**
     * Подсчитывает количество элементов с заданной сложностью.
     *
     * @param difficulty уровень сложности
     * @return количество элементов
     */
    public int countByDifficulty(Difficulty difficulty){
        return (int) collection.stream()
                .filter(lw -> lw.getDifficulty() == difficulty)
                .count();
    }

    /**
     * Фильтрует элементы по имени.
     *
     * <p>Возвращает все элементы, имя которых содержит заданную подстроку.</p>
     *
     * @param arg подстрока для поиска
     * @return новая коллекция отфильтрованных элементов
     */
    public ArrayDeque<LabWork> filterInName(String arg){
        return collection.stream()
                .filter(lw -> lw.getName() != null && lw.getName().contains(arg))
                .sorted(java.util.Comparator.comparing(LabWork::getName))
                .collect(java.util.stream.Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * Возвращает список значений сложности в порядке убывания.
     *
     * <p>Игнорирует элементы с {@code null}-значением сложности.</p>
     *
     * @return отсортированный список сложностей
     */
    public ArrayList<Difficulty> PFDD() {
        return collection.stream()
                .map(LabWork::getDifficulty)
                .filter(java.util.Objects::nonNull)
                .sorted(java.util.Comparator.reverseOrder())
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }

}
