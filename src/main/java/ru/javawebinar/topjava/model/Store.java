package ru.javawebinar.topjava.model;

import java.util.List;

public interface Store {
    Meal create(Meal meal);

    boolean update(Integer id, Meal meal);

    boolean delete(Integer id);

    List<Meal> findAll();

    Meal findById(Integer id);
}
