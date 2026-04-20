package com.smartseason.modules.field.controller;

import com.smartseason.modules.field.dto.FieldCreateRequest;
import com.smartseason.modules.field.dto.FieldResponse;
import com.smartseason.modules.field.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @PostMapping("/register")
    public FieldResponse createField(@RequestBody FieldCreateRequest req) {
        return fieldService.createField(req);
    }

}
