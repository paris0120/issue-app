package microapp.service;

import microapp.domain.IssueEmployee;
import microapp.repository.IssueEmployeeRepository;
import microapp.service.dto.IssueEmployeeDTO;
import microapp.service.mapper.IssueEmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link IssueEmployee}.
 */
@Service
@Transactional
public class IssueEmployeeService {

    private final Logger log = LoggerFactory.getLogger(IssueEmployeeService.class);

    private final IssueEmployeeRepository issueEmployeeRepository;

    private final IssueEmployeeMapper issueEmployeeMapper;

    public IssueEmployeeService(IssueEmployeeRepository issueEmployeeRepository, IssueEmployeeMapper issueEmployeeMapper) {
        this.issueEmployeeRepository = issueEmployeeRepository;
        this.issueEmployeeMapper = issueEmployeeMapper;
    }

    /**
     * Save a issueEmployee.
     *
     * @param issueEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeDTO> save(IssueEmployeeDTO issueEmployeeDTO) {
        log.debug("Request to save IssueEmployee : {}", issueEmployeeDTO);
        return issueEmployeeRepository.save(issueEmployeeMapper.toEntity(issueEmployeeDTO)).map(issueEmployeeMapper::toDto);
    }

    /**
     * Update a issueEmployee.
     *
     * @param issueEmployeeDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeDTO> update(IssueEmployeeDTO issueEmployeeDTO) {
        log.debug("Request to update IssueEmployee : {}", issueEmployeeDTO);
        return issueEmployeeRepository.save(issueEmployeeMapper.toEntity(issueEmployeeDTO)).map(issueEmployeeMapper::toDto);
    }

    /**
     * Partially update a issueEmployee.
     *
     * @param issueEmployeeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IssueEmployeeDTO> partialUpdate(IssueEmployeeDTO issueEmployeeDTO) {
        log.debug("Request to partially update IssueEmployee : {}", issueEmployeeDTO);

        return issueEmployeeRepository
            .findById(issueEmployeeDTO.getId())
            .map(existingIssueEmployee -> {
                issueEmployeeMapper.partialUpdate(existingIssueEmployee, issueEmployeeDTO);

                return existingIssueEmployee;
            })
            .flatMap(issueEmployeeRepository::save)
            .map(issueEmployeeMapper::toDto);
    }

    /**
     * Get all the issueEmployees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IssueEmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssueEmployees");
        return issueEmployeeRepository.findAllBy(pageable).map(issueEmployeeMapper::toDto);
    }

    /**
     * Returns the number of issueEmployees available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return issueEmployeeRepository.count();
    }

    /**
     * Get one issueEmployee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IssueEmployeeDTO> findOne(Long id) {
        log.debug("Request to get IssueEmployee : {}", id);
        return issueEmployeeRepository.findById(id).map(issueEmployeeMapper::toDto);
    }

    /**
     * Delete the issueEmployee by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IssueEmployee : {}", id);
        return issueEmployeeRepository.deleteById(id);
    }
}
