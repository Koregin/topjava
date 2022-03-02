package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

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
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else {
            if (get(meal.getId(), userId) != null) {
                meal.setUserId(userId);
            } else {
                throw new NotFoundException("User id is not equal meal id");
            }
        }
        return Objects.equals(meal.getUserId(), userId) ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) : null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return get(id, userId) != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        Meal meal = repository.getOrDefault(id, null);
        return meal != null && Objects.equals(meal.getUserId(), userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return filterByPredicateForGet(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFilter(Integer userId, LocalDate startDate, LocalDate endDate) {
        Predicate<Meal> filter = meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate);
        return filterByPredicateForGet(userId, filter);
    }

    public List<Meal> filterByPredicateForGet(Integer userId, Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(meal -> Objects.equals(meal.getUserId(), userId))
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

