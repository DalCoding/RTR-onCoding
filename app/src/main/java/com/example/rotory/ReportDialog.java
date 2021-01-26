package com.example.rotory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

//import com.example.rotory.Adapter.ReportAdapter;

public class ReportDialog extends DialogFragment implements View.OnClickListener{
    TextView reportTextView;
    public static final String TAG_EVENT_DIALOG = "report";
    Spinner reportSpinner;
    //ReportAdapter reportAdapter;

    public ReportDialog(){}
    public static ReportDialog getInstance() {
        ReportDialog e = new ReportDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.report, container);
        reportTextView = v.findViewById(R.id.reportTextView);
        reportSpinner = v.findViewById(R.id.reportSpinner);

        reportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
               reportTextView.setText(reportSpinner.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                reportTextView.setText("");
            }
        });

        return v;
        //showToast(getContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
}
    private void showToast(String s) {Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();}

    @Override
    public void onClick(View v) {
    dismiss();
    }


}

//https://altongmon.tistory.com/254
//https://kukuta.tistory.com/147
//https://machine-woong.tistory.com/53
//https://developer88.tistory.com/132


