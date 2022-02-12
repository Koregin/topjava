package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealStorage;
import ru.javawebinar.topjava.dao.MemMealStorage;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    public static final int KILOCALORIES = 2000;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String CREATE_OR_UPDATE = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MealStorage mealStorage = new MemMealStorage();

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = request.getParameter("action");
        action = action == null ? "listMeals" : action;
        log.debug("Action " + action);

        switch (action) {
            case "delete":
                mealStorage.delete(getMealId(request));
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealStorage.findAll(), LocalTime.MIN, LocalTime.MAX, KILOCALORIES));
                response.sendRedirect("meals");
                return;
            case "edit":
                forward = CREATE_OR_UPDATE;
                request.setAttribute("meal", mealStorage.findById(getMealId(request)));
                request.setAttribute("actionTitle", "Edit meal");
                break;
            case "create":
                log.debug("Create or update");
                forward = CREATE_OR_UPDATE;
                request.setAttribute("actionTitle", "Add meal");
                request.setAttribute("dateTimeNow", LocalDateTime.now().withSecond(0).withNano(0));
                break;
            default:
                forward = LIST_MEAL;
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealStorage.findAll(), LocalTime.MIN, LocalTime.MAX, KILOCALORIES));
                break;
        }
        request.setAttribute("FORMATTER", FORMATTER);
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime datetime = LocalDateTime.parse(request.getParameter("dateTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal(datetime, request.getParameter("description"), calories);
        String mealIdStr = request.getParameter("mealId");

        if (mealIdStr == null || mealIdStr.isEmpty()) {
            log.debug("Creating meal");
            mealStorage.create(meal);
        } else {
            log.debug("Updating meal");
            Integer mealId = Integer.parseInt(mealIdStr);
            meal.setId(mealId);
            log.debug("Meal update ID=" + meal.getId().toString());
            mealStorage.update(meal);
        }
        request.setAttribute("meals", MealsUtil.filteredByStreams(mealStorage.findAll(), LocalTime.MIN, LocalTime.MAX, KILOCALORIES));
        response.sendRedirect("meals");
    }

    public Integer getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("mealId"));
    }
}
