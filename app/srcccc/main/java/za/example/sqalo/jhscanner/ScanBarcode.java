package za.example.sqalo.jhscanner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanBarcode extends AppCompatActivity {
    MainActivity activity =   MainActivity.instance;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    Button btn_scan,btn_accept;
    TextView scan_results;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_upc);

        fragmentManager = getFragmentManager();
           btn_scan = (Button) findViewById(R.id.btn_scan);

        scan_results = (TextView) findViewById(R.id.scan_results);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator scanIntegrator = new IntentIntegrator(ScanBarcode.this);
                scanIntegrator.initiateScan();
            }
        });



    }


    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {

            String contents = intent.getStringExtra( "SCAN_RESULT" );
            String format = intent.getStringExtra( "SCAN_RESULT_FORMAT" ) ;
           // Toast.makeText(ScanBarcode.this, "Content" + contents , Toast.LENGTH_LONG).show();
            scan_results.setText(contents);

           Intent intent1 = new Intent(ScanBarcode.this, MainActivity.class);

            Bundle extras = new Bundle();
            extras.putString("barcode", scan_results.getText().toString());
            extras.putString("username",activity.edtuseremail.getText().toString());
            extras.putString("password",activity.edtpass.getText().toString());
            intent1.putExtras(extras);
            startActivity(intent1);
        }
    }
}
