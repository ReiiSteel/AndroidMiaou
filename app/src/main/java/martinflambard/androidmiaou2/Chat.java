package martinflambard.androidmiaou2;

/**
 * Created by wilfi on 13/01/2017.
 */

        import android.graphics.Color;
        import android.graphics.Path;
        import android.graphics.Typeface;
        import android.os.SystemClock;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.Toolbar;
        import android.text.Layout;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.EditText;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.ScrollView;
        import android.widget.TextView;

        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.collection.LLRBNode;

        import java.util.Calendar;
        import java.util.HashMap;
        import java.util.Map;

public class Chat extends AppCompatActivity {
    private Toolbar toolbar;
    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.mipmap.polygon);
        toolbar.setTitle("Chating with "+UserDetails.chatWith);
        setSupportActionBar(toolbar);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidmiaou-b586c.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://androidmiaou-b586c.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("timestamp", Timestamp());
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }

                messageArea.setText("");
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String timestamp = "no timestamp";
                if(map.size()>2){timestamp = map.get("timestamp").toString();}



                if(userName.equals(UserDetails.username)){
                    //addMessageBox("You:-\n" + message, 1);
                    addMessageBox(message, timestamp,true);
                }
                else{
                    //addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                    addMessageBox(message, timestamp, false);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private String Timestamp(){
        Calendar cal = Calendar.getInstance();
        String ret = cal.get(Calendar.DAY_OF_MONTH)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.YEAR)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
        return ret;
    }

    public void addMessageBox(String message, String timestamp, boolean fromSelf){
        TextView messageView = new TextView(Chat.this);
        messageView.setText(message);
        messageView.setTextColor(Color.BLACK);
        TextView timestampView = new TextView(Chat.this);
        timestampView.setText(timestamp);
        timestampView.setTextColor(Color.GRAY);
        timestampView.setTypeface(null, Typeface.ITALIC);
        LinearLayout messageBox = new LinearLayout(Chat.this);
        messageBox.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParamsForMessage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsForTimestamp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsForBox = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(fromSelf) {
            layoutParamsForBox.setMargins(25, 0, 150, 10);
            messageBox.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            layoutParamsForBox.setMargins(150, 0, 25, 10);
            messageBox.setBackgroundResource(R.drawable.rounded_corner2);
        }

        messageBox.addView(messageView, layoutParamsForMessage);
        messageBox.addView(timestampView, layoutParamsForTimestamp);
        layout.addView(messageBox, layoutParamsForBox);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}