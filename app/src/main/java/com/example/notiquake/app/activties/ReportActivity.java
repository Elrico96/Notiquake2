package com.example.notiquake.app.activties;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.notiquake.R;
import com.example.notiquake.app.MailUtils;
import com.example.notiquake.app.model.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReportActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION =1;
    private static final String TAG_ID = "ReportAct";


    private RadioGroup radioGroup_no1;
    private RadioGroup rg_question1;
    private RadioGroup rg_question2;
    private RadioGroup rg_question3;
    private RadioGroup rg_question4;
    private RadioGroup rg_question5;
    private RadioGroup rg_question6;
    private EditText ed_time;
    private EditText ed_location;
    private EditText ed_comments;
    private EditText ed_name;
    private EditText ed_email;
    private EditText ed_phone;

    private FirebaseUser firebaseUser;
    private DatabaseReference firebaseDatabase;

    private ProgressDialog progressDialog;

    private LocationManager locationManager;
    private String latitude;
    private String longitude;
    private List<Address> addresses;
    private String felt_it;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;
    private String answer6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        radioGroup_no1 = findViewById(R.id.rg_felt_it);
        rg_question1 = findViewById(R.id.rg_question1);
        rg_question2 = findViewById(R.id.rg_question2);
        rg_question3 = findViewById(R.id.rg_question3);
        rg_question4 = findViewById(R.id.rg_question4);
        rg_question5 = findViewById(R.id.rg_question5);
        rg_question6 = findViewById(R.id.rg_question6);
        ed_time = findViewById(R.id.ed_time);
        ed_location = findViewById(R.id.ed_location);
        ed_comments = findViewById(R.id.ed_comments);
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email_add);
        ed_phone = findViewById(R.id.ed_phone);

        ImageButton imgBtn_location = findViewById(R.id.imgbtn_location);
        Button btnSubmitReport = findViewById(R.id.btnReport);

        ed_name.setText(firebaseUser.getDisplayName());
        ed_email.setText(firebaseUser.getEmail());

        imgBtn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    openGPSSettings();
                }else{
                    getLocation();
                }
            }
        });

        btnSubmitReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }

    public void submitReport(){

        final String RG_TOAST_TEXT = getString(R.string.rg_toast_text);

        if(radioGroup_no1.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT , Toast.LENGTH_SHORT).show();
        }else{
            int radioId = radioGroup_no1.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn = findViewById(radioId);
            felt_it = selectedRadioBtn.getText().toString();
        }

        if(rg_question1.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question1.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
             answer1 = selectedRadioBtn.getText().toString();
        }

        if(rg_question2.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(),RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question2.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
            answer2 = selectedRadioBtn.getText().toString();
        }

        if(rg_question3.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question3.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
            answer3 = selectedRadioBtn.getText().toString();
        }

        if(rg_question4.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question4.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
            answer4 = selectedRadioBtn.getText().toString();
        }

        if(rg_question5.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question5.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
            answer5 = selectedRadioBtn.getText().toString();
        }
        if(rg_question6.getCheckedRadioButtonId() == -1){
            Toast.makeText(getApplicationContext(), RG_TOAST_TEXT, Toast.LENGTH_SHORT).show();
        }else{
            int radioId = rg_question6.getCheckedRadioButtonId();
            RadioButton selectedRadioBtn  = findViewById(radioId);
            answer6 = selectedRadioBtn.getText().toString();
        }


        String time = ed_time.getText().toString().trim();
        String additionalComments = ed_comments.getText().toString();
        String providedName = ed_name.getText().toString();
        String providedEmail = ed_email.getText().toString();
        String phoneNo = ed_phone.getText().toString();

        String coordinates = "Location :"+ latitude +" "+ longitude;

        Report report = new Report(felt_it,time,coordinates, answer1, answer2, answer3, answer4, answer5, answer6,additionalComments,providedName,providedEmail,phoneNo);

        String emailSubject = "Notiquake Report Received";
        String body = generateEmailBody(providedName,time);
        SendEmail sendEmail = new SendEmail(getApplicationContext(),providedEmail,emailSubject,body);
        sendEmail.execute();
        saveUserReport(report);
    }

    private void getLocation() {
        if(ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location locationPass = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            double latitudeDouble;
            double longitudeDouble;
            if(locationGPS != null){
                latitudeDouble = locationGPS.getLatitude();
                longitudeDouble = locationGPS.getLongitude();

                latitude = formatCoordinate(latitudeDouble)+ determineLatitude(latitudeDouble);
                longitude = formatCoordinate(longitudeDouble)+determineLongitude(longitudeDouble);

                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitudeDouble, longitudeDouble,1);
                } catch (IOException e) {
                    Log.e(TAG_ID,e.toString());
                }

                String location  = addresses.get(0).getAddressLine(0);

                String mainLoc = "Your are located at "+location;

                 ed_location.setText(mainLoc);
            }else if(locationNetwork != null){
                 latitudeDouble = locationNetwork.getLatitude();
                 longitudeDouble = locationNetwork.getLongitude();

                 latitude = formatCoordinate(latitudeDouble)+ determineLatitude(latitudeDouble);
                 longitude = formatCoordinate(longitudeDouble)+determineLongitude(longitudeDouble);

                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                     addresses = geocoder.getFromLocation(latitudeDouble, longitudeDouble,1);
                } catch (IOException e) {
                    Log.e(TAG_ID,e.toString());
                }
                String location = addresses.get(0).getAddressLine(0);

                String mainLoc = "Your are located at "+location;
                ed_location.setText(mainLoc);
            }else if(locationPass != null){
                latitudeDouble = locationPass.getLatitude();
                longitudeDouble = locationPass.getLongitude();

                 latitude = formatCoordinate(latitudeDouble)+ determineLatitude(latitudeDouble);
                 longitude = formatCoordinate(longitudeDouble)+determineLongitude(longitudeDouble);

                Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                     addresses = geocoder.getFromLocation(latitudeDouble, longitudeDouble,1);
                } catch (IOException e) {
                    Log.e(TAG_ID,e.toString());
                }

                String location = addresses.get(0).getAddressLine(0);
                String mainLoc = "Your are located at "+location;

                ed_location.setText( mainLoc);
            }else{
                Toast.makeText(this, "We are unable to access your current location!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserReport(Report report) {
        firebaseDatabase.child("reports").child(firebaseUser.getUid()).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Report Submitted",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ReportActivity.this, ListEarthquakesActivity.class));
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private  void openGPSSettings(){
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private String generateEmailBody(String receiptName,String timeofEarthquake){
        return  "Hi.. "+receiptName+"\n"
                +"Hope you are well"+"\n"
                +""+"\n"
                +"We would like to confirm that we received your report on the earthquake happend "+timeofEarthquake+"\n"
                +""+"\n"
                +"Thank you for the feedback"+"\n"
                +""+"\n"
                +"Notiquake Team";
    }

    private class SendEmail extends AsyncTask<Void,Void,Void>{

        private Context context;

        private String emailAddreess;
        private String subject;
        private String emailBody;

        public SendEmail(Context context, String emailAddreess, String subject, String emailBody) {
            this.context = context;
            this.emailAddreess = emailAddreess;
            this.subject = subject;
            this.emailBody = emailBody;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Submiting Report..");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Properties properties = new Properties();

            properties.put("mail.smtp.host","smtp.gmail.com");
            properties.put("mail.smtp.socketFactory.port","465");
            properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.auth","true");
            properties.put("mai;.smtp.port","465");


            Session session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(MailUtils.EMAIL, MailUtils.PASSWORD);
                        }
                    });
            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(MailUtils.EMAIL));
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddreess));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(emailBody);
                Transport.send(mimeMessage);

            }catch (MessagingException me){
                Log.e(TAG_ID,me.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Toast.makeText(context,"Report Submitted",Toast.LENGTH_LONG).show();
        }
    }
    private String determineLatitude(Double latitude){
        String direction = "";
        if(latitude >= 0){
            direction = " 'N";
        }else if (latitude < 0){
            direction = " 'S";
        }
        return direction;
    }

    private String determineLongitude(Double longitude){
        String direction = "";
        if(longitude >= 0){
            direction = " 'E";
        }else if (longitude < 0){
            direction = " 'W";
        }
        return direction;
    }

    private String formatCoordinate(double coordinate){
        DecimalFormat coordinateformat = new DecimalFormat("0.000");
        return  coordinateformat.format(coordinate);
    }
}
