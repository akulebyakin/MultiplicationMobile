package pro.kulebyakin.multiplicationmobile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> list;
    private String rating = "1";

    public UserAdapter(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        User user = list.get(position);

        userViewHolder.nameTextView.setText(user.username);
        userViewHolder.scoreTextView.setText(Integer.toString(user.result));
        userViewHolder.ratingTextView.setText((position + 1) + ".");

        userViewHolder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(userViewHolder.getAdapterPosition(), 0, 0, "Удалить");
                menu.add(userViewHolder.getAdapterPosition(), 1, 0, "Изменииь");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView ratingTextView, nameTextView, scoreTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            ratingTextView = itemView.findViewById(R.id.rating_text_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            scoreTextView = itemView.findViewById(R.id.score_text_view);


        }
    }

}
