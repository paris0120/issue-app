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

describe('IssueEmployeeAssignment e2e test', () => {
  const issueEmployeeAssignmentPageUrl = '/issue/issue-employee-assignment';
  const issueEmployeeAssignmentPageUrlPattern = new RegExp('/issue/issue-employee-assignment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const issueEmployeeAssignmentSample = {
    issueId: 96017,
    issueUuid: 'c94bcf43-1b9b-4a83-a2c0-b74b90277e31',
    username: 'Human',
    issueAssignmentTitle: 'Rubber Concrete',
  };

  let issueEmployeeAssignment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/issue/api/issue-employee-assignments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/issue/api/issue-employee-assignments').as('postEntityRequest');
    cy.intercept('DELETE', '/services/issue/api/issue-employee-assignments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (issueEmployeeAssignment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/issue/api/issue-employee-assignments/${issueEmployeeAssignment.id}`,
      }).then(() => {
        issueEmployeeAssignment = undefined;
      });
    }
  });

  it('IssueEmployeeAssignments menu should load IssueEmployeeAssignments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('issue/issue-employee-assignment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IssueEmployeeAssignment').should('exist');
    cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
  });

  describe('IssueEmployeeAssignment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(issueEmployeeAssignmentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IssueEmployeeAssignment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/issue/issue-employee-assignment/new$'));
        cy.getEntityCreateUpdateHeading('IssueEmployeeAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/issue/api/issue-employee-assignments',
          body: issueEmployeeAssignmentSample,
        }).then(({ body }) => {
          issueEmployeeAssignment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/issue/api/issue-employee-assignments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/issue/api/issue-employee-assignments?page=0&size=20>; rel="last",<http://localhost/services/issue/api/issue-employee-assignments?page=0&size=20>; rel="first"',
              },
              body: [issueEmployeeAssignment],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(issueEmployeeAssignmentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IssueEmployeeAssignment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('issueEmployeeAssignment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
      });

      it('edit button click should load edit IssueEmployeeAssignment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueEmployeeAssignment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
      });

      it('edit button click should load edit IssueEmployeeAssignment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueEmployeeAssignment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
      });

      it('last delete button click should delete instance of IssueEmployeeAssignment', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('issueEmployeeAssignment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);

        issueEmployeeAssignment = undefined;
      });
    });
  });

  describe('new IssueEmployeeAssignment page', () => {
    beforeEach(() => {
      cy.visit(`${issueEmployeeAssignmentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IssueEmployeeAssignment');
    });

    it('should create an instance of IssueEmployeeAssignment', () => {
      cy.get(`[data-cy="issueId"]`).type('39428').should('have.value', '39428');

      cy.get(`[data-cy="issueUuid"]`)
        .type('89be66f2-391d-4a52-b714-10b86151219d')
        .invoke('val')
        .should('match', new RegExp('89be66f2-391d-4a52-b714-10b86151219d'));

      cy.get(`[data-cy="username"]`).type('deposit').should('have.value', 'deposit');

      cy.get(`[data-cy="issueAssignmentTitle"]`).type('mobile invoice bypassing').should('have.value', 'mobile invoice bypassing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        issueEmployeeAssignment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', issueEmployeeAssignmentPageUrlPattern);
    });
  });
});
