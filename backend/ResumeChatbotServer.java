import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResumeChatbotServer {
    private static final Map<String, String> responses = new HashMap<>();

    // Entry point: Starts the HTTP server and sets up the chat API endpoint.
    public static void main(String[] args) throws IOException {
        // Initialize responses
        initializeResponses();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/chat", (HttpExchange exchange) -> {
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                Headers headers = exchange.getResponseHeaders();
                headers.add("Access-Control-Allow-Origin", "*");
                headers.add("Access-Control-Allow-Methods", "POST, OPTIONS");
                headers.add("Access-Control-Allow-Headers", "Content-Type");
                exchange.sendResponseHeaders(204, -1);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                handleChat(exchange);
            } else {
                exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            }
        });
        server.setExecutor(null);
        System.out.println("Server started on port 8080");
        server.start();
    }

    // Handles incoming chat requests and sends back a JSON response.
    private static void handleChat(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Access-Control-Allow-Origin", "*");

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String userMessage = extractMessage(requestBody);
        String responseText = getResponse(userMessage);
        String jsonResponse = "{\"response\":\"" + escapeJson(responseText) + "\"}";

        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
    }

    // Extracts the value of the "message" key from a simple JSON string.
    private static String extractMessage(String json) {
        String key = "\"message\":";
        int idx = json.indexOf(key);
        if (idx == -1) return "";
        int start = json.indexOf('"', idx + key.length());
        int end = json.indexOf('"', start + 1);
        if (start == -1 || end == -1) return "";
        return json.substring(start + 1, end);
    }

    // Fills the responses map with predefined questions and answers.
    private static void initializeResponses() {
        responses.put("about yourself", "Hi, I'm George Litsardakis, currently a third-year Computer Science student at the University of Crete.");
        responses.put("who are you", "Hi, I'm George Litsardakis, currently a third-year Computer Science student at the University of Crete.");
        responses.put("projects", "I've worked on a web application for event management involving front-end, back-end, and database management. Also, I developed a Java game application as part of my Object-Oriented Programming course.");
        responses.put("worked on", "I've worked on a web application for event management involving front-end, back-end, and database management. Also, I developed a Java game application as part of my Object-Oriented Programming course.");
        responses.put("skills", "I have strong knowledge of C, C++, Java, and SQL. I also have basic familiarity with Python, JavaScript, and HTML.");
        responses.put("expertise", "I have strong knowledge of C, C++, Java, and SQL. I also have basic familiarity with Python, JavaScript, and HTML.");
        responses.put("study", "I am currently studying Computer Science at the University of Crete, now in my third year.");
        responses.put("education", "I am currently studying Computer Science at the University of Crete, now in my third year.");
        responses.put("hobbies", "In my free time, I enjoy exploring new technologies, playing video games, and play sports.");
        responses.put("interests", "In my free time, I enjoy exploring new technologies, playing video games, and reading tech articles.");
        responses.put("age", "I am 20 years old.");
        responses.put("how old are you", "I am 20 years old.");
        responses.put("languages", "I am fluent in English and Greek.");
        responses.put("certifications", "I currently do not hold any professional certifications.");
        responses.put("work experience", "I do not yet have professional experience in the field, but I am highly motivated to learn and develop further.");
        responses.put("previous job", "I have not held any professional jobs yet.");
        responses.put("current job", "I am currently focused on my studies and personal development.");
        responses.put("strengths", "I am a quick learner, highly motivated, and a strong team player.");
        responses.put("weaknesses", "Sometimes I can be a perfectionist, which slows me down, but I am working on balancing quality with efficiency.");
        responses.put("dream job", "For now, I am open to any opportunity related to my field to discover what I truly enjoy.");
        responses.put("why should we hire you", "Because I am eager to learn, adaptable, and passionate about developing my skills in computer science.");
        responses.put("favorite technology", "I enjoy working with Java and C++ because of their robustness and versatility.");
        responses.put("relocate", "I am open to relocation after the completion of my studies.");
        responses.put("salary expectation", "My salary expectation is flexible as I am focused on gaining experience and learning.");
        responses.put("teamwork", "I enjoy collaborating with others and believe teamwork enhances project outcomes.");
        responses.put("leadership", "While I have limited leadership experience, I am eager to develop those skills.");
        responses.put("biggest achievement", "My biggest achievement so far is successfully developing a full-stack web application for event management as part of my coursework.");
        responses.put("motivation", "I am motivated by the desire to learn new technologies and improve my programming skills.");
        responses.put("handle stress", "I handle stress by staying organized, breaking tasks into smaller parts, and taking short breaks when needed.");
        responses.put("future goals", "My future goals are to specialize in a programming field I enjoy and gain professional experience.");
        responses.put("remote work", "I am comfortable with and open to remote work opportunities.");
        responses.put("availability", "I am available to start immediately, whether it's for an internship, a seasonal position, or a full-time job.");
        responses.put("available", "I am available to start immediately, whether it's for an internship, a seasonal position, or a full-time job.");
        responses.put("motivation", "I'm motivated by the process of learning and improving. I enjoy solving problems, building useful applications, and seeing my code come to life. The idea that I can always get better and create something meaningful keeps me going.");
        responses.put("motivate", "I'm motivated by the process of learning and improving. I enjoy solving problems, building useful applications, and seeing my code come to life. The idea that I can always get better and create something meaningful keeps me going.");
    }

    // Finds the best response for the user's message (with fuzzy matching).
    private static String getResponse(String message) {
        String lowerMessage = message.toLowerCase();
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            String keyword = entry.getKey();
            if (lowerMessage.contains(keyword) || hasFuzzyMatch(lowerMessage, keyword)) {
                return entry.getValue();
            }
        }
        return "I'm not sure how to answer that. Try to be a bit more specific so I can help you better.";
    }

    // Checks if the message contains a word that matches the keyword with one character difference.
    private static boolean hasFuzzyMatch(String message, String keyword) {
        // Split message into words
        String[] words = message.split("\\s+");
        
        for (String word : words) {
            if (isOneCharDifferent(word, keyword)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the word and keyword differ by only one character (missing, wrong, or extra letter).
    private static boolean isOneCharDifferent(String word, String keyword) {
        // Case 1: Missing letter (word is shorter by 1)
        if (word.length() == keyword.length() - 1) {
            for (int i = 0; i < keyword.length(); i++) {
                String modifiedKeyword = keyword.substring(0, i) + keyword.substring(i + 1);
                if (word.equals(modifiedKeyword)) {
                    return true;
                }
            }
        }
        
        // Case 2: Wrong letter (same length, one different character)
        if (word.length() == keyword.length()) {
            int differences = 0;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) != keyword.charAt(i)) {
                    differences++;
                }
            }
            if (differences == 1) {
                return true;
            }
        }
        
        // Case 3: Extra letter (word is longer by 1)
        if (word.length() == keyword.length() + 1) {
            for (int i = 0; i < word.length(); i++) {
                String modifiedWord = word.substring(0, i) + word.substring(i + 1);
                if (modifiedWord.equals(keyword)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    // Escapes special characters for safe JSON output.
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
} 