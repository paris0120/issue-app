import dayjs from 'dayjs/esm';
import { IssueType } from 'app/entities/enumerations/issue-type.model';
import { IssueStatus } from 'app/entities/enumerations/issue-status.model';
import { IssuePriority } from 'app/entities/enumerations/issue-priority.model';

export interface IIssue {
  id: number;
  username?: string | null;
  issueTitle?: string | null;
  issueContent?: string | null;
  issueType?: IssueType | null;
  issueStatus?: IssueStatus | null;
  issuePriority?: IssuePriority | null;
  uuid?: string | null;
  created?: dayjs.Dayjs | null;
  modified?: dayjs.Dayjs | null;
  deleted?: dayjs.Dayjs | null;
}

export type NewIssue = Omit<IIssue, 'id'> & { id: null };
