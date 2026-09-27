
package com.pharmapack.qms.production;

import com.pharmapack.qms.batch.Batch;
import com.pharmapack.qms.batch.BatchRepository;
import com.pharmapack.qms.master.Equipment;
import com.pharmapack.qms.master.EquipmentRepository;
import com.pharmapack.qms.master.PackagingLine;
import com.pharmapack.qms.master.LineRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    private final ProductionRunRepository rr;
    private final ProductionEntryRepository er;
    private final BatchRepository br;
    private final LineRepository lr;
    private final EquipmentRepository eqr;

    public ProductionController(
            ProductionRunRepository r,
            ProductionEntryRepository e,
            BatchRepository b,
            LineRepository l,
            EquipmentRepository eq) {

        rr = r;
        er = e;
        br = b;
        lr = l;
        eqr = eq;
    }

    @GetMapping("/runs")
    public List<ProductionRunResponse> runs() {
        return rr.findAllWithRefs()
                .stream()
                .map(this::dto)
                .toList();
    }

    public record RunCreate(
            Long batchId,
            Long lineId,
            Long equipmentId
    ) {}

    @PostMapping("/runs")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductionRunResponse run(@RequestBody RunCreate x) {

        if (x.batchId() == null) {
            throw new IllegalArgumentException("batchId is required");
        }

        Batch b = br.findById(x.batchId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Batch not found"));

        if (x.lineId() == null) {
            throw new IllegalArgumentException("lineId is required");
        }

        PackagingLine l = lr.findById(x.lineId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Packaging line not found"));

        Equipment eq = x.equipmentId() == null
                ? null
                : eqr.findById(x.equipmentId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Equipment not found"));

        ProductionRun saved = rr.save(
                ProductionRun.builder()
                        .batch(b)
                        .line(l)
                        .equipment(eq)
                        .startedAt(LocalDateTime.now())
                        .status("IN_PROGRESS")
                        .build()
        );

        return dto(
                rr.findById(saved.getId())
                        .orElseThrow()
        );
    }

    public record EntryCreate(
            Long runId,
            BigDecimal produced,
            BigDecimal good,
            BigDecimal reject,
            Long operatorId,
            String remarks
    ) {}

    @PostMapping("/entries")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductionEntryResponse entry(@RequestBody EntryCreate x) {

        // ---------------------------------------------------------
        // 1. Required production quantities
        // ---------------------------------------------------------

        if (x.runId() == null) {
            throw new IllegalArgumentException("runId is required");
        }

        if (x.produced() == null ||
                x.good() == null ||
                x.reject() == null) {

            throw new IllegalArgumentException(
                    "Production quantities are required"
            );
        }

        // ---------------------------------------------------------
        // 2. Non-negative quantity validation
        // ---------------------------------------------------------

        if (x.produced().signum() < 0 ||
                x.good().signum() < 0 ||
                x.reject().signum() < 0) {

            throw new IllegalArgumentException(
                    "Production quantities must be non-negative"
            );
        }

        // ---------------------------------------------------------
        // 3. Good + Reject cannot exceed Produced
        // ---------------------------------------------------------

        if (x.good()
                .add(x.reject())
                .compareTo(x.produced()) > 0) {

            throw new IllegalArgumentException(
                    "Good + Reject cannot exceed Produced"
            );
        }

        // ---------------------------------------------------------
        // 4. Find Production Run
        // ---------------------------------------------------------

        ProductionRun r = rr.findById(x.runId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Production run not found"
                        ));

        // ---------------------------------------------------------
        // 5. Find Batch
        // ---------------------------------------------------------

        Batch batch = r.getBatch();

        if (batch == null) {
            throw new IllegalArgumentException(
                    "Production run has no batch"
            );
        }

        if (batch.getLotSize() == null) {
            throw new IllegalArgumentException(
                    "Batch lot size is not defined"
            );
        }

        // ---------------------------------------------------------
        // 6. Cumulative production quantity
        // ---------------------------------------------------------

        BigDecimal alreadyProduced =
                er.sumProducedByBatchId(batch.getId());

        if (alreadyProduced == null) {
            alreadyProduced = BigDecimal.ZERO;
        }

        BigDecimal cumulativeProduced =
                alreadyProduced.add(x.produced());

        // ---------------------------------------------------------
        // 7. Lot-size control
        // ---------------------------------------------------------

        if (cumulativeProduced.compareTo(batch.getLotSize()) > 0) {

            throw new IllegalArgumentException(
                    "Production quantity exceeds batch lot size. " +
                    "Lot size: " + batch.getLotSize() +
                    ", already produced: " + alreadyProduced +
                    ", requested: " + x.produced() +
                    ", cumulative: " + cumulativeProduced
            );
        }

        // ---------------------------------------------------------
        // 8. Create Production Entry
        // ---------------------------------------------------------

        ProductionEntry e = new ProductionEntry();

        e.setProductionRun(r);
        e.setEntryTime(LocalDateTime.now());
        e.setQuantityProduced(x.produced());
        e.setQuantityGood(x.good());
        e.setQuantityReject(x.reject());
        e.setOperatorId(x.operatorId());
        e.setRemarks(x.remarks());

        ProductionEntry saved = er.save(e);

        return entryDto(
                er.findById(saved.getId())
                        .orElseThrow()
        );
    }

    @GetMapping("/runs/{id}/entries")
    public List<ProductionEntryResponse> entries(
            @PathVariable Long id) {

        return er.findByProductionRunIdOrderByEntryTimeAsc(id)
                .stream()
                .map(this::entryDto)
                .toList();
    }

    private ProductionRunResponse dto(ProductionRun r) {

        var b = r.getBatch();
        var l = r.getLine();
        var eq = r.getEquipment();

        return new ProductionRunResponse(
                r.getId(),

                b == null ? null : b.getId(),
                b == null ? null : b.getBatchNumber(),

                b == null || b.getProduct() == null
                        ? null
                        : b.getProduct().getProductCode(),

                b == null || b.getProduct() == null
                        ? null
                        : b.getProduct().getProductName(),

                l == null ? null : l.getId(),
                l == null ? null : l.getLineCode(),
                l == null ? null : l.getLineName(),

                eq == null ? null : eq.getId(),
                eq == null ? null : eq.getEquipmentCode(),
                eq == null ? null : eq.getEquipmentName(),

                r.getStartedAt(),
                r.getEndedAt(),
                r.getStatus()
        );
    }

    private ProductionEntryResponse entryDto(
            ProductionEntry e) {

        var r = e.getProductionRun();
        var b = r == null ? null : r.getBatch();

        return new ProductionEntryResponse(
                e.getId(),

                r == null ? null : r.getId(),

                b == null ? null : b.getId(),
                b == null ? null : b.getBatchNumber(),

                e.getEntryTime(),
                e.getQuantityProduced(),
                e.getQuantityGood(),
                e.getQuantityReject(),
                e.getOperatorId(),
                e.getRemarks()
        );
    }

}
