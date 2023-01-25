package microapp.repository;

import microapp.domain.IssueEmployeeAssignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IssueEmployeeAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueEmployeeAssignmentRepository
    extends ReactiveCrudRepository<IssueEmployeeAssignment, Long>, IssueEmployeeAssignmentRepositoryInternal {
    Flux<IssueEmployeeAssignment> findAllBy(Pageable pageable);

    @Override
    <S extends IssueEmployeeAssignment> Mono<S> save(S entity);

    @Override
    Flux<IssueEmployeeAssignment> findAll();

    @Override
    Mono<IssueEmployeeAssignment> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IssueEmployeeAssignmentRepositoryInternal {
    <S extends IssueEmployeeAssignment> Mono<S> save(S entity);

    Flux<IssueEmployeeAssignment> findAllBy(Pageable pageable);

    Flux<IssueEmployeeAssignment> findAll();

    Mono<IssueEmployeeAssignment> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IssueEmployeeAssignment> findAllBy(Pageable pageable, Criteria criteria);

}
