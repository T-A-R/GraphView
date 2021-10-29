package com.template;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends SmartActivity {

    Button btnGames;
    Button btnGraph;
    ImageView btnRate;
    ImageView btnShare;
    LinearLayout gamesCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        gamesCont = findViewById(R.id.games_cont);
        btnShare = findViewById(R.id.btn_share);
        btnRate = findViewById(R.id.btn_rate);
        btnGames = findViewById(R.id.btn_games);
        btnGraph = findViewById(R.id.btn_graph);
        btnGames.setOnClickListener(v -> showWebView(true));
        btnGraph.setOnClickListener(v -> startActivity(GraphActivity.class));
        btnRate.setOnClickListener(v -> showRateDialog());
        btnShare.setOnClickListener(v -> share());

    }

    private void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.app_name));
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
        } catch(Exception e) {
            Toast.makeText(this, "Unable to share app", Toast.LENGTH_LONG).show();
        }
    }

    private void rate() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void showWebView(boolean show) {
        startActivity(WebActivity.class);
    }

    public void showRateDialog() {
        if (!isFinishing()) {
            new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                    .setCancelable(true)
                    .setTitle(R.string.rate_dialog_title)
                    .setMessage(R.string.rate_dialog_message)
                    .setPositiveButton(R.string.view_yes, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        rate();
                    })
                    .setNegativeButton(R.string.view_no, null).show();
        }
    }
}