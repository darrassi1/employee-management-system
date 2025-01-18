package employeemanagementUI;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiService {

    private static final String BASE_URL = "http://localhost:8091/api/employees";
    private static final HttpClient client = HttpClient.newBuilder()
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL))
            .build();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private AuthService authService;

    public ApiService(AuthService authService) {
        this.authService = authService;
    }

    private HttpRequest.Builder createRequestBuilder(String endpoint) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json");

        // Debug print to confirm the session cookie is included in the headers
        System.out.println("Creating request to " + BASE_URL + endpoint);

        return builder;
    }
    
    public List<EmployeeModel> getAllEmployees() throws Exception {
        HttpRequest request = createRequestBuilder("").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API request failed with status: " + response.statusCode() +
                    ", body: " + response.body());
        }

        String responseBody = response.body();
        if (responseBody == null || responseBody.trim().isEmpty()) {
            return List.of();
        }

        return objectMapper.readValue(
            responseBody,
            objectMapper.getTypeFactory().constructCollectionType(
                List.class,
                EmployeeModel.class
            )
        );
    }

    public List<EmployeeModel> searchEmployees(String query) throws Exception {
        HttpRequest request = createRequestBuilder("/search?query=" + query).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, EmployeeModel.class));
    }

    public EmployeeModel addEmployee(EmployeeModel employee) throws Exception {
        String json = objectMapper.writeValueAsString(employee);
        HttpRequest request = createRequestBuilder("").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), EmployeeModel.class);
    }

    // Method to update employee details
    public EmployeeModel updateEmployee(EmployeeModel employee) throws Exception {
        String json = objectMapper.writeValueAsString(employee);

        // Construct the PUT request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + employee.getEmployeeId()))  // URL for specific employee
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(json))  // PUT request with employee data
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Handle response and convert to EmployeeModel
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), EmployeeModel.class);  // Successfully updated
        } else {
            throw new Exception("Failed to update employee. HTTP Status: " + response.statusCode());
        }
    }

    public EmployeeModel getEmployeeById(String id) throws Exception {
        HttpRequest request = createRequestBuilder("/" + id).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), EmployeeModel.class);
    }

    public List<AuditLog> getAuditLogs() throws IOException, InterruptedException {
        HttpRequest request = createRequestBuilder("/auditlogs").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, AuditLog.class));
    }

    public void deleteEmployee(String id) throws Exception {
        HttpRequest request = createRequestBuilder("/" + id).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}