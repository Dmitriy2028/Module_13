package org.example;

import static org.example.JsonWithApiTutor.*;

public class JsonWithApiTutorTest {
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
}
