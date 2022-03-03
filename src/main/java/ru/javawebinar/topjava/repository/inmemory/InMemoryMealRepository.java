package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Meal resultMeal = null;
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            resultMeal = meal;
        } else {
            if (get(meal.getId(), userId) != null) {
                meal.setUserId(userId);
                resultMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
        }
        return resultMeal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return meal != null && Objects.equals(meal.getUserId(), userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicateForGet(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilter(int userId, LocalDate startDate, LocalDate endDate) {
        Predicate<Meal> filter = meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate);
        return filterByPredicateForGet(userId, filter);
    }

    private List<Meal> filterByPredicateForGet(int userId, Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

