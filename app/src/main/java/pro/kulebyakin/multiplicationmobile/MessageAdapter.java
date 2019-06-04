package pro.kulebyakin.multiplicationmobile;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import android.text.format.DateFormat;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> list;
    ProgressBar mProgressBar;

    public MessageAdapter(List<Message> list, ProgressBar mProgressBar) {
        this.list = list;
        this.mProgressBar = mProgressBar;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int position) {
        Message message = list.get(position);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        messageViewHolder.messageTextView.setText(message.getText());
        messageViewHolder.messengerTextView.setText(message.getName());
        messageViewHolder.timeMessageTextView
                .setText(DateFormat.format("dd-MM-yyyy HH:mm", message.getTimeMessage()));

        if (message.getPhotoUrl() == null) {
            messageViewHolder.messengerImageView.setImageResource(R.drawable.horse);
        } else {
            Picasso.get().load(message.getPhotoUrl()).into(messageViewHolder.messengerImageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;
        TextView timeMessageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            timeMessageTextView = itemView.findViewById(R.id.timeMessageTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            messengerTextView = itemView.findViewById(R.id.messengerTextView);
            messengerImageView = itemView.findViewById(R.id.messengerImageView);

        }
    }

}
