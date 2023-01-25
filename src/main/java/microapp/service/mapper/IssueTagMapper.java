package microapp.service.mapper;

import microapp.domain.IssueTag;
import microapp.service.dto.IssueTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IssueTag} and its DTO {@link IssueTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface IssueTagMapper extends EntityMapper<IssueTagDTO, IssueTag> {}
