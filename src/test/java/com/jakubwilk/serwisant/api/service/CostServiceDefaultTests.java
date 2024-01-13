package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.repository.CostRepository;
import com.jakubwilk.serwisant.api.entity.jpa.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class CostServiceDefaultTests {

    @Autowired
    private CostService costService;

    @Autowired
    private CostRepository costRepository;

    private Cost testCost;

    @BeforeEach
    public void setup(){
        testCost = Cost.builder()
                .price(100)
                .costType(Cost.CostType.PART)
                .repair(Repair.builder()
                        .device(new Device())
                        .issuer(User.builder()
                                .userInfo(new UserInfo())
                                .build())
                        .build())
                .build();
        testCost = costRepository.save(testCost);
    }

    @AfterEach
    public void cleanUp(){
        costRepository.delete(testCost);
    }

    @Test
    public void findByIdShouldReturnACost(){
        Cost toCheck = costService.findById(testCost.getId());
        testCost = costRepository.findById(testCost.getId()).get();

        assertThat(toCheck).usingRecursiveComparison().isEqualTo(testCost);
    }

    @Test
    public void findByIdShouldThrowRuntimeExceptionWhenNoUserWasFound(){
        int id = testCost.getId();
        costRepository.delete(testCost);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            costService.findById(id);
        });

        String expectedMessage = "Cost with id: " + id + " not found";
        String assertedMessage = exception.getMessage();
        assertEquals(expectedMessage, assertedMessage);
    }

    @Test
    public void findAllShouldReturnAlCosts(){
        List<Cost> expected = costRepository.findAll();
        List<Cost> asserted = costService.findAll();

        assertThat(asserted).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void findAllShouldThrowRuntimeExceptionWhenNoCostsWereFound(){
        costRepository.deleteAll();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            costService.findAll();
        });

        String expectedMessage = "No costs were found.";

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void saveCostShouldPersistNewCost(){
        Cost newCost = Cost.builder()
                .price(100)
                .costType(Cost.CostType.PART)
                .repair(Repair.builder()
                        .device(new Device())
                        .issuer(User.builder()
                                .userInfo(new UserInfo())
                                .build())
                        .build())
                .build();

        costService.saveCost(newCost);

        newCost = costService.findById(newCost.getId());
        Cost expected = costService.findById(newCost.getId());

        assertThat(newCost.getId()).isNotEqualTo(0);
        assertThat(newCost).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void updateCostShouldUpdateCost(){
        Cost previousCost = testCost;
        testCost.setPrice(200);

        costService.updateCost(testCost);
        Cost afterUpdate = costService.findById(testCost.getId());
        assertThat(previousCost).usingRecursiveComparison().isNotEqualTo(afterUpdate);
    }

    @Test
    public void deleteCostShouldDeleteCost(){
        int id = testCost.getId();
        costService.deleteCost(id);
        Optional<Cost> shouldBeNull = costRepository.findById(id);

        assertFalse(shouldBeNull.isPresent());
    }
}
