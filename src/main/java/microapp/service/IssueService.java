package microapp.service;

import java.time.Instant;
import java.util.UUID;
import microapp.domain.Issue;
import microapp.domain.enumeration.IssuePriority;
import microapp.domain.enumeration.IssueStatus;
import microapp.repository.IssueRepository;
import microapp.service.dto.IssueDTO;
import microapp.service.mapper.IssueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Issue}.
 */
@Service
@Transactional
public class IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueService.class);

    private final IssueRepository issueRepository;

    private final IssueMapper issueMapper;

    public IssueService(IssueRepository issueRepository, IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
    }

    /**
     * Save a issue.
     *
     * @param issueDTO the entity to save.
     * @return the persisted entity.
     */

    public Mono<IssueDTO> save(IssueDTO issueDTO) {
        issueDTO.setIssuePriority(IssuePriority.NORMAL);
        issueDTO.setIssueStatus(IssueStatus.OPEN);
        issueDTO.setCreated(Instant.now());
        issueDTO.setModified(Instant.now());
        issueDTO.setUuid(UUID.randomUUID());
        log.debug("Request to save Issue : {}", issueDTO);
        return issueRepository.save(issueMapper.toEntity(issueDTO)).map(issueMapper::toDto);
    }

    //    /**
    //     * Update a issue.
    //     *
    //     * @param issueDTO the entity to save.
    //     * @return the persisted entity.
    //     */
    //    public Mono<IssueDTO> update(IssueDTO issueDTO) {
    //        issueDTO.setModified(Instant.now());
    //        log.debug("Request to update Issue : {}", issueDTO);
    //        return issueRepository.save(issueMapper.toEntity(issueDTO)).map(issueMapper::toDto);
    //    }
    //
    //    /**
    //     * Partially update a issue.
    //     *
    //     * @param issueDTO the entity to update partially.
    //     * @return the persisted entity.
    //     */
    //    public Mono<IssueDTO> partialUpdate(IssueDTO issueDTO) {
    //        log.debug("Request to partially update Issue : {}", issueDTO);
    //        issueDTO.setModified(Instant.now());
    //
    //        return issueRepository
    //            .findById(issueDTO.getId())
    //            .map(existingIssue -> {
    //                issueMapper.partialUpdate(existingIssue, issueDTO);
    //
    //                return existingIssue;
    //            })
    //            .flatMap(issueRepository::save)
    //            .map(issueMapper::toDto);
    //    }

    /**
     * Partially update a issue.
     *
     * @param issueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IssueDTO> updateUserContent(IssueDTO issueDTO) {
        log.debug("Request to partially update Issue : {}", issueDTO);
        issueDTO.setModified(Instant.now());
        return issueRepository
            .findById(issueDTO.getId())
            .map(existingIssue -> {
                existingIssue.setIssueContent(issueDTO.getIssueContent());
                existingIssue.setIssueTitle(issueDTO.getIssueTitle());
                return existingIssue;
            })
            .flatMap(issueRepository::save)
            .map(issueMapper::toDto);
    }

    public Mono<IssueDTO> updateManagementContent(IssueDTO issueDTO) {
        log.debug("Request to partially update Issue : {}", issueDTO);
        issueDTO.setModified(Instant.now());
        return issueRepository
            .findById(issueDTO.getId())
            .map(existingIssue -> {
                existingIssue.setIssueStatus(issueDTO.getIssueStatus());
                existingIssue.setIssuePriority(issueDTO.getIssuePriority());
                existingIssue.setIssueType(issueDTO.getIssueType());
                return existingIssue;
            })
            .flatMap(issueRepository::save)
            .map(issueMapper::toDto);
    }

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IssueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issueRepository.findAllBy(pageable).map(issueMapper::toDto);
    }

    /**
     * Returns the number of issues available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return issueRepository.count();
    }

    /**
     * Get one issue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IssueDTO> findOne(Long id) {
        log.debug("Request to get Issue : {}", id);
        return issueRepository.findById(id).map(issueMapper::toDto);
    }

    /**
     * Delete the issue by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Issue : {}", id);
        return issueRepository.deleteById(id);
    }
}
