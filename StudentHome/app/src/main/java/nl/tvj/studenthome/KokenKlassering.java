package nl.tvj.studenthome;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kevin on 15-10-2015.
 */
public class KokenKlassering implements IKlassering {

    ArrayList<Gebruiker> deelnemers;
    Gebruiker cookOffWinnaar;
    Date cookOfDatum;

    public KokenKlassering() {
        this.deelnemers = new ArrayList<>();
    }

    @Override
    public ArrayList<Gebruiker> getDeelnemers() {
        return deelnemers;
    }

    @Override
    public Gebruiker getBesteDeelnemer() {
        return null;
    }

    @Override
    public Gebruiker getSlechsteDeelnemer() {
        return null;
    }

    @Override
    public void addDeelnemer() {

    }
    public Gebruiker getCookOffWinnaar(){
        return cookOffWinnaar;
    }
    public void setCookOffWinnaar(Gebruiker cookOffWinnaar){
        this.cookOffWinnaar = cookOffWinnaar;
    }
    public Date getCookOfDatum(){
        return getCookOfDatum();
    }
    public void setCookOfDatum(Date date){
        this.cookOfDatum = date;
    }
}
