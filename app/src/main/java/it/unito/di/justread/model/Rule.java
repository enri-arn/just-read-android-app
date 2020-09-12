package it.unito.di.justread.model;

/**
 * @date 18-ott-2017
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class Rule {

    private int id;
    private int borrowDays;
    private String address;
    private String phoneNumber;
    private String name;
    private String buisnessHour;
    private int maxRenewal;
    private int lowerRenewal;

    public Rule() {
    }

    public int getId() {
        return id;
    }

    public int getBorrowDays() {
        return borrowDays;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getBuisnessHour() {
        return buisnessHour;
    }

    public int getMaxRenewal() {
        return maxRenewal;
    }

    public int getLowerRenewal() {
        return lowerRenewal;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBorrowDays(int borrowDays) {
        this.borrowDays = borrowDays;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBuisnessHour(String buisnessHour) {
        this.buisnessHour = buisnessHour;
    }

    public void setMaxRenewal(int maxRenewal) {
        this.maxRenewal = maxRenewal;
    }

    public void setLowerRenewal(int lowerRenewal) {
        this.lowerRenewal = lowerRenewal;
    }

}
