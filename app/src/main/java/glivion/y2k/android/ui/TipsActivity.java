package glivion.y2k.android.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.pnikosis.materialishprogress.ProgressWheel;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import glivion.y2k.R;
import glivion.y2k.android.adapter.TipAdapter;
import glivion.y2k.android.listener.RecyclerItemClickListener;
import glivion.y2k.android.model.NewsItem;

/**
 * Created by blanka on 8/6/2016.
 */
public class TipsActivity extends AppCompatActivity {

    static final String KEY_ITEM = "item"; // parent node
    static final String KEY_TITLE = "title";
    static final String KEY_DESC = "description";
    static final String KEY_DATE = "pubDate";
    static final String KEY_LINK = "link";

    private RecyclerView mTips;
    private ProgressWheel mLoader;
    private ArrayList<NewsItem> mNewsItems;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tips_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        mNewsItems = new ArrayList<>();
        mTips = (RecyclerView) findViewById(R.id.tips);
        mTips.setHasFixedSize(true);
        mTips.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(TipsActivity.this, TipDetail.class);
                intent.putExtra("link", mNewsItems.get(position).getNews_url());
                intent.putExtra("title", Jsoup.parse(mNewsItems.get(position).getNews_title()).text());
                intent.putExtra("desc", Jsoup.parse(mNewsItems.get(position).getNews_desc()).text());
                startActivity(intent);
            }
        }));
        mLoader = (ProgressWheel) findViewById(R.id.loader);
        mLoader.setProgress(2);
        new GetTips().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private class GetTips extends AsyncTask<Void, Integer, Void> {
        int progress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mLoader.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mLoader.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                java.net.URL url = new URL("http://feeds2.feedburner.com/moolanomy");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                int lengthOfFile = con.getContentLength();
                InputStream is = con.getInputStream();
                File testDirectory = new File(Environment.getExternalStorageDirectory() + "/Y2K");
                File fileProper = new File(testDirectory + "/y2k.xml");
                if (!testDirectory.exists()) {
                    testDirectory.mkdir();
                } else {
                    //getActivity().deleteFile(testDirectory+"/ebola.xml");
                    boolean result = fileProper.delete();
                }
                FileOutputStream fos = new FileOutputStream(testDirectory + "/y2k.xml");
                byte data[] = new byte[1024];
                int count;
                long total = 0;

                while ((count = is.read(data)) != -1) {
                    total += count;
                    int progress_temp = (int) total * 100 / lengthOfFile;
                    if (progress_temp % 10 == 0 && progress != progress_temp) {
                        progress = progress_temp;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onProgressUpdate(progress);
                        }
                    });
                    fos.write(data, 0, count);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            parseIntoList();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mLoader.setVisibility(View.GONE);
            mTips.setAdapter(new TipAdapter(mNewsItems));
            mTips.setVisibility(View.VISIBLE);
        }
    }


    private static String getNode(String tag, Element e) {
        NodeList nodeList = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        String nodeValue = node.getNodeValue();
        Log.v("Node Tag", tag);
        Log.v("Node Value", nodeValue);
        return nodeValue;
    }

    private void parseIntoList() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Y2K/y2k.xml");
        String URL = file.getAbsolutePath();
        //String xml = parser.getXmlFromUrl(URL); // getting XML
        Document doc;
        try {
            InputStream is = new FileInputStream(URL);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(is));

            NodeList nl = doc.getElementsByTagName(KEY_ITEM);

            for (int i = 0; i < nl.getLength(); i++) {

                Element e = (Element) nl.item(i);
                mNewsItems.add(new NewsItem(getNode(KEY_TITLE, e), getNode(KEY_DESC, e), getNode(KEY_DATE, e), getNode(KEY_LINK, e)));
            }

        } catch (ParserConfigurationException | SAXException | IOException | NullPointerException e) {
            e.printStackTrace();
        }


    }

}
