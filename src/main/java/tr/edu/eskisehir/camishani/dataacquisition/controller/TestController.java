package tr.edu.eskisehir.camishani.dataacquisition.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.Recommendation;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.model.User;
import tr.edu.eskisehir.camishani.dataacquisition.jpa.repository.UserRepository;
import tr.edu.eskisehir.camishani.dataacquisition.service.CollaborativeService;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("test")
public class TestController {

    private final CollaborativeService collaborativeService;
    private final UserRepository userRepository;

    public TestController(CollaborativeService collaborativeService, UserRepository userRepository) {
        this.collaborativeService = collaborativeService;
        this.userRepository = userRepository;
    }

    @GetMapping("userBased")
    public String testUserBased(Integer neighbors) throws InterruptedException {
        Queue<User> users = new ArrayDeque<>(userRepository.getAllUsersWithRatings());

        AtomicReference<Double> accuracy = new AtomicReference<>(0.0);
        AtomicInteger calculatedUserCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        final Semaphore semaphore = new Semaphore(8);
        for (User user = users.poll(); user != null; user = users.poll()) {
            final User currentUser = user;
            semaphore.acquire();
            executor.execute(() -> {
                Recommendation recommendation = collaborativeService.getUserBasedRecommend(currentUser, neighbors);
                if (recommendation.getAccuracy() != 0)
                    accuracy.updateAndGet((current) -> {
                        calculatedUserCount.incrementAndGet();
                        return (current * (calculatedUserCount.get()) + recommendation.getAccuracy()) / calculatedUserCount.incrementAndGet();
                    });
                System.out.println(recommendation);
                semaphore.release();
            });
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.HOURS);

        return "for k=" + neighbors + " accuracy: " + accuracy;
    }

    @GetMapping("itemBased")
    public String testItemBased(Integer neighbors) {
        double accuracy = 0;
        int suggestionCount = 0;
        for (User user : userRepository.getAllUsersWithRatings()) {
            Recommendation recommendation = collaborativeService.getItemBasedRecommend(user, neighbors);
            accuracy += recommendation.getAccuracy();
            suggestionCount++;
            System.out.println(recommendation);
        }
        accuracy /= suggestionCount;

        return "for k=" + neighbors + " accuracy: " + accuracy;
    }
}
