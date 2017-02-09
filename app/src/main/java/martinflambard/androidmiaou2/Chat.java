package martinflambard.androidmiaou2;

/**
 * Created by wilfi on 13/01/2017.
 */

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.provider.MediaStore;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.ImageView;
        import com.firebase.client.ChildEventListener;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.realtime.util.StringListReader;

        import java.util.HashMap;
        import java.util.Map;
        import static martinflambard.androidmiaou2.FonctionUtils.BitmapToString;
        import static martinflambard.androidmiaou2.FonctionUtils.Timestamp;

public class Chat extends AppCompatActivity {
    private Toolbar toolbar;
    ImageView sendButton;
    EditText messageArea;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MessageRecyclerAdapter messageRecyclerAdapter;

    Firebase reference1, reference2;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.mipmap.polygon);
        toolbar.setTitle("Chating with "+UserDetails.chatWith);
        setSupportActionBar(toolbar);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerAdapter = new MessageRecyclerAdapter();
        recyclerView.setAdapter(messageRecyclerAdapter);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://androidmiaou-b586c.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://androidmiaou-b586c.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    //Message msg = new Message(messageText, Timestamp(), MessageType.TEXT, UserDetails.username);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("timestamp", Timestamp());
                    map.put("type", String.valueOf(MessageType.TEXT));
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
                //Message msg = dataSnapshot.getValue(Message.class);
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                messageRecyclerAdapter.addItem(new Message(map.get("message").toString(), map.get("timestamp").toString(), Integer.parseInt(map.get("type").toString()), map.get("user").toString()));
                messageRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.scrollToPosition(messageRecyclerAdapter.getItemCount()-1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                launchCamera();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", BitmapToString(imageBitmap));
            map.put("timestamp", Timestamp());
            map.put("type", String.valueOf(MessageType.PHOTO));
            map.put("user", UserDetails.username);
            reference1.push().setValue(map);
            reference2.push().setValue(map);
        }
    }

    /*public void addMessageBox(String message, String timestamp, boolean fromSelf){
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
        LinearLayout.LayoutParams layoutParamsForBox = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(fromSelf) {
            layoutParamsForBox.gravity = Gravity.RIGHT;
            layoutParamsForBox.setMargins(150, 0, 25, 10);
            messageBox.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            layoutParamsForBox.setMargins(25, 0, 150, 10);
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
    }*/

    /*public void addPhotoBox(Bitmap imageBitmap, String timestamp, boolean fromSelf){
        ImageView imageView = new ImageView(Chat.this);
        imageView.setImageBitmap(imageBitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setAdjustViewBounds(true);
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
            layoutParamsForBox.gravity = Gravity.RIGHT;
            layoutParamsForBox.setMargins(150, 0, 25, 10);
            messageBox.setBackgroundResource(R.drawable.rounded_corner1);
        }
        else{
            layoutParamsForBox.setMargins(25, 0, 150, 10);
            messageBox.setBackgroundResource(R.drawable.rounded_corner2);
        }

        messageBox.addView(imageView, layoutParamsForMessage);
        messageBox.addView(timestampView, layoutParamsForTimestamp);
        layout.addView(messageBox, layoutParamsForBox);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }*/
}