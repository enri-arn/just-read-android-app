package it.unito.di.justread.model;

/**
 * @date 18-ott-2017
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class Category {

    private int id;
    private String name;
    private String icon;

    public Category() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}
