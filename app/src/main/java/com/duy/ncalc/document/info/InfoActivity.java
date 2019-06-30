/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.ncalc.document.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.ncalc.utils.DLog;
import com.duy.calculator.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class InfoActivity extends AppCompatActivity {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
    private RecyclerView mListTranslate;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_info);
        mToolbar = findViewById(R.id.toolbar);
        mListTranslate = findViewById(R.id.list_translate);
        setupActionBar();
        initContent();
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.title_activity_app_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        new TaskLoadData().execute();
        Log.d(TAG, "initContent: ");

    }

    public static class HelpTranslateAdapter extends RecyclerView.Adapter<HelpTranslateAdapter.ViewHolder> {
        private static final String TAG = HelpTranslateAdapter.class.getSimpleName();
        private LayoutInflater inflater;
        private ArrayList<ItemInfo> listData;

        HelpTranslateAdapter(Context context, ArrayList<ItemInfo> listData) {
            this.inflater = LayoutInflater.from(context);
            this.listData = listData;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item_info, parent, false);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.bindContent(listData.get(position));
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtTitle;
            TextView txtDesc;
            View root;

            public ViewHolder(View itemView) {
                super(itemView);
                txtTitle = itemView.findViewById(R.id.txt_title);
                txtDesc = itemView.findViewById(R.id.txt_desc);
                root = itemView.findViewById(R.id.container);
            }

            void bindContent(ItemInfo itemInfo) {
                txtTitle.setText(itemInfo.getLang());
                String desc = itemInfo.getTitle() + (itemInfo.getLink().trim().isEmpty() ? "" : "\n" + itemInfo.getLink());
                txtDesc.setText(desc);
            }
        }

    }

    public static class ItemInfo {
        private String title;
        private String link;
        private String imgPath;
        private String lang;

        public ItemInfo(String title, String link, String imgPath, String lang) {
            this.title = title;
            this.link = link;
            this.imgPath = imgPath;
            this.lang = lang;
        }

        public ItemInfo(String title, String link, String imgPath) {
            this.title = title;
            this.link = link;
            this.imgPath = imgPath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        @Override
        public String toString() {
            return title;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }
    }

    /**
     * Created by Duy on 29-Mar-17.
     */

    public static class InfoAppUtil {

        public static ArrayList<ItemInfo> readListTranslate(InputStream inputStream) {
            ArrayList<ItemInfo> result = new ArrayList<>();
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                Element root = document.getDocumentElement();
                NodeList nodeList = root.getChildNodes();
                DLog.i(nodeList.getLength());
                for (int index = 0; index < nodeList.getLength(); index++) {
                    Node node = nodeList.item(index);
                    if (node instanceof Element) {
                        String name = ((Element) node).getAttribute("name");
                        String link = ((Element) node).getAttribute("link");
                        String img = ((Element) node).getAttribute("image");
                        String lang = ((Element) node).getAttribute("lang");
                        result.add(new ItemInfo(name, link, img, lang));
                    }
                }
            } catch (ParserConfigurationException | SAXException | IOException e) {
                // e.printStackTrace();
            }

            return result;
        }

    }

    @SuppressLint("StaticFieldLeak")
    class TaskLoadData extends AsyncTask<Void, Void, Void> {
        private ArrayList<ItemInfo> mData;

        @Override
        protected Void doInBackground(Void... params) {
            mData = InfoAppUtil.readListTranslate(getResources().openRawResource(R.raw.help_translate));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HelpTranslateAdapter adapterTranslate = new HelpTranslateAdapter(InfoActivity.this, mData);
            mListTranslate.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListTranslate.setHasFixedSize(false);
            mListTranslate.setAdapter(adapterTranslate);
        }
    }
}
