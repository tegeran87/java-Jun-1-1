package ru.geekbrains.seminar_1.task_2;

/**
 * Чипсы
 */
public class Crisps implements Snack{
    @Override
    public boolean getProteins() {
        return false;
    }

    @Override
    public boolean getFats() {
        return true;
    }

    @Override
    public boolean getCarbohydrates() {
        return false;
    }

    @Override
    public String getName() {
        return "Чипсы";
    }
}