package edu.utsa.cs3443.blackjack2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import edu.utsa.cs3443.blackjack2.model.Card;
import edu.utsa.cs3443.blackjack2.model.Player;

public class TableActivity extends AppCompatActivity {
    private ArrayList<Player> players;
    private Player player;
    private ArrayList<Card> deck;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private int dealerScore, dealerAceCount, currentDealerCardIndex;

    private int playerScore, playerAceCount, currentPlayerCardIndex;
    private int playerBet;


    /**
     * onCreate():
     *  - Finds betButton and adds a Listener for it.

     *  - Creates a new Player object based on the Intent
     *    from MainActivity.java (String name, int chipCount)
     *    and displays it to the screen in playerText TextView.

     *  - If betEditText TextView is NOT empty, we start the game.

     *  @param savedInstanceState - idk what this is but we need it for something :)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        // Get ArrayList of Players
        Intent intent = getIntent();
        player = new Player(intent.getStringExtra("name"), 0);
        players = (ArrayList<Player>)intent.getSerializableExtra("players");

        // Find current Player in list
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(player.getName())) {
                player = players.get(i);
            }
        }

        // Buttons
        Button betButton;
        Button exitButton;

        // TextViews
        TextView currentBetText;
        EditText betEditText;
        TextView playerText;

        // Player Initializing
        playerText = findViewById(R.id.playerText);

        // Change TextView to Player representation
        playerText.setText(player.toString());

        // Find TextViews
        betEditText = findViewById(R.id.betEditText);
        currentBetText = findViewById(R.id.currentBetText);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (player.getChipCount() > 0) {
            // betButton Listener
            betButton = findViewById(R.id.betButton);
            betButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (betEditText.getText().toString().equals("")) {
                        Toast.makeText(TableActivity.this, "Please enter a bet amount to start the game", Toast.LENGTH_SHORT).show();
                    } else {
                        initializeCardImages();
                        playerBet = Integer.parseInt(betEditText.getText().toString());
                        currentBetText.setText("Current Bet: " + playerBet);
                        player.betChips(playerBet);
                        playerText.setText(player.toString());
                        startGame();
                    }

                    // Buttons
                    Button hitButton;
                    Button standButton;
                    Button doubleDownButton;
                    Button splitButton;

                    // hitButton Listener
                    hitButton = findViewById(R.id.hitButton);
                    hitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hit();
                        }
                    });

                    // standButton Listener
                    standButton = findViewById(R.id.standButton);
                    standButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stand();
                        }
                    });

                    // doubleDownButton Listener
                    doubleDownButton = findViewById(R.id.doubleDownButton);
                    doubleDownButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // doubleDown
                        }
                    });

                    // check if playerHand is able to split
                    if (playerHand.get(0).getValue() == playerHand.get(1).getValue()) {
                        // splitButton Listener
                        splitButton = findViewById(R.id.splitButton);
                        splitButton.setEnabled(true);
                        splitButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // split
                            }
                        });
                    }
                }
            });
        } else {
            finish();
            Toast.makeText(TableActivity.this, player.getName() + " ran out of chips.", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * initializeCardImages():
     *  + Initializes all Card ImageViews to default
     *    which is an image of the back of a card.
     */
    public void initializeCardImages() {

        Card cardBack = new Card("card", "back");

        for (int i = 1; i < 5; i++) {
            updateCardImage(TableActivity.this, cardBack, "R.id.playerCard" + i);
            updateCardImage(TableActivity.this, cardBack, "R.id.dealerCard" + i);
        }
    }

    /**
     * startGame():
     *  - Called by betButton onClick() method when given
     *    an chip bet amount.

     *  - Initializes deck with buildDeck() and shuffleDeck().

     *  - Clears Text in the betEditText.

     *  - Creates playerHand and dealerHand.

     *  - Draws two initial cards for playerHand and
     *    two initial cards for dealerHand (one hidden).

     *  - If the two drawn cards make BlackJack ([10, jack, queen, king] and Ace),
     *    end the game and display winnings as well as hiddenCard in dealerHand.

     *  - Else, listen for hit(), stand(), doubleDown(), and split().
     */

    public void startGame() {
        // Initialize Game
        buildDeck(); // deck = 52 cards
        shuffleDeck(); // randomize deck

        // Remove Text in betEditText
        EditText editText = findViewById(R.id.betEditText);
        editText.setText("");

        // Dealer Initialization
        dealerHand = new ArrayList<>(); // ArrayList<Card> holding all of the dealer's Cards

        dealerAceCount = 0; // keep track of Aces in dealer's hand
        dealerScore = 0; // keep track of the sum of the dealer's hand
        currentDealerCardIndex = 0; // keep track of the current Card index in dealer's hand to place ImageView
        updateDealerScore(); // display initial score (dealer)

        // Player Initialization
        playerHand = new ArrayList<>(); // ArrayList<Card> holding all of the player's Cards

        playerAceCount = 0; // keep track of Aces in player's hand
        playerScore = 0; // keep track of the sum of the player's hand
        currentPlayerCardIndex = 0; // keep track of the current Card index in player's hand to place ImageView
        updatePlayerScore(); // display initial score (player)

        // Draw Player's First Two Cards
        for (int i = 1; i < 3; i++) {
            Card card = deck.remove(deck.size() - 1); // Draw Card from deck
            playerHand.add(card); // add drawn Card to player's hand
            currentPlayerCardIndex++; // increment 1 Card index (player) == 1 -> 2

            playerScore += card.getValue(); // add card's value to player's score
            updatePlayerScore(); // display amount

            playerAceCount += card.isAce() ? 1 : 0; // add 1 if Card is an Ace (player)

            updateCardImage(TableActivity.this, card, "R.id.playerCard" + i); // update ImageView to corresponding Card
        }

        // Draw Dealer's Shown Card
        Card shownCard = deck.remove(deck.size() - 1); // Draw Card from deck
        dealerHand.add(shownCard); // add shown card to dealer's hand
        currentDealerCardIndex++; // Increment 1 Card index (dealer) == 1

        dealerScore += shownCard.getValue(); // add card's value to dealer's score
        updateDealerScore(); // display amount

        dealerAceCount += shownCard.isAce() ? 1 : 0; // add 1 if Card is an Ace (dealer)

        updateCardImage(TableActivity.this, shownCard, "R.id.dealerCard1"); // update ImageView to corresponding Card

        // Draw Dealer's Hidden Card
        Card hiddenCard = deck.remove(deck.size() - 1); // remove one card from last card in deck
        dealerHand.add(hiddenCard); // add hidden card to dealer's hand
        currentDealerCardIndex++; // Increment 1 Card index (dealer) == 2

        dealerScore += hiddenCard.getValue(); // add card's value to dealer's score
        // DO NOT DISPLAY SCORE OR IMAGE YET

        dealerAceCount += hiddenCard.isAce() ? 1 : 0; // add 1 if Card is an Ace (dealer)

        // Check if dealer or player has BlackJack w/ initial 2 Cards
        if (playerScore == 21 || dealerScore == 21) {
            updateCardImage(TableActivity.this, hiddenCard, "R.id.dealerCard2");
            updateDealerScore();

            if (playerScore == 21 && dealerScore == 21) {
                // Player wins his bet back
                Toast.makeText(this, "Push, $ +" + playerBet, Toast.LENGTH_SHORT).show();
                player.setChipCount(playerBet);
            }

            // Player Has BlackJack
            if (playerScore == 21) {

                // Dealer pays 3x current bet amount
                Toast.makeText(this, "Player Wins by BlackJack, $ +" + playerBet * 3, Toast.LENGTH_SHORT).show();
                player.setChipCount(playerBet * 3); // sets player's chipCount to 3x the amount of the pot


            } else if (dealerScore == 21) {

                // Player Loses current bet amount
                Toast.makeText(this, "Dealer Wins by BlackJack, $ -" + playerBet, Toast.LENGTH_SHORT).show();


            }
        }

        // Update player's chip count
        TextView playerText = findViewById(R.id.playerText);
        playerText.setText(player.toString());
    }

    /**
     *  buildDeck():
     *      - Creates the new ArrayList (deck)
     *        and adds each possible combination of Card
     *        objects into it.

     *      - 13 values * 4 suits = 52 Card objects
     */
    public void buildDeck() {
        deck = new ArrayList<>();
        String[] value = {"ace", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "jack", "queen", "king"};
        String[] suit = {"diamonds", "clubs", "spades", "hearts"};

        for (int i = 0; i < suit.length; i++) {
            for (int j = 0; j < value.length; j++) {
                Card card = new Card(suit[i], value[j]);
                deck.add(card);
            }
        }
    }

    /**
     *   shuffleDeck():
     *      - Based on the number of cards in a standard
     *        deck of cards (52), randomly swap Card objects
     *        within the deck ArrayList using a Random object
     *        until we reach the last element in the
     *        deck( deck.size() ).
     */
    public void shuffleDeck() {

        Random random = new Random(); // initialize Random object

        // Loop through the deck randomly swapping cards
        for (int i = 0; i < deck.size(); i++) {

            int j = random.nextInt(deck.size()); // random Card object index in deck
            Card card = deck.get(i); // current Card object
            Card randomCard = deck.get(j); // random Card object

            // Swap cards
            deck.set(i, randomCard);
            deck.set(j, card);
        }
    }

    /**
     *  updateDealerScore():
     *      - Finds TextView for "Dealer Score:" in .xml file
     *        using the id: dealerScoreText.

     *      - Displays and appends instance variable dealerScore
     *        to the screen in the TextView element.
     */
    public void updateDealerScore() {
        TextView text = findViewById(R.id.dealerScoreText);
        text.setText("Dealer Score: " + dealerScore);
    }

    /**
     * updatePlayerScore():
     *  - Finds TextView for "Player Score:" in .xml file
     *    using the id: playerScoreText.

     *  - Displays and appends instance variable playerScore
     *    to the screen in the TextView element.
     */
    public void updatePlayerScore() {
        TextView text = findViewById(R.id.playerScoreText);
        text.setText("Player Score: " + playerScore);
    }

    /**
     * updateCardImage():
     *  - Finds the resource's name based on the resourceId.

     *  - Uses a helper function getResourceId() to return
     *    the ID of the resource.

     *  - Finds the ImageView using android.app

     *  - If the view is not equal to null, set the ImageView.
     *
     *  @param context - TableActivity.this
     *  @param card - Card object we are trying to display
     *  @param resourceId - R.id.resourceId in .xml file
     */

    public void updateCardImage(Context context, Card card, String resourceId) {
        String[] parts = resourceId.split("\\.");
        if (parts.length != 3)
            return;

        String resourceName = parts[2];

        int resId = getResourceId(context, resourceName, "id");

        ImageView imageView = ((android.app.Activity)context).findViewById(resId);

        if (imageView != null) {
            int imageResourceId = context.getResources().getIdentifier(card.toString(), "drawable", context.getPackageName());
            imageView.setImageResource(imageResourceId);
        }
    }

    /**
     * getResourceId():
     *  - Helper function for updateCardImage().

     *  - Using the name of the ImageView's id,
     *    we can try/catch search for the resource.

     *  - If found, return the id at which it was found.
     *
     *  @param context - TableActivity.this
     *  @param resourceName - name of the ImageView we are trying to set
     *  @param resourceType - type of resource (== id: R.id.idName)
     *  @return - (int): id of the ImageView we are trying to set
     */
    public int getResourceId(Context context, String resourceName, String resourceType) {
        try {
            Class<?> resClass = Class.forName(context.getPackageName() + ".R$" + resourceType);
            Field field = resClass.getField(resourceName);
            return field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * stand():
     *  - Called by standButton.onClick()

     *  - Checks if the player busted. If so,
     *    player loses the initial bet.

     *  - If player has bust, dealer draws
     *    Cards until reaches hand score >=17.

     *  - Checks if the dealer busted. If so,
     *    player += bet * 2

     *  - If the player's hand score > dealer's hand score,
     *      + player wins!
     *      + winnings = bet * 2

     *  - If the dealer's hand score > player's hand score,
     *      + dealer wins!
     *      + winnings = -bet

     *  - If the player's hand score == dealer's hand score,
     *      + push!
     *      + winnings = +bet
     */
    public void stand() {
        Button splitButton = findViewById(R.id.splitButton);
        splitButton.setEnabled(false);

        TextView playerText = findViewById(R.id.playerText);

        updateCardImage(TableActivity.this, dealerHand.get(1), "R.id.dealerCard2");

        updateDealerScore();

        // Player Busts
        if (playerScore > 21) {
            Toast.makeText(TableActivity.this, "Player Bust $ -" + playerBet, Toast.LENGTH_SHORT).show();
        } else if (playerScore == 21) {
            Toast.makeText(TableActivity.this, "Player w/ Lucky 21 $ +" + playerBet * 2, Toast.LENGTH_SHORT).show();
            player.setChipCount(playerBet * 2);
            playerText.setText(player.toString());
        } else {
            // Dealer draws until dealerScore <= 17
            while (dealerScore <= 17) {
                Card card = deck.remove(deck.size() - 1);
                dealerHand.add(card);
                currentDealerCardIndex++; // == 1 -> 2 -> ...

                // Check for multiple Aces
                if (dealerAceCount == 0 && dealerScore >= 11 && card.isAce()) {
                    dealerAceCount++;
                    dealerScore++;
                    updateDealerScore();
                } else if (dealerAceCount >= 1 && !card.isAce()) {
                    dealerScore += card.getValue();
                    if (dealerScore > 21) {
                        dealerScore -= 10;
                        updateDealerScore();
                    }
                    updateDealerScore();
                } else if (dealerAceCount >= 1 && card.isAce()) {
                    dealerAceCount++;
                    dealerScore++; // if dealer's hand has more than one Ace and the next Card is an Ace, the Card must be equal to 1
                    updateDealerScore();
                } else {
                    dealerScore += card.getValue();
                    updateDealerScore();
                }

                if (currentDealerCardIndex < 5) {
                    updateCardImage(TableActivity.this, card, "R.id.dealerCard" + currentDealerCardIndex);
                } else {
                    updateCardImage(TableActivity.this, card, "R.id.dealerCard5");
                }
            }

            // Dealer Busts
            if (dealerScore > 21) {
                Toast.makeText(TableActivity.this, "Dealer Bust $ +" + playerBet * 2, Toast.LENGTH_SHORT).show();
                player.setChipCount(playerBet * 2);
                playerText.setText(player.toString());
            } else {
                if (dealerScore > playerScore) {
                    Toast.makeText(TableActivity.this, "Dealer wins w/ " + dealerScore + ", $ -" + playerBet, Toast.LENGTH_SHORT).show();
                } else if (playerScore > dealerScore) {
                    Toast.makeText(TableActivity.this, "Player wins w/ " + playerScore + ", $ +" + playerBet * 2, Toast.LENGTH_SHORT).show();
                    player.setChipCount(playerBet * 2);
                    playerText.setText(player.toString());
                } else {
                    Toast.makeText(TableActivity.this, "Push, $ +" + playerBet, Toast.LENGTH_SHORT).show();
                    player.setChipCount(playerBet);
                    playerText.setText(player.toString());
                }
            }

        }
    }

    public void hit() {
        Button splitButton = findViewById(R.id.splitButton);
        splitButton.setEnabled(false);

        Card card = deck.remove(deck.size() - 1);
        playerHand.add(card);
        currentPlayerCardIndex++;

        if (playerAceCount == 0 && card.isAce() && playerScore >= 11) {
            playerAceCount++;
            playerScore++;
            updatePlayerScore();
        } else if (playerAceCount >= 1 && !card.isAce()) {
            playerScore += card.getValue();
            if (playerScore > 21) {
                playerScore -= 10;
            }
            updatePlayerScore();
        } else if (playerAceCount >= 1 && card.isAce()) {
            playerAceCount++;
            playerScore++;
            updatePlayerScore();
        } else {
            playerScore += card.getValue();
            updatePlayerScore();
        }

        // if player hits more than the amount of cards on the table, user playerCard5 ? idk :)
        if (currentPlayerCardIndex < 5) {
            updateCardImage(TableActivity.this, card, "R.id.playerCard" + currentPlayerCardIndex);
        } else {
            updateCardImage(TableActivity.this, card, "R.id.playerCard5");
        }

        // Player bust
        if (playerScore > 21) {
            stand();
        }

        // Player gets lucky
        if (playerScore == 21) {
            stand();
        }
    }
}