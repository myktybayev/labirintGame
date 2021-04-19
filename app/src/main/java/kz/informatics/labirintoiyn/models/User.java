package kz.informatics.labirintoiyn.models;

public class User {
    String name;
    String level;
    String rating;

    public User(String name, String level, String rating) {
        this.name = name;
        this.level = level;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
