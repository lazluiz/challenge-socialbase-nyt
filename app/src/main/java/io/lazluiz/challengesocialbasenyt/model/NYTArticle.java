package io.lazluiz.challengesocialbasenyt.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */

public class NYTArticle extends RealmObject {

    @PrimaryKey
    private long id;
    private int views;
    private String title;
    private String byline;
    private String section;
    private String url;
    private String adx_keywords;
    private String published_date;
    private String abstract_str;
    private RealmList<NYTMedia> media;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdx_keywords() {
        return adx_keywords;
    }

    public void setAdx_keywords(String adx_keywords) {
        this.adx_keywords = adx_keywords;
    }

    public String getPublished_date() {
        return published_date;
    }

    public void setPublished_date(String published_date) {
        this.published_date = published_date;
    }

    public RealmList<NYTMedia> getMedia() {
        return media;
    }

    public void setMedia(RealmList<NYTMedia> media) {
        this.media = media;
    }

    public String getAbstract_str() {
        return abstract_str;
    }

    public void setAbstract_str(String abstract_str) {
        this.abstract_str = abstract_str;
    }
}
