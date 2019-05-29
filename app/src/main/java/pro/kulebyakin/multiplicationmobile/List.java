package pro.kulebyakin.multiplicationmobile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

public class List extends AppCompatActivity {

    String name;
    int result = 37;

    private DatabaseReference myRef;
    private List list;

    ListView listView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        name = getIntent().getStringExtra("name");
        listView = findViewById(R.id.list_view);
        textView = findViewById(R.id.text_view);

        // Write to DataBase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        //myRef.setValue("Hello, World!");
        //myRef.child("users").child("USER1").child("username").setValue("Vasya");

        //Получаем объект БД и ссылку на него
//        myRef = FirebaseDatabase.getInstance().getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.child(name).getValue(User.class);
//                String value = dataSnapshot.getValue(String.class);
                //textView.setText("Value is: " + value);
//                Toast.makeText(List.this, "Value is " + value, Toast.LENGTH_SHORT).show();

                if (user == null) {
                    writeNewUser(name,name,result);
                    //textView.setText("New user!\nName: " + user.username + "\nResult: " + user.result);
                } else {
                    textView.setText("User already exists!\nName: " + user.username + "\nResult: " + user.result);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(List.this, "Fail to read value ", Toast.LENGTH_SHORT).show();
            }
        });

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                GenericTypeIndicator<List> t = new GenericTypeIndicator<List>(){};
//                list = dataSnapshot.getValue(t);
//
////                updateUI();
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void writeNewUser(String userId, String name, int result) {
        User user = new User(name, result);
        myRef.child(userId).setValue(user);
    }

//    private void updateUI() {
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list);
//
//        listView.setAdapter(adapter);
//    }
}
