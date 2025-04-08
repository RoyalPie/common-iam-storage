package com.evo.ddd.presentation.rest;

import com.evo.common.dto.response.ApiResponses;
import com.evo.ddd.application.dto.request.SearchUserRequest;
import com.evo.ddd.application.dto.response.UserDTO;
import com.evo.ddd.application.service.UserCommandService;
import com.evo.ddd.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/excel")
public class ExcelController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/import/users")
    public ApiResponses<List<UserDTO>> importUserFile(@RequestParam MultipartFile file){
        List<UserDTO> userResponse = userCommandService.importUserFile(file);
        return ApiResponses.<List<UserDTO>>builder()
                .data(userResponse)
                .success(true)
                .code(200)
                .message("Import user file successfully")
                .timestamp(System.currentTimeMillis())
                .status("OK")
                .build();
    }
    @GetMapping("/export/users")
    public ResponseEntity<byte[]> exportUserListToExcel(@RequestBody SearchUserRequest searchUserRequest) {
        byte[] excelBytes = userQueryService.exportUserListToExcel(searchUserRequest);

        return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=users.xlsx").body(excelBytes);
    }
}
