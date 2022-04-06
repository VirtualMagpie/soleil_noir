package fr.virtualmagpie.soleilnoir.service;

import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.stat.CardDrawStatistics;
import org.junit.jupiter.api.Test;

import java.util.Random;

class StatServiceTest {

  @Test
  void test() {

    Random random = new Random();
    long seed = random.nextLong();
    System.out.println("Seed: " + seed);
    random.setSeed(seed);

    StatService statService = new StatService(random);
    PrinterService printerService = new PrinterService();

    int nbTry = 10_000;
    int nbCardsMin = 1;
    int nbCardMax = 12;
    Combination[] difficulties = {
      // 1 card
      new Combination(1, CardValue.JACK),
      new Combination(1, CardValue.QUEEN),
      new Combination(1, CardValue.KING),
      new Combination(1, CardValue.ACE),
      // 2 cards
      new Combination(2, CardValue.TWO),
      new Combination(2, CardValue.JACK),
      new Combination(2, CardValue.ACE),
      // 3 cards
      new Combination(3, CardValue.TWO),
      new Combination(3, CardValue.JACK),
      new Combination(3, CardValue.ACE),
      // 4 cards
      new Combination(4, CardValue.TWO),
      new Combination(4, CardValue.JACK),
      new Combination(4, CardValue.ACE),
    };
    CardDrawStatistics stats =
        statService.statsCardDraw(nbTry, nbCardsMin, nbCardMax, difficulties);
    printerService.printCardDrawStat(stats);
  }
}
