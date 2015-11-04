package nl.tvj.studenthome;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kevin on 15-10-2015.
 */
public class Studentenhuis {
    int id;
    ArrayList<Gebruiker>huisBewoners;
    Gebruiker huisBaas;
    ArrayList<IKlassering>klasseringen;
    ArrayList<Activiteit>activiteiten;
    String adres;
    String verstigingsplaats;
    String postcode;
    String naam;
    double longitude;
    double latitude;
    Database db;

    public Studentenhuis(int id, String adres, String verstigingsplaats, String postcode, String naam, Gebruiker huisBaas) {
        this.id = id;
        this.huisBaas = huisBaas;
        this.adres = adres;
        this.verstigingsplaats = verstigingsplaats;
        this.postcode = postcode;
        this.naam = naam;
        this.huisBewoners = new ArrayList<>();
        this.klasseringen = new ArrayList<>();
        this.activiteiten = new ArrayList<>();
        db = new Database();
    }
    public Studentenhuis(int id, String adres, String verstigingsplaats, String postcode, String naam) {
        this.id = id;
        this.huisBaas = null;
        this.adres = adres;
        this.verstigingsplaats = verstigingsplaats;
        this.postcode = postcode;
        this.naam = naam;
        this.huisBewoners = new ArrayList<>();
        this.klasseringen = new ArrayList<>();
        this.activiteiten = new ArrayList<>();
        db = new Database();
    }
    public String getNaam(){
        return naam;
    }

    public void setNaam(String naam){
        this.naam = naam;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public boolean addBewoner(Gebruiker bewoner){
        //  Kijkt of de bewoner nog niet in de lijst staat
        boolean excists = false;

        for (Gebruiker g : huisBewoners) {
            if (g == bewoner) {
                excists = true;
            }
        }

        if(!excists) {
            try {
                if (db.addHuisbewoner(this, bewoner)) {
                    huisBewoners.add(bewoner);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
        else {
            return false;
        }
    }

    //TODO invullen
    public boolean addActiviteit(Activiteit activiteit, boolean tochDoorgaan){
        if (!overlappenActiviteiten(activiteit)) {
            if (tochDoorgaan && (!(activiteit instanceof Avondeten))) {
                try {
                    if (db.addActiviteit(this, activiteit)) {
                        activiteiten.add(activiteit);
                        return true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //TODO invullen
    public boolean overlappenActiviteiten(Activiteit activiteit){
        //  Check voor elk type activiteit of er overlap is op dezelfde dag
        boolean overlap = false;
        for (Activiteit a : activiteiten) {
            if (a.getClass() == activiteit.getClass() && activiteit instanceof Avondeten) {
                if (a.starttijd.getTime() == activiteit.starttijd.getTime()) {
                    overlap = true;
                }
            }
            else if (a.getClass() == activiteit.getClass()){
                if (a.starttijd.getDate() == activiteit.starttijd.getDate()) {
                    overlap = true;
                }
            }
        }
        return overlap;
    }

    //kijkt of de gebruiker niet al in de activiteit zit en voegd hem vervolgens toe
    public boolean joinActiviteit(Gebruiker deelnemer, Activiteit activiteit){
        for(Activiteit a : activiteiten){
            if(a == activiteit){
                ArrayList<Gebruiker> deelnemers = a.getDeelnemers();
                boolean excists = false;
                for(Gebruiker gebruiker : deelnemers)
                {
                    if(deelnemer == gebruiker)
                        excists = true;
                }
                if(!excists) {
                    return activiteit.addDeelnemer(deelnemer);
                }
            }
        }
        return false;
    }

    //TODO invullen
    public boolean addBeoordeling(Activiteit activiteit, double Beoordeling, Gebruiker beoordeeldDoor){
        //  Kijkt of de gebruiker de activiteit al beoordeeld heeft
        boolean alBeoordeeld = false;
        for (Beoordeling b : activiteit.beoordelingen) {
            if (b.beoordeeldDoor.equals(beoordeeldDoor)) {
                alBeoordeeld = true;
            }
        }

        boolean returnValue = false;

        if (!alBeoordeeld) {
            try {
                returnValue = activiteit.addBeoordeling(new Beoordeling(beoordeeldDoor, Beoordeling));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //TODO vergelijken van cook-off resultaten
//            if (activiteit.getIedereenGestemd() && activiteit instanceof CookOff) {
//                //  Vergelijk beoordeling van andere CookOff
//                CookOff cookOff = (CookOff)activiteit;
//                if (cookOff.tegenstander.getIedereenGestemd()) {
//                    //
//                }
//            }

            return returnValue;
        }
        return returnValue;
    }

    //TODO invullen
    public boolean createCookOff(Gebruiker verdediger, Gebruiker uitdager, CookOff activiteitVerdediger, CookOff activiteitUitdager, boolean tochDoorgaan){
        //  Kijk of de CookOff-activiteiten niet op dezelfde dag worden uitgevoerd
        if (activiteitVerdediger.starttijd.getDate() == activiteitUitdager.starttijd.getDate()) {
            return false;
        }

        //  Kijk of de verdediger en uitdager niet dezelfde Gebruiker zijn
        if (verdediger.equals(uitdager)) {
            return false;
        }

        if (!overlappenActiviteiten(activiteitUitdager) && !overlappenActiviteiten(activiteitVerdediger)) {
            try {
                if (db.addActiviteit(this, activiteitUitdager) && db.addActiviteit(this, activiteitVerdediger)) {
                    //TODO onderstaande DRIE regels code in de 'main activity' of de activity waar een cook-off wordt aangegaan plaatsen
    //                CookOff coUitdager = new CookOff(activiteitUitdager.getId(), activiteitUitdager.totaalbedrag, activiteitUitdager.omschrijving, activiteitUitdager.host, activiteitUitdager.starttijd, false, null);
    //                CookOff coVerdediger = new CookOff(activiteitVerdediger.getId(), activiteitVerdediger.totaalbedrag, activiteitVerdediger.omschrijving, activiteitVerdediger.host, activiteitVerdediger.starttijd, true, coUitdager);
    //                coUitdager.tegenstander = coVerdediger;

                    activiteiten.add(activiteitUitdager);
                    activiteiten.add(activiteitVerdediger);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public boolean addVotingOption(Activiteit activiteit, String option) {
        if (!option.isEmpty()) {
            return activiteit.addVotingOption(option);
        }
        return false;
    }
    public ArrayList<Gebruiker>wieIsThuis   ()
    {
        ArrayList<Gebruiker> gebruikers = null;
        try {
            gebruikers = db.getGebruikersInHuis(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruikers;
    }
    public int getId ()
    {
        return id;
    }
    public ArrayList<Gebruiker>getGebruikersAVGScore()
    {
        ArrayList<Gebruiker>gebruikersMetRating = new ArrayList<>();
        try {
          gebruikersMetRating = db.getGebruikersAVGRating();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gebruikersMetRating;
    }
}
