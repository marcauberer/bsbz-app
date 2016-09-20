package com.mrgames13.jimdo.bsbz_app.Tools;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.mrgames13.jimdo.bsbz_app.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ServerMessagingUtils {
    //Konstanten
    private final String SERVER_ADRESS = "http://bsbzapp.mrgames-server.de/";
    private final String ALTERNATIVE_SERVER_ADRESS = "http://mrgamesserver.esy.es/bsbz_app_masterserver/";
    private final String SERVER_MAIN_SCRIPT = SERVER_ADRESS + "ServerScript.php";
    private final String SERVER_UPLOAD_SCRIPT = SERVER_ADRESS + "UploadReceiver.php";
    private final int UPLOAD_BLOCK_SIZE = 256;
    private final int IMAGE_COMPRESSION_QUALITY = 70;

    //Variablen als Objekte
    private ConnectivityManager cm;
    private Context context;
    private WifiManager wifiManager;
    private SharedPreferences prefs;
    private Resources res;
    private URL url;
    private Handler handler;
    private ProgressDialog pd;
    private ContentResolver cr;

    //Variablen
    int i;

    //Konstruktor
    public ServerMessagingUtils(ConnectivityManager cm, Context context) {
        this.cm = cm;
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        res = context.getResources();
        cr = context.getContentResolver();
        handler = new Handler();
        //UTL erstellen
        try { url = new URL(SERVER_MAIN_SCRIPT); } catch (MalformedURLException e) {}
    }

    public String sendRequest(View v, final String param) {
        if(isInternetAvailable()) {
            try {
                //Connection aufbauen
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setFixedLengthStreamingMode(param.getBytes().length);
                //Anfrage senden
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                out.write(param);
                out.flush();
                out.close();
                //Antwort empfangen
                InputStream in = connection.getInputStream();
                String answer = getAnswerFromInputStream(in);
                //Connection schließen
                connection.disconnect();
                Log.i("BSBZ-App", "Answer from Server: '"+answer.replace("<br>", "").trim()+"'");
                //Antwort zurückgeben
                return answer.replace("<br>", "").trim();
            } catch (Exception e) {}
        } else {
            if(v != null) checkConnection(v);
        }
        return "";
    }

    public void downloadFile(String imageFolder, String imageName, File dir) {
        try {
            if(!imageName.endsWith(".jpg")) imageName = imageName + ".jpg";
            //Connection aufbauen
            URL url = new URL(SERVER_ADRESS + "images/" + URLEncoder.encode(imageFolder, "UTF-8") + "/" + URLEncoder.encode(imageName, "UTF-8"));
            URLConnection connection = url.openConnection();
            connection.connect();
            //InputStream erstellen
            InputStream i = new BufferedInputStream(connection.getInputStream(), 8192);
            //Dateien initialisieren
            if(dir == null) {
                dir = new File(Environment.getExternalStorageDirectory(), "Download");
                imageName = imageFolder.replace(".", "") + "_" + imageName;
            }
            if(!dir.exists()) dir.mkdirs();
            File file = new File(dir, imageName);
            //FileOutputStreams erstellen
            OutputStream o = new FileOutputStream(file);
            //In Datei hineinschreiben
            byte[] buffer = new byte[1024];
            int read;
            while((read = i.read(buffer)) != -1) {
                o.write(buffer, 0, read);
            }
            //Streams schließen
            o.flush();
            o.close();
            i.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFolder(final Context context, String imageFolder, final ArrayList<String> fileNames) {
        //Ordner erstellen
        File download_dir = new File(Environment.getExternalStorageDirectory(), "Download");
        if(!download_dir.exists()) download_dir.mkdirs();
        File dir = new File(download_dir.getAbsolutePath(), imageFolder);
        handler.post(new Runnable() {
            @Override
            public void run() {
                pd = new ProgressDialog(context);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.setTitle(res.getString(R.string.download));
                pd.setMessage(res.getString(R.string.download_in_progress) + " (0/"+String.valueOf(fileNames.size())+")");
                pd.show();
            }
        });
        i = 0;
        for(String fileName : fileNames) {
            downloadFile(imageFolder, fileName, dir);
            i++;
            handler.post(new Runnable() {
                @Override
                public void run() {
                    pd.setMessage(res.getString(R.string.download_in_progress) + " ("+String.valueOf(i)+"/"+String.valueOf(fileNames.size())+")");
                }
            });
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        });
    }

    public Bitmap downloadImage(String imageFolder, String imageName) {
        Bitmap b = null;
        try {
            if(!imageName.endsWith(".jpg")) imageName = imageName + ".jpg";
            //Connection aufbauen
            URL url = new URL(SERVER_ADRESS + "images/" + URLEncoder.encode(imageFolder, "UTF-8") + "/" + URLEncoder.encode(imageName, "UTF-8"));
            URLConnection connection = url.openConnection();
            connection.connect();
            //InputStream erstellen
            InputStream i = new BufferedInputStream(connection.getInputStream(), 8192);
            b = BitmapFactory.decodeStream(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public void uploadImage(final ProgressDialog pd, final Bitmap bitmap, final String folderName, final String imageName) {
        try {
            String boundary = "---boundary"+System.currentTimeMillis();
            String firstLineBoundary = "--"+boundary+"\r\n";
            String contentDisposition = "Content-Disposition: form-data;name=\"fileupload\";filename=\"" + URLEncoder.encode(folderName, "UTF-8") + "," + URLEncoder.encode(imageName, "UTF-8") + "\"\r\n";
            String newLine = "\r\n";
            String lastLineBoundary = "--"+boundary+"--\r\n";

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_COMPRESSION_QUALITY, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream imageInputStream = new ByteArrayInputStream(bitmapdata);
            int uploadSize = (firstLineBoundary+contentDisposition+newLine+newLine+lastLineBoundary).getBytes().length + imageInputStream.available();
            pd.setMax(uploadSize);

            URL uploadUrl = new URL(SERVER_UPLOAD_SCRIPT);
            HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setFixedLengthStreamingMode(uploadSize);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(firstLineBoundary);
            dataOutputStream.writeBytes(contentDisposition);
            dataOutputStream.writeBytes(newLine);

            int byteCounter = 0;
            byte[] buffer = new byte[UPLOAD_BLOCK_SIZE];
            int read;
            while ((read = imageInputStream.read(buffer)) != -1){
                dataOutputStream.write(buffer, 0, read);
                byteCounter+=UPLOAD_BLOCK_SIZE;
                pd.setProgress(byteCounter);
                Thread.sleep(0, 500);
            }

            dataOutputStream.writeBytes(newLine);
            dataOutputStream.writeBytes(lastLineBoundary);
            dataOutputStream.flush();
            dataOutputStream.close();

            pd.dismiss();
        } catch (Exception e) {}
    }

    public long ping() {
        //Nutzernamen herausfinden
        String username = prefs.getString("Name", res.getString(R.string.guest));
        //Zeit berechnen
        long start = System.currentTimeMillis();
        sendRequest(null, "name=" + username + "&command=ping");
        long end = System.currentTimeMillis();
        return end - start;
    }

    public String getAnswerFromInputStream(InputStream in) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String currentLine;
        while((currentLine = reader.readLine()) != null) {
            sb.append(currentLine);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void checkConnection(View v) {
        if(!isInternetAvailable()) {
            Snackbar.make(v, context.getResources().getString(R.string.internet_is_not_available), Snackbar.LENGTH_LONG)
                    .setAction(R.string.activate_wlan, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wifiManager.setWifiEnabled(true);
                        }
                    })
                    .show();
        }
    }

    public boolean isInternetAvailable() {
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}