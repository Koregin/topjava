package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {
    Meal create(Meal meal);

    Meal update(Meal meal);

    boolean delete(int id);

    List<Meal> findAll();

    Meal findById(int id);
}
