package martinflambard.androidmiaou2;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

import static martinflambard.androidmiaou2.FonctionUtils.StringToBitmap;

/**
 * Created by William on 09/02/2017.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> messages;

    public MessageRecyclerAdapter() {
        messages = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MessageType.TEXT:
                viewHolder = new MessageTextHolder(inflater.inflate(R.layout.message_text, parent, false));
                break;
            case MessageType.PHOTO:
                viewHolder = new MessagePhotoHolder(inflater.inflate(R.layout.message_photo, parent, false));
                break;
            default:
                viewHolder = new MessageTextHolder(inflater.inflate(R.layout.message_text, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case MessageType.TEXT:
                MessageTextHolder messageTextHolder = (MessageTextHolder) holder;
                messageTextHolder.set(messages.get(position));
                break;
            case MessageType.PHOTO:
                MessagePhotoHolder messagePhotoHolder = (MessagePhotoHolder) holder;
                messagePhotoHolder.set(messages.get(position));
                break;
            default:
                MessageTextHolder message = (MessageTextHolder) holder;
                message.set(messages.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public void addItem(Message msg){
        this.messages.add(msg);
    }

    public class MessagePhotoHolder extends RecyclerView.ViewHolder {
        private FrameLayout itemContainer;
        private LinearLayout messagePhotoBox;
        private ImageView messagePhoto;
        private TextView messagePhotoTimestamp;

        public MessagePhotoHolder(View v) {
            super(v);
            itemContainer = (FrameLayout)v.findViewById(R.id.message_text_container);
            messagePhotoBox = (LinearLayout)v.findViewById(R.id.message_photo_box);
            messagePhoto = (ImageView)v.findViewById(R.id.message_photo);
            messagePhotoTimestamp = (TextView)v.findViewById(R.id.message_photo_timestamp);
        }

        public void set(Message msg){
            messagePhoto.setImageBitmap(StringToBitmap(msg.getMessage()));
            messagePhotoTimestamp.setText(msg.getTimestamp());
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) messagePhotoBox.getLayoutParams();
            if (msg.getUser().equals(UserDetails.username)){
                messagePhotoBox.setGravity(Gravity.RIGHT);
                lp.setMargins(150, 0, 25, 10);
                messagePhotoBox.setBackgroundResource(R.drawable.rounded_corner1);
            }
            else{
                messagePhotoBox.setGravity(Gravity.START);
                lp.setMargins(25, 0, 150, 10);
                messagePhotoBox.setBackgroundResource(R.drawable.rounded_corner2);
            }
        }
    }

    public class MessageTextHolder extends RecyclerView.ViewHolder {
        private LinearLayout messageTextBox;
        private TextView messageText;
        private TextView messageTextTimestamp;

        public MessageTextHolder(View v) {
            super(v);
            messageTextBox = (LinearLayout) v.findViewById(R.id.message_text_box);
            messageText = (TextView)v.findViewById(R.id.message_text);
            messageTextTimestamp = (TextView)v.findViewById(R.id.message_text_timestamp);
        }

        public void set(Message msg){
            messageText.setText(msg.getMessage());
            messageTextTimestamp.setText(msg.getTimestamp());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (msg.getUser().equals(UserDetails.username)){
                lp.setMargins(150, 0, 25, 10);
                lp.gravity = Gravity.FILL_VERTICAL;
                messageTextBox.setLayoutParams(lp);
                messageTextBox.setBackgroundResource(R.drawable.rounded_corner1);
            }
            else{
                messageTextBox.setGravity(Gravity.START);
                lp.setMargins(25, 0, 150, 10);
                messageTextBox.setBackgroundResource(R.drawable.rounded_corner2);
            }
        }
    }
}
