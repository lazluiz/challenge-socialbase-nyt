package io.lazluiz.challengesocialbasenyt.screen.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.lazluiz.challengesocialbasenyt.R;
import io.lazluiz.challengesocialbasenyt.model.NYTArticle;
import io.lazluiz.challengesocialbasenyt.model.NYTMediaMetadata;

/**
 * Created by luiz on 07/07/16.
 * E-mail: lf.lazzarin@gmail.com
 * GitHub: github.com/luizfelippe
 */


public class ArticleAdapter extends AbstractRecyclerAdapter<NYTArticle, ArticleAdapter.ViewHolder> {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public ArticleAdapter(Context c) {
        mContext = c;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(mInflater
                .inflate(R.layout.list_item_articles, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mRoot;
        private final TextView mTextTitle;
        private final TextView mTextByLine;
        private final TextView mTextDate;
        private final ImageView mImageMain;

        private NYTArticle mArticle;

        public ViewHolder(View v) {
            super(v);
            mRoot = v;
            mTextTitle = (TextView) v.findViewById(R.id.item_article_title);
            mTextByLine = (TextView) v.findViewById(R.id.item_article_byline);
            mTextDate = (TextView) v.findViewById(R.id.item_article_date);
            mImageMain = (ImageView) v.findViewById(R.id.item_article_picture);

            mRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mArticle);
                    }
                }
            });
        }

        public void bind(NYTArticle data) {
            mArticle = data;
            mTextTitle.setText(data.getTitle());
            mTextByLine.setText(data.getByline());
            mTextDate.setText(formatDate(data.getPublished_date()));
            mImageMain.setContentDescription(data.getTitle());

            if (data.getMedia().size() > 0) {
                NYTMediaMetadata image = data.getMedia().get(0).getMedia_metadata().where().equalTo("format", "mediumThreeByTwo440").findFirst();
                Glide.with(mContext).load(image.getUrl()).into(mImageMain);
            }

        }
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date formattedDate = format.parse(date);

            return DateUtils.getRelativeDateTimeString(mContext,
                    formattedDate.getTime(),
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.WEEK_IN_MILLIS, 0
            ).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface OnItemClickListener {
        void onItemClick(NYTArticle data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}