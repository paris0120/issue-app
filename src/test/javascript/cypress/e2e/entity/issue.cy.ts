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

describe('Issue e2e test', () => {
  const issuePageUrl = '/issue/issue';
  const issuePageUrlPattern = new RegExp('/issue/issue(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const issueSample = {
    issueTitle: 'Extension holistic user-centric',
    issueContent: 'IBXXXXXXXX',
    issueType: 'FEATURE',
    issueStatus: 'SOLVED',
    issuePriority: 'HIGHEST',
    uuid: '4319994d-393b-4d2c-9b88-1f176166d627',
    created: '2023-01-24T22:49:42.743Z',
    modified: '2023-01-24T07:28:38.654Z',
  };

  let issue;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/issue/api/issues+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/issue/api/issues').as('postEntityRequest');
    cy.intercept('DELETE', '/services/issue/api/issues/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (issue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/issue/api/issues/${issue.id}`,
      }).then(() => {
        issue = undefined;
      });
    }
  });

  it('Issues menu should load Issues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('issue/issue');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Issue').should('exist');
    cy.url().should('match', issuePageUrlPattern);
  });

  describe('Issue page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(issuePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Issue page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/issue/issue/new$'));
        cy.getEntityCreateUpdateHeading('Issue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issuePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/issue/api/issues',
          body: issueSample,
        }).then(({ body }) => {
          issue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/issue/api/issues+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/issue/api/issues?page=0&size=20>; rel="last",<http://localhost/services/issue/api/issues?page=0&size=20>; rel="first"',
              },
              body: [issue],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(issuePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Issue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('issue');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issuePageUrlPattern);
      });

      it('edit button click should load edit Issue page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Issue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issuePageUrlPattern);
      });

      it('edit button click should load edit Issue page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Issue');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issuePageUrlPattern);
      });

      it('last delete button click should delete instance of Issue', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('issue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issuePageUrlPattern);

        issue = undefined;
      });
    });
  });

  describe('new Issue page', () => {
    beforeEach(() => {
      cy.visit(`${issuePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Issue');
    });

    it('should create an instance of Issue', () => {
      cy.get(`[data-cy="username"]`).type('attitude').should('have.value', 'attitude');

      cy.get(`[data-cy="issueTitle"]`).type('Unbranded Ridge Planner').should('have.value', 'Unbranded Ridge Planner');

      cy.get(`[data-cy="issueContent"]`).type('invoice Front-line').should('have.value', 'invoice Front-line');

      cy.get(`[data-cy="issueType"]`).select('BUG');

      cy.get(`[data-cy="issueStatus"]`).select('IN_PROGRESS');

      cy.get(`[data-cy="issuePriority"]`).select('LOWER');

      cy.get(`[data-cy="uuid"]`)
        .type('da502bdc-236a-42f7-9cc9-977a06cfd084')
        .invoke('val')
        .should('match', new RegExp('da502bdc-236a-42f7-9cc9-977a06cfd084'));

      cy.get(`[data-cy="created"]`).type('2023-01-24T04:17').blur().should('have.value', '2023-01-24T04:17');

      cy.get(`[data-cy="modified"]`).type('2023-01-24T08:10').blur().should('have.value', '2023-01-24T08:10');

      cy.get(`[data-cy="deleted"]`).type('2023-01-24T21:54').blur().should('have.value', '2023-01-24T21:54');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        issue = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', issuePageUrlPattern);
    });
  });
});
