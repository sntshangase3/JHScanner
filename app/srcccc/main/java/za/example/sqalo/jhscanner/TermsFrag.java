package za.example.sqalo.jhscanner;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by sibusison on 2017/07/30.
 */
public class TermsFrag extends Fragment  {

    View rootView;
TextView txtback;

    MainActivity activity =   MainActivity.instance;
    FragmentManager fragmentManager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.terms, container, false);

        txtback = (TextView) rootView. findViewById(R.id.txtback);
        fragmentManager = getFragmentManager();
        txtback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("termsread", "termsread");

                RegisterFrag fragment = new RegisterFrag();
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit();




            }
        });
          return rootView;
    }



}


