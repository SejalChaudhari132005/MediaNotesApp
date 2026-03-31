package com.example.mediaapp;

public class NoteModel {
    private int id;
    private String title;
    private String description;
    private String imagePath;
    private String date;
    private String noteType;

    public NoteModel(int id, String title, String description, String imagePath, String date, String noteType) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.date = date;
        this.noteType = noteType;
    }

    public NoteModel() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNoteType() { return noteType; }
    public void setNoteType(String noteType) { this.noteType = noteType; }
}
