import java.util.Random;
import java.util.Scanner;

public class PokemonGame {


    Scanner scan = new Scanner(System.in);
    Card[] deck = new Card[60]; // 60-card deck
    Card[] hand = new Card[60];  // Cards in hand
    Card[] prizePile = new Card[6];// Prize pile
    Card[] bench = new Card[5]; //the pokemon bench
    Card[] activeSpot = new Card[1]; //the active pokemon
    private int deckIndex = 0;// Keeps track of the top card in the deck
    private int pokemonMaxHp = 0;//The initial hp of the pokemon card
    private boolean attachedAnEnergy = false; // used to track if a energy was attached
    private boolean supporterCardLimit = false; // used to make sure only 1 supporter card is played per turn
    private int benchCounter = 0; // used to determine where to put pokemon from hand to bench


    //opponents board
    Card[] opponentActiveSpot = new Card[1];// Opponent's active Pokémon
    Card[] opponentBench = new Card[5];
    Card[] deck2 = new Card[60]; // 60-card deck
    Card[] hand2 = new Card[60];  // Cards in hand
    Card[] prizePile2 = new Card[6];
    private int deckIndex2 = 0;// Keeps track of the top card in the deck
    private boolean fullBench = false; // tracks if the opponents bench is full
    private int benchIndex = 0; //tracks which spots of the bench are full


    // Method to assign cards into the deck
    public void assignDeck() {
        for (int i = 0; i < deck.length; i++) {
            deck[i] = new Card("", ""); //initialize the deck
        }

        // Assign 20 Pokémon cards
        for (int i = 0; i < 4; i++) {
            deck[i] = new Charmander();
        }
        for (int i = 4; i < 8; i++) {
            deck[i] = new Larvesta();
        }
        for (int i = 8; i < 12; i++) {
            deck[i] = new Litleo();
        }
        for (int i = 12; i < 16; i++) {
            deck[i] = new Pansear();
        }
        for (int i = 16; i < 20; i++) {
            deck[i] = new Ponyta();
        }

        // Assign 20 Trainer cards
        for (int i = 20; i < 24; i++) {
            deck[i] = new Card("Bill", "Trainer");
        }
        for (int i = 24; i < 28; i++) {
            deck[i] = new Card("Giant cape", "Trainer");
        }
        for (int i = 28; i < 32; i++) {
            deck[i] = new Card("Professor Oak", "Trainer");
        }
        for (int i = 32; i < 36; i++) {
            deck[i] = new Card("Professor's research", "Trainer");
        }
        for (int i = 36; i < 40; i++) {
            deck[i] = new Card("Potion", "Trainer");
        }

        //assigns 20 energy cards
        for (int i = 40; i < 60; i++) {
            deck[i] = new Card("Fire energy", "Energy");
        }
    }

    // Method to draw a card
    public void drawCard() {
        // Check if the deck is empty
        if (deckIndex >= deck.length || deck[deckIndex] == null) {
            System.out.println("There are no more cards left in the deck.");
            return;
        }

        // Shift hand left to make space for the new card
        for (int i = hand.length - 1; i > 0; i--) {
            hand[i] = hand[i - 1];
        }

        // Draw the top card from the deck
        hand[0] = deck[deckIndex];
        System.out.println("Drew a card: " + hand[0].getName() + " - " + hand[0].getType());

        // Move to the next card in the deck
        deckIndex++;
    }


    // Method to check if a card is a Pokémon card


    // Method to check if a card is a Trainer card
    private boolean isTrainer(Card card) {
        return card != null && card.getType().equals(Card.TRAINER);
    }

    // Method to check if a card is an Energy card
    private boolean isEnergy(Card card) {
        return card != null && card.getType().equals(Card.ENERGY);
    }


    // Fill hand with 7 cards (ensures at least one Pokémon)
    public void fillHand() {
        boolean hasPokemon = false;

        while (!hasPokemon) {
            for (int i = 0; i < 7; i++) {
                hand[i] = deck[i];
            }

            // Check if hand contains a Pokémon
            for (int i = 0; i < 7; i++) {
                if ((hand[i].getType().equals("Pokemon"))) {
                    hasPokemon = true;
                    break;
                }
            }

            if (!hasPokemon) {
                //putting cards back into the deck if a pokemon card was not drawn
                for(int i = 0; i < 7; i++){
                    deck[i] = hand[i];
                    hand[i] = null;
                }
                shuffleDeck(); // Shuffle and retry if no Pokémon is found
            }
        }

        deckIndex += 7; // Move deckIndex forward to account for drawn cards
    }

    // Shuffle deck using Fisher-Yates algorithm
    public void shuffleDeck() {
        Random rand = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }




    // Fill the prize pile with 6 cards
    public void fillPrizePile() {
        for (int i = 0; i < 6; i++) {
            if(deck[i + deckIndex] != null){
                prizePile[i] = deck[i + deckIndex];
            }
        }
        deckIndex += 6;
    }

    //method to discard all the cards in hand
    public void discardHand() {
        for (int i = 0; i < hand.length; i++) {
            hand[i] = null;//sets every card to null one by one
        }
        System.out.println("Your hand was discarded.");
    }

    // Draw a prize card
    public void drawPrizeCard() {

        for (int i = 0; i < prizePile.length; i++) {
            if (prizePile[i] != null) {
                // Find first empty slot in hand
                int emptySlot = -1;
                for (int j = 0; j < hand.length; j++) {
                    if (hand[j] == null) {
                        emptySlot = j;
                        break;
                    }
                }

                if (emptySlot != -1) {
                    System.out.println("You drew a prize card: " + prizePile[i].getName());
                    hand[emptySlot] = prizePile[i]; // Add prize card to hand
                    prizePile[i] = null; // Remove from prize pile

                    // Check if player has won
                    boolean prizesRemaining = false;
                    for (Card prize : prizePile) {
                        if (prize != null) {
                            prizesRemaining = true;
                            break;
                        }
                    }

                    if (!prizesRemaining) {
                        System.out.println("Congratulations! You've collected all prize cards and won the game!");
                        System.exit(0);
                    }

                    return;
                } else {
                    System.out.println("Your hand is full. Cannot draw prize card.");
                    return;
                }
            }
        }
        System.out.println("No more prize cards left!");
    }

    // Replace a defeated active Pokémon with one from the bench
    public void replaceDefeatedPokemon() {
        System.out.println("Your Active Pokémon is knocked out! Choose a new one from the bench:");

        boolean hasBenchedPokemon = false;
        for (int i = 0; i < bench.length; i++) {
            //checks if the bench has a pokemon
            if (bench[i] != null) {
                System.out.println((i + 1) + ". " + bench[i].getName());
                hasBenchedPokemon = true;
            }
        }

        //if there is no pokemon on the bench
        if (!hasBenchedPokemon) {
            System.out.println("No Pokémon on bench. You lost the game!");
            System.exit(0);
            return;
        }

        int choice = scan.nextInt() - 1;

        if (choice >= 0 && choice < bench.length && bench[choice] != null) {
            activeSpot[0] = bench[choice];//places pokemon from bench to active spot
            bench[choice] = null;//empties that spot from bench
            System.out.println("New Active Pokémon: " + activeSpot[0].getName());
        } else {
            System.out.println("Invalid choice. Try again.");
            replaceDefeatedPokemon();
        }
    }


    //resets opponents hp after a pokemon is knocked out, so if the same pokemon is brought out it has full hp
    public void resetHp(){
        //resetting hp for different pokemon
        if(opponentActiveSpot[0].getName().equals("Charmander")){
            opponentActiveSpot[0].setHp(70);
        }
        if(opponentActiveSpot[0].getName().equals("Larvesta")){
            opponentActiveSpot[0].setHp(70);
        }
        if(opponentActiveSpot[0].getName().equals("Litleo")){
            opponentActiveSpot[0].setHp(70);
        }
        if(opponentActiveSpot[0].getName().equals("Pansear")){
            opponentActiveSpot[0].setHp(60);
        }
        if(opponentActiveSpot[0].getName().equals("Ponyta")){
            opponentActiveSpot[0].setHp(40);
        }
    }

    //if opponents pokemon is knocked the energy on the active spot must be reset
    public void resetEnergy(){
        if(opponentActiveSpot[0].getType().equals("Pokemon")){
            opponentActiveSpot[0].setEnergyAttached(0);
        }
    }
    //resets the attached energy per turn counter
    public void energyReset(){
        attachedAnEnergy = false;
    }
    //resets the one supporter per turn counter
    public void supporterReset(){
        supporterCardLimit = false;
    }


    //to check if the opponent's bench is full
    public void checkBench() {
        fullBench = true; // Assume full unless proven otherwise

        for (int i = 0; i < opponentBench.length; i++) {
            if (opponentBench[i] == null) {
                fullBench = false;
                break;
            }
        }
    }


    //hp resets for if the opponent knocks out my pokemon
    public void resetHp2(){
        if(activeSpot[0].getName().equals("Charmander")){
            activeSpot[0].setHp(70);
        }
        if(activeSpot[0].getName().equals("Larvesta")){
            activeSpot[0].setHp(70);
        }
        if(activeSpot[0].getName().equals("Litleo")){
            activeSpot[0].setHp(70);
        }
        if(activeSpot[0].getName().equals("Pansear")){
            activeSpot[0].setHp(60);
        }
        if(activeSpot[0].getName().equals("Ponyta")){
            activeSpot[0].setHp(40);
        }
    }

    //resets players activepokemon's energy
    public void resetEnergy2(){
        if(activeSpot[0].getType().equals("Pokemon")){
            activeSpot[0].setEnergyAttached(0);
        }
    }

    //assigning opponents deck
    public void assignDeck2() {

        for (int i = 0; i < deck2.length; i++) {
            deck2[i] = new Card("", ""); // Empty card initialization
        }

        // Assign 20 Pokémon cards
        for (int i = 0; i < 4; i++) {
            deck2[i] = new Charmander();
        }
        for (int i = 4; i < 8; i++) {
            deck2[i] = new Larvesta();
        }
        for (int i = 8; i < 12; i++) {
            deck2[i] = new Litleo();
        }
        for (int i = 12; i < 16; i++) {
            deck2[i] = new Pansear();
        }
        for (int i = 16; i < 20; i++) {
            deck2[i] = new Ponyta();
        }

        // Assign 20 Trainer cards
        for (int i = 20; i < 24; i++) {
            deck2[i] = new Card("Bill", "Trainer");
        }
        for (int i = 24; i < 28; i++) {
            deck2[i] = new Card("Giant cape", "Trainer");
        }
        for (int i = 28; i < 32; i++) {
            deck2[i] = new Card("Professor Oak", "Trainer");
        }
        for (int i = 32; i < 36; i++) {
            deck2[i] = new Card("Professor's research", "Trainer");
        }
        for (int i = 36; i < 40; i++) {
            deck2[i] = new Card("Potion", "Trainer");
        }

        for (int i = 40; i < 60; i++) {
            deck2[i] = new Card("Fire energy", "Energy");
        }
    }

    // Shuffle deck using Fisher-Yates algorithm
    public void shuffleDeck2() {
        Random rand = new Random();
        for (int i = deck2.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = deck2[i];
            deck2[i] = deck2[j];
            deck2[j] = temp;
        }
    }

    // Fill hand with 7 cards (ensures at least one Pokémon)
    public void fillHand2() {
        boolean hasPokemon2 = false;

        while (!hasPokemon2) {
            // Fill hand from deck
            for (int i = 0; i < 7; i++) {
                if (deckIndex2 < deck2.length) {
                    hand2[i] = deck2[deckIndex2++];
                } else {
                    break; // Exit if deck is empty
                }
            }

            // Check if hand contains at least one Pokémon
            for (int i = 0; i < 7; i++) {
                if (hand2[i] != null && hand2[i].getType().equals("Pokemon")) {
                    hasPokemon2 = true;
                    break;
                }
            }

            if (!hasPokemon2) {
                // Reset hand and shuffle deck if no Pokémon is found
                for (int i = 0; i < 7; i++) {
                    deck2[deckIndex2 - 1] = hand2[i]; // Return cards to the deck
                    hand2[i] = null; // Clear hand
                }
                shuffleDeck2(); // Shuffle and retry if no Pokémon drawn
            }
        }
    }

    // Fill the prize pile with 6 cards
    public void fillPrizePile2() {
        for (int i = 0; i < 6; i++) {
            if(deck2[i + deckIndex2] != null){
                prizePile2[i] = deck2[i + deckIndex2];
            }
        }
        deckIndex2 += 6;
    }

    //A method that replaces the opponents knocked out pokemon
    public void replaceDefeatedPokemon2() {
        for(int i = 0; i < opponentBench.length; i++){
            if(opponentBench[i] != null && opponentBench[i].getType().equals("Pokemon")){
                opponentActiveSpot[0] = opponentBench[i];
            }
        }
        if (opponentActiveSpot[0] != null){
            System.out.println("The opponent placed a " + opponentActiveSpot[0].getName() + " in the active spot");
        }else{
            System.out.println("the opponent is unable to play a pokemon in the active spot you win!");
            System.exit(0);
        }

    }

    //opponent draws prize card
    public void drawPrizeCard2() {
        for (int i = 0; i < prizePile2.length; i++) {
            if (prizePile2[i] != null) {
                // Find first empty slot in hand
                int emptySlot = -1;
                for (int j = 0; j < hand2.length; j++) {
                    if (hand2[j] == null) {
                        emptySlot = j;
                        break;
                    }
                }

                if (emptySlot != -1) {
                    hand2[emptySlot] = prizePile2[i]; // Add prize card to hand
                    prizePile2[i] = null; // Remove from prize pile

                    // Check if player has won
                    boolean prizesRemaining2 = false;
                    for (int t = 0; t < prizePile2.length; t++) {
                        if (prizePile2[t] != null) {
                            prizesRemaining2 = true;
                            break;
                        }
                    }

                    if (!prizesRemaining2) {
                        System.out.println("The opponent collected al their prize cards you lose");
                        System.exit(0);
                    }


                } else {
                    return;
                }
            }
        }
        System.out.println("No more prize cards left!");
    }

    // Opponent's turn logic
    public void opponentTurn() {
        System.out.println("\n=== OPPONENT'S TURN ===");

        // choose a random pokemon to play from the hand
        if(opponentActiveSpot[0] == null){
            for(int i = 0; i < hand2.length; i++){
                if(hand2[i] != null && hand2[i].getType().equals("Pokemon")){
                    opponentActiveSpot[0] = hand2[i];
                    hand2[i] = null;
                    break;
                }
            }
        }
        //adding pokemon from the hand to bench
        checkBench();
        if(!fullBench && benchIndex <= 4){//checks if there is room on the bench
            for(int i = 0; i < hand2.length; i++){
                if(hand2[i] != null && hand2[i].getType().equals("Pokemon")){
                    opponentBench[benchIndex] = hand2[i];//places pokemon from hand to bench
                    hand2[i] = null;//empties the spot from hand
                    benchIndex++;//increases the bench tracker
                }
            }
        }

        //prints out opponent's active pokemon's name and hp
        System.out.println("Opponent's active Pokémon: " + opponentActiveSpot[0].getName() +
                " (HP: " + opponentActiveSpot[0].getHp() + ")");

        //Opponent AI always attaches energy to active pokemon
        opponentActiveSpot[0].attachEnergy();



        //attack the player's active Pokémon if available
        if (activeSpot[0] != null && opponentActiveSpot[0] != null && opponentActiveSpot[0].getEnergyAttached() >= opponentActiveSpot[0].getAttack1Cost()) {
            int damage = opponentActiveSpot[0].getAttack1();
            int newHp = activeSpot[0].getHp() - damage;//calculates the pokemon's remaining hp

            //displays the opponent attacking your pokemon
            System.out.println("Opponent's " + opponentActiveSpot[0].getName() +
                    " attacks your " + activeSpot[0].getName() +
                    " for " + damage + " damage!");

            if (newHp <= 0) {
                //prints if opponent knocked out your pokemon
                System.out.println("Your " + activeSpot[0].getName() + " was knocked out!");
                resetHp2();//resets the hp for active spot
                resetEnergy2();//resets the energy for active spot
                activeSpot[0] = null;//empties the active spot
                drawPrizeCard2();//opponent draws a prize card
                replaceDefeatedPokemon();//checks if player can replace the knocked ot pokemon
            } else {
                activeSpot[0].setHp(newHp);//sets a new hp for player's pokemon
                //displays new hp of pokemon
                System.out.println("Your " + activeSpot[0].getName() + " has " +
                        newHp + " HP remaining.");
                }
            }

        System.out.println("Opponent's turn ends.\n");

    }


    public void playerTurn() {
        System.out.println("\n=== YOUR TURN ===");
        displayGameState();//displays opponent's active spot and bench. Also displays active spot and bench of the player
        drawCard(); // Draw a card at the beginning of the turn
        energyReset();//this reset allows the player to attach an energy
        supporterReset();// this reset allows the player to use a supporter card
        boolean turnOver = false;//tracks if the player's turn should end

        while (!turnOver) {
            //allows you to choose from 5 actions
            System.out.println("Choose an action from 1-5:");
            System.out.println("1: Choose a Pokémon to play.");
            System.out.println("2: Attach an Energy to a Pokémon.");
            System.out.println("3: Play a Trainer card.");
            System.out.println("4: Attack.");
            System.out.println("5: End turn.");

            int action = scan.nextInt();
            scan.nextLine();
            //uses cases for each situation https://stackoverflow.com/questions/57347001/java-switch-use-case
            switch (action) {
                case 1:
                    playPokemon();
                    break;
                case 2:
                    attachEnergy();
                    break;
                case 3:
                    playTrainerCard();
                    break;
                case 4:
                    if (opponentActiveSpot != null) {
                        attack();
                        turnOver = true;
                    } else {
                        System.out.println("No opponent Pokémon to attack.");
                    }
                    break;
                case 5:
                    System.out.println("Your turn has ended.");
                    turnOver = true;
                    break;
                default:
                    System.out.println("Invalid action. Choose again.");
            }
        }
    }

    public void playPokemon(){
        System.out.println("do you want a pokemon in the active spot or bench. 1:for active 2:for bench");
        int activeOrBench = scan.nextInt();//lets player choose to bench a pokemon or play to active spot

        //playing a pokemon in the active spot
        if (activeOrBench == 1) {
            System.out.println("Choose a Pokémon to play:");
            //displays every card in hand
            for (int i = 0; i < hand.length; i++) {
                if (hand[i] != null) {
                    System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
                }
            }
            int pokemonChoice = scan.nextInt() - 1;//player chooses pokemon to play
            activeSpot[0] = hand[pokemonChoice];//active spot is set to that pokemon
            System.out.println("this is your active pokemon:" + activeSpot[0].getName() + " Hp" + activeSpot[0].getHp());
            pokemonMaxHp = activeSpot[0].getHp();
            hand[pokemonChoice] = null;
        }

        //to bench a pokemon
        if (activeOrBench == 2) {
            System.out.println("Choose a Pokémon to bench:");
            for (int i = 0; i < hand.length; i++) {
                if (hand[i] != null && hand[i].getType().equals("Pokemon")) {
                    System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
                }
            }
            int benchChoice = scan.nextInt()-1;
            bench[benchCounter] = hand[benchChoice];
            System.out.println("This pokemon was placed on the bench:" + hand[benchChoice].getName());
            hand[benchChoice] = null;
            benchCounter ++;
        }
    }

    public void attachEnergy(){
        //makes sure only one energy is attached per turn
        if (attachedAnEnergy) {
            System.out.println("You can only attach one energy card per turn. You've already attached one energy this turn.");
            return;
        }

        System.out.println("choose a energy:");
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] != null) {
                System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
            }
        }
        int energyChoice = scan.nextInt()-1;

        if (!hand[energyChoice].getType().equals("Energy")) {
            System.out.println("You must choose an Energy card!");
            return;
        }


        System.out.println("would you like to attach the energy to to the bench or active spot 1:active 2:bench");
        int energyPlacement = scan.nextInt();

        //if Player wants energy on active pokemon
        if(energyPlacement == 1){
            if (activeSpot[0] != null) {
                activeSpot[0].attachEnergy();
                System.out.println(activeSpot[0].getName() + " has a " + hand[energyChoice].getName() + " attached to it ");
            }
            hand[energyChoice] = null;
            attachedAnEnergy = true;//sets this to true so a player cant attach another energy
        }

        //if Player wants energy on benched pokemon
        if(energyPlacement == 2){
            System.out.println("which pokemon from the bench do you want to attach an energy to?");
            for (int i = 0; i < bench.length; i++) {
                if (bench[i] != null) {
                    System.out.println((i + 1) + ". " + bench[i].getName() + " (" + bench[i].getType() + ")");
                }
            }
            int benchPokemon = scan.nextInt()-1;
            bench[benchPokemon].attachEnergy();
            System.out.println(bench[benchPokemon].getName() + " has a " + hand[energyChoice].getName() + " attached to it");
            hand[energyChoice] = null;
            attachedAnEnergy = true;
        }
    }

    public void playTrainerCard(){
        System.out.print("Enter the number of the Trainer card you want to play: ");
        // Display cards in hand
        for(int i = 0; i < hand.length; i++){
            if(hand[i] != null){
                System.out.println((i + 1) + ". " + hand[i].getName());
            }
        }
        int trainerChoice = scan.nextInt() - 1;

        // makes sure trainerChoice is valid
        if (trainerChoice < 0 || trainerChoice >= hand.length || hand[trainerChoice] == null) {
            System.out.println("Invalid choice. Please select a valid Trainer card.");
            return;
        }

        if (hand[trainerChoice] != null) {

            System.out.println("You played: " + hand[trainerChoice].getName());

            // Apply Trainer card effects
            if(hand[trainerChoice].getName().equals("Bill")){
                hand[trainerChoice] = null;// remove trainer card from the hand
                drawCard();
                drawCard();
                System.out.println("Bill's effect activated: You drew 2 cards. This is your new hand!");
                //displays hand
                for (int i = 0; i < hand.length; i++) {
                    if (hand[i] != null) {
                        System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
                    }
                }
            }
            else if(hand[trainerChoice] != null && hand[trainerChoice].getName().equals("Giant cape")){ //if it's giant cape
                System.out.println("choose which pokemon to attach the giant cape to 1:active 2:bench");
                int capeChoice = scan.nextInt();//lets player choose between active or bench

                if(capeChoice == 1){
                    hand[trainerChoice] = null;//remove trainer card from hand
                    activeSpot[0].setHp(activeSpot[0].getHp() + 20);//adds 20 hp
                    System.out.println(activeSpot[0].getName() + " has " + activeSpot[0].getHp() + "remaining");
                }
                if(capeChoice == 2){
                    for (int i = 0; i < bench.length; i++) {
                        System.out.println("Choose a bench pokemon to attach Giant cape to:");
                        //prints out bench
                        if (bench[i] != null) {
                            System.out.println((i + 1) + ". " + bench[i].getName() + " (" + bench[i].getType() + ")");
                        }
                        int capeToBench = scan.nextInt()-1;//lets player choose bench pokemon
                        hand[trainerChoice] = null;//remove trainer card from hand
                        bench[capeToBench].setHp(bench[capeToBench].getHp() + 20);//adds 20 hp
                        System.out.println(bench[capeToBench].getName() + " has " + bench[capeToBench].getHp() + "remaining");
                    }
                }
            }
            else if(hand[trainerChoice] != null && hand[trainerChoice].getName().equals("Potion")){//if it's potion
                if(activeSpot[0].getHp() < pokemonMaxHp){//checks if pokemon is damaged
                    //if the damage done to the pokemon is less than 20
                    if(pokemonMaxHp - activeSpot[0].getHp() < 20){
                        int healHp = pokemonMaxHp - activeSpot[0].getHp();//only heal the amount that is damaged
                        int newHp = activeSpot[0].getHp() + healHp;//sets hp = to hp healed from potion
                        activeSpot[0].setHp(newHp);
                        hand[trainerChoice] = null;
                        System.out.println(activeSpot[0].getName() + " has " + newHp + " remaining hp after potion");
                    }
                    //if it's greater than 20 we do not need to worry about potion changing the hp to more than what the card listed
                    if(pokemonMaxHp - activeSpot[0].getHp() >= 20){
                        int newHp = activeSpot[0].getHp() + 20;//adds 20 hp
                        activeSpot[0].setHp(newHp);
                        hand[trainerChoice] = null;
                        System.out.println(activeSpot[0].getName() + " has " + newHp + " remaining hp after potion");
                    }

                }else{
                    System.out.println(" you can't use potion because your pokemon's hp is full");
                }
            }
            //if it's professor oak or professor research
            else if(hand[trainerChoice] != null && hand[trainerChoice].getName().equals("Professor Oak") || hand[trainerChoice].getName().equals("Professor's research")){
                //makes sure only on supporter can be played per turn
                if(supporterCardLimit){
                    System.out.println("you reached the supporter card limit");
                }

                hand[trainerChoice] = null; //remove card from hand
                discardHand();
                //draws 7 cards
                for(int i = 0; i < 7; i++){
                    drawCard();
                }
                System.out.println("this is your new hand:");
                for (int i = 0; i < hand.length; i++) {
                    if (hand[i] != null) {
                        System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
                    }
                }
                System.out.println("you cannot play anymore supporter cards");
                supporterCardLimit = true;//sets it to true after a supporter card has been played
            }
        } else {
            System.out.println("Invalid choice. Please select a valid Trainer card.");
        }
    }

    public void attack(){
        //checks if the opponent has a pokemon in the active spot to attack
        if (opponentActiveSpot[0] == null) {
            System.out.println("No opponent Pokémon to attack!");
            return;
        }

        System.out.println("choose an attack. 1:attack1 2:attack2");
        int attChoice = scan.nextInt();

        //attack1
        if(attChoice == 1 && activeSpot[0].getEnergyAttached() >= activeSpot[0].getAttack1Cost()){
            int dmg1 = opponentActiveSpot[0].getHp() - activeSpot[0].getAttack1();//dmg calc using opponent's pokemon - my pokemon's attack
            opponentActiveSpot[0].setHp(dmg1);//sets ne hp
            //if opponents pokemon was knocked out
            if(opponentActiveSpot[0].getHp() <= 0){
                System.out.println("Opponents pokemon knocked out");
                resetHp();
                resetEnergy();
                opponentActiveSpot[0] = null;
                drawPrizeCard();
                replaceDefeatedPokemon2();
            }else{
                //displays your pokemon attacking opponents pokemon
                System.out.println("Your " + activeSpot[0].getName() + " attacks opponent's " +
                        opponentActiveSpot[0].getName() + " has " + dmg1 + " hp remaining!");
            }

        }

        //attack 2
        if(attChoice == 2 && activeSpot[0].getEnergyAttached() >= activeSpot[0].getAttack2Cost()){
            int dmg2 = opponentActiveSpot[0].getHp() - activeSpot[0].getAttack2();
            opponentActiveSpot[0].setHp(dmg2);
            if(dmg2 <= 0){
                System.out.println("Opponents pokemon knocked out");
                resetHp();
                resetEnergy();
                opponentActiveSpot[0] = null;
                drawPrizeCard();
                replaceDefeatedPokemon2();
            }else{
                System.out.println("Your " + activeSpot[0].getName() + " attacks opponent's " +
                        opponentActiveSpot[0].getName() + " has " + dmg2 + " hp remaining!");
            }

        }

    }

    // Display the current game state
    public void displayGameState() {
        System.out.println("\n=== GAME STATE ===");

        // Show active Pokémon
        System.out.println("YOUR ACTIVE POKÉMON: " +
                (activeSpot[0] != null ? activeSpot[0].getName() + " (HP: " +
                        activeSpot[0].getHp() + ")" : "None"));

        // Show bench
        System.out.println("YOUR BENCH:");
        boolean hasBenchedPokemon = false;
        for (int i = 0; i < bench.length; i++) {
            if (bench[i] != null) {
                System.out.println((i + 1) + ". " + bench[i].getName() + " (HP: " +
                        bench[i].getHp() + ")");
                hasBenchedPokemon = true;
            }
        }
        if (!hasBenchedPokemon) {
            System.out.println("Empty");
        }

        // Show opponent's active Pokémon
        System.out.println("\nOPPONENT'S ACTIVE POKÉMON: " +
                (opponentActiveSpot[0] != null ? opponentActiveSpot[0].getName() +
                        " (HP: " + opponentActiveSpot[0].getHp() + ")" : "None"));

        // Show opponent's bench
        System.out.println("OPPONENT'S BENCH:");
        boolean opponentHasBenchedPokemon = false;
        for (int i = 0; i < opponentBench.length; i++) {
            if (opponentBench[i] != null) {
                System.out.println((i + 1) + ". " + opponentBench[i].getName());
                opponentHasBenchedPokemon = true;
            }
        }
        if (!opponentHasBenchedPokemon) {
            System.out.println("Empty");
        }

        // Show hand
        System.out.println("\nYOUR HAND:");
        boolean hasCards = false;
        for (int i = 0; i < hand.length; i++) {
            if (hand[i] != null) {
                System.out.println((i + 1) + ". " + hand[i].getName() + " (" + hand[i].getType() + ")");
                hasCards = true;
            }
        }
        if (!hasCards) {
            System.out.println("Empty");
        }

        // Show prize cards remaining
        int prizesRemaining = 0;
        for (Card prize : prizePile) {
            if (prize != null) {
                prizesRemaining++;
            }
        }
        System.out.println("\nPRIZE CARDS REMAINING: " + prizesRemaining);
    }

    //loops between player and opponent turns
    public void battle(){
        while(activeSpot[0] != null && opponentActiveSpot[0] != null){
            playerTurn();
            opponentTurn();
        }

        if(opponentActiveSpot[0] == null){
            System.out.println("congratulations you win");
        }
        if(activeSpot[0] == null){
            System.out.println("you lose");
        }
    }
    // Set up the game
    public void gameSetup () {
        //set up player
        assignDeck();
        shuffleDeck();
        fillHand();
        fillPrizePile();

        //setup opponent
        assignDeck2();
        shuffleDeck2();
        fillHand2();
        fillPrizePile2();

        opponentTurn();
        playerTurn();
    }
}