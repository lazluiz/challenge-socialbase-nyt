package io.lazluiz.challengesocialbasenyt.screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.data.FetchAPIData;

public class MainActivity extends AppCompatActivity {

    private final static String[] SECTIONS = {
            "Arts",
            "Science",
            "Sports",
            "Technology"
    };
    private final static int SECTION_ARTS = 0;
    private final static int SECTION_SCIENCE = 1;
    private final static int SECTION_SPORTS = 2;
    private final static int SECTION_TECHNOLOGY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnSectionArts = (Button) findViewById(R.id.btnSectionArts);
        btnSectionArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticleActivity(SECTION_ARTS);
            }
        });

        Button btnSectionScience = (Button) findViewById(R.id.btnSectionScience);
        btnSectionScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticleActivity(SECTION_SCIENCE);
            }
        });

        Button btnSectionSports = (Button) findViewById(R.id.btnSectionSports);
        btnSectionSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticleActivity(SECTION_SPORTS);
            }
        });

        Button btnSectionTechnology = (Button) findViewById(R.id.btnSectionTechnology);
        btnSectionTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openArticleActivity(SECTION_TECHNOLOGY);
            }
        });

        // Fetch NYT API Data to Realm database
        FetchAPIData fetchAPIData = new FetchAPIData(this, SECTIONS);
        fetchAPIData.fetchData();
    }

    private void openArticleActivity(int section){
        Intent it = new Intent(MainActivity.this, ArticleActivity.class);
        it.putExtra(ArticleActivity.EXTRA_SECTION, SECTIONS[section]);
        startActivity(it);
    }
}
