package nl.tvj.studenthome;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kevin on 15-10-2015.
 */
public abstract class Activiteit {
    int id;
    double totaalbedrag;
    String omschrijving;
    Gebruiker host;
    ArrayList<Gebruiker> deelnemers;
    ArrayList<Beoordeling> beoordelingen;
    Date starttijd;
    Database db;
    boolean iedereenGestemd;
    ArrayList<String> votingOptions;


    Activiteit(int id, double totaalbedrag, String omschrijving, Gebruiker host, Date starttijd) {
        this.id = id;
        this.totaalbedrag = totaalbedrag;
        this.omschrijving = omschrijving;
        this.host = host;
        this.starttijd = starttijd;
        deelnemers = new ArrayList<>();
        beoordelingen = new ArrayList<>();
        db = new Database();
        iedereenGestemd = false;
        votingOptions = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public boolean getIedereenGestemd() {
        return this.iedereenGestemd;
    }

    public void setIedereenGestemd(boolean iedereenGestemd) {
        this.iedereenGestemd = iedereenGestemd;
    }

    public boolean addDeelnemer(Gebruiker deelnemer) {
        try {
            return db.addDeelnemer(deelnemer,this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void setTotaalbedrag(double totaalbedrag) {
        this.totaalbedrag = totaalbedrag;
    }

    public boolean addBeoordeling(Beoordeling beoordeling) throws SQLException {
        if (db.addBeoordeling(this, beoordeling)) {
            beoordelingen.add(beoordeling);
            return true;
        }

        return true;
    }

    public boolean addVotingOption(String option) {
        try {
            if (db.addVotingOption(this, option)) {
                this.votingOptions.add(option);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public ArrayList<Gebruiker> getDeelnemers() {
        return this.deelnemers;
    }

    @Override
    public String toString(){
        return omschrijving + " " + starttijd.toString();
    }
}
