package fr.virtualmagpie.soleilnoir;

import fr.virtualmagpie.soleilnoir.config.StatConfig;
import fr.virtualmagpie.soleilnoir.model.stat.CardDrawStatistics;
import fr.virtualmagpie.soleilnoir.service.PrinterService;
import fr.virtualmagpie.soleilnoir.service.StatService;

import java.util.Random;

public class Main {

  public static void main(String... args) {
    Random random = new Random();
    long seed = random.nextLong();
    System.out.println("Seed: " + seed);
    random.setSeed(seed);

    StatConfig statConfig = new StatConfig();
    StatService statService = new StatService(random);
    PrinterService printerService = new PrinterService();

    CardDrawStatistics statistics = statService.statsCardDraw(statConfig);
    printerService.printCardDrawStat(statistics);
  }
}
