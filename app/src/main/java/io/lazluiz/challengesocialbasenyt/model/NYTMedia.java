package io.lazluiz.challengesocialbasenyt.model;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by luiz on 06/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */

public class NYTMedia extends RealmObject{

    private String caption;
    private String copyright;
    private RealmList<NYTMediaMetadata> media_metadata;


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public RealmList<NYTMediaMetadata> getMedia_metadata() {
        return media_metadata;
    }

    public void setMedia_metadata(RealmList<NYTMediaMetadata> media_metadata) {
        this.media_metadata = media_metadata;
    }
}
