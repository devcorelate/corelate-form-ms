package com.corelate.forms.controllers;

import com.corelate.forms.constants.AppConstants;
import com.corelate.forms.dto.*;
import com.corelate.forms.service.IDynamicTableService;
import com.corelate.forms.service.IFormService;
import com.corelate.forms.service.impl.DynamicTableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path="/builder", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    private final IFormService iFormService;

    @Autowired
    private DynamicTableService dynamicTableService;

    public FormController(IFormService iFormService) {
        this.iFormService = iFormService;
    }

    @Operation(
            summary = "UPDATE Form REST API",
            description = "REST API to UPDATE new Form inside Corelate"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status UPDATED"
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
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateForm(@Valid @RequestBody FormDto formDto) {
        logger.debug("updateForm method start");
        boolean isUpdated = iFormService.updateForm(formDto);
        logger.debug("updateForm method end");
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AppConstants.STATUS_200, AppConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(AppConstants.STATUS_417, AppConstants.MESSAGE_417_UPDATE));
        }
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
    public ResponseEntity<FormDto> createForm(@Valid @RequestBody FormDto formDto) {
        logger.debug("createForm method start");
        FormDto newFormDto = iFormService.createForm(formDto);
        logger.debug("createForm method end");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newFormDto);
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
    @PostMapping("/fetch")
    public ResponseEntity<FormDto> fetchForm(@RequestBody FormDto formDto) {
        logger.debug("fetchForm method start");
        FormDto rFormDto = iFormService.fetchForm(formDto.getFormId());
        logger.debug("fetchForm method end");
        return ResponseEntity.status(HttpStatus.OK).body(rFormDto);
    }


    @Operation(
            summary = "Fetch Form Templates Selections REST API",
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
    @GetMapping("/fetch/selections")
    public ResponseEntity<List<FormSelectionDto>> fetchSelectionForms() {
        logger.debug("fetchSelectionForms method start");
        List<FormSelectionDto> formSelectionDtosDtos = iFormService.fetchSelections();
        logger.debug("fetchSelectionForms method end");
        return ResponseEntity.status(HttpStatus.OK).body(formSelectionDtosDtos);
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
    @PostMapping("/create/database")
    public String createTable(@RequestBody TableSchemaDto tableSchemaDto) {
        dynamicTableService.createTable(tableSchemaDto);
        return "Table " + tableSchemaDto.getTableName() + " created successfully.";
    }

    @DeleteMapping("/delete/schema/{formId}")
    public String deleteFormSchemas(@PathVariable String formId) {
        iFormService.deleteFormSchemas(formId);
        return "Schemas for " + formId + " deleted successfully.";
    }
}
