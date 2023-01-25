package microapp.repository;

import microapp.domain.IssueEmployee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IssueEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueEmployeeRepository extends ReactiveCrudRepository<IssueEmployee, Long>, IssueEmployeeRepositoryInternal {
    Flux<IssueEmployee> findAllBy(Pageable pageable);

    @Override
    <S extends IssueEmployee> Mono<S> save(S entity);

    @Override
    Flux<IssueEmployee> findAll();

    @Override
    Mono<IssueEmployee> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IssueEmployeeRepositoryInternal {
    <S extends IssueEmployee> Mono<S> save(S entity);

    Flux<IssueEmployee> findAllBy(Pageable pageable);

    Flux<IssueEmployee> findAll();

    Mono<IssueEmployee> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IssueEmployee> findAllBy(Pageable pageable, Criteria criteria);

}
