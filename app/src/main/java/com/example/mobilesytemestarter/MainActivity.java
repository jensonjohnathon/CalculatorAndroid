package com.example.mobilesytemestarter;

import android.content.Context;
import android.os.Bundle;
import org.mozilla.javascript.Scriptable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView anzeige;

    StringBuilder expression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anzeige = findViewById(R.id.anzeige);

    }

    public void addNumOrOperator(View v) {
        Button b = (Button) v;
        expression.append(b.getText());
        anzeige.setText(expression.toString());
    }

    public void addMultiple(View v) {
        expression.append("*");
        anzeige.setText(expression.toString());
    }


    public void onClear(View v) {
        expression.setLength(0);
        anzeige.setText("Fangen sie an zu tippen...");
    }

    public void einzelClear(View v) {
        if (expression.length() > 0) {
            expression.deleteCharAt(expression.length() - 1);
            anzeige.setText(expression.toString());
        }
    }

    public void berechneAnzeige(View view) {
        try {
            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
            rhino.setOptimizationLevel(-1);

            Scriptable scope = rhino.initStandardObjects();
            String result = rhino.evaluateString(scope, expression.toString(), "JavaScript", 1, null).toString();

            anzeige.setText(result);
            expression.setLength(0);
            expression.append(result);
        } catch (Exception e) {
            anzeige.setText("Fehler");
            expression.setLength(0);
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }

}