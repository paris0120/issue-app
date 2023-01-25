export interface IIssueEmployeeAssignment {
  id: number;
  issueId?: number | null;
  issueUuid?: string | null;
  username?: string | null;
  issueAssignmentTitle?: string | null;
}

export type NewIssueEmployeeAssignment = Omit<IIssueEmployeeAssignment, 'id'> & { id: null };
