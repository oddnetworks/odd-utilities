package oddnetworks.io.oddplayertest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.exoplayer.util.Util;

public class MainActivity extends AppCompatActivity {
    private EditText urlEditText;
    private Button playButton;
    private Button exoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureTextListener();
        configurePlayButtonClickHandlers();
    }

    void configureTextListener() {
        urlEditText = (EditText) findViewById(R.id.url_editText);
        playButton = (Button) findViewById(R.id.play_button);

        urlEditText.setText("http://www.sample-videos.com/video/mp4/240/big_buck_bunny_240p_1mb.mp4", null);

        urlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                playButton.setEnabled(s.length() > 0);
                exoButton.setEnabled(s.length() > 0);
            }
        });
    }

    void configurePlayButtonClickHandlers() {
        playButton = (Button) findViewById(R.id.play_button);
        if (playButton != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToPlayerView(PlayerType.NATIVE);
                }
            });
        }

        exoButton = (Button) findViewById(R.id.exo_button);
        if (exoButton != null) {
            exoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToPlayerView(PlayerType.EXO);
                }
            });
        }
    }

    void switchToPlayerView(PlayerType playerType) {
        Intent videoPlayerIntent;
        videoPlayerIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
        String url = urlEditText.getText().toString();

        if (playerType == PlayerType.NATIVE) {
            videoPlayerIntent.putExtra("url", url);
        } else {
            videoPlayerIntent = new Intent(MainActivity.this, ExoPlayerActivity.class)
                .setData(Uri.parse(url))
                .putExtra(ExoPlayerActivity.CONTENT_ID_EXTRA, "contentId")
                .putExtra(ExoPlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_OTHER)
                .putExtra(ExoPlayerActivity.PROVIDER_EXTRA, "provider");
        }

        MainActivity.this.startActivity(videoPlayerIntent);
    }
}
