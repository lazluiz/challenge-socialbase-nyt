package io.lazluiz.challengesocialbasenyt.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.lazluiz.challengesocialbasenyt.model.NYTMedia;
import io.lazluiz.challengesocialbasenyt.model.NYTMediaMetadata;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ArticleDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = ArticleDetailsActivity.class.getSimpleName();
    public static final String EXTRA_ARTICLE_ID = LOG_TAG.concat(".ExtraArticle");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadData();
    }

    private void loadData(){
        final String FIELD_ID = "id";
        long articleId = getIntent().getLongExtra(EXTRA_ARTICLE_ID, 0);

        Realm realm = Realm.getDefaultInstance();
        NYTArticle article = realm.where(NYTArticle.class)
                .equalTo(FIELD_ID, articleId)
                .findFirst();

        if(article != null){
            TextView txtTitle = (TextView) findViewById(R.id.detail_title);
            TextView txtAbstract = (TextView) findViewById(R.id.detail_abstract);
            TextView txtByLine = (TextView) findViewById(R.id.detail_byline);
            TextView txtViews = (TextView) findViewById(R.id.detail_views);
            TextView txtUrl = (TextView) findViewById(R.id.detail_url);

            txtTitle.setText(article.getTitle());
            txtAbstract.setText(article.getAbstract_str());
            txtByLine.setText(article.getByline());
            txtViews.setText(String.valueOf(article.getViews()));
            txtUrl.setText(article.getUrl());

            if(article.getMedia().size() > 0){
                ImageView imgPicture = (ImageView)findViewById(R.id.detail_picture);
                TextView txtMediaCaption = (TextView) findViewById(R.id.detail_media_caption);
                TextView txtMediaCopyright = (TextView) findViewById(R.id.detail_media_copyright);

                NYTMedia media =  article.getMedia().get(0);
                txtMediaCaption.setText(media.getCaption());
                txtMediaCopyright.setText(media.getCopyright());

                NYTMediaMetadata metadata = media.getMedia_metadata()
                        .where()
                        .equalTo("format", "mediumThreeByTwo210")
                        .or()
                        .equalTo("format", "mediumThreeByTwo440").findFirst();
                if(metadata != null)
                    Glide.with(ArticleDetailsActivity.this).load(metadata.getUrl()).into(imgPicture);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
