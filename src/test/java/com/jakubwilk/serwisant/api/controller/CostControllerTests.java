package com.jakubwilk.serwisant.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jakubwilk.serwisant.api.entity.jpa.Cost;
import com.jakubwilk.serwisant.api.repository.CostRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class CostControllerTests {
    @Autowired
    private CostRepository costRepository;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;

    Cost testCost;

    @BeforeEach
    public void setup(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        testCost = Cost.builder()
                .name("test")
                .costType(Cost.CostType.SERVICE)
                .price(10F)
                .build();

        costRepository.save(testCost);
    }

    @AfterEach
    public void cleanup(){
        costRepository.delete(testCost);
    }
    @Test
    @WithMockUser(username="cust", password = "root")
    void findCostByIdShouldReturnValidCostItem() throws Exception {
        this.mockMvc.perform(get("/cost/" + testCost.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String costType = res.getString("costType");
                    String name = res.getString("name");
                    assertThat(costType).isEqualTo(Cost.CostType.SERVICE.toString());
                    assertThat(name).isEqualTo(testCost.getName());
                });
    }

    @Test
    void findCostsByIdShouldUseAuth() throws Exception{
        this.mockMvc.perform(get("/cost/" + testCost.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void findCostByIdShouldReturnBadRequestWithInvalidId() throws Exception {
        this.mockMvc.perform(get("/cost/" + Integer.MAX_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void getAllCostsShouldReturnCostList() throws Exception {
        this.mockMvc.perform(get("/cost/"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    JSONArray res = new JSONArray(result.getResponse().getContentAsString());
                });
    }

    @Test
    void getAllCostsShouldUseAuthentication() throws Exception{
        this.mockMvc.perform(get("/cost/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void getAllCostsByRepairId() throws Exception {
        this.mockMvc.perform(get("/cost/repair?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void saveCostShouldPersistCost() throws Exception{
        Cost newCost = Cost.builder()
                .name("newCostForTests")
                .costType(Cost.CostType.PART)
                .price(10F)
                .build();

        this.mockMvc.perform(post("/cost/?repairid=1")
                .content(mapper.writeValueAsString(newCost))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String costName = res.getString("name");
                    assertThat(costName).isEqualTo(newCost.getName());
                });
    }

    @Test
    void saveShouldUseAuth() throws Exception{
        this.mockMvc.perform(post("/cost/?repairid=1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void saveShouldReturnBadRequestOnNoRequestBody() throws Exception{
        this.mockMvc.perform(post("/cost/"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void updateCostShouldPassOnValidBody() throws Exception {
        String newName = "newTestCostNameForPurposeOfThatParticularTestUpdatingCostName";
        testCost.setName(newName);

        this.mockMvc.perform(put("/cost/")
                .content(mapper.writeValueAsString(testCost))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    JSONObject res = new JSONObject(result.getResponse().getContentAsString());
                    String name = res.getString("name");
                    assertThat(name).isEqualTo(newName);
                });
    }

    @Test
    void updateCostShouldUseAuth() throws Exception {
        String newName = "notGonnaWorkAnyway";
        testCost.setName(newName);

        this.mockMvc.perform(put("/cost/")
                        .content(mapper.writeValueAsString(testCost))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="cust", password = "root")
    void deleteCostShouldDeleteOnValidId() throws Exception {
        int theId = testCost.getId();
        this.mockMvc.perform(delete("/cost/" + testCost.getId()))
                .andExpect(status().isOk());

        Optional<Cost> test = costRepository.findById(theId);
        assertThat(test.isPresent()).isFalse();
    }

    @Test
    void deleteShouldUseAuth() throws Exception {
        this.mockMvc.perform(delete("/cost/" + testCost.getId()))
                .andExpect(status().isUnauthorized());
    }
}