package employeemanagementUI;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AuthService {
	private static String authenticatedUsername = null;
    private static final String BASE_URL = "http://localhost:8091/api/auth";
    private static final HttpClient client = HttpClient.newBuilder()
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
            .build();
    private User currentUser;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public boolean login(String username, String password) throws Exception {
        User loginUser = new User();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        String json = objectMapper.writeValueAsString(loginUser);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Login successful");
            authenticatedUsername = username;
            return true;
        } else {
            System.out.println("Login failed, status code: " + response.statusCode());
            System.out.println("Response body: " + response.body());
        }

        return false;
    }

    public boolean register(String username, String password) throws Exception {
        User registerUser = new User();
        registerUser.setUsername(username);
        registerUser.setPassword(password);

        String json = objectMapper.writeValueAsString(registerUser);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.statusCode() == 201;
    }

    public boolean logout() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            System.out.println("Logout successful");
            return true;
        }

        return false;
    }
    public User getCurrentUser() {
        if (authenticatedUsername != null) {
            User currentUser = new User();
            currentUser.setUsername(authenticatedUsername);
            return currentUser;
        }
        return null;
    }
}