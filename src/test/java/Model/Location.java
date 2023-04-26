package Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Location {
    String postacode;
    String country;
    String countryabbreviation;
    ArrayList<Place> places;

    public String getPostacode() {
        return postacode;
    }

    @JsonProperty("post code")
    public void setPostacode(String postacode) {
        this.postacode = postacode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryabbreviation() {
        return countryabbreviation;
    }

    @JsonProperty("country abbreviation")
    public void setCountryabbreviation(String countryabbreviation) {
        this.countryabbreviation = countryabbreviation;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

}
