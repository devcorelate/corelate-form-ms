package com.corelate.forms.controllers;

import com.corelate.forms.constants.AppConstants;
import com.corelate.forms.dto.ErrorResponseDto;
import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.ResponseDto;
import com.corelate.forms.service.IFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Seth Hernandez
 */

@Tag(
        name = "CRUD REST APIs for Orchestrator in Corelate",
        description = "CRUD REST APIs in Corelate to CREATE, UPDATE, FETCH AND DELETE orchestrator details"
)

@RestController
@RequestMapping(path="/forms", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    private final IFormService iFormService;

    public FormController(IFormService iFormService) {
        this.iFormService = iFormService;
    }

    @Operation(
            summary = "Create Form REST API",
            description = "REST API to create new Form inside Corelate"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createForm(@Valid @RequestBody FormDto formDto) {
        logger.debug("createForm method start");
        iFormService.createForm(formDto);
        logger.debug("createForm method end");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AppConstants.STATUS_201, AppConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Form Templates Details REST API",
            description = "REST API to fetch all Form Templates"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetch/all")
    public ResponseEntity<List<FormDto>> fetchAllForms() {
        logger.debug("fetchAllForms method start");
        List<FormDto> formDtos = iFormService.fetchAllForm();
        logger.debug("fetchAllForms method end");
        return ResponseEntity.status(HttpStatus.OK).body(formDtos);
    }
}
