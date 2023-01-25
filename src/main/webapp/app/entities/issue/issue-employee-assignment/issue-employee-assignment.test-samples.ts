import { IIssueEmployeeAssignment, NewIssueEmployeeAssignment } from './issue-employee-assignment.model';

export const sampleWithRequiredData: IIssueEmployeeAssignment = {
  id: 33899,
  issueId: 40320,
  issueUuid: 'b97292cb-449b-40af-ba91-659482948fd9',
  username: 'repurpose',
  issueAssignmentTitle: 'Bedfordshire Visionary',
};

export const sampleWithPartialData: IIssueEmployeeAssignment = {
  id: 32819,
  issueId: 63308,
  issueUuid: '4e154d1e-b45a-41b5-b5a0-15f4790264d3',
  username: 'partnerships tan Health',
  issueAssignmentTitle: 'hack orchid',
};

export const sampleWithFullData: IIssueEmployeeAssignment = {
  id: 10988,
  issueId: 19322,
  issueUuid: 'dfd40c9a-87fa-45c2-b356-2bc3fec55eb4',
  username: 'Card Kwacha Outdoors',
  issueAssignmentTitle: 'Future calculating Rustic',
};

export const sampleWithNewData: NewIssueEmployeeAssignment = {
  issueId: 65306,
  issueUuid: '35e8678b-22e3-4351-8dd6-657348239ac3',
  username: 'Bike Saint',
  issueAssignmentTitle: 'alarm Rubber Account',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
