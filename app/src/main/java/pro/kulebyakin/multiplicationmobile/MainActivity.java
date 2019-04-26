package pro.kulebyakin.multiplicationmobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startGame(View view) {

        EditText nameEditText = findViewById(R.id.name);
        String name = nameEditText.getText().toString().trim().replaceAll("\\s+", "_");;
        if (name.length() != 0) {
            Intent intent = new Intent(this, Game.class);
            intent.putExtra("name", name);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Enter your name!", Toast.LENGTH_SHORT).show();
        }


    }

    public void results(View view) {
        Intent intent = new Intent(this, Results.class);
        startActivity(intent);
    }

    public void returnBack(View view) {
        setContentView(R.layout.activity_main);
    }

    public void exitGame(View view) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage("Ты че офигел выходить?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert= a_builder.create();
        alert.setTitle("Close app");
        alert.show();
    }
}
