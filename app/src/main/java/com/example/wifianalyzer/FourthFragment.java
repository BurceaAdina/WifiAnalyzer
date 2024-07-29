package com.example.wifianalyzer;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FourthFragment extends Fragment {

    private ProgressBar progressBar;
    private Button startButton;
    private TextView downloadSpeedText, uploadSpeedText, pingText, warningText, text;
    private Handler handler = new Handler();
    private OkHttpClient client;
    private float downloadSpeed, uploadSpeed;
    private long ping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        startButton = view.findViewById(R.id.startButton);
        downloadSpeedText = view.findViewById(R.id.downloadSpeedText);
        uploadSpeedText = view.findViewById(R.id.uploadSpeedText);
        pingText = view.findViewById(R.id.pingText);
        warningText = view.findViewById(R.id.warningText);
        text = view.findViewById(R.id.text);

        client = new OkHttpClient();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeedTest();
            }
        });

        return view;
    }

    private void startSpeedTest() {
        progressBar.setVisibility(View.VISIBLE);
        text.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        downloadSpeedText.setText("Viteză de Descărcare: ");
        uploadSpeedText.setText("Viteză de Încărcare: ");
        pingText.setText("Ping: ");
        warningText.setText("");
        startButton.setVisibility(View.GONE);

        // Measure Ping, Download, and Upload Speed simultaneously
        new Thread(new Runnable() {
            @Override
            public void run() {
                measurePing();
                measureDownloadSpeed();
                measureUploadSpeed();
            }
        }).start();
    }

    private void measurePing() {
        final long startTime = System.nanoTime();
        Request request = new Request.Builder().url("https://www.google.com").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("SpeedTest", "Eroare Ping: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final long endTime = System.nanoTime();
                ping = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pingText.setText("Ping: " + ping + " ms");
                    }
                });
            }
        });
    }

    private void measureDownloadSpeed() {
        try {
            URL url = new URL("https://proof.ovh.net/files/10Mb.dat"); //
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            byte[] data = new byte[1024];
            long total = 0;
            long startTime = System.currentTimeMillis();
            int count;
            int fileSize = connection.getContentLength(); // Total size for progress calculation

            while ((count = input.read(data)) != -1) {
                total += count;
                final long currentTime = System.currentTimeMillis();
                downloadSpeed = (total * 8.0f) / ((currentTime - startTime) / 1000.0f) / 1024 / 1024; // Mbps
                final int progress = (int) (total * 100 / fileSize);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        downloadSpeedText.setText("Viteză de Descărcare: " + String.format("%.2f", downloadSpeed) + " Mbps");
                    }
                });
            }
            input.close();
        } catch (Exception e) {
            Log.e("SpeedTest", "Eroare Viteză de Descărcare: " + e.getMessage());
        }
    }

    private void measureUploadSpeed() {
        try {
            URL url = new URL("https://proof.ovh.net/files/10Mb.dat");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();

            OutputStream output = new BufferedOutputStream(connection.getOutputStream());
            byte[] data = new byte[1024];
            long total = 0;
            long startTime = System.currentTimeMillis();
            int count = 0;
            int totalUploadSize = 2 * 1024 * 1024;

            while (total < totalUploadSize) {
                output.write(data);
                total += data.length;
                final long currentTime = System.currentTimeMillis();
                uploadSpeed = (total * 8.0f) / ((currentTime - startTime) / 1000.0f) / 1024 / 1024; // Mbps
                final int progress = (int) (total * 100 / totalUploadSize);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(progress);
                        uploadSpeedText.setText("Viteză de Încărcare: " + String.format("%.2f", uploadSpeed) + " Mbps");
                    }
                });
            }
            output.close();
        } catch (Exception e) {
            Log.e("SpeedTest", "Eroare Viteză de Încărcare: " + e.getMessage());
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    text.setVisibility(View.GONE);
                    startButton.setVisibility(View.VISIBLE);
                    checkForWarnings();
                }
            });
        }
    }

    private void checkForWarnings() {
        StringBuilder warnings = new StringBuilder();
        if (downloadSpeed < 5) { // 5 Mbps
            warnings.append("Avertisment: Viteza de descărcare este prea mică.\n");
            warnings.append("Explicație: Viteza lentă de descărcare poate duce la timpi lungi de buffering pentru videoclipuri, timpi de descărcare lenți pentru fișiere și o experiență generală de internet încetinită.\n\n");
        }
        if (uploadSpeed < 1) { // 1 Mbps
            warnings.append("Avertisment: Viteza de încărcare este prea mică.\n");
            warnings.append("Explicație: Viteza de încărcare lentă poate afecta calitatea conferințelor video, jocurile online și capacitatea de a încărca fișiere mari.\n\n");
        }
        if (ping > 100) {
            warnings.append("Avertisment: Timp de ping ridicat.\n");
            warnings.append("Explicație: Ping-ul ridicat indică probleme de latență, care pot cauza întârzieri în jocurile online, întârzieri în apelurile video și timpi de răspuns lenți atunci când se utilizează aplicații online. \n");
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                warningText.setText(warnings.toString());
                warningText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });
    }
}
