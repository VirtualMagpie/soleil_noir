# Soleil Noir modelisation

This project is inspired by tabletop role-playing game "Soleil Noir". See https://soleilnoir-jdr.blogspot.com/

This project attempts to model some actions performed in this game. In particular, it allows producing statistics on
this actions.

For now, this project only model card draw, which allows knowing probability of success of a draw.

### Card draw statistics

This program executes multiple random card draws. Each card draw consist of:

- a number of card to draw from a classical 54-cards deck
- a difficulty for the draw (see combination/difficulty below)
- a combination strategy, which define how we compute the force of the hand of card that has been drawn

By comparing the force of the hand against the difficulty to pass, we can define if the draw was a success or not. By
performing multiple draw, we can compute a success rate for each experience. This gives us an estimation of the
probability of success of card draw given a difficulty to pass, and a number of cards to draw.

##### Cards

Soleil Noir system uses a classical deck of 54 cards, consisting of 52 "normal" cards, and 2 jokers. Normal cards have a
value (in ascending order of their force: 2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace) and a symbol (club, spade,
heart, diamond). When representing a card value in this program, we usually use a single character in this order: "2", "
3", "4", "5", "6", "7", "8", "9", "0", "J", "Q", "K", "A" (value 10 is represented with a "0" to prevent confusion with
the Ace card).

##### Combination/Difficulty

In Soleil Noir system, a set of cards can form together a combination, defining how strong this set of card is. A
combination of card consists of several cards with the same value. The more card in the combination, the better. In case
of tie between combinations, the combination using the stronger value is the better combination. Therefore, a pair of
Aces is better than a pair of 2, but a pair of 2 is better than a single Ace.

When defining a difficulty for a draw, we juste define the minimum combination to get. If drawn card are enough to
produce a combination at least as strong as the difficulty, then the draw is a success.

In this program, when representing a combination, we usually use this pattern: number_of_card[card_value_symbol]. For
instance, 2[A] represents a pair of aces.

##### Strategy

Combination strategy represent how we pick the combination of a hand based on the set of cards in this hand. Default
rule in Soleil Noir is to pick the best combination existing in current hand without further modification. But sometimes
other rules may apply (when character has some advantage or disadvantage for instance), modifying how cards are
considered. When running this program, we must define which strategy is applied.

The implemented strategy are:

- "default" strategy:

  This is the most common strategy, applied when not special rule is involved. With such strategy, we simply look at the
  hand of card, and count which card value is present with the higher amount. In case of a tie, we pick the symbol with
  higher value. For instance, with a hand of "Queen of club", "Queen of heart", "Ten of spade", "Ten of diamond" and "
  Ace of club", the final combination is 2[Q] (there is 2 possible combination with 2 cards: a pair of Queen, or a pair
  of Ten (Ace has only one card) ; since the Queen is stronger than Ten, the final combination is the pair of Queen).
  When Joker is present in hand, it can take any value, meaning it can enhance existing combination (for instance in
  previous example, if the hand also possessed a Joker card, the final combination would be a 3[Q], because Joker would
  be converted into a Queen card).

- "disadvantage" strategy:

  This strategy is the implementation of some disadvantages which exist in Soleil Noir. With such strategy, one of the
  four card symbols is treated as a disadvantage symbol. When performing a card draw, and before defining the
  combination of the hand, we must throw away from the hand any card with this symbol (excepted Aces). Otherwise, we
  apply the same rules as in "default" strategy. For instance, with a disadvantage on symbol "club", and a hand of "Ace
  of club", "Ace of heart", "King of club", "King of heart", "King of diamond", the final combination is 2[A] (the King
  of club is removed because it is a club card, but the Ace of club is not removed because it is an Ace).

- "advantage" strategy:

  This strategy is the implementation of some advantages which exist in Soleil Noir. With such strategy, one of the four
  card symbols is treated as an advantage symbol. When performing a card draw, and before defining the combination of
  the hand, we must convert some cards into a new value: all cards with the advantage symbol (including Joker if they
  pick this symbol) are converted into card with the same symbol, but with the value of the higher card with this
  symbol (including Joker if they pick this symbol). For instance, with an advantage on symbol "club", and a hand of "
  King of heart", "Jack of club", "Two of club" and "Joker", the final combination is 4[K] (the Joker is converted into
  a "King of club", then all club cards are converted into the club card with the higher value, ie the King of club...
  the hand is therefore now a hand with one "King of heart" and three "King of club").

- "advantage-limited-joker-before":

  *(/!\ Caution: this is a personal interpretation of Soleil Noir rules, and not the official rule. I have implemented
  this custom rule interpretation because I feel the original "advantage" strategy is too powerful, and too complex to
  resolve during card draw. /!\\)*

  This strategy is the implementation of some advantages which exist in Soleil Noir, with a personal variation. With
  this strategy, we apply the same rule as in "advantage" strategy, where cards with the advantage symbol are converted
  into the best card with this symbol. However, with this specific strategy, we no longer consider those converted cards
  as compatible with normal cards. If player choose to apply its advantage, he must create a combination with only cards
  with the advantage symbol. For instance, with an advantage on symbol "club", and a hand of "King of heart", "Jack of
  club", "Two of club" and "Joker", the final combination is 3[A] (if the player had chosen not to apply its advantage,
  the best combination would be a pair of King by converting Joker into a King - however, when applying advantage,
  player can create a combination made of clubs, which would be a "Three-of-a-kind of club" because 3 club cards are
  present in the hand (2 club cards and 1 Joker which can be converted into a club card - it is no longer interesting to
  convert the Joker into a "King of club" because the King of heart cannot be applied in the combination of advantage
  cards (we cannot put this heart card into the "Three-of-a-kind of club")) - furthermore, if the player choose the
  Joker to be an "Ace of club", all other club cards are converted into an Ace, producing a combination of "
  Three-of-a-kind of club" evaluated as a "Three-of-a-kind of aces").

- "advantage-limited-joker-after":

  *(/!\ Caution: this is a personal interpretation of Soleil Noir rules, and not the official rule. I have implemented
  this custom rule interpretation because I feel the original "advantage" strategy is too powerful, and too complex to
  resolve during card draw. Furthermore, this is the strategy I personally use when being Game Master in Soleil Noir. /!
  \\)*

  This strategy is the same as "advantage-limited-joker-before" with a small variation: Joker value is defined after
  cards with the advantage symbol have changed their value. This means we can no longer converts all advantage card into
  an Ace combination only by defining the Joker as an Ace of the advantage symbol. Here the Joker should instead take
  the value of the higher card of the combination. For instance, with an advantage on symbol "club" and a hand of "King
  of heart", "Jack of club", "Two of club" and "Joker", the final combination is 3[J] (the two club cards are converted
  into a "pair of club", evaluated as a "pair of Jack" - then the Joker is added as another club card, converting this
  hand into a "three-of-a-kind of club", evaluated as a "three-of-a-kind of Jack").

##### Configuration

To run this program with different configuration, you should edit configuration file:
src/main/resources/stat-config.properties

Configuration are:

- nb-try: (integer) Number of repetition of each experience to perform statistics. For instance, if this value is 100,
  this means that we will perform 100 draws for first difficulty with first number of cards, 100 others draws for second
  difficulty and first number of cards, etc... With a higher amount, the result will be more precise, but the program
  will take longer to finish (please check normal distribution variance for an estimation of the precision).

- nb-card-min / nb-card-max: (integer/integer) Those numbers define the range of cards numbers used for those
  statistics. For instance, with values 1 and 12 (respectively), program will perform statistics when drawing only 1
  card, when drawing 2 cards, ... and when drawing 12 cards.

- difficulties: (list<string>) This list define all difficulties against which statistics will be performed. For
  instance, "1[A],2[2],2[A]" means statistics will be performed against difficulties "one-card of Ace", "pair of 2"
  and "pair of Ace".

- combination-strategy: (string) Define which combination strategy is applied during those statistics. See section "
  Strategy" for list of strategy names and signification.

##### Export result

When program is completed, a result csv file will be generated in directory "export".

File name has this pattern: "soleil-noir-statistics_<strategy_name>_<nb_try>.csv", where <strategy_name> and <nb_try>
are values of those parameters provided when the program was run.

The result file is a csv table defining success rate (in percentage) for each number of drawn cards against each
difficulty.

I run this program for each strategy and with a sample size of 10 000 000 draws. Those results are provided in export
folder. Given this sample size, they are supposed to have a margin of error of 0.04% (with a confidence level of 99%).
Since results are provided with a precision of 0.1%, we can suppose all digits of those results to be significant (
-+0.1% because of rounded result). (For instance, probability of success against 1[J] with a single card is
theoretically 33.333...%. With such sample size for statistics, we should have at least 99% chance to find in export
result a success rate of 33.3% or 33.4%).

(See https://en.wikipedia.org/wiki/Margin_of_error for calculation of margin of error)