package fr.virtualmagpie.soleilnoir.model.combinaison;

import fr.virtualmagpie.soleilnoir.model.card.CardValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinationTest {

  @Test
  void comparisonByQuantity() {
    // Combinations with different quantity
    List<Combination> combinations =
        List.of(
            new Combination(3, CardValue.ACE),
            new Combination(1, CardValue.ACE),
            new Combination(4, CardValue.ACE),
            new Combination(2, CardValue.ACE));
    // Order should be on quantity
    List<Combination> ordered = combinations.stream().sorted().toList();
    assertEquals(ordered.get(0).getNbCard(), 1);
    assertEquals(ordered.get(1).getNbCard(), 2);
    assertEquals(ordered.get(2).getNbCard(), 3);
    assertEquals(ordered.get(3).getNbCard(), 4);
  }

  @Test
  void comparisonByValue() {
    // Combinations with different values
    List<Combination> combinations =
        List.of(
            new Combination(1, CardValue.TWO),
            new Combination(1, CardValue.SIX),
            new Combination(1, CardValue.NINE),
            new Combination(1, CardValue.TEN),
            new Combination(1, CardValue.ACE),
            new Combination(1, CardValue.FIVE),
            new Combination(1, CardValue.FOUR),
            new Combination(1, CardValue.KING),
            new Combination(1, CardValue.JACK),
            new Combination(1, CardValue.QUEEN),
            new Combination(1, CardValue.EIGHT),
            new Combination(1, CardValue.SEVEN),
            new Combination(1, CardValue.THREE));
    // Order should be on values
    List<Combination> ordered = combinations.stream().sorted().toList();
    assertEquals(ordered.get(0).getCardValue(), CardValue.TWO);
    assertEquals(ordered.get(1).getCardValue(), CardValue.THREE);
    assertEquals(ordered.get(2).getCardValue(), CardValue.FOUR);
    assertEquals(ordered.get(3).getCardValue(), CardValue.FIVE);
    assertEquals(ordered.get(4).getCardValue(), CardValue.SIX);
    assertEquals(ordered.get(5).getCardValue(), CardValue.SEVEN);
    assertEquals(ordered.get(6).getCardValue(), CardValue.EIGHT);
    assertEquals(ordered.get(7).getCardValue(), CardValue.NINE);
    assertEquals(ordered.get(8).getCardValue(), CardValue.TEN);
    assertEquals(ordered.get(9).getCardValue(), CardValue.JACK);
    assertEquals(ordered.get(10).getCardValue(), CardValue.QUEEN);
    assertEquals(ordered.get(11).getCardValue(), CardValue.KING);
    assertEquals(ordered.get(12).getCardValue(), CardValue.ACE);
  }

  @Test
  void comparisonByQuantityAndValue() {
    // Combinations with different values
    List<Combination> combinations =
        List.of(
            new Combination(2, CardValue.ACE),
            new Combination(1, CardValue.JACK),
            new Combination(1, CardValue.ACE),
            new Combination(2, CardValue.JACK));
    // Order should be on quantity first, then on values
    List<Combination> ordered = combinations.stream().sorted().toList();
    assertEquals(ordered.get(0).getNbCard(), 1);
    assertEquals(ordered.get(0).getCardValue(), CardValue.JACK);
    assertEquals(ordered.get(1).getNbCard(), 1);
    assertEquals(ordered.get(1).getCardValue(), CardValue.ACE);
    assertEquals(ordered.get(2).getNbCard(), 2);
    assertEquals(ordered.get(2).getCardValue(), CardValue.JACK);
    assertEquals(ordered.get(3).getNbCard(), 2);
    assertEquals(ordered.get(3).getCardValue(), CardValue.ACE);
  }

  @Test
  void stringFormatting() {
    Combination combination1 = new Combination(1, CardValue.JACK);
    Combination combination2 = new Combination(2, CardValue.FOUR);
    Combination combination3 = new Combination(3, CardValue.TEN);
    Combination combination4 = new Combination(4, CardValue.ACE);
    assertEquals("1[J]", combination1.toString());
    assertEquals("2[4]", combination2.toString());
    assertEquals("3[0]", combination3.toString());
    assertEquals("4[A]", combination4.toString());
  }

  @Test
  void stringParsing() {
    Combination combination1 = Combination.fromString("1[J]");
    Combination combination2 = Combination.fromString("2[4]");
    Combination combination3 = Combination.fromString("3[0]");
    Combination combination4 = Combination.fromString("4[A]");
    assertEquals(new Combination(1, CardValue.JACK), combination1);
    assertEquals(new Combination(2, CardValue.FOUR), combination2);
    assertEquals(new Combination(3, CardValue.TEN), combination3);
    assertEquals(new Combination(4, CardValue.ACE), combination4);
  }
}
