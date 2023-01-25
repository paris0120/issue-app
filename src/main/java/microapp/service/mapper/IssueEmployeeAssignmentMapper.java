package microapp.service.mapper;

import microapp.domain.IssueEmployeeAssignment;
import microapp.service.dto.IssueEmployeeAssignmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link IssueEmployeeAssignment} and its DTO {@link IssueEmployeeAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface IssueEmployeeAssignmentMapper extends EntityMapper<IssueEmployeeAssignmentDTO, IssueEmployeeAssignment> {}
