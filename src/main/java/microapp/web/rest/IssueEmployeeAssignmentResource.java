package microapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.repository.IssueEmployeeAssignmentRepository;
import microapp.service.IssueEmployeeAssignmentService;
import microapp.service.dto.IssueEmployeeAssignmentDTO;
import microapp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link microapp.domain.IssueEmployeeAssignment}.
 */
@RestController
@RequestMapping("/api")
public class IssueEmployeeAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(IssueEmployeeAssignmentResource.class);

    private static final String ENTITY_NAME = "issueIssueEmployeeAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IssueEmployeeAssignmentService issueEmployeeAssignmentService;

    private final IssueEmployeeAssignmentRepository issueEmployeeAssignmentRepository;

    public IssueEmployeeAssignmentResource(
        IssueEmployeeAssignmentService issueEmployeeAssignmentService,
        IssueEmployeeAssignmentRepository issueEmployeeAssignmentRepository
    ) {
        this.issueEmployeeAssignmentService = issueEmployeeAssignmentService;
        this.issueEmployeeAssignmentRepository = issueEmployeeAssignmentRepository;
    }

    /**
     * {@code POST  /issue-employee-assignments} : Create a new issueEmployeeAssignment.
     *
     * @param issueEmployeeAssignmentDTO the issueEmployeeAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new issueEmployeeAssignmentDTO, or with status {@code 400 (Bad Request)} if the issueEmployeeAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/issue-employee-assignments")
    public Mono<ResponseEntity<IssueEmployeeAssignmentDTO>> createIssueEmployeeAssignment(
        @Valid @RequestBody IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IssueEmployeeAssignment : {}", issueEmployeeAssignmentDTO);
        if (issueEmployeeAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueEmployeeAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return issueEmployeeAssignmentService
            .save(issueEmployeeAssignmentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/issue-employee-assignments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /issue-employee-assignments/:id} : Updates an existing issueEmployeeAssignment.
     *
     * @param id the id of the issueEmployeeAssignmentDTO to save.
     * @param issueEmployeeAssignmentDTO the issueEmployeeAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issueEmployeeAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the issueEmployeeAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the issueEmployeeAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/issue-employee-assignments/{id}")
    public Mono<ResponseEntity<IssueEmployeeAssignmentDTO>> updateIssueEmployeeAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IssueEmployeeAssignment : {}, {}", id, issueEmployeeAssignmentDTO);
        if (issueEmployeeAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issueEmployeeAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return issueEmployeeAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return issueEmployeeAssignmentService
                    .update(issueEmployeeAssignmentDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /issue-employee-assignments/:id} : Partial updates given fields of an existing issueEmployeeAssignment, field will ignore if it is null
     *
     * @param id the id of the issueEmployeeAssignmentDTO to save.
     * @param issueEmployeeAssignmentDTO the issueEmployeeAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issueEmployeeAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the issueEmployeeAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the issueEmployeeAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the issueEmployeeAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/issue-employee-assignments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IssueEmployeeAssignmentDTO>> partialUpdateIssueEmployeeAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IssueEmployeeAssignment partially : {}, {}", id, issueEmployeeAssignmentDTO);
        if (issueEmployeeAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issueEmployeeAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return issueEmployeeAssignmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IssueEmployeeAssignmentDTO> result = issueEmployeeAssignmentService.partialUpdate(issueEmployeeAssignmentDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /issue-employee-assignments} : get all the issueEmployeeAssignments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issueEmployeeAssignments in body.
     */
    @GetMapping("/issue-employee-assignments")
    public Mono<ResponseEntity<List<IssueEmployeeAssignmentDTO>>> getAllIssueEmployeeAssignments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of IssueEmployeeAssignments");
        return issueEmployeeAssignmentService
            .countAll()
            .zipWith(issueEmployeeAssignmentService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /issue-employee-assignments/:id} : get the "id" issueEmployeeAssignment.
     *
     * @param id the id of the issueEmployeeAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the issueEmployeeAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/issue-employee-assignments/{id}")
    public Mono<ResponseEntity<IssueEmployeeAssignmentDTO>> getIssueEmployeeAssignment(@PathVariable Long id) {
        log.debug("REST request to get IssueEmployeeAssignment : {}", id);
        Mono<IssueEmployeeAssignmentDTO> issueEmployeeAssignmentDTO = issueEmployeeAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueEmployeeAssignmentDTO);
    }

    /**
     * {@code DELETE  /issue-employee-assignments/:id} : delete the "id" issueEmployeeAssignment.
     *
     * @param id the id of the issueEmployeeAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/issue-employee-assignments/{id}")
    public Mono<ResponseEntity<Void>> deleteIssueEmployeeAssignment(@PathVariable Long id) {
        log.debug("REST request to delete IssueEmployeeAssignment : {}", id);
        return issueEmployeeAssignmentService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
