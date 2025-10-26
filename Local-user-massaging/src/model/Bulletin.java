package model;

public class Bulletin {
    private String poster;
    private String content;

    public Bulletin(String poster, String content) {
        this.poster = poster;
        this.content = content;
    }

    public String getPoster() { return poster; }
    public String getContent() { return content; }
}
