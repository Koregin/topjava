package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.MemStore;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    public static final int KILOCALORIES = 2000;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String CREATE_OR_UPDATE = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";

    List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    MemStore memStore = new MemStore(meals);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = request.getParameter("action");
        log.debug("Action " + action);

        if (action.equalsIgnoreCase("delete")) {
            Integer mealId = Integer.parseInt(request.getParameter("mealId"));
            memStore.delete(mealId);
            forward = LIST_MEAL;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(memStore.findAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), KILOCALORIES);
            request.setAttribute("meals", mealsTo);
        } else if (action.equalsIgnoreCase("edit")) {
            Integer mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = memStore.findById(mealId);
            forward = CREATE_OR_UPDATE;
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEAL;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(memStore.findAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), KILOCALORIES);
            request.setAttribute("meals", mealsTo);
        } else {
            log.debug("Create or update");
            forward = CREATE_OR_UPDATE;
        }
        request.setAttribute("FORMATTER", FORMATTER);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("dateTime"), FORMATTER);
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(datetime, request.getParameter("description"), calories);
        String mealIdStr = request.getParameter("mealId");
        if (mealIdStr == null || mealIdStr.isEmpty()) {
            memStore.create(meal);
        } else {
            Integer mealId = Integer.parseInt(mealIdStr);
            memStore.update(mealId, meal);
        }
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(memStore.findAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), KILOCALORIES);
        request.setAttribute("meals", mealsTo);
        request.setAttribute("FORMATTER", FORMATTER);
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
