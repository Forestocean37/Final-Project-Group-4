package edu.neu.final_project_group_4.ui.ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.final_project_group_4.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<String> messages = new ArrayList<>();

    public void setMessages(List<String> newMessages) {
        this.messages = newMessages != null ? newMessages : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String message = messages.get(position);

        if (message.startsWith("User:")) {
            holder.userMessageLayout.setVisibility(View.VISIBLE);
            holder.aiMessageLayout.setVisibility(View.GONE);
            holder.userMessageTextView.setText(message.substring(6));
        } else if (message.startsWith("AI:")) {
            holder.aiMessageLayout.setVisibility(View.VISIBLE);
            holder.userMessageLayout.setVisibility(View.GONE);
            holder.aiMessageTextView.setText(message.substring(4));
        }
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        LinearLayout aiMessageLayout, userMessageLayout;
        TextView aiMessageTextView, userMessageTextView;
        ImageView aiIcon, userIcon;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for AI and user messages
            aiMessageLayout = itemView.findViewById(R.id.aiMessageLayout);
            userMessageLayout = itemView.findViewById(R.id.userMessageLayout);
            aiMessageTextView = itemView.findViewById(R.id.aiMessageTextView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
            aiIcon = itemView.findViewById(R.id.aiIcon);
            userIcon = itemView.findViewById(R.id.userIcon);
        }
    }
}
