export interface IIssueTag {
  id: number;
  issueId?: number | null;
  issueUuid?: string | null;
  tag?: string | null;
}

export type NewIssueTag = Omit<IIssueTag, 'id'> & { id: null };
