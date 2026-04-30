package com.corelate.forms.dto;

import lombok.Data;

import java.util.List;

@Data
public class BatchFieldLabelsRequestDto {
    private List<String> formIds;
}
