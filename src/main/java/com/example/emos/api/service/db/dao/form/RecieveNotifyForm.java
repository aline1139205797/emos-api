package com.example.emos.api.service.db.dao.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RecieveNotifyForm {
    @NotBlank
    private String processId;
    @NotBlank
    private String uuid;
    @NotBlank
    private String result;
}

