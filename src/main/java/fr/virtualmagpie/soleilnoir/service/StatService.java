package fr.virtualmagpie.soleilnoir.service;

import fr.virtualmagpie.soleilnoir.model.Deck;
import fr.virtualmagpie.soleilnoir.model.card.Card;
import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.combinaison.CombinationStrategy;

import java.util.Random;

public class StatService {
  public final Deck deck;
  public final Random random;

  public StatService(Random random) {
    this.deck = new Deck();
    this.deck.init();
    this.random = random;
  }

  public float[][] rangeStatOfCardDrawAgainstMultipleDifficulty(
      int nbTry, int nbCardDrawnMin, int nbCardDrawnMax, Combination[] difficulties) {
    if (nbCardDrawnMax < nbCardDrawnMin) {
      throw new IllegalArgumentException("Wrong range definition of card drawn");
    }
    float[][] stats = new float[difficulties.length][nbCardDrawnMax - nbCardDrawnMin + 1];
    for (int i = 0; i < difficulties.length; i++) {
      Combination difficulty = difficulties[i];
      System.out.printf("Difficulty: %s%n", difficulty);
      stats[i] =
          rangeStatOfCardDrawAgainstDifficulty(nbTry, nbCardDrawnMin, nbCardDrawnMax, difficulty);
      System.out.println();
    }
    return stats;
  }

  public float[] rangeStatOfCardDrawAgainstDifficulty(
      int nbTry, int nbCardDrawnMin, int nbCardDrawnMax, Combination difficulty) {
    if (nbCardDrawnMax < nbCardDrawnMin) {
      throw new IllegalArgumentException("Wrong range definition of card drawn");
    }
    float[] stats = new float[nbCardDrawnMax - nbCardDrawnMin + 1];
    for (int nbCardDrawn = nbCardDrawnMin; nbCardDrawn <= nbCardDrawnMax; nbCardDrawn++) {
      // System.out.printf("Draw %s cards%n", nbCardDrawn);
      float successRate = statOfCardDrawAgainstDifficulty(nbTry, nbCardDrawn, difficulty);
      System.out.println(
          String.format("%02d cards ==> success rate = %.1f %%", nbCardDrawn, successRate * 100));
      stats[nbCardDrawn - nbCardDrawnMin] = successRate;
    }
    return stats;
  }

  public float statOfCardDrawAgainstDifficulty(int nbTry, int nbCardDrawn, Combination difficulty) {
    if (nbCardDrawn < 1) {
      throw new IllegalArgumentException("At least one card to draw");
    }

    int nbSuccess = 0;
    for (int i = 0; i < nbTry; i++) {
      deck.shuffle(random);
      Card[] drawn = deck.seeNext(nbCardDrawn);
      Combination combination = CombinationStrategy.findBestCombination(drawn);
      if (combination.compareTo(difficulty) >= 0) {
        nbSuccess++;
      }
    }

    float successRate = ((float) nbSuccess) / nbTry;
    // System.out.println(String.format("===> success rate = %s %%", successRate * 100));
    return successRate;
  }
}
