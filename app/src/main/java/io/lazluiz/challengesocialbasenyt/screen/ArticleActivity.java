package io.lazluiz.challengesocialbasenyt.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.lazluiz.challengesocialbasenyt.screen.adapter.ArticleAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ArticleActivity extends AppCompatActivity {

    private static String LOG_TAG = ArticleActivity.class.getSimpleName();
    public static String EXTRA_SECTION = LOG_TAG.concat(".ExtraSection");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(getLayoutManager());
        recycler.setAdapter(getAdapter());
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    // Recycler Layout Manager
    private RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    // Recycler Adapter
    private RecyclerView.Adapter getAdapter() {
        final String FIELD_SECTION = "section";
        String extraSection = getIntent().getStringExtra(EXTRA_SECTION);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<NYTArticle> articles = realm.where(NYTArticle.class)
                .equalTo(FIELD_SECTION, extraSection)
                .findAllSorted("published_date", Sort.DESCENDING);

        ArticleAdapter articleAdapter = new ArticleAdapter(this);
        articleAdapter.setData(articles);
        articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NYTArticle data) {
//                Intent it = new Intent(ArticleActivity.this, ArticleDetailsActivity.class);
//                startActivity(it);
            }
        });
        return articleAdapter;
    }

    // Recycler Item Decoration
    class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context c) {
            mDivider = ContextCompat.getDrawable(c, R.drawable.shape_line_divider);
        }

        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
