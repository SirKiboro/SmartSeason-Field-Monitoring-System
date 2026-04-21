package com.smartseason.modules.field.controller;

import com.smartseason.modules.field.dto.FieldCreateRequest;
import com.smartseason.modules.field.dto.FieldResponse;
import com.smartseason.modules.field.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @PostMapping
    public FieldResponse createField(@RequestBody FieldCreateRequest req) {

        return fieldService.createField(req);
    }

    @GetMapping
    public List<FieldResponse> getAllFields(){
        return fieldService.getAllFields();
    }

    @GetMapping("/{id}")
    public FieldResponse getById (@PathVariable UUID id){
        return fieldService.getFieldById(id);
    }

}
