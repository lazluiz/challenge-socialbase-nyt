package io.lazluiz.challengesocialbasenyt.screen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.data.FetchAPIData;
import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static String[] SECTIONS = {
            "arts",
            "science",
            "sports",
            "technology"
    };
    private final static int SECTION_ARTS = 0;
    private final static int SECTION_SCIENCE = 1;
    private final static int SECTION_SPORTS = 2;
    private final static int SECTION_TECHNOLOGY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch NYT API Data to Realm database
        FetchAPIData fetchAPIData = new FetchAPIData(this, SECTIONS);
        fetchAPIData.fetchData();

        // Check if everything's ok
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NYTArticle> nytArticles = realm.where(NYTArticle.class).findAll();

        for (NYTArticle article : nytArticles) {
            Log.i(LOG_TAG, article.getSection() + " :: " + article.getTitle());
        }

        Button btnSectionArts = (Button) findViewById(R.id.btnSectionArts);
        btnSectionArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticlesActivity(SECTION_ARTS);
            }
        });

        Button btnSectionScience = (Button) findViewById(R.id.btnSectionScience);
        btnSectionScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticlesActivity(SECTION_SCIENCE);
            }
        });

        Button btnSectionSports = (Button) findViewById(R.id.btnSectionSports);
        btnSectionSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticlesActivity(SECTION_SPORTS);
            }
        });

        Button btnSectionTechnology = (Button) findViewById(R.id.btnSectionTechnology);
        btnSectionTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticlesActivity(SECTION_TECHNOLOGY);
            }
        });
    }

    private void openArticlesActivity(int section){
//        Intent it = new Intent(MainActivity.this, ArticleActivity.class);
//        it.putExtra(ArticleActivity.EXTRA_SECTION, SECTIONS[section]);
//        startActivity(it);
    }
}
