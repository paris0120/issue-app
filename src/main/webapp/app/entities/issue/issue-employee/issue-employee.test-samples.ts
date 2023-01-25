import { IIssueEmployee, NewIssueEmployee } from './issue-employee.model';

export const sampleWithRequiredData: IIssueEmployee = {
  id: 36166,
  username: 'Rustic Puerto',
  issueAssignmentTitle: 'PCI',
};

export const sampleWithPartialData: IIssueEmployee = {
  id: 20925,
  username: 'Ports benchmark panel',
  issueAssignmentTitle: 'explicit Colorado',
};

export const sampleWithFullData: IIssueEmployee = {
  id: 31414,
  username: 'Chips Alabama array',
  issueAssignmentTitle: 'grey Brand',
};

export const sampleWithNewData: NewIssueEmployee = {
  username: 'even-keeled function Bulgarian',
  issueAssignmentTitle: 'actuating out-of-the-box SSL',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
