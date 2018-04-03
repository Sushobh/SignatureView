package org.sushobh.signatureview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.sushobh.signview.Option;
import org.sushobh.signview.OptionClickListener;
import org.sushobh.signview.SignatureView;

public class MainActivity extends AppCompatActivity {


    SignatureView signatureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signatureView = new SignatureView(this);
        signatureView.addOption(new Option("Save ",R.drawable.ic_save));
        signatureView.setOptionClickListener(new OptionClickListener() {
            @Override
            public void clickedOnOption(Option option) {
                Log.i("Clicked on option !",option.getTitle());
            }
        });


        setContentView(signatureView);
    }
}
