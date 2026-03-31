package com.example.mediaapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private Context context;
    private List<NoteModel> notesList;

    public NotesAdapter(Context context, List<NoteModel> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteModel note = notesList.get(position);
        holder.tvTitle.setText(note.getTitle());
        holder.tvDescription.setText(note.getDescription());
        holder.tvDate.setText(note.getDate());
        holder.tvType.setText(note.getNoteType());

        if (note.getImagePath() != null && !note.getImagePath().isEmpty()) {
            try {
                // Using try-catch to prevent crashes if image access is denied or invalid
                holder.ivImage.setImageURI(Uri.parse(note.getImagePath()));
            } catch (Exception e) {
                holder.ivImage.setImageResource(android.R.drawable.ic_menu_report_image);
            }
        } else {
            holder.ivImage.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvDate, tvType;
        ImageView ivImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvDescription = itemView.findViewById(R.id.tvNoteDescription);
            tvDate = itemView.findViewById(R.id.tvNoteDate);
            tvType = itemView.findViewById(R.id.tvNoteType);
            ivImage = itemView.findViewById(R.id.ivNoteImage);
        }
    }
}
