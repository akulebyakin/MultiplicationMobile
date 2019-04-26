package pro.kulebyakin.multiplicationmobile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Results extends AppCompatActivity {

    final String FILE_NAME = "results.txt";
    private  String name = "Andrey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        printInFile(12);
        printInFile(16);
        printInFile(1);
        TextView resultTextView = findViewById(R.id.results_text_view);

        //Делаем скролл в TextView
        //resultTextView.setMovementMethod(new ScrollingMovementMethod());
        resultTextView.setText(readFromFile());
    }

    public void returnBack(View view) {
        finish();
    }

    void printInFile (int points) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILE_NAME, MODE_APPEND)));
            bufferedWriter.write(name + " - " + points + " points\n");
        } catch (IOException e){
            e.printStackTrace();;
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readFromFile () {
        String currentLine;
        String tempLine[];
        String str;
        List<List<String>> arrayList = new ArrayList<>();
        BufferedReader bufferedReader = null; // Вместо файла можно указать имя
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(openFileInput(FILE_NAME)));

            // Читаем в ArrayList
            while ((currentLine = bufferedReader.readLine()) != null) {
                tempLine = currentLine.split(" ");
                arrayList.add(Arrays.asList(tempLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO (1) Разобраться в этой магии
        Collections.sort(arrayList, ((left, right) -> Integer.parseInt(right.get(2)) - Integer.parseInt(left.get(2))));

        // Преобразовываем обратно в строку для вывода и добавляем номер
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < arrayList.size(); i++) {
            stringBuilder.append((i + 1) + ". ");
            for (int j = 0; j < arrayList.get(i).size(); j++) {
                stringBuilder.append(arrayList.get(i).get(j));
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        str = stringBuilder.toString();

        return str;
    }
}
