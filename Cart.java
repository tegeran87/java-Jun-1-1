package ru.geekbrains.seminar_1.task_2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Корзина
 *
 * @param <T> Еда
 */
public class Cart<T extends Food> {

    /**
     * Товары в магазине
     */
    private final ArrayList<T> foodstuffs;
    private final UMarket market;
    private final Class<T> clazz;

    public Cart(Class<T> clazz, UMarket market) {
        this.clazz = clazz;
        this.market = market;
        foodstuffs = new ArrayList<>();
    }

    public Collection<T> getFoodstuffs() {
        return foodstuffs;
    }

    /**
     * Распечатать список продуктов в корзине
     */
    public void printFoodstuffs() {
        AtomicInteger index = new AtomicInteger(1);
        foodstuffs.forEach(food -> {
            System.out.printf("[%d] %s (Белки: %s Жиры: %s Углеводы: %s)\n",
                    index.getAndIncrement(), food.getName(),
                    food.getProteins() ? "Да" : "Нет",
                    food.getFats() ? "Да" : "Нет",
                    food.getCarbohydrates() ? "Да" : "Нет");
        });
    }

    /**
     * Балансировка корзины
     * Метод cardBalancing создает объект AtomicInteger с начальным значением 0 и вызывает метод addFoodForBalance
     * с тремя разными функциями и одним и тем же объектом check.
     * Метод addFoodForBalance принимает функцию и объект check в качестве аргументов.
     * Он вызывает переданную функцию для каждого элемента списка, увеличивая значение объекта check каждый раз,
     * когда функция возвращает true.
     * После вызова метода addFoodForBalance, метод cardBalancing устанавливает строку баланса,
     * которая будет использоваться для вывода результата. Затем он проверяет,
     * все ли три функции вернули true, и если это так, выводит сообщение о том,
     * что корзина сбалансирована по белкам, жирам и углеводам.
     * В противном случае выводится сообщение о том, что невозможно сбалансировать корзину по этим параметрам.
     */
    public void cardBalancing() {
        AtomicInteger check = new AtomicInteger(0);
        boolean proteins = addFoodForBalance(Food::getProteins, check);
        boolean fats = addFoodForBalance(Food::getFats, check);
        boolean carbohydrates = addFoodForBalance(Food::getCarbohydrates, check);
        String balance = " ";
        if (check.get() == 3)
            balance = " уже ";
        if (proteins && fats && carbohydrates) {
            System.out.printf("\nКорзина%s сбалансирована по БЖУ\n. ", balance);
        } else
            System.out.println("\nНевозможно сбалансировать корзину по БЖУ\n.");
    }

    /**
     * Метод addFoodForBalance принимает предикат и объект check, который отслеживает количество успешных вызовов предиката.
     * Сначала метод проверяет, есть ли в списке foodstuffs элемент, удовлетворяющий предикату.
     * Если такой элемент есть, метод увеличивает значение объекта check и возвращает true.
     * Если такого элемента нет, метод добавляет новый элемент, который удовлетворяет предикату, в список foodstuffs.
     * Для этого он создает поток из объектов market.getThings(Food.class), фильтрует его с помощью предиката,
     * находит любой подходящий элемент и добавляет его в список. После этого метод возвращает true.
     */
    private boolean addFoodForBalance(Predicate<Food> food, AtomicInteger check) {
        if (foodstuffs.stream().noneMatch(food)) {
            return foodstuffs.add((T) market.getThings(Food.class).stream()
                    .filter(food)
                    .findAny()
                    .get());
        } else {
            check.incrementAndGet();
            return true;
        }
    }

//    /**
//     * Балансировка корзины
//     */
//    public void cardBalancing()
//    {
//        boolean proteins = checkNutrientFlag(Food::getProteins);
//        boolean fats = checkNutrientFlag(Food::getFats);
//        boolean carbohydrates = checkNutrientFlag(Food::getCarbohydrates);
//
//        if (proteins && fats && carbohydrates) {
//            System.out.println("Корзина уже сбалансирована по БЖУ.");
//            return;
//        }
//
//        Collection<T> marketFoods = market.getThings(clazz);
//        proteins = checkNutrientFlag(proteins, Food::getProteins, marketFoods);
//        fats = checkNutrientFlag(fats, Food::getFats, marketFoods);
//        carbohydrates = checkNutrientFlag(carbohydrates, Food::getCarbohydrates, marketFoods);
//
//        if (proteins && fats && carbohydrates) {
//            System.out.println("Корзина сбалансирована по БЖУ.");
//        } else {
//            System.out.println("Невозможно сбалансировать корзину по БЖУ.");
//        }
//
//    }
//
//    /**
//     * Проверка наличия конкретного питательного элемента в корзине
//     * @param nutrientCheck список продуктов в корзине
//     * @return состояние обновленного флага питательного элемента
//     */
//    private boolean checkNutrientFlag(Predicate<Food> nutrientCheck) {
//        Optional<T> optionalFood = foodstuffs.stream()
//                .filter(nutrientCheck)
//                .findFirst();
//        return optionalFood.isPresent();
//    }
//
//    /**
//     * Поиск недостающих питательных элементов в корзине и добавление питательно элемента
//     * исходя из общего фильтра продуктов
//     * @param nutrientFlag наличие питательного элемента
//     * @param nutrientCheck список продуктов в корзине
//     * @param foods доступный список продуктов (исходя из текущего фильтра)
//     * @return состояние обновленного флага питательного элемента (скорее всего будет true,
//     * false - в случае, если невозможно найти продукт с нужным питательным элементом, в таком
//     * случае, невозможно сбалансировать корзину по БЖУ
//     */
//    private boolean checkNutrientFlag(boolean nutrientFlag, Predicate<Food> nutrientCheck, Collection<T> foods) {
//        if (!nutrientFlag) {
//            Optional<T> optionalFood = foods.stream()
//                    .filter(nutrientCheck)
//                    .findFirst();
//            optionalFood.ifPresent(foodstuffs::add);
//            return optionalFood.isPresent();
//        }
//        return true;
//    }

}
