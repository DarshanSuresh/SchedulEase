# 📘 SchedulEase – Advanced Exam Scheduling System (Java Swing)

**SchedulEase** is a graphical Exam Scheduler application built using **Java Swing**.  
It automates the process of assigning rooms, validating timing, distributing roll numbers, and storing exam information efficiently in JSON format.

Designed with **OOP principles**, modular components, and a user-friendly GUI interface, SchedulEase is suitable for both academic submissions and real-world institutional use.

---

## 🚀 Features

### ✔ GUI-Based Scheduling
- Interactive Java Swing interface  
- Add, edit, delete, save, and load exams  
- Tabular display of scheduled exams  

### ✔ Automatic Room Assignment
- Rooms used: **N201** and **N203**  
- Each room supports **30 students**  
- Automatically assigns a non-conflicting room  

### ✔ Time-Clash Prevention
- Prevents overlapping exams in the same room  
- Validates AM/PM formatted times  
- Ensures start time < end time  

### ✔ Automatic Day Detection
- Date input: **YYYY-MM-DD**  
- System auto-calculates the weekday (e.g., Monday, Friday)

### ✔ Round-Robin Roll Distribution
- Roll numbers: **1 to 60**  
- Odd rolls → Room **N201**  
- Even rolls → Room **N203**  
- Ensures balanced seating  

### ✔ JSON-Based Persistence
- Saves exam data to `data/exams.json`  
- Loads previously stored exams  
- Clean, structured, portable file format  

### ✔ Modular Architecture
| Component | Responsibility |
|----------|----------------|
| **MainGUI** | Core user interface and actions |
| **AddEditDialog** | Exam input form |
| **Exam** | Data model for each exam |
| **ExamScheduler** | Logic: room allocation, clash prevention, roll distribution, validations |
| **ExamTableModel** | Handles JTable data for display |
| **FileExamStorage** | JSON saving and loading |

---

## 📁 Project Structure

