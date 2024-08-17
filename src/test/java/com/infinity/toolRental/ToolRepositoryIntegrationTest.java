package com.infinity.toolRental;


import com.infinity.toolRental.model.Tool;
import com.infinity.toolRental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

  public  class ToolRepositoryIntegrationTest {

        @Autowired
        private ToolRepository toolRepository;

        @BeforeEach
        void setUp() {
            // Optional: Clear the repository before each test (if needed)
            toolRepository.deleteAll();

            // Load some test data into the repository
            toolRepository.save(new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true));
            toolRepository.save(new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false));
            toolRepository.save(new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false));
            toolRepository.save(new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false));
        }

        @Test
        @DisplayName("Verify Tools are Loaded into In-Memory Database")
        void shouldLoadToolsIntoInMemoryDatabase() {
            // Verify 'CHNS' tool exists
            Optional<Tool> chainsaw = toolRepository.findById("CHNS");
            assertThat(chainsaw).isPresent()
                    .hasValueSatisfying(tool -> {
                        assertThat(tool.getToolType()).isEqualTo("Chainsaw");
                        assertThat(tool.getBrand()).isEqualTo("Stihl");
                        assertThat(tool.getDailyCharge()).isEqualTo(1.49);
                        assertThat(tool.isWeekdayCharge()).isTrue();
                        assertThat(tool.isWeekendCharge()).isFalse();
                        assertThat(tool.isHolidayCharge()).isTrue();
                    });

            // Verify 'LADW' tool exists
            Optional<Tool> ladder = toolRepository.findById("LADW");
            assertThat(ladder).isPresent();

            // Verify 'JAKD' tool exists
            Optional<Tool> jackhammerDewalt = toolRepository.findById("JAKD");
            assertThat(jackhammerDewalt).isPresent();

            // Verify 'JAKR' tool exists
            Optional<Tool> jackhammerRidgid = toolRepository.findById("JAKR");
            assertThat(jackhammerRidgid).isPresent();
        }

        @Test
        @DisplayName("Verify Specific Tool Data is Loaded Correctly")
        void shouldLoadSpecificToolDataCorrectly() {
            // Fetch the Chainsaw tool and verify its properties
            Tool chainsaw = toolRepository.findById("CHNS").orElseThrow();
            assertThat(chainsaw).isNotNull();
            assertThat(chainsaw.getToolType()).isEqualTo("Chainsaw");
            assertThat(chainsaw.getBrand()).isEqualTo("Stihl");
            assertThat(chainsaw.getDailyCharge()).isEqualTo(1.49);
            assertThat(chainsaw.isWeekdayCharge()).isTrue();
            assertThat(chainsaw.isWeekendCharge()).isFalse();
            assertThat(chainsaw.isHolidayCharge()).isTrue();
        }
    }


