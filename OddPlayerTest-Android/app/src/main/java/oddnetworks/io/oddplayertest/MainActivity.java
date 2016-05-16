package oddnetworks.io.oddplayertest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText urlEditText;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureTextListener();
        configurePlayButtonClickHandler();
    }

    void configureTextListener() {
        urlEditText = (EditText) findViewById(R.id.url_editText);
        playButton = (Button) findViewById(R.id.play_button);

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
            }
        });
    }

    void configurePlayButtonClickHandler() {
        Button playButton = (Button) findViewById(R.id.play_button);
        if (playButton != null) {
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchToPlayerView();
                }
            });
        }
    }

    void switchToPlayerView() {
        Intent videoPlayerIntent = new Intent(MainActivity.this, VideoPlayerActivity.class);
        String url = urlEditText.getText().toString();

        videoPlayerIntent.putExtra("url", url);
        MainActivity.this.startActivity(videoPlayerIntent);
    }
}
