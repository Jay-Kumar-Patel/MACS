package Dao;

public class Book {

    private String bookTitle;
    private String authorName;
    private int publishedYear;
    private String publisherName;
    private String authorEmail;
    private String publisherEmail;
    private String publisherAddress;

    public Book(String bookTitle, String authorName, int publishedYear, String publisherName, String authorEmail, String publisherEmail, String publisherAddress) {
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.publishedYear = publishedYear;
        this.publisherName = publisherName;
        this.authorEmail = authorEmail;
        this.publisherEmail = publisherEmail;
        this.publisherAddress = publisherAddress;
    }

    public Book(){}

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getPublisherEmail() {
        return publisherEmail;
    }

    public void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    public String getPublisherAddress() {
        return publisherAddress;
    }

    public void setPublisherAddress(String publisherAddress) {
        this.publisherAddress = publisherAddress;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookTitle='" + bookTitle + '\'' +
                ", authorName='" + authorName + '\'' +
                ", publishedYear=" + publishedYear +
                ", publisherName='" + publisherName + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", publisherEmail='" + publisherEmail + '\'' +
                ", publisherAddress='" + publisherAddress + '\'' +
                '}';
    }
}
