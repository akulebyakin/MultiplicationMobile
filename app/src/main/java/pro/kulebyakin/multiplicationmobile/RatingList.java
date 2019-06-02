package pro.kulebyakin.multiplicationmobile;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RatingList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<User> result;
    private UserAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    
    private TextView noDataTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        result = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");

        noDataTextView = findViewById(R.id.no_data_text_view);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        // createresult();

        adapter = new UserAdapter(result);
        recyclerView.setAdapter(adapter);

        updateList();
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (result.size() == 0) {
            recyclerView.setVisibility(View.INVISIBLE);
            noDataTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        switch (item.getItemId()){
            case 0:
                Toast.makeText(this, "Недостаточно прав", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, "Недостаточно прав", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void createresult () {
        int rnd = 0;
        for (int i = 0; i < 50; i++) {
            rnd = (int) (Math.random() * 50.0);
            result.add(new User("Test Name", rnd, null));
        }

    }

    private void updateList() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                result.add(dataSnapshot.getValue(User.class));
                Collections.sort(result);
                adapter.notifyDataSetChanged();
                checkIfEmpty();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                User user = dataSnapshot.getValue(User.class);
                int index = getItemIndex(user);

                result.set(index, user);
                Collections.sort(result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int index = getItemIndex(user);

                result.remove(index);
                Collections.sort(result);
                adapter.notifyDataSetChanged();
                checkIfEmpty();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Ищем индекс
    private int getItemIndex(User user) {
        int index = -1;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).username.equals(user.username)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
