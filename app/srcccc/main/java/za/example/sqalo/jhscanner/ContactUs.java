package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Owner on 2015-10-13.
 */
public class ContactUs extends Fragment {
    private static final int REQUEST_CODE = 1;
    Button btnOK;
    EditText txtFrom;
    EditText txtSubject;
    EditText txtMessage;
    ImageView fb,twt,web,instagram;
    View rootView;
    Bundle bundle;
    MainActivity activity =   MainActivity.instance;

    //---------con--------
    Connection con;
    String un,pass,db,ip;

    public ContactUs(){

        super();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_contactus,
                container, false);


        btnOK = (Button) rootView.findViewById(R.id.btnOK);

        txtFrom = (EditText)rootView. findViewById(R.id.etFrom);
        txtSubject = (EditText)rootView. findViewById(R.id.etSubject);
        txtMessage = (EditText) rootView.findViewById(R.id.etMessage);

        fb = (ImageView)rootView.findViewById(R.id.fbook);
        twt = (ImageView)rootView.findViewById(R.id.twitter);
        instagram = (ImageView)rootView.findViewById(R.id.instagram);
        web = (ImageView) rootView.findViewById(R.id.web);
        bundle = this.getArguments();
        try{
            if(bundle != null){
                txtFrom.setText(bundle.getString("email"));
                          }
        }catch (Exception ex){

        }

        fb.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/GoingDotsZA/"));
                startActivity(intent);
            }
        });

        twt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://twitter.com/GoingDotsZA"));
                startActivity(intent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.instagram.com/goingdots"));
                startActivity(intent);
            }
        });

        web.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://www.goingdots.com/"));
                startActivity(intent);
            }
        });






        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*String to = "jabun@ngobeniholdings.co.za";
                String subject = txtSubject.getText().toString();
                String message = txtMessage.getText().toString();
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(     "mailto",to, null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(emailIntent, "Send email via..."));
                    Toast.makeText(rootView.getContext(), "Sent...",Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(rootView.getContext(), "Device has no email account",Toast.LENGTH_LONG).show();
                }*/

                SendMail send =new SendMail();
                send.execute("");

            }
        });



        return rootView;
    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;
private String z="";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
           // super.onPreExecute();
            progressDialog = ProgressDialog.show(rootView.getContext(), "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
           // super.onPostExecute(aVoid);
                      progressDialog.dismiss();

            if(isSuccess) {
                Toast.makeText(rootView.getContext(), z, Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(rootView.getContext(), "Could not send email", Toast.LENGTH_LONG).show();
            }
        }

        protected Void doInBackground(String... params) {



            try {
                Mail m = new Mail("Info@sqaloitsolutions.co.za", "Info@01");
                //String to = "jabun@ngobeniholdings.co.za";
                // String to = "sntshangase3@gmail.com";
                // m.setFrom("Info@ngobeniholdings.co.za");
                // String to = "SibusisoN@sqaloitsolutions.co.za";
                String to = "support@goingdots.com";
                String from =txtFrom.getText().toString();
                String subject = txtSubject.getText().toString();
                String message = txtMessage.getText().toString()+"\n\n------\nRegards - GoingDots App\n\nThis email was intended for & sent to you by GoingDots App\nA platform of J.H. Ngobeni  Digicomm Company\nHead Office – Kempton Park, Gauteng – South Africa";

                String[] toArr = {to};
                m.setTo(toArr);
                m.setFrom(from);
                m.setSubject(subject);
                m.setBody(message);

                if(m.send()) {

                    z="Email was sent successfully.";
                    isSuccess=true;
                } else {
                    z="Could not send to email";
                    isSuccess = false;
                }
            } catch(Exception e) {
             txtFrom.setBackground(getResources().getDrawable(R.drawable.edittexterror_bground));
            }
            return null;
        }
    }

}
