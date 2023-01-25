import dayjs from 'dayjs/esm';

import { IssueType } from 'app/entities/enumerations/issue-type.model';
import { IssueStatus } from 'app/entities/enumerations/issue-status.model';
import { IssuePriority } from 'app/entities/enumerations/issue-priority.model';

import { IIssue, NewIssue } from './issue.model';

export const sampleWithRequiredData: IIssue = {
  id: 52849,
  issueTitle: 'Wooden JSON',
  issueContent: 'deliverables Refined virtual',
  issueType: IssueType['FEATURE'],
  issueStatus: IssueStatus['REOPENED'],
  issuePriority: IssuePriority['LOWER'],
  uuid: '95bc8130-5230-4819-a1b5-5d464af04163',
  created: dayjs('2023-01-24T17:04'),
  modified: dayjs('2023-01-24T18:33'),
};

export const sampleWithPartialData: IIssue = {
  id: 82043,
  issueTitle: 'efficient responsive',
  issueContent: 'Pants Rubber',
  issueType: IssueType['BUG'],
  issueStatus: IssueStatus['VERIFIED'],
  issuePriority: IssuePriority['LOW'],
  uuid: '7d8038c3-020e-48c4-9393-88db018c3c5a',
  created: dayjs('2023-01-24T08:27'),
  modified: dayjs('2023-01-24T14:16'),
};

export const sampleWithFullData: IIssue = {
  id: 60943,
  username: 'contextually-based Malagasy',
  issueTitle: 'withdrawal',
  issueContent: 'EnhancedXX',
  issueType: IssueType['BUG'],
  issueStatus: IssueStatus['WAITING_FOR_RESPONSE'],
  issuePriority: IssuePriority['HIGHEST'],
  uuid: 'a1558ece-87da-47f0-9fe8-8bb35c3a03f7',
  created: dayjs('2023-01-24T16:26'),
  modified: dayjs('2023-01-24T17:02'),
  deleted: dayjs('2023-01-24T20:27'),
};

export const sampleWithNewData: NewIssue = {
  issueTitle: 'schemas Shilling calculating',
  issueContent: 'ManagedXXX',
  issueType: IssueType['BUG'],
  issueStatus: IssueStatus['VERIFIED'],
  issuePriority: IssuePriority['NORMAL'],
  uuid: '6cea8c36-6b72-45d4-87b0-8a8619260797',
  created: dayjs('2023-01-24T04:30'),
  modified: dayjs('2023-01-24T08:17'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
