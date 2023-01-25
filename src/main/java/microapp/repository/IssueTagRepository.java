package microapp.repository;

import microapp.domain.IssueTag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the IssueTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueTagRepository extends ReactiveCrudRepository<IssueTag, Long>, IssueTagRepositoryInternal {
    Flux<IssueTag> findAllBy(Pageable pageable);

    @Override
    <S extends IssueTag> Mono<S> save(S entity);

    @Override
    Flux<IssueTag> findAll();

    @Override
    Mono<IssueTag> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface IssueTagRepositoryInternal {
    <S extends IssueTag> Mono<S> save(S entity);

    Flux<IssueTag> findAllBy(Pageable pageable);

    Flux<IssueTag> findAll();

    Mono<IssueTag> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<IssueTag> findAllBy(Pageable pageable, Criteria criteria);

}
