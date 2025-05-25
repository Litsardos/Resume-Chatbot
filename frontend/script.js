// Adds a message to the chat window. If isUser is true, styles as user message.
function addMessage(message, isUser = false) {
    const chatMessages = document.getElementById('chat-messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isUser ? 'user' : 'bot'}`;
    messageDiv.textContent = message;
    chatMessages.appendChild(messageDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// Sends the user's message to the backend and displays the response.
async function sendMessage() {
    const input = document.getElementById('user-input');
    const message = input.value.trim();
    
    if (message === '') return;
    
    // Add user message to chat
    addMessage(message, true);
    input.value = '';
    
    try {
        const response = await fetch('http://localhost:8080/api/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ message: message })
        });
        
        const data = await response.json();
        addMessage(data.response);
    } catch (error) {
        addMessage('Sorry, I encountered an error. Please try again later.');
        console.error('Error:', error);
    }
}

// Fills the input with a suggested question and sends it.
function askQuestion(question) {
    const input = document.getElementById('user-input');
    input.value = question;
    sendMessage();
}

// Sends the message when the user presses Enter in the input field.
document.getElementById('user-input').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        sendMessage();
    }
}); 