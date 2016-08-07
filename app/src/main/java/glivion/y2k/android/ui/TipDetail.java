package glivion.y2k.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import glivion.y2k.R;

/**
 * Created by blanka on 8/6/2016.
 */
public class TipDetail extends AppCompatActivity {

    WebView webView;
    Toolbar toolbar;
    String link, title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_detail);

        webView = (WebView) findViewById(R.id.webview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView.setWebViewClient(new MywebView());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        desc = getIntent().getStringExtra("desc");
        title = getIntent().getStringExtra("title");
        toolbar.setTitle(title);
        getSupportActionBar().setTitle(title);

        link = getIntent().getStringExtra("link");
        Log.e("Link", link);
        if (link != null) {
            open();
        }

    }


    public void open() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(link);

    }

    private class MywebView extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        switch (item.getItemId()) {

            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.shareart: {
                String text = "Title: " + title + "\n\n " + desc + "\n\n" + link + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t Powered by Y2K";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(share, "Share Via"));
                break;
            }
            case R.id.viewlink: {
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
