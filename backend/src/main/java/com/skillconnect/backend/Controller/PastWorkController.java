package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Service.pastWork.PastWorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/freelancer/pastworks")
@RequiredArgsConstructor
public class PastWorkController {

    private final PastWorkService pastWorkService;

    @PostMapping
    public ResponseEntity<ApiResponse<PastWorkDTO>> addPastWork(@RequestBody PastWorkDTO dto) {
        return new ResponseEntity<>(ApiResponse.success(pastWorkService.addPastWork(dto)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PastWork>> updatePastWork(@PathVariable Long id, @RequestBody PastWorkDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(pastWorkService.updatePastWork(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePastWork(@PathVariable Long id) {
        pastWorkService.deletePastWork(id);
        return ResponseEntity.ok(ApiResponse.success("Past work deleted."));
    }
}
