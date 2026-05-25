package dk.sdu.cbse.scoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ScoringService {

    private long totalScore = 0L;

    public static void main(String[] args) {
        SpringApplication.run(ScoringService.class, args);
    }

    @GetMapping("/score")
    public Long addScore(@RequestParam(value = "point") Long point) {
        totalScore += point;
        return totalScore;
    }
    
    @GetMapping("/reset")
    public Long reset() {
        totalScore = 0L;
        return totalScore;
    }
}