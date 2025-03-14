package com.evo.storage.resource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileResource {
    @GetMapping("")
    LocalDate test() {
        log.info("test");
        return LocalDate.now();
    }

    @GetMapping("api/test")
    LocalDate test2() {
        return LocalDate.now();
    }
}
