# 📒 JotDown – Media Notes App

![Platform](https://img.shields.io/badge/Platform-Android-green)
![Language](https://img.shields.io/badge/Language-Java-orange)
![Database](https://img.shields.io/badge/Database-SQLite-blue)
![IDE](https://img.shields.io/badge/IDE-Android%20Studio-yellow)

JotDown is a lightweight Android-based Media Notes application developed to provide users with a simple and efficient way to manage personal notes with multimedia attachments.

The application integrates multiple Android components including:
- SQLite Database for local persistence
- WorkManager for background reminders
- RecyclerView for efficient note listing
- Accelerometer Sensor for motion detection
- Persistent URI Permissions for secure image access
---
## SQLite Database Structure

### Database Name
`NotesDB_12`

### Table Name
`notes_12`

| Column Name | Type | Description |
|-------------|------|-------------|
| id | INTEGER | Primary Key, Auto Increment |
| title | TEXT | Title of the note |
| description | TEXT | Detailed note content |
| image_path | TEXT | Persistent image URI |
| date | TEXT | Creation date |
| note_type | TEXT | Category of note |

---
## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/f8eb6752-ebd9-4825-8256-e3a5c9fc68a7" width="200"/>
  <img src="https://github.com/user-attachments/assets/e763b3b9-f209-4b4d-ae72-1b99947308ff" width="200"/>
  <img src="https://github.com/user-attachments/assets/c259fc5d-992c-4050-b673-462229a329ea" width="200"/>
  <img src="https://github.com/user-attachments/assets/5a7bab83-f1bd-473a-9016-b6d9947ce19e" width="200"/>
</p>

# 📄 License

This project is developed for educational and learning purposes.

---
