package com.example.wifianalyzer;

import androidx.fragment.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.NotificationCompat;

public class SecondFragment extends Fragment {
    private static final String CHANNEL_ID = "new_device_channel";

    private ListView listadispozitive;
    private Button buttonScan;
    private ProgressBar progressBar;
    private SwitchCompat switchNotification;
    private ArrayAdapter<String> devicesAdapter;
    private List<String> connectedDevicesList;
    private Set<String> knownDevices;
    private ExecutorService executorService;

    private boolean notifyOnNewDevice = false;

    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean postNotificationsGranted = result.getOrDefault(Manifest.permission.POST_NOTIFICATIONS, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        scanConnectedDevices();
                    } else {
                        Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        buttonScan.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    if (!postNotificationsGranted) {
                        Toast.makeText(getContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);

        listadispozitive = rootView.findViewById(R.id.listadispozitive);
        buttonScan = rootView.findViewById(R.id.buttonScan);
        progressBar = rootView.findViewById(R.id.progressBar);
        switchNotification = rootView.findViewById(R.id.switchNotifications);
        connectedDevicesList = new ArrayList<>();
        devicesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, connectedDevicesList);
        listadispozitive.setAdapter(devicesAdapter);
        executorService = Executors.newSingleThreadExecutor();
        knownDevices = new HashSet<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        buttonScan.setOnClickListener(view -> {
            buttonScan.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                scanConnectedDevices();
            }
        });

        switchNotification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            notifyOnNewDevice = isChecked;
            if (notifyOnNewDevice) {
                requestNotificationPermission();
            }
        });

        return rootView;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(new String[]{Manifest.permission.POST_NOTIFICATIONS});
            }
        }
    }

    private void scanConnectedDevices() {
        Toast.makeText(getContext(), "Scanning...", Toast.LENGTH_SHORT).show();

        executorService.execute(() -> {
            List<String> result = new ArrayList<>();
            try {
                WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();
                String ipString = String.format("%d.%d.%d.", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff));

                for (int i = 1; i < 255; i++) {
                    String testIp = ipString + i;
                    InetAddress address = InetAddress.getByName(testIp);
                    boolean reachable = address.isReachable(100);
                    if (reachable) {
                        String device = testIp + " - " + address.getHostName();
                        result.add(device);
                        if (notifyOnNewDevice && !knownDevices.contains(device)) {
                            knownDevices.add(device);
                            sendNotification(device);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ScanConnectedDevices", "Error scanning devices", e);
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    connectedDevicesList.clear();
                    connectedDevicesList.addAll(result);
                    devicesAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Scan complete", Toast.LENGTH_SHORT).show();
                    buttonScan.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "New Device Channel";
            String description = "Channel for new device notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(String device) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("New Device Connected")
                .setContentText(device)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
