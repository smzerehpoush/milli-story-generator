package ir.sarmayehzarin.scheduler;

import ir.sarmayehzarin.service.StoryGeneratorService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StoryGeneratorScheduler {
  private final StoryGeneratorService storyGeneratorService;

  @Scheduled(cron = "0 0/1 * * * *")
  public void doWork() {
    LocalDateTime now = LocalDateTime.now();
    if ((now.getHour() == 11 && now.getMinute() == 30)
        || (now.getHour() == 17)
        || (now.getHour() == 21)) {
      try {
        storyGeneratorService.generateStory(now.getHour());
      } catch (Exception e) {
        log.error("error in generate story", e);
      }
    }
  }
}
