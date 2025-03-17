public class Card {
    public static final String POKEMON = "Pokemon";
    public static final String ENERGY = "Energy";
    public static final String TRAINER = "Trainer";

    private final String name;
    private final String type;

    // Pokémon-specific attributes
    private int hp;
    private int attack1;
    private int attack2;
    private int attack1Cost;
    private int attack2Cost;
    private int energyAttached;
    // Constructor for general cards (Energy, Trainer)
    public Card(String name, String type) {
        this.name = name;
        this.type = type;
    }

    // Constructor for Pokémon cards, including Pokémon-specific stats
    public Card(String name, int hp, int attack1, int attack2, int attack1Cost, int attack2Cost) {
        this.name = name;
        this.type = POKEMON;
        this.hp = hp;
        this.attack1 = attack1;
        this.attack2 = attack2;
        this.attack1Cost = attack1Cost;
        this.attack2Cost = attack2Cost;
    }

    // Getters for common attributes
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    // Pokémon-specific methods
    public int getHp() {
        return hp;
    }

    public int getAttack1() {
       return attack1;
    }

    public int getAttack2() {
        return attack2;
    }
    public int getAttack1Cost() {
        return attack1Cost;
    }
    public int getAttack2Cost() {
        return attack2Cost;
    }
    public int getEnergyAttached(){
        return energyAttached;
    }

    // Setters for Pokémon stats
    public void setHp(int hp) {
        if (this.type.equals(POKEMON)) {
            this.hp = hp;
        }
    }

    public void setEnergyAttached(int energyAttached) {
        this.energyAttached = energyAttached;
    }


    //energy methods
    public void attachEnergy(){
        energyAttached++;
    }

}
