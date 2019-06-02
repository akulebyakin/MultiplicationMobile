package pro.kulebyakin.multiplicationmobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Chat";
    public static final String MESSAGES_CHILD = "messages";
    public static final String ANONYMOUS = "anonymous";

    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    private RelativeLayout chatRelativeLayout;
    private RecyclerView chatRecyclerView;
    private Button chatSendButton;
    private ProgressBar progressBar;
    private EditText messageEditText;

    private String mUsername;
    private String mPhotoUrl;

    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

//    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mUsername = "Anonymous";

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            //startActivity(new Intent(this, SignInActivity.class));
            //finish();
            //return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API)
//                .build();

        messageList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(MESSAGES_CHILD);

        chatRelativeLayout = findViewById(R.id.chat_relative_layout);
        chatSendButton = findViewById(R.id.chat_send_button);
        messageEditText = findViewById(R.id.chat_message_edit_text);
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    chatSendButton.setEnabled(true);
                } else {
                    chatSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        progressBar = findViewById(R.id.chat_progress_bar);
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message (
                        messageEditText.getText().toString(),
                        mUsername,
                        mPhotoUrl);
                myRef.push().setValue(message);
                messageEditText.setText("");
            }
        });

        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(mLinearLayoutManager);

        messageAdapter = new MessageAdapter(messageList, progressBar);
        messageAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = messageAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    chatRecyclerView.scrollToPosition(positionStart);
                }
            }
        });
        chatRecyclerView.setAdapter(messageAdapter);

        updateList();
        // TODO Сделать проверку на пустой лист сообщений
        //checkIfEmpty();
    }

    private void updateList() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageList.add(dataSnapshot.getValue(Message.class));
                messageAdapter.notifyDataSetChanged();
                //checkIfEmpty();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                int index = getItemIndex(message);

                messageList.set(index, message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                int index = getItemIndex(message);

                messageList.remove(index);
                messageAdapter.notifyDataSetChanged();
                //checkIfEmpty();
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
    private int getItemIndex(Message message) {
        int index = -1;
        for (int i = 0; i < messageList.size(); i++) {
            if (messageList.get(i).getName().equals(message.getName())) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bg1:
                chatRelativeLayout.setBackgroundResource(R.drawable.bg5);
                return true;
            case R.id.bg2:
                chatRelativeLayout.setBackgroundResource(R.drawable.bg13);
                return true;
            case R.id.bg3:
                chatRelativeLayout.setBackgroundResource(R.drawable.bg14);
                return true;
            case R.id.bg4:
                chatRelativeLayout.setBackgroundResource(R.drawable.bg15);
                return true;
            case R.id.bg5:
                chatRelativeLayout.setBackgroundResource(R.drawable.bg16);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
