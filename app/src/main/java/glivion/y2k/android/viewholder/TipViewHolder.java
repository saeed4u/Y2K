package glivion.y2k.android.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;

import glivion.y2k.R;
import glivion.y2k.android.model.NewsItem;

/**
 * Created by blanka on 8/6/2016.
 */
public class TipViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitle;
    private TextView mDate;
    private TextView mMessage;
    private View mShare;

    public TipViewHolder(View itemView) {
        super(itemView);
        mTitle = (TextView) itemView.findViewById(R.id.news_title);
        mDate = (TextView) itemView.findViewById(R.id.news_date);
        mMessage = (TextView) itemView.findViewById(R.id.news_msg);
        mShare = itemView.findViewById(R.id.share);

    }

    public void bind(final NewsItem newsItem) {
        final String title = Jsoup.parse(newsItem.getNews_title()).text();
        mTitle.setText(Jsoup.parse(newsItem.getNews_title()).text());
        mDate.setText(newsItem.getNews_date());
        final String message = Jsoup.parse(newsItem.getNews_desc()).text();
        mMessage.setText(message);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "Title: " + title + "\n\n " + message + "\n\n" + newsItem.getNews_url() + "\n\n\t\t\t\t\t\t Powered by Y2K";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, text);
                v.getContext().startActivity(Intent.createChooser(share, "Share Via"));
            }
        });
    }
}
