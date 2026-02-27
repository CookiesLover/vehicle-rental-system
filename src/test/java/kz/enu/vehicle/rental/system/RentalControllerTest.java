package kz.enu.vehicle.rental.system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnVehicleList() throws Exception {
        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404ForMissingVehicle() throws Exception {
        mockMvc.perform(get("/api/vehicles/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldCreateUpdateAndDeleteVehicle() throws Exception {
        String newVehicle = """
                {
                  "id": 99,
                  "brand": "Tesla",
                  "model": "Model 3",
                  "type": "Sedan",
                  "pricePerDay": 30000,
                  "imageUrl": "https://example.com/tesla.jpg"
                }
                """;

        String updatedVehicle = """
                {
                  "id": 99,
                  "brand": "Tesla",
                  "model": "Model Y",
                  "type": "SUV",
                  "pricePerDay": 35000,
                  "imageUrl": "https://example.com/tesla-y.jpg"
                }
                """;

        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newVehicle))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.model").value("Model 3"));

        mockMvc.perform(put("/api/vehicles/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedVehicle))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("Model Y"));

        mockMvc.perform(delete("/api/vehicles/99"))
                .andExpect(status().isNoContent());
    }
}
