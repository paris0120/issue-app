import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('IssueEmployee e2e test', () => {
  const issueEmployeePageUrl = '/issue/issue-employee';
  const issueEmployeePageUrlPattern = new RegExp('/issue/issue-employee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const issueEmployeeSample = { username: 'focus Granite', issueAssignmentTitle: 'User-friendly Investment' };

  let issueEmployee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/issue/api/issue-employees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/issue/api/issue-employees').as('postEntityRequest');
    cy.intercept('DELETE', '/services/issue/api/issue-employees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (issueEmployee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/issue/api/issue-employees/${issueEmployee.id}`,
      }).then(() => {
        issueEmployee = undefined;
      });
    }
  });

  it('IssueEmployees menu should load IssueEmployees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('issue/issue-employee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IssueEmployee').should('exist');
    cy.url().should('match', issueEmployeePageUrlPattern);
  });

  describe('IssueEmployee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(issueEmployeePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IssueEmployee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/issue/issue-employee/new$'));
        cy.getEntityCreateUpdateHeading('IssueEmployee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/issue/api/issue-employees',
          body: issueEmployeeSample,
        }).then(({ body }) => {
          issueEmployee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/issue/api/issue-employees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/issue/api/issue-employees?page=0&size=20>; rel="last",<http://localhost/services/issue/api/issue-employees?page=0&size=20>; rel="first"',
              },
              body: [issueEmployee],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(issueEmployeePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IssueEmployee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('issueEmployee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeePageUrlPattern);
      });

      it('edit button click should load edit IssueEmployee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueEmployee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeePageUrlPattern);
      });

      it('edit button click should load edit IssueEmployee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueEmployee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeePageUrlPattern);
      });

      it('last delete button click should delete instance of IssueEmployee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('issueEmployee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeePageUrlPattern);

        issueEmployee = undefined;
      });
    });
  });

  describe('new IssueEmployee page', () => {
    beforeEach(() => {
      cy.visit(`${issueEmployeePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IssueEmployee');
    });

    it('should create an instance of IssueEmployee', () => {
      cy.get(`[data-cy="username"]`).type('Account real-time').should('have.value', 'Account real-time');

      cy.get(`[data-cy="issueAssignmentTitle"]`).type('24/7 Roads').should('have.value', '24/7 Roads');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        issueEmployee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', issueEmployeePageUrlPattern);
    });
  });
});
