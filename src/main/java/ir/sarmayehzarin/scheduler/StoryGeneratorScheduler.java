package ir.sarmayehzarin.scheduler;

import ir.sarmayehzarin.service.StoryGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class StoryGeneratorScheduler {
  private final StoryGeneratorService storyGeneratorService;

  @Scheduled(cron = "0 0/30 11-21 * * *")
  public void doWork() {
    LocalDateTime now = LocalDateTime.now();
    if ((now.getHour() == 11 && now.getMinute() == 30)
        || (now.getHour() == 17 && now.getMinute() == 0)
        || (now.getHour() == 21) && now.getMinute() == 0) {
      try {
        log.info("try to generate story");
        storyGeneratorService.generateStory(now.getHour());
      } catch (Exception e) {
        log.error("error in generate story", e);
      }
    }
  }
}
