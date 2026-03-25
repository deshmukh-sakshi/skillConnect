package com.skillconnect.backend.Controller;
import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.Service.freelancer.FreelancerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/freelancers")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FreelancerDTO>> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(freelancerService.getFreelancerProfile(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
        return ResponseEntity.ok(ApiResponse.success("Freelancer deleted successfully."));
    }
}
