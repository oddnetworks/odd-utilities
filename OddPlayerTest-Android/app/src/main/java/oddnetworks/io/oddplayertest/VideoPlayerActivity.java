package oddnetworks.io.oddplayertest;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoView = (VideoView) findViewById(R.id.videoView);

        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        configureForVideo(url);
    }

    void configureForVideo(String url) {
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                String message = "generic audio playback error";
                String meta = "unknown playback error";

                switch (what){
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        message = "unknown media playback error";
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        message = "server connection died";
                        break;
                    default:
                        break;
                }

                switch (extra){
                    case MediaPlayer.MEDIA_ERROR_IO:
                        meta = "IO media error";
                        break;
                    case MediaPlayer.MEDIA_ERROR_MALFORMED:
                        meta = "media error, malformed";
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                        meta = "unsupported media content";
                        break;
                    case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                        meta = "media timeout error";
                        break;
                    default:
                        break;
                }

                Log.e("Odd", String.format("Media Player error: %s - %s", message, meta ) );
                displayError(message, meta);
                return false;
            }
        });
        videoView.setVideoPath(url);
        videoView.start();
    }

    void displayError(String message, String meta) {
        AlertDialog alertDialog = new AlertDialog.Builder(VideoPlayerActivity.this).create();
        alertDialog.setTitle("Error");
        String errorStr = String.format("Media Player error: %s - %s", message, meta );
        alertDialog.setMessage(errorStr);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
