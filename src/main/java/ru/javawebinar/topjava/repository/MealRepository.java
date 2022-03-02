package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository {

    Meal save(Meal meal, Integer userId);

    boolean delete(int id, Integer userId);

    Meal get(int id, Integer userId);

    List<Meal> getAll(Integer userId);

    List<Meal> getAllFilter(Integer userId, LocalDate startDate, LocalDate endDate);
}
