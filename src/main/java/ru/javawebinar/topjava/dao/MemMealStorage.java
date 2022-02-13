package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemMealStorage implements MealStorage {
    private final Map<Integer, Meal> mealsMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();

    public MemMealStorage() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        for (Meal meal : meals) {
            create(meal);
        }
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(counter.incrementAndGet());
        mealsMap.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        Meal mealResult = mealsMap.get(meal.getId());
        if (mealResult != null && mealsMap.replace(meal.getId(), mealResult, meal)) {
            mealResult = meal;
        }
        return  mealResult;
    }

    @Override
    public boolean delete(int id) {
        return mealsMap.remove(id) != null;
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealsMap.values());
    }

    @Override
    public Meal findById(int id) {
        return mealsMap.get(id);
    }
}
