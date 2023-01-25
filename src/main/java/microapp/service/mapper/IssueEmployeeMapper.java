package microapp.service.mapper;

import microapp.domain.IssueEmployee;
import microapp.service.dto.IssueEmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IssueEmployee} and its DTO {@link IssueEmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface IssueEmployeeMapper extends EntityMapper<IssueEmployeeDTO, IssueEmployee> {}
