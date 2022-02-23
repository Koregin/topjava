package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static LocalDate dateFrom;
    private static LocalDate dateTo;
    private static LocalTime timeFrom;
    private static LocalTime timeTo;

    ConfigurableApplicationContext appCtx;
    MealRestController mealRestController;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
        dateFrom = null;
        dateTo = null;
        timeFrom = null;
        timeTo = null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if ("edit".equals(request.getParameter("purpose"))) {
            String id = request.getParameter("id");

            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    SecurityUtil.authUserId());

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                mealRestController.create(meal);
            } else {
                mealRestController.update(meal, getId(request));
            }
        } else if ("filter".equals(request.getParameter("purpose"))) {
            log.debug("Change datetime for filter");
            getDateTime(request);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action)
                        ? new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId())
                        : mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                log.debug("DateFrom: {}", dateFrom);
                log.debug("DateTo: {}", dateTo);
                log.debug("TimeFrom: {}", timeFrom);
                log.debug("TimeTo: {}", timeTo);
                if (dateFrom != null
                        || dateTo != null
                        || timeFrom != null
                        || timeTo != null) {
                    request.setAttribute("dateFrom", dateFrom);
                    request.setAttribute("dateTo", dateTo);
                    request.setAttribute("timeFrom", timeFrom);
                    request.setAttribute("timeTo", timeTo);
                    request.setAttribute("meals", mealRestController.getAllFilter(
                            LocalDateTime.of(dateFrom != null ? dateFrom : LocalDate.MIN, timeFrom != null ? timeFrom : LocalTime.MIN),
                            LocalDateTime.of(dateTo != null ? dateTo : LocalDate.MAX, timeTo != null ? timeTo : LocalTime.MAX)));
                } else {
                    request.setAttribute("meals", mealRestController.getAll());
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
    /*
    Заполнение переменных даты и времени из формы фильтра
    Если дата или время в форме не заполнены то переменная обнуляется
     */
    private void getDateTime(HttpServletRequest request) {
        String dateFromStr = request.getParameter("dateFrom");
        String dateToStr = request.getParameter("dateTo");
        String timeFromStr = request.getParameter("timeFrom");
        String timeToStr = request.getParameter("timeTo");
        log.debug("DateFrom FORM {}", dateFromStr);
        log.debug("DateTo FORM {}", dateToStr);
        log.debug("TimeFrom FORM {}", timeFromStr);
        log.debug("TimeTo FORM {}", timeToStr);
        if (!timeFromStr.isEmpty()) {
            timeFrom = LocalTime.parse(timeFromStr);
        } else {
            timeFrom = null;
        }
        if (!timeToStr.isEmpty()) {
            timeTo = LocalTime.parse(timeToStr);
        } else {
            timeTo = null;
        }
        if (!dateFromStr.isEmpty()) {
            dateFrom = LocalDate.parse(dateFromStr);
        } else {
            dateFrom = null;
        }
        if (!dateToStr.isEmpty()) {
            dateTo = LocalDate.parse(dateToStr);
        } else {
            dateTo = null;
        }
    }
}
