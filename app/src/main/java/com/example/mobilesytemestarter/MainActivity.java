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

    String memory1 = null;

    String memory2 = null;

    boolean toggleMemorySlot = false;

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

    public void leerzeichen(View v) {
        expression.append(" ");
        anzeige.setText(expression.toString());
    }

    public void memorySave(View v) {
        String current = anzeige.getText().toString();
        if (!current.equals("Fehler") && !current.isEmpty()) {
            if (toggleMemorySlot) {
                memory2 = current;
                anzeige.setText("Gespeichert: M2");
            } else {
                memory1 = current;
                anzeige.setText("Gespeichert: M1");
            }
            toggleMemorySlot = !toggleMemorySlot; // Beim nächsten Klick wird gewechselt
        }
    }

    public void memoryRead(View v) {
        String recalled = toggleMemorySlot ? memory2 : memory1;
        if (recalled != null) {
            expression.append(recalled).append(" ");
            anzeige.setText(expression.toString());
            toggleMemorySlot = !toggleMemorySlot;
        } else {
            anzeige.setText("Kein Wert in M" + (toggleMemorySlot ? "2" : "1"));
        }
    }

    public boolean memoryReadLong(View v) {
        StringBuilder sb = new StringBuilder();
        if (memory1 != null) sb.append("M1: ").append(memory1).append("\n");
        if (memory2 != null) sb.append("M2: ").append(memory2);
        if (sb.length() == 0) {
            anzeige.setText("Kein Verlauf");
        } else {
            anzeige.setText(sb.toString());
        }
        return true; // true = Langklick wurde behandelt
    }


    public void berechneAnzeige(View view) {
        try {
            String[] tokens = expression.toString().trim().split("\\s+");
            String infix = UPNToInfixConverter.convertToInfix(tokens);

            if (infix.equals("ERROR")) {
                anzeige.setText("Fehler");
                expression.setLength(0);
                return;
            }

            org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
            rhino.setOptimizationLevel(-1);
            Scriptable scope = rhino.initStandardObjects();
            String result = rhino.evaluateString(scope, infix, "JavaScript", 1, null).toString();

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

