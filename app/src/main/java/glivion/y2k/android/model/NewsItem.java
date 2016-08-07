package glivion.y2k.android.model;

/**
 * Created by blanka on 8/6/2016.
 */
public class NewsItem {

    private int news_id = 0;
    private String news_title = "";
    private String news_desc = "";
    private String date = "";
    private String news_url = "";

    public NewsItem(String news_title, String news_desc,String date, String news_url) {
        this.news_title = news_title;
        this.news_desc = news_desc;
        this.date = date;
        this.news_url = news_url;
    }

    public NewsItem() {
    }

    public String getNews_date() {
        return date;
    }

    public void setNews_date(String date) {
        this.date = date;
    }


    public int getNews_id() {
        return news_id;
    }

    public void setNews_id(int news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_desc() {
        return news_desc;
    }

    public void setNews_desc(String news_desc) {
        this.news_desc = news_desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNews_url() {
        return news_url;
    }

    public void setNews_url(String news_url) {
        this.news_url = news_url;
    }

}
