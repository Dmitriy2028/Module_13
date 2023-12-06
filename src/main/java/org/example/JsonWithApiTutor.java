package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonPlaceholderApiExample {

    private final String BASE_URL = "https://jsonplaceholder.typicode.com/users";

    public static void main(String[] args) {
        try {
            // Створення нового об'єкта
            String newUser = createUser("John Doe", "john.doe@example.com");
            System.out.println("New User: " + newUser);

            // Оновлення об'єкту
            String updatedUser = updateUser(2, "John Doe Updated");
            System.out.println("Updated User: " + updatedUser);

            // Отримання інформації про всіх користувачів
            String allUsers = getAllUsers();
            System.out.println("All Users: " + allUsers);

            // Отримання інформації про користувача за id
            String userById = getUserById(3);
            System.out.println("User by ID: " + userById);

            // Отримання інформації про користувача за username
            String userByUsername = getUserByUsername("Antonette");
            System.out.println("User by Username: " + userByUsername);

            // Видалення об'єкту
            String deleteStatus = deleteUser(10);
            System.out.println(deleteStatus);

            // Отримання невиконанних завдань для юзера
            String toDosForUser = getToDosForUser(1);
            System.out.println("To do: " + toDosForUser);

            // Отримання коментарів для останнього поста юзера за ідентифікатором і створення файлу
            String comments = getCommentsForUser(1);
            System.out.println("comments = " + comments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createUser(String name, String email) throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = "{\"name\":\"" + name + "\",\"email\":\"" + email + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String updateUser(int userId, String newName) throws Exception {
        URL url = new URL(BASE_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = "{\"name\":\"" + newName + "\"}";

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String deleteUser(int userId) throws Exception {
        URL url = new URL(BASE_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        int responseCode = connection.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            return ("User deleted successfully. Response Code: " + responseCode);
        } else {
            return ("Failed to delete user. Response Code: " + responseCode);
        }
    }

    public String getAllUsers() throws Exception {
        URL url = new URL(BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String getUserById(int userId) throws Exception {
        URL url = new URL(BASE_URL + "/" + userId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public String getUserByUsername(String username) throws Exception {
        URL url = new URL(BASE_URL + "?username=" + username);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //return response.toString().substring(1,response.length()-1);
            return response.toString();
        }
    }

    public String getToDosForUser(int userId) throws Exception {
        URL url = new URL(BASE_URL + "/" + userId + "/todos");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonArray jsonArray = new Gson().fromJson(response.toString(), JsonArray.class);
            JsonArray resultArr = new JsonArray();
            for (JsonElement jsonElement : jsonArray) {
                if (!jsonElement.getAsJsonObject().get("completed").getAsBoolean()) {
                    resultArr.add(jsonElement);
                }
            }
            return resultArr.toString();
        }
    }

    public String getCommentsForUser(int userId) throws Exception {
        int maxId = getMaxPostId(userId);
        final String BASE_POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
        URL url = new URL(BASE_POSTS_URL + "/" + maxId + "/comments");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonArray jsonArray = new Gson().fromJson(response.toString(), JsonArray.class);
            JsonArray resultArr = new JsonArray();

            System.out.println("File status: " + writeToJsonFile(response.toString(),userId,maxId));

            return response.toString();
            //return jsonArray;
        }
    }

    private int getMaxPostId(int userId) throws Exception {
        URL url = new URL(BASE_URL + "/" + userId + "/posts");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JsonArray jsonArray = new Gson().fromJson(response.toString(), JsonArray.class);
            int maxId = jsonArray.get(0).getAsJsonObject().get("id").getAsInt();
            for (JsonElement jsonElement : jsonArray) {
                if (jsonElement.getAsJsonObject().get("id").getAsInt() > maxId) {
                    maxId = jsonElement.getAsJsonObject().get("id").getAsInt();
                }
            }
            return maxId;
        }
    }

    private String writeToJsonFile(String jsonUser, int userId, int maxId) throws IOException {
        File file = new File(System.getProperty("user.dir"), "src/main/resources/user-" + userId + "-post-" + maxId + "-comments.json");
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        } else {
            file.createNewFile();
        }
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(jsonUser);
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "src/main/resources/user-" + userId + "-post-" + maxId + "-comments.json created";
    }
}