# 🤖 Resume Chatbot Web App

This is a simple chatbot web application that simulates a job interview by answering questions about my resume. It uses keyword-based detection to match user questions with predefined answers.

---

## 📂 Project Structure

📦 ResumeChatbot
├── backend/
│ └── ResumeChatbotServer.java # Java backend with keyword-response logic
├── frontend/
│ ├── index.html # Basic user interface
│ ├── script.js # Sends user input to backend & displays responses
│ └── styles.css #  CSS styling

---

## 🧪 How to Run Locally

### 1️⃣ Start the Java Backend Server

Open a first terminal and run:
cd backend
javac ResumeChatbotServer.java  
java ResumeChatbotServer    


### 2️⃣ Start the Frontend 

Open a second terminal and run:
cd frontend
python -m http.server 5500

Keep both terminals running at the same time.

Then open your browser and visit:
👉 http://localhost:5500

Now you're ready to chat with the bot!

---

🚀 How It Works
The frontend provides a simple UI for asking questions.

User input is sent to the Java backend.

The backend detects keywords and returns predefined answers.

Responses are displayed in the chat interface.

---

💡 Sample Questions to Ask the Chatbot

Tell me about yourself
What are your skills?
Where did you study?
What projects have you worked on?
What motivates you?
What are your strengths?

---

🛠 Tech Stack
Java (Backend server)

HTML, JavaScript (Frontend)

CSS

Python HTTP Server (for serving the frontend)
