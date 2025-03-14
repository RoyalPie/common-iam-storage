package com.evo.iam.controller;


import com.evo.iam.dto.UserDto;
import com.evo.iam.payload.response.MessageResponse;
import com.evo.iam.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-manager")
public class ManagerController {
    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @Operation(summary = "See a list of all users")
    @PreAuthorize("hasPermission(null, 'USER.VIEW')")
    @GetMapping("/users_list")
    public ResponseEntity<Page<UserDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "") String keyword) {

        Page<UserDto> users = managerService.getAllUsers(page, size, sortBy, sortDir, keyword);

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Soft Delete an User")
    @PreAuthorize("hasPermission(null, 'USER.DELETE')")
    @PutMapping("/delete")
    public ResponseEntity<?> softDeleteUser(@RequestParam String email) {
        return ResponseEntity.ok(new MessageResponse(managerService.softDelete(email)));
    }

    @Operation(summary = "Lock/Unlock an User")
    @PreAuthorize("hasPermission(null, 'USER.UPDATE')")
    @PutMapping("/status")
    public ResponseEntity<?> changeUserStatus(@RequestParam String email, Boolean status) {
        return ResponseEntity.ok(new MessageResponse(managerService.changeUserStatus(email, status)));
    }

    @Operation(summary = "Assign a role to User")
    @PreAuthorize("hasPermission(null, 'USER.UPDATE')")
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRole(@RequestParam String email, @RequestParam String roleName) {
        managerService.assignRoleToUser(email, roleName);
        return ResponseEntity.ok("Role assigned successfully");
    }

    @Operation(summary = "Remove a role from User")
    @PreAuthorize("hasPermission(null, 'USER.UPDATE')")
    @PostMapping("/remove-role")
    public ResponseEntity<String> removeRole(@RequestParam String email, @RequestParam String roleName) {
        managerService.removeRoleFromUser(email, roleName);
        return ResponseEntity.ok("Role removed successfully");
    }
}
