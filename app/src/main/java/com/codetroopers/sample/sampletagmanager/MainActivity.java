package com.codetroopers.sample.sampletagmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TagManager tagManager = TagManager.getInstance(this);
        ((Button) findViewById(R.id.main_button_load_fresh)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeatureStatus("...", "...", "...", "...");
                loadFreshGTMContainer(tagManager);
            }
        });

        ((Button) findViewById(R.id.main_button_load_default)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFeatureStatus("...", "...", "...", "...");
                loadDefaultGTMContainer(tagManager);
            }
        });


    }

    public void loadFreshGTMContainer(TagManager tagManager) {
        // Enable verbose logging
        tagManager.setVerboseLoggingEnabled(true);
        // Load the container
        PendingResult pending = tagManager.loadContainerPreferFresh(getString(R.string.tag_manager_id), R.raw.tag_manager_initial_conf);
        pending.setResultCallback(resultCallback, 2, TimeUnit.SECONDS);
    }

    public void loadDefaultGTMContainer(TagManager tagManager) {
        // Enable verbose logging
        tagManager.setVerboseLoggingEnabled(true);
        // Load the container
        PendingResult pending = tagManager.loadContainerDefaultOnly(getString(R.string.tag_manager_id), R.raw.tag_manager_initial_conf);

        pending.setResultCallback(resultCallback, 2, TimeUnit.SECONDS);
    }

    // Define the callback to store the loaded container
    private ResultCallback<ContainerHolder> resultCallback = new ResultCallback<ContainerHolder>() {
        @Override
        public void onResult(@NonNull ContainerHolder containerHolder) {
            // If unsuccessful, return
            if (!containerHolder.getStatus().isSuccess()) {
                // Deal with failure
                return;
            }
            // Manually refresh the container holder
            // Can only do this once every 15 minutes or so
            containerHolder.refresh();

            final boolean feature1 = containerHolder.getContainer().getBoolean("feature1");
            final boolean feature2 = containerHolder.getContainer().getBoolean("feature2");
            final boolean feature3 = containerHolder.getContainer().getBoolean("feature3");
            final boolean feature4 = containerHolder.getContainer().getBoolean("feature4");

            displayFeatureStatus(String.valueOf(feature1), String.valueOf(feature2), String.valueOf(feature3), String.valueOf(feature4));

        }
    };

    private void displayFeatureStatus(final String status1, final String status2, final String status3, final String status4) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) findViewById(R.id.feature_1_status)).setText(status1);
                ((TextView) findViewById(R.id.feature_2_status)).setText(status2);
                ((TextView) findViewById(R.id.feature_3_status)).setText(status3);
                ((TextView) findViewById(R.id.feature_4_status)).setText(status4);
            }
        });
    }

}
