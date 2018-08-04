package com.duy.ncalc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.duy.ncalc.utils.DLog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentView extends WebView {
    protected static final Pattern ASSET_FILE_MD_PATTERN
            = Pattern.compile("(file:///android_asset/)(.*)/(.*.md)");
    private static final String TAG = DocumentView.class.getSimpleName();
    private static final String IMAGE_PATTERN = "!\\[(.*)\\]\\((.*)\\)";
    private final Context mContext;
    private String mPreviewText;
    private boolean mIsOpenUrlInBrowser;
    private String previousDocumentPath;

    public DocumentView(Context context) {
        this(context, null);
    }

    public DocumentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DocumentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initialize() {
        setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    loadUrl(mPreviewText);
                } else {
                    evaluateJavascript(mPreviewText, null);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (DLog.DEBUG)
                    DLog.d(TAG, "shouldOverrideUrlLoading() called with: url = [" + url + "]");
                Matcher matcher = ASSET_FILE_MD_PATTERN.matcher(url);
                if (matcher.find()) {
                    String fileName = matcher.group(3);

                    Pattern pattern = Pattern.compile("(.*/)(.*.md)");
                    matcher = pattern.matcher(previousDocumentPath);
                    if (matcher.find()) {
                        String baseUrl = matcher.group(1);
                        loadMarkdownFromAssets(baseUrl + fileName);
                        return true;
                    }
                }
                if (isOpenUrlInBrowser()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        loadUrl("file:///android_asset/html/preview.html");
        getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getSettings().setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public void loadMarkdownFromFile(File markdownFile) {
        String mdText = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(markdownFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String readText;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readText = bufferedReader.readLine()) != null) {
                stringBuilder.append(readText);
                stringBuilder.append("\n");
            }
            fileInputStream.close();
            mdText = stringBuilder.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException:" + e);
        } catch (IOException e) {
            Log.e(TAG, "IOException:" + e);
        }
        setMarkDownText(mdText);
    }

    public void loadMarkdownFromAssets(String assetsFilePath) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = getContext().getAssets().open(assetsFilePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                buf.append(str).append("\n");
            }
            in.close();
            setMarkDownText(buf.toString());
            this.previousDocumentPath = assetsFilePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMarkDownText(String markdownText) {
        String bs64MdText = imgToBase64(markdownText);
        String escMdText = escapeForText(bs64MdText);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mPreviewText = String.format("javascript:preview('%s')", escMdText);
        } else {
            mPreviewText = String.format("preview('%s')", escMdText);
        }
        initialize();
    }

    private String escapeForText(String mdText) {
        String escText = mdText.replace("\n", "\\\\n");
        escText = escText.replace("'", "\\\'");
        escText = escText.replace("\r", "");
        return escText;
    }

    private String imgToBase64(String mdText) {
        Pattern ptn = Pattern.compile(IMAGE_PATTERN);
        Matcher matcher = ptn.matcher(mdText);
        if (!matcher.find()) {
            return mdText;
        }
        String imgPath = matcher.group(2);
        if (isUrlPrefix(imgPath) || !isPathExCheck(imgPath)) {
            return mdText;
        }
        String baseType = imgEx2BaseType(imgPath);
        if (baseType.equals("")) {
            return mdText;
        }
        File file = new File(imgPath);
        byte[] bytes = new byte[(int) file.length()];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException:" + e);
        } catch (IOException e) {
            Log.e(TAG, "IOException:" + e);
        }
        String base64Img = baseType + Base64.encodeToString(bytes, Base64.NO_WRAP);
        return mdText.replace(imgPath, base64Img);
    }

    private boolean isUrlPrefix(String text) {
        return text.startsWith("http://") || text.startsWith("https://");
    }

    private boolean isPathExCheck(String text) {
        return text.endsWith(".png")
                || text.endsWith(".jpg")
                || text.endsWith(".jpeg")
                || text.endsWith(".gif");
    }

    private String imgEx2BaseType(String text) {
        if (text.endsWith(".png")) {
            return "data:image/png;base64,";
        } else if (text.endsWith(".jpg") || text.endsWith(".jpeg")) {
            return "data:image/jpg;base64,";
        } else if (text.endsWith(".gif")) {
            return "data:image/gif;base64,";
        } else {
            return "";
        }
    }

    public boolean isOpenUrlInBrowser() {
        return mIsOpenUrlInBrowser;
    }

    public void setOpenUrlInBrowser(boolean openUrlInBrowser) {
        mIsOpenUrlInBrowser = openUrlInBrowser;
    }
}