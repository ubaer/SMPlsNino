package nl.tvj.studenthome;

/**
 * Created by Kevin on 15-10-2015.
 */
public class Beoordeling {
    Gebruiker beoordeeldDoor;
    double beoordeling;

    public Beoordeling(Gebruiker beoordeeldDoor, double beoordeling) {
        this.beoordeeldDoor = beoordeeldDoor;
        this.beoordeling = beoordeling;
    }
    public double getBeoordeling()
    {
        return beoordeling;
    }
}
