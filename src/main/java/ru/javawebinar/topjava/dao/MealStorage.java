package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    Meal create(Meal meal);

    Meal update(Meal meal);

    Meal delete(Integer id);

    List<Meal> findAll();

    Meal findById(Integer id);
}
