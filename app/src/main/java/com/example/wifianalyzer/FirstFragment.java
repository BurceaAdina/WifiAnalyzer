package com.example.wifianalyzer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class FirstFragment extends Fragment {

    private TextView IP, SSID, BSSID, Gateway, DNS;
    private TextView ISP, publicIP, Hostname, location, Timezone;
    private TextView protocol, alert;
    private RequestQueue requestQueue;
    private TextView textsemnal;
    private ImageView imagesemnal;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                Boolean wifiStateGranted = result.getOrDefault(Manifest.permission.ACCESS_WIFI_STATE, false);

                if (fineLocationGranted != null && wifiStateGranted != null && fineLocationGranted && wifiStateGranted) {
                    displayWifiInfo();
                    fetchExternalInfo();
                } else {
                    IP.setText("Permission denied");
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);

        IP = view.findViewById(R.id.IP);
        SSID = view.findViewById(R.id.SSID);
        BSSID = view.findViewById(R.id.BSSID);
        Gateway = view.findViewById(R.id.Gateway);
        DNS = view.findViewById(R.id.DNS);
        ISP = view.findViewById(R.id.ISP);
        publicIP = view.findViewById(R.id.publicIP);
        Hostname = view.findViewById(R.id.hostname);
        location = view.findViewById(R.id.location);
        Timezone = view.findViewById(R.id.timezone);
        protocol = view.findViewById(R.id.protocol);
        alert = view.findViewById(R.id.alert);
        textsemnal = view.findViewById(R.id.textsemnal);
        imagesemnal = view.findViewById(R.id.imagesemnal);

        requestQueue = Volley.newRequestQueue(getContext());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE
            });
        } else {
            displayWifiInfo();
            fetchExternalInfo();
        }

        return view;
    }

    private void displayWifiInfo() {
        WifiManager wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (wifiInfo != null && wifiInfo.getSupplicantState() == android.net.wifi.SupplicantState.COMPLETED) {
                // IP Address
                int ipAddress = wifiInfo.getIpAddress();
                String ipString = intToInetAddress(ipAddress).getHostAddress();
                IP.setText("Adresă IP: " + ipString);

                // SSID
                String ssid = wifiInfo.getSSID();
                SSID.setText("SSID: " + ssid);

                // BSSID
                String bssid = wifiInfo.getBSSID();
                BSSID.setText("BSSID: " + bssid);

                // Puterea semnalului
                int signalStrength = wifiInfo.getRssi();
                textsemnal.setText("Puterea Semnalului: " + signalStrength + " dBm");


                // DhcpInfo
                DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();

                // Gateway
                int gateway = dhcpInfo.gateway;
                String gatewayString = intToInetAddress(gateway).getHostAddress();
                Gateway.setText("Gateway: " + gatewayString);

                // DNS
                int dns1 = dhcpInfo.dns1;
                int dns2 = dhcpInfo.dns2;
                String dnsString;
                if (dns1 != 0) {
                    dnsString = intToInetAddress(dns1).getHostAddress();
                } else if (dns2 != 0) {
                    dnsString = intToInetAddress(dns2).getHostAddress();
                } else {
                    dnsString = "Niciun DNS Valabil";
                }

                DNS.setText("DNS: " + dnsString);


                // Security Protocol
                String securityProtocol = getSecurityProtocol(wifiInfo);
                protocol.setText("Protocol de Securitate: " + securityProtocol);

                // Check for Security Warnings
                checkForSecurityWarnings(ssid, bssid, securityProtocol, ipString, gatewayString, dnsString);
                // Update Wi-Fi Signal Strength Icon
                int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
                updateWifiSignalAnimation(signalLevel);
            } else {
                IP.setText("Neconectat la o rețea WI-FI");
            }
        } else {
            requestPermissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            });
        }
    }
    private void updateWifiSignalAnimation(int signalLevel) {
        switch (signalLevel) {
            case 4:
                imagesemnal.setImageResource(R.drawable.wifi_signal_4);
                break;
            case 3:
                imagesemnal.setImageResource(R.drawable.wifi_signal_3);
                break;
            case 2:
                imagesemnal.setImageResource(R.drawable.wifi_signal_2);
                break;
            case 1:
                imagesemnal.setImageResource(R.drawable.wifi_signal_1);
                break;
            default:
                imagesemnal.setImageResource(R.drawable.wifi_signal_0);
                break;
        }
    }
    private InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = {(byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
    }

    private String getSecurityProtocol(WifiInfo wifiInfo) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                switch (wifiInfo.getCurrentSecurityType()) {
                    case WifiInfo.SECURITY_TYPE_OPEN:
                        return "Open";
                    case WifiInfo.SECURITY_TYPE_WEP:
                        return "WEP";
                    case WifiInfo.SECURITY_TYPE_PSK:
                        return "WPA/WPA2 PSK";
                    case WifiInfo.SECURITY_TYPE_EAP:
                        return "WPA/WPA2 EAP";
                    case WifiInfo.SECURITY_TYPE_SAE:
                        return "WPA3 SAE";
                    case WifiInfo.SECURITY_TYPE_OWE:
                        return "WPA3 OWE";
                    default:
                        return "Unknown";
                }} else { return "Tipul de securitate nu este disponibil (este necesar Android 12+)";}
        } else { return "Tipul de securitate nu este disponibil"; }}

    private void fetchExternalInfo() {
        String url = "https://ipinfo.io/json?token=282905b54b2997";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String ip = response.getString("ip");
                            String hostname = response.optString("hostname", "N/A");
                            String city = response.getString("city");
                            String region = response.getString("region");
                            String country = response.getString("country");
                            String loc = response.getString("loc");
                            String org = response.getString("org");
                            String timezone = response.getString("timezone");

                            publicIP.setText("Adresă IP Publică: " + ip);
                            Hostname.setText("Gazdă: " + hostname);
                            location.setText("Locație: " + city + ", " + region + ", " + country + " (" + loc + ")");
                            ISP.setText("ISP: " + org);
                            Timezone.setText("Fus Orar: " + timezone);

                            // Check for external IP related warnings
                            checkForExternalWarnings(ip, hostname, city, region, country, loc, org, timezone);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void checkForSecurityWarnings(String ssid, String bssid, String securityProtocol, String ipAddress, String gateway, String dns) {
        StringBuilder warnings = new StringBuilder();

        if (securityProtocol.equals("Open")) {
            warnings.append("Atenție: Rețeaua este deschisă și neprotejată. Datele ar putea fi expuse.\n");
        } else if (securityProtocol.equals("WEP")) {
            warnings.append("Atenție: Rețeaua utilizează WEP, care este depășit și nesigur. Luați în considerare îmbunătățirea securitații rețelei.\n");
        } else if (securityProtocol.equals("WPA/WPA2 PSK")) {
            warnings.append("Atenție: WPA/WPA2 PSK este în general sigur, dar asigurați-vă că parola este puternică.\n");
        } else if (securityProtocol.equals("WPA3 SAE")) {
            warnings.append("Rețeaua utilizează WPA3 SAE, care este în prezent cel mai sigur protocol disponibil.\n");
        }

        if (ssid == null || ssid.equals("")) {
            warnings.append("Atenție: SSID nu este valabil.\n");
        }
        if (bssid == null || bssid.equals("")) {
            warnings.append("Atenție: BSSID nu este valabil.\n");
        }

        if (ipAddress.equals("0.0.0.0")) {
            warnings.append("Atenție: Adresa IP nu este valabilă.\n");
        }

        if (gateway.equals("0.0.0.0")) {
            warnings.append("Atenție: Gateway este invalid.\n");
        }
        if (dns.equals("0.0.0.0, 0.0.0.0")) {
            warnings.append("Atenție: DNS este invalid.\n");
        }

         alert.setText(warnings.toString());
    }

    private void checkForExternalWarnings(String ip, String hostname, String city, String region, String country, String loc, String org, String timezone) {
        StringBuilder warnings = new StringBuilder();

        if (ip == null || ip.equals("")) {
            warnings.append("Atenție: IP-ul public nu este valabil.\n");
        }
        if (hostname == null || hostname.equals("")) {
            warnings.append("Atenție: Hostname-ul nu este valabil.\n");
        }
        if (city == null || city.equals("")) {
            warnings.append("Atenție: Informațiile despre oras nu sunt valabile.\n");
        }
        if (region == null || region.equals("")) {
            warnings.append("Atenție: Informațiile despre regiune nu sunt valabile.\n");
        }
        if (country == null || country.equals("")) {
            warnings.append("Atenție: Informațiile despre tara nu sunt valabile\n");
        }
        if (loc == null || loc.equals("")) {
            warnings.append("Atenție: Coordonatele locatiei nu sunt valabile.\n");
        }
        if (org == null || org.equals("")) {
            warnings.append("Atenție: Informațiile despre ISP nu sunt valabile.\n");
        }
        if (timezone == null || timezone.equals("")) {
            warnings.append("Atenție: Informațiile despre fus orar nu sunt valabile.\n");
        }

        alert.append("\n" + warnings.toString());
    }
}
