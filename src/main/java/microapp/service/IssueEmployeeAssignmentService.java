package microapp.service;

import microapp.domain.IssueEmployeeAssignment;
import microapp.repository.IssueEmployeeAssignmentRepository;
import microapp.service.dto.IssueEmployeeAssignmentDTO;
import microapp.service.mapper.IssueEmployeeAssignmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link IssueEmployeeAssignment}.
 */
@Service
@Transactional
public class IssueEmployeeAssignmentService {

    private final Logger log = LoggerFactory.getLogger(IssueEmployeeAssignmentService.class);

    private final IssueEmployeeAssignmentRepository issueEmployeeAssignmentRepository;

    private final IssueEmployeeAssignmentMapper issueEmployeeAssignmentMapper;

    public IssueEmployeeAssignmentService(
        IssueEmployeeAssignmentRepository issueEmployeeAssignmentRepository,
        IssueEmployeeAssignmentMapper issueEmployeeAssignmentMapper
    ) {
        this.issueEmployeeAssignmentRepository = issueEmployeeAssignmentRepository;
        this.issueEmployeeAssignmentMapper = issueEmployeeAssignmentMapper;
    }

    /**
     * Save a issueEmployeeAssignment.
     *
     * @param issueEmployeeAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeAssignmentDTO> save(IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO) {
        log.debug("Request to save IssueEmployeeAssignment : {}", issueEmployeeAssignmentDTO);
        return issueEmployeeAssignmentRepository
            .save(issueEmployeeAssignmentMapper.toEntity(issueEmployeeAssignmentDTO))
            .map(issueEmployeeAssignmentMapper::toDto);
    }

    /**
     * Update a issueEmployeeAssignment.
     *
     * @param issueEmployeeAssignmentDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeAssignmentDTO> update(IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO) {
        log.debug("Request to update IssueEmployeeAssignment : {}", issueEmployeeAssignmentDTO);
        return issueEmployeeAssignmentRepository
            .save(issueEmployeeAssignmentMapper.toEntity(issueEmployeeAssignmentDTO))
            .map(issueEmployeeAssignmentMapper::toDto);
    }

    /**
     * Partially update a issueEmployeeAssignment.
     *
     * @param issueEmployeeAssignmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeAssignmentDTO> partialUpdate(IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO) {
        log.debug("Request to partially update IssueEmployeeAssignment : {}", issueEmployeeAssignmentDTO);

        return issueEmployeeAssignmentRepository
            .findById(issueEmployeeAssignmentDTO.getId())
            .map(existingIssueEmployeeAssignment -> {
                issueEmployeeAssignmentMapper.partialUpdate(existingIssueEmployeeAssignment, issueEmployeeAssignmentDTO);

                return existingIssueEmployeeAssignment;
            })
            .flatMap(issueEmployeeAssignmentRepository::save)
            .map(issueEmployeeAssignmentMapper::toDto);
    }

    /**
     * Get all the issueEmployeeAssignments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IssueEmployeeAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssueEmployeeAssignments");
        return issueEmployeeAssignmentRepository.findAllBy(pageable).map(issueEmployeeAssignmentMapper::toDto);
    }

    /**
     * Returns the number of issueEmployeeAssignments available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return issueEmployeeAssignmentRepository.count();
    }

    /**
     * Get one issueEmployeeAssignment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IssueEmployeeAssignmentDTO> findOne(Long id) {
        log.debug("Request to get IssueEmployeeAssignment : {}", id);
        return issueEmployeeAssignmentRepository.findById(id).map(issueEmployeeAssignmentMapper::toDto);
    }

    /**
     * Delete the issueEmployeeAssignment by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IssueEmployeeAssignment : {}", id);
        return issueEmployeeAssignmentRepository.deleteById(id);
    }
}
