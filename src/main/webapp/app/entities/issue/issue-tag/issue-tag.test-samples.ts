import { IIssueTag, NewIssueTag } from './issue-tag.model';

export const sampleWithRequiredData: IIssueTag = {
  id: 53080,
  issueId: 6070,
  issueUuid: '5c495c57-fb03-4820-8454-731c1b66684c',
  tag: 'Optional',
};

export const sampleWithPartialData: IIssueTag = {
  id: 84024,
  issueId: 90186,
  issueUuid: '88ec6f97-b4f4-4e6a-8879-88e375737502',
  tag: '(customarily Account monetize',
};

export const sampleWithFullData: IIssueTag = {
  id: 67200,
  issueId: 76436,
  issueUuid: '1d46be62-54d5-4a7c-9590-47054447bd73',
  tag: 'Computer',
};

export const sampleWithNewData: NewIssueTag = {
  issueId: 49490,
  issueUuid: 'e0638c7c-9fe7-4b9c-9f40-1fe84db839aa',
  tag: 'B2B Salad Investment',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
