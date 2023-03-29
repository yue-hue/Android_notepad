package com.example.notepad_1;

public class Note {
    private int noteId;
    private String noteName;
    private String noteDate;
    private String noteText;

    public Note() {
    }

    public Note(int noteId, String noteName, String noteDate, String noteText) {
        this.noteId = noteId;
        this.noteName = noteName;
        this.noteDate = noteDate;
        this.noteText = noteText;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int id) {
        this.noteId = noteId;
    }
}