package nl.tvj.studenthome;

import java.util.ArrayList;

/**
 * Created by Kevin on 15-10-2015.
 */
public interface IKlassering {
     ArrayList<Gebruiker> deelnemers = null;

    public ArrayList<Gebruiker> getDeelnemers();
    public Gebruiker getBesteDeelnemer();
    public Gebruiker getSlechsteDeelnemer();
    public void addDeelnemer();
}
