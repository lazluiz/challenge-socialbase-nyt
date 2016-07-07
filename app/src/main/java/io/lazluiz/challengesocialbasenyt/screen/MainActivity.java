package io.lazluiz.challengesocialbasenyt.screen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.data.FetchAPIData;
import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fetch NYT API Data to Realm database
        FetchAPIData fetchAPIData = new FetchAPIData(this);
        fetchAPIData.fetchData();

        // Check if everything's ok
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NYTArticle> nytArticles = realm.where(NYTArticle.class).findAll();

        TextView tvTest = (TextView) findViewById(R.id.tvTest);
        for (NYTArticle article : nytArticles) {
            tvTest.append(article.getTitle() + "\n");
        }
    }
}
