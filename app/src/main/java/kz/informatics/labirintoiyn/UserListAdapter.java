package kz.informatics.labirintoiyn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.informatics.labirintoiyn.models.User;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyTViewHolder> {

    private Context context;
    private ArrayList<User> groupList;

    public class MyTViewHolder extends RecyclerView.ViewHolder{
        public TextView info;
        public TextView userLevel, userRating;

        public MyTViewHolder(View view) {
            super(view);
            info = view.findViewById(R.id.info);
            userLevel = view.findViewById(R.id.userLevel);
            userRating = view.findViewById(R.id.userRating);

        }
    }

    public UserListAdapter(Context context, ArrayList<User> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);

        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position) {
        User item = groupList.get(position);
        holder.info.setText(item.getName());
        holder.userLevel.setText(item.getLevel());
        holder.userRating.setText(item.getRating());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

}