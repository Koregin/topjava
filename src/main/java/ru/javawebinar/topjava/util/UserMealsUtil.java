package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2019, Month.DECEMBER, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2019, Month.DECEMBER, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2019, Month.DECEMBER, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(10, 0), LocalTime.of(13, 0), 2000);
        mealsTo.forEach(System.out::println);
        filteredByStreams(meals, LocalTime.of(10, 0), LocalTime.of(13, 0), 2000).forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();
        Map<LocalDate, Integer> daysWithCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            daysWithCalories.computeIfPresent(meal.getDateTime().toLocalDate(), (date, cal) -> cal + meal.getCalories());
            daysWithCalories.putIfAbsent(meal.getDateTime().toLocalDate(), meal.getCalories());
        }
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                if (daysWithCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                    mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
                } else {
                    mealWithExcesses.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
                }
            }
        }
        return mealWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> daysWithCalories = meals.stream()
                .collect(Collectors.toMap(m -> m.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));
        return meals.stream().filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                             .map(meal -> daysWithCalories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay
                                        ? new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true)
                                        : new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false))
                             .collect(Collectors.toList());
    }
}
