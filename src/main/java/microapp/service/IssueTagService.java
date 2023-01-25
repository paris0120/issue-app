package microapp.service;

import microapp.domain.IssueTag;
import microapp.repository.IssueTagRepository;
import microapp.service.dto.IssueTagDTO;
import microapp.service.mapper.IssueTagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link IssueTag}.
 */
@Service
@Transactional
public class IssueTagService {

    private final Logger log = LoggerFactory.getLogger(IssueTagService.class);

    private final IssueTagRepository issueTagRepository;

    private final IssueTagMapper issueTagMapper;

    public IssueTagService(IssueTagRepository issueTagRepository, IssueTagMapper issueTagMapper) {
        this.issueTagRepository = issueTagRepository;
        this.issueTagMapper = issueTagMapper;
    }

    /**
     * Save a issueTag.
     *
     * @param issueTagDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueTagDTO> save(IssueTagDTO issueTagDTO) {
        log.debug("Request to save IssueTag : {}", issueTagDTO);
        return issueTagRepository.save(issueTagMapper.toEntity(issueTagDTO)).map(issueTagMapper::toDto);
    }

    /**
     * Update a issueTag.
     *
     * @param issueTagDTO the entity to save.
     * @return the persisted entity.
     */
    public Mono<IssueTagDTO> update(IssueTagDTO issueTagDTO) {
        log.debug("Request to update IssueTag : {}", issueTagDTO);
        return issueTagRepository.save(issueTagMapper.toEntity(issueTagDTO)).map(issueTagMapper::toDto);
    }

    /**
     * Partially update a issueTag.
     *
     * @param issueTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<IssueTagDTO> partialUpdate(IssueTagDTO issueTagDTO) {
        log.debug("Request to partially update IssueTag : {}", issueTagDTO);

        return issueTagRepository
            .findById(issueTagDTO.getId())
            .map(existingIssueTag -> {
                issueTagMapper.partialUpdate(existingIssueTag, issueTagDTO);

                return existingIssueTag;
            })
            .flatMap(issueTagRepository::save)
            .map(issueTagMapper::toDto);
    }

    /**
     * Get all the issueTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<IssueTagDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssueTags");
        return issueTagRepository.findAllBy(pageable).map(issueTagMapper::toDto);
    }

    /**
     * Returns the number of issueTags available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return issueTagRepository.count();
    }

    /**
     * Get one issueTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<IssueTagDTO> findOne(Long id) {
        log.debug("Request to get IssueTag : {}", id);
        return issueTagRepository.findById(id).map(issueTagMapper::toDto);
    }

    /**
     * Delete the issueTag by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete IssueTag : {}", id);
        return issueTagRepository.deleteById(id);
    }
}
