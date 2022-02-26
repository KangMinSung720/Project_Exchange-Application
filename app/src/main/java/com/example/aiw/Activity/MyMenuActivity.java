package com.example.aiw.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.example.aiw.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MyMenuActivity extends Activity {
    private TextView textViewEmail, textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_menu);

        textViewEmail = findViewById(R.id.my_menu_email);
        textViewResult = findViewById(R.id.my_menu_result);

        textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        getPayment();
    }

    public void getPayment() {
        FirebaseFirestore.getInstance().collection("Exchange_List")
                .whereEqualTo("Receiver_Email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String text = "";
                            if (task.getResult().isEmpty()) {
                                textViewResult.setText("거래 내역이 없습니다");
                            } else {
                                for (int i = 0; i < task.getResult().size(); i++) {
                                    String receiverName = task.getResult().getDocuments().get(i).getString("Receiver_Name");
                                    String currencyName = task.getResult().getDocuments().get(i).getString("Exchange_Unit");
                                    int exchangeAmount = task.getResult().getDocuments().get(i).getLong("Exchange_Amount").intValue();
                                    int payedPrice = task.getResult().getDocuments().get(i).getLong("Exchange_Price").intValue();
                                    String deliveryOption = task.getResult().getDocuments().get(i).getString("Receiver_Method");
                                    String deliveryStatus = task.getResult().getDocuments().get(i).getString("Status_Receive");

                                    String currencyUnit;
                                    if (currencyName.equals("미국 달러")) {
                                        currencyUnit = "$";
                                    } else if (currencyName.equals("유로")) {
                                        currencyUnit = "€";
                                    } else {
                                        currencyUnit = "¥";
                                    }

                                    text = text + receiverName + " | " + exchangeAmount + currencyUnit + " | " + payedPrice + "원 " +
                                            " | " + deliveryOption + " | " + deliveryStatus + "\n";
                                }
                                textViewResult.setText(text);
                            }
                        } else {

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }
}
