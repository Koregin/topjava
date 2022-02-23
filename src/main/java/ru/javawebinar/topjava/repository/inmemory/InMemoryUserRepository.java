package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.users.forEach(this::save);
    }
    /*
    public static void main(String[] args) {
        InMemoryUserRepository userRep = new InMemoryUserRepository();
        userRep.save(new User(null, "Vasya Pupkin", "vasya@gmail.com", "passwd", Role.USER));
        userRep.save(new User(null, "Evgeny Amosov", "amos88@gmail.com", "passwd", Role.USER));
        userRep.save(new User(null, "Ilya Burdenko", "burd33@gmail.com", "passwd", Role.USER));
        userRep.save(new User(null, "Michail Lukshin", "mluksh78@gmail.com", "passwd", Role.USER));
        userRep.save(new User(null, "Michail Lukshin", "mluksh788@gmail.com", "passwd", Role.USER));
        userRep.save(new User(null, "Till Lindemann", "tillberling@bg.com", "passwd", Role.USER));

        System.out.println(userRep.get(5));
        System.out.println(userRep.get(6));
        System.out.println(userRep.getByEmail("burd33@gmail.com"));
        System.out.println(userRep.getByEmail("noemail@gmail.com"));
        System.out.println(userRep.delete(5));
        System.out.println("After delete Till");
        userRep.getAll().forEach(System.out::println);
        System.out.println(userRep.delete(5));
    }*/

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> usersList = new ArrayList<>(repository.values());
        usersList.sort(Comparator.comparing(AbstractNamedEntity::getName));
        System.out.println(usersList);
        return usersList;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> users = repository.values().stream()
                .filter(user -> email.equals(user.getEmail()))
                .collect(Collectors.toList());
        return !users.isEmpty() ? users.get(0) : null;
    }
}
