package microapp.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import microapp.repository.IssueEmployeeRepository;
import microapp.service.IssueEmployeeService;
import microapp.service.dto.IssueEmployeeDTO;
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
 * REST controller for managing {@link microapp.domain.IssueEmployee}.
 */
@RestController
@RequestMapping("/api")
public class IssueEmployeeResource {

    private final Logger log = LoggerFactory.getLogger(IssueEmployeeResource.class);

    private static final String ENTITY_NAME = "issueIssueEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IssueEmployeeService issueEmployeeService;

    private final IssueEmployeeRepository issueEmployeeRepository;

    public IssueEmployeeResource(IssueEmployeeService issueEmployeeService, IssueEmployeeRepository issueEmployeeRepository) {
        this.issueEmployeeService = issueEmployeeService;
        this.issueEmployeeRepository = issueEmployeeRepository;
    }

    /**
     * {@code POST  /issue-employees} : Create a new issueEmployee.
     *
     * @param issueEmployeeDTO the issueEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new issueEmployeeDTO, or with status {@code 400 (Bad Request)} if the issueEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/issue-employees")
    public Mono<ResponseEntity<IssueEmployeeDTO>> createIssueEmployee(@Valid @RequestBody IssueEmployeeDTO issueEmployeeDTO)
        throws URISyntaxException {
        log.debug("REST request to save IssueEmployee : {}", issueEmployeeDTO);
        if (issueEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return issueEmployeeService
            .save(issueEmployeeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/issue-employees/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /issue-employees/:id} : Updates an existing issueEmployee.
     *
     * @param id the id of the issueEmployeeDTO to save.
     * @param issueEmployeeDTO the issueEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issueEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the issueEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the issueEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/issue-employees/{id}")
    public Mono<ResponseEntity<IssueEmployeeDTO>> updateIssueEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IssueEmployeeDTO issueEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IssueEmployee : {}, {}", id, issueEmployeeDTO);
        if (issueEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issueEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return issueEmployeeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return issueEmployeeService
                    .update(issueEmployeeDTO)
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
     * {@code PATCH  /issue-employees/:id} : Partial updates given fields of an existing issueEmployee, field will ignore if it is null
     *
     * @param id the id of the issueEmployeeDTO to save.
     * @param issueEmployeeDTO the issueEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issueEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the issueEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the issueEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the issueEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/issue-employees/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<IssueEmployeeDTO>> partialUpdateIssueEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IssueEmployeeDTO issueEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IssueEmployee partially : {}, {}", id, issueEmployeeDTO);
        if (issueEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issueEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return issueEmployeeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<IssueEmployeeDTO> result = issueEmployeeService.partialUpdate(issueEmployeeDTO);

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
     * {@code GET  /issue-employees} : get all the issueEmployees.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issueEmployees in body.
     */
    @GetMapping("/issue-employees")
    public Mono<ResponseEntity<List<IssueEmployeeDTO>>> getAllIssueEmployees(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of IssueEmployees");
        return issueEmployeeService
            .countAll()
            .zipWith(issueEmployeeService.findAll(pageable).collectList())
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
     * {@code GET  /issue-employees/:id} : get the "id" issueEmployee.
     *
     * @param id the id of the issueEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the issueEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/issue-employees/{id}")
    public Mono<ResponseEntity<IssueEmployeeDTO>> getIssueEmployee(@PathVariable Long id) {
        log.debug("REST request to get IssueEmployee : {}", id);
        Mono<IssueEmployeeDTO> issueEmployeeDTO = issueEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueEmployeeDTO);
    }

    /**
     * {@code DELETE  /issue-employees/:id} : delete the "id" issueEmployee.
     *
     * @param id the id of the issueEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/issue-employees/{id}")
    public Mono<ResponseEntity<Void>> deleteIssueEmployee(@PathVariable Long id) {
        log.debug("REST request to delete IssueEmployee : {}", id);
        return issueEmployeeService
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
