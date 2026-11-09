package com.pharmapack.qms.production;

import com.pharmapack.qms.batch.Batch;
import com.pharmapack.qms.batch.BatchRepository;
import com.pharmapack.qms.master.EquipmentRepository;
import com.pharmapack.qms.master.LineRepository;
import com.pharmapack.qms.master.PackagingLine;
import com.pharmapack.qms.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Shift entries: quantities must add up and the batch can never produce more than its lot size. */
class ProductionControllerTest {

    private ProductionRunRepository runs;
    private ProductionEntryRepository entries;
    private BatchRepository batches;
    private LineRepository lines;
    private ProductionController controller;
    private final AtomicReference<ProductionEntry> saved = new AtomicReference<>();

    @BeforeEach
    void setUp() {
        runs = mock(ProductionRunRepository.class);
        entries = mock(ProductionEntryRepository.class);
        batches = mock(BatchRepository.class);
        lines = mock(LineRepository.class);
        controller = new ProductionController(runs, entries, batches, lines, mock(EquipmentRepository.class));

        Product product = Product.builder().id(1L).productCode("PMP-001").productName("Demo tablets").build();
        Batch batch = Batch.builder().id(5L).batchNumber("BATCH-LOT-500").product(product)
                .lotSize(new BigDecimal("500")).build();
        ProductionRun run = ProductionRun.builder().id(11L).batch(batch).status("IN_PROGRESS").build();
        when(runs.findById(11L)).thenReturn(Optional.of(run));
        when(runs.findById(404L)).thenReturn(Optional.empty());
        when(batches.findById(5L)).thenReturn(Optional.of(batch));

        when(entries.save(any())).thenAnswer(inv -> {
            ProductionEntry e = inv.getArgument(0);
            e.setId(77L);
            saved.set(e);
            return e;
        });
        when(entries.findById(77L)).thenAnswer(inv -> Optional.ofNullable(saved.get()));
    }

    private ProductionController.EntryCreate entry(String produced, String good, String reject) {
        return new ProductionController.EntryCreate(11L, dec(produced), dec(good), dec(reject), 1L, "unit test");
    }

    private static BigDecimal dec(String v) {
        return v == null ? null : new BigDecimal(v);
    }

    @Test
    void aValidEntryIsSaved() {
        when(entries.sumProducedByBatchId(5L)).thenReturn(BigDecimal.ZERO);

        ProductionEntryResponse r = controller.entry(entry("100", "95", "5"));

        assertEquals(77L, r.id());
        assertEquals("BATCH-LOT-500", r.batchNumber());
        assertEquals(0, r.quantityGood().compareTo(new BigDecimal("95")));
        assertNotNull(r.entryTime());
    }

    @Test
    void exactlyReachingTheLotSizeIsAllowed() {
        when(entries.sumProducedByBatchId(5L)).thenReturn(new BigDecimal("400"));
        assertDoesNotThrow(() -> controller.entry(entry("100", "100", "0")));
    }

    @Test
    void goingOverTheLotSizeIsRefused() {
        when(entries.sumProducedByBatchId(5L)).thenReturn(new BigDecimal("400"));

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> controller.entry(entry("101", "101", "0")));
        assertTrue(e.getMessage().contains("exceeds batch lot size"), e.getMessage());
        assertTrue(e.getMessage().contains("cumulative: 501"), e.getMessage());
        verify(entries, never()).save(any());
    }

    @Test
    void noEarlierProductionCountsAsZero() {
        when(entries.sumProducedByBatchId(5L)).thenReturn(null);
        assertDoesNotThrow(() -> controller.entry(entry("500", "490", "10")));
    }

    @Test
    void goodPlusRejectCannotExceedProduced() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> controller.entry(entry("100", "95", "10")));
        assertEquals("Good + Reject cannot exceed Produced", e.getMessage());
    }

    @Test
    void negativeOrMissingQuantitiesAreRefused() {
        assertThrows(IllegalArgumentException.class, () -> controller.entry(entry("-1", "0", "0")));
        assertThrows(IllegalArgumentException.class, () -> controller.entry(entry("10", null, "0")));
        verify(entries, never()).save(any());
    }

    @Test
    void unknownRunIsRefused() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> controller.entry(
                new ProductionController.EntryCreate(404L, dec("1"), dec("1"), dec("0"), 1L, null)));
        assertEquals("Production run not found", e.getMessage());
    }

    @Test
    void aRunNeedsAnExistingBatchAndLine() {
        assertThrows(IllegalArgumentException.class,
                () -> controller.run(new ProductionController.RunCreate(null, 1L, null)));
        when(lines.findById(1L)).thenReturn(Optional.of(new PackagingLine()));
        assertThrows(IllegalArgumentException.class,
                () -> controller.run(new ProductionController.RunCreate(5L, null, null)));
        verify(runs, never()).save(any());
    }
}
