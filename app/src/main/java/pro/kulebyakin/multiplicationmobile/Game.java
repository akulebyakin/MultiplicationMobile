package pro.kulebyakin.multiplicationmobile;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Timer;

public class Game extends Activity {

    //final String FILE_NAME = "results.txt";
    private int answer = 0;
    private int points = 0;
    private int numberRightAnswers = 0;
    private int numberFalseAnswers = 0;
    String name;
    TextView usernameTextView;

    String currentUserUID;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        name = getIntent().getStringExtra("name");
        currentUserUID = getIntent().getStringExtra("currentUserUID");

        myRef = FirebaseDatabase.getInstance().getReference("users");

        usernameTextView = findViewById(R.id.username_text_view);
        usernameTextView.setText("Username: " + name);

        TextView timerTextView = findViewById(R.id.timer_text_view);

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                int time = (int) l / 1000;
                int minutes = (time / 60);
                String strMin = "0";
                if (minutes < 10) {
                    strMin += minutes;
                } else {
                    strMin = String.valueOf(minutes);
                }

                int seconds = (time) % 60;
                String strSec = "0";
                if (seconds < 10) {
                    strSec += seconds;
                } else {
                    strSec = String.valueOf(seconds);
                }
                timerTextView.setText(strMin + ":" + strSec);
            }

            @Override
            public void onFinish() {
                // TODO (2) Make finish method

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(currentUserUID).getValue(User.class);
                        //bestResult = user.result;
                        if (user == null)  {
                            writeNewUser(currentUserUID, name, points);
                        } else if (user.result < points) {
                            writeNewUser(currentUserUID, name, points);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                endGame();
            }
        }.start();

        startGame();
    }

    public void finishGame(View view) {
        finish();
    }

    private void startGame() {
        nextMission();
    }

    private void writeNewUser(String userId, String name, int result) {
        User user = new User(name, result);
        myRef.child(userId).setValue(user);
    }

    private void endGame() {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(this);
        a_builder.setMessage("Время вышло!" +
                "\nВаш счёт: " + points +
                "\nПравильных ответов: " + numberRightAnswers +
                "\nНеправильных ответов: " + numberFalseAnswers)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("Game over");
        alert.show();
    }

//    void printInFile(int points) {
//        BufferedWriter bufferedWriter = null;
//        try {
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILE_NAME, MODE_APPEND)));
//            bufferedWriter.write(name + " - " + points + " points\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//            ;
//        } finally {
//            try {
//                bufferedWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void nextMission() {
        TextView questionTextView = findViewById(R.id.question_text_view);
        Button answerButton1 = findViewById(R.id.btn1);
        Button answerButton2 = findViewById(R.id.btn2);
        Button answerButton3 = findViewById(R.id.btn3);
        Button answerButton4 = findViewById(R.id.btn4);

        int numberOne = getNumber();
        int numberTwo = getNumber();
        questionTextView.setText(String.valueOf(numberOne) + " x " + String.valueOf(numberTwo));
        answer = numberOne * numberTwo;

        Button[] answerButtons = {answerButton1, answerButton2, answerButton3, answerButton4};

        setRandomAnswerToButton(answerButtons);

        //TODO (4) Кажется, наиболее вероятный правильный ответ будет 1 кнопкой
        int buttonGoodNumber = 0;
        for (int i = 0; i < (int) (10.0 * Math.random()); i++) {
            buttonGoodNumber = i;
            if (i > 3) {
                buttonGoodNumber = 0;
            }
        }
        Button goodButton = answerButtons[buttonGoodNumber];
        goodButton.setText(String.valueOf(answer));

        testDoubleRandomAnswerToButton(answerButtons);

    }

    public void checkAnswer(View view) {
        Button currentButton = (Button) view;

        if (currentButton.getText().equals(String.valueOf(answer))) {
            points += 1;
            numberRightAnswers += 1;
            drawPoints();
            // TODO: Сделать информирование об успешности ответа без всплывающего сообщения
        } else {
            points -= 1;
            numberFalseAnswers += 1;
            drawPoints();
        }
        nextMission();
    }

    //Варианты чисел умножени от [2-9]
    private int getNumber() {
        int number = getRandomNumber();
        while (number < 2) {
            number = getRandomNumber();
        }
        return number;
    }

    private int getRandomNumber() {
        return (int) (10.0 * Math.random());
    }

    private void setRandomAnswerToButton(Button[] buttons) {
        for (Button button : buttons) {
            button.setText(String.valueOf(getNumber() * getNumber()));
        }
    }

    //Убирает одинаковые ответы в кнопках
    private void testDoubleRandomAnswerToButton(Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons.length; j++) {
                if (i == j) {
                    continue;
                }
                while (buttons[i].getText().equals(buttons[j].getText())) {
                    int newNumber = getNumber() * getNumber();
                    buttons[i].setText(String.valueOf(newNumber));
                }
            }
        }
    }

    //TODO Сейчас points может быть отрицательным. Думаю, стоит поправить
    private void drawPoints() {
        TextView pointsTextView = findViewById(R.id.points_text_id);
        pointsTextView.setText("Score: " + String.valueOf(points) + " points");
    }


}
