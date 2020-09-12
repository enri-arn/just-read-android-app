package it.unito.di.justread.model;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BookInfoData {

    private String isbn;
    private String publisher;
    private String language;
    private String title;
    private String plot;
    private String cover;
    private String coverHD;
    private int pages;
    private String yearsOfPublication;
    private boolean isAvaiable;
    private String author;
    private float rate;
    private int numVoters;
    private int fiveStar;
    private int fourStar;
    private int threeStar;
    private int twoStar;
    private int oneStar;

    public BookInfoData() {
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCoverHD() {
        return coverHD;
    }

    public void setCoverHD(String coverHD) {
        this.coverHD = coverHD;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getYearsOfPublication() {
        return yearsOfPublication;
    }

    public void setYearsOfPublication(String yearsOfPublication) {
        this.yearsOfPublication = yearsOfPublication;
    }

    public boolean isIsAvaiable() {
        return isAvaiable;
    }

    public void setIsAvaiable(boolean isAvaiable) {
        this.isAvaiable = isAvaiable;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getNumVoters() {
        return numVoters;
    }

    public void setNumVoters(int numVoters) {
        this.numVoters = numVoters;
    }

    public int getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(int fiveStar) {
        this.fiveStar = fiveStar;
    }

    public int getFourStar() {
        return fourStar;
    }

    public void setFourStar(int fourStar) {
        this.fourStar = fourStar;
    }

    public int getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(int threeStar) {
        this.threeStar = threeStar;
    }

    public int getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(int twoStar) {
        this.twoStar = twoStar;
    }

    public int getOneStar() {
        return oneStar;
    }

    public void setOneStar(int oneStar) {
        this.oneStar = oneStar;
    }

    @Override
    public String toString() {
        return "BookInfoData{" +
                "isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", language='" + language + '\'' +
                ", title='" + title + '\'' +
                ", plot='" + plot + '\'' +
                ", cover='" + cover + '\'' +
                ", coverHD='" + coverHD + '\'' +
                ", pages=" + pages +
                ", yearsOfPublication='" + yearsOfPublication + '\'' +
                ", isAvaiable=" + isAvaiable +
                ", author='" + author + '\'' +
                ", rate=" + rate +
                ", numVoters=" + numVoters +
                ", fiveStar=" + fiveStar +
                ", fourStar=" + fourStar +
                ", threeStar=" + threeStar +
                ", twoStar=" + twoStar +
                ", oneStar=" + oneStar +
                '}';
    }
}
