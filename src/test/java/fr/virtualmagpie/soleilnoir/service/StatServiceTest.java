package fr.virtualmagpie.soleilnoir.service;

import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import org.junit.jupiter.api.Test;

import java.util.Random;

class StatServiceTest {

  @Test
  void test() {
    Random random = new Random();
    Long seed = random.nextLong();
    System.out.println("Seed = " + seed);
    random.setSeed(seed);
    StatService statService = new StatService(random);

    int nbTry = 10_000;
    int nbCardDrawnMin = 1;
    int nbCardDrawnMax = 12;
    Combination[] difficulties = {
      new Combination(1, CardValue.JACK),
      new Combination(1, CardValue.QUEEN),
      new Combination(1, CardValue.KING),
      new Combination(1, CardValue.ACE),
      new Combination(2, CardValue.TWO),
      new Combination(2, CardValue.JACK),
      new Combination(2, CardValue.ACE),
      new Combination(3, CardValue.TWO),
      new Combination(3, CardValue.JACK),
      new Combination(3, CardValue.ACE),
      new Combination(4, CardValue.TWO),
      new Combination(4, CardValue.JACK),
      new Combination(4, CardValue.ACE),
    };
    statService.rangeStatOfCardDrawAgainstMultipleDifficulty(
        nbTry, nbCardDrawnMin, nbCardDrawnMax, difficulties);
  }
}
