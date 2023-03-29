package com.example.notepad_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
        interface OnNoteClickListener {
            void onNoteClick(Note note, int position);
        }

        interface OnNoteLongClickListener {
            void onNoteLongClick(Note note, int position);
        }

        private final OnNoteClickListener onClickListener;
        private final OnNoteLongClickListener onLongClickListener;
        private final LayoutInflater inflater;
        private final List<Note> notes;

        NoteAdapter(Context context, List<Note> notes, OnNoteClickListener onClickListener, OnNoteLongClickListener onLongClickListener) {
            this.onClickListener = onClickListener;
            this.onLongClickListener = onLongClickListener;
            this.notes = notes;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteAdapter.ViewHolder holder, int position) {
            Note note = notes.get(position);

            holder.nameView.setText(note.getNoteName());
            holder.dateView.setText(note.getNoteDate());
            holder.noteView.setText(note.getNoteText());

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    onClickListener.onNoteClick(note, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v)
                {
                    onLongClickListener.onNoteLongClick(note, position);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView nameView, dateView, noteView;

            ViewHolder(View view){
                super(view);
                nameView = view.findViewById(R.id.name);
                dateView = view.findViewById(R.id.date);
                noteView = view.findViewById(R.id.note);
            }
        }
    }