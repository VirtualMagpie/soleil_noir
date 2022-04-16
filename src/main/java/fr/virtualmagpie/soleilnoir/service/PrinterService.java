package fr.virtualmagpie.soleilnoir.service;

import fr.virtualmagpie.soleilnoir.model.combinaison.Combination;
import fr.virtualmagpie.soleilnoir.model.stat.CardDrawStatistics;

/** This service is used to beautifully log data in logs. */
public class PrinterService {

  public void printCardDrawStat(CardDrawStatistics statistics) {
    System.out.println();
    for (Combination difficulty : statistics.getDifficulties()) {
      System.out.println("Difficulty: " + difficulty.toString());
      for (int nbCards : statistics.getCardNumbers()) {
        float successRate = statistics.getStat(difficulty, nbCards);
        System.out.printf("%d cards ==> %.1f %%%n", nbCards, successRate * 100);
      }
      System.out.println();
    }
  }
}
