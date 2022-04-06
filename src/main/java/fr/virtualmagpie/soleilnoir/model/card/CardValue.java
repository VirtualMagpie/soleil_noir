package fr.virtualmagpie.soleilnoir.model.card;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CardValue {
  TWO(IntValue.TWO, "2"),
  THREE(IntValue.THREE, "3"),
  FOUR(IntValue.FOUR, "4"),
  FIVE(IntValue.FIVE, "5"),
  SIX(IntValue.SIX, "6"),
  SEVEN(IntValue.SEVEN, "7"),
  EIGHT(IntValue.EIGHT, "8"),
  NINE(IntValue.NINE, "9"),
  TEN(IntValue.TEN, "0"),
  JACK(IntValue.JACK, "J"),
  QUEEN(IntValue.QUEEN, "Q"),
  KING(IntValue.KING, "K"),
  ACE(IntValue.ACE, "A");

  private final int intValue;
  private final String stringValue;

  public String toString() {
    return "[" + stringValue + "]";
  }

  public static CardValue fromInt(int intValue) {
    for (CardValue cardValue : CardValue.values()) {
      if (intValue == cardValue.getIntValue()) {
        return cardValue;
      }
    }
    throw new IllegalArgumentException(
        String.format("Value %s cannot be converted to card value", intValue));
  }

  public static CardValue fromString(String stringValue) {
    for (CardValue cardValue : CardValue.values()) {
      if (Objects.equals(stringValue, cardValue.getStringValue())) {
        return cardValue;
      }
    }
    throw new IllegalArgumentException(
        String.format("Value %s cannot be converted to card value", stringValue));
  }

  public static class IntValue {
    public static int TWO = 2;
    public static int THREE = 3;
    public static int FOUR = 4;
    public static int FIVE = 5;
    public static int SIX = 6;
    public static int SEVEN = 7;
    public static int EIGHT = 8;
    public static int NINE = 9;
    public static int TEN = 10;
    public static int JACK = 11;
    public static int QUEEN = 12;
    public static int KING = 13;
    public static int ACE = 14;
  }
}
