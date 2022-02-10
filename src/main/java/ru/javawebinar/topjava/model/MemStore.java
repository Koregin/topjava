package ru.javawebinar.topjava.model;

import java.util.ArrayList;
import java.util.List;

public class MemStore implements Store {
    private final List<Meal> mealList = new ArrayList<>();
    private Integer ids = 1;

    public MemStore(List<Meal> initMeals) {
        for (Meal meal : initMeals) {
            create(meal);
        }
    }

    @Override
    public Meal create(Meal meal) {
        meal.setId(ids++);
        mealList.add(meal);
        return meal;
    }

    @Override
    public boolean update(Integer id, Meal meal) {
        int index = indexOf(id);
        boolean result = index != -1;
        if (result) {
            meal.setId(id);
            mealList.set(index, meal);
        }
        return result;
    }

    @Override
    public boolean delete(Integer id) {
        int index = indexOf(id);
        if (index != -1) {
            mealList.remove(index);
        }
        return index != -1;
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(mealList);
    }

    @Override
    public Meal findById(Integer id) {
        int index = indexOf(id);
        return index != -1 ? mealList.get(index) : null;
    }

    private int indexOf(int id) {
        int result = -1;
        for (int index = 0; index < mealList.size(); index++) {
            if (mealList.get(index).getId() == id) {
                result = index;
                break;
            }
        }
        return result;
    }
}
