package com.example.rotory.Contents;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rotory.R;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ReportDialog {

    private void openReportDialog(Spinner reportSpinner, QueryDocumentSnapshot pDocument, String pDocumentId) {

     /*   //reportSpinner = getView().findViewById(R.id.reportSpinner);

        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reportList, android.R.layout.simple_spinner_dropdown_item);

        reportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportSpinner.setAdapter(reportAdapter);

        reportSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
    }
}
