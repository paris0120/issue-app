export interface IIssueEmployee {
  id: number;
  username?: string | null;
  issueAssignmentTitle?: string | null;
}

export type NewIssueEmployee = Omit<IIssueEmployee, 'id'> & { id: null };
