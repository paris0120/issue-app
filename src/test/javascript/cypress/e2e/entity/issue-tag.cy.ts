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

describe('IssueTag e2e test', () => {
  const issueTagPageUrl = '/issue/issue-tag';
  const issueTagPageUrlPattern = new RegExp('/issue/issue-tag(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const issueTagSample = { issueId: 59056, issueUuid: 'fe1bff2f-32a7-4fbb-9f17-553a20185eea', tag: 'explicit e-services' };

  let issueTag;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/issue/api/issue-tags+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/issue/api/issue-tags').as('postEntityRequest');
    cy.intercept('DELETE', '/services/issue/api/issue-tags/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (issueTag) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/issue/api/issue-tags/${issueTag.id}`,
      }).then(() => {
        issueTag = undefined;
      });
    }
  });

  it('IssueTags menu should load IssueTags page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('issue/issue-tag');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IssueTag').should('exist');
    cy.url().should('match', issueTagPageUrlPattern);
  });

  describe('IssueTag page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(issueTagPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IssueTag page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/issue/issue-tag/new$'));
        cy.getEntityCreateUpdateHeading('IssueTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueTagPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/issue/api/issue-tags',
          body: issueTagSample,
        }).then(({ body }) => {
          issueTag = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/issue/api/issue-tags+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/issue/api/issue-tags?page=0&size=20>; rel="last",<http://localhost/services/issue/api/issue-tags?page=0&size=20>; rel="first"',
              },
              body: [issueTag],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(issueTagPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IssueTag page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('issueTag');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueTagPageUrlPattern);
      });

      it('edit button click should load edit IssueTag page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueTag');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueTagPageUrlPattern);
      });

      it('edit button click should load edit IssueTag page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IssueTag');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueTagPageUrlPattern);
      });

      it('last delete button click should delete instance of IssueTag', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('issueTag').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', issueTagPageUrlPattern);

        issueTag = undefined;
      });
    });
  });

  describe('new IssueTag page', () => {
    beforeEach(() => {
      cy.visit(`${issueTagPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IssueTag');
    });

    it('should create an instance of IssueTag', () => {
      cy.get(`[data-cy="issueId"]`).type('50288').should('have.value', '50288');

      cy.get(`[data-cy="issueUuid"]`)
        .type('068fd3aa-0f14-4674-9ba1-b1123cea87c8')
        .invoke('val')
        .should('match', new RegExp('068fd3aa-0f14-4674-9ba1-b1123cea87c8'));

      cy.get(`[data-cy="tag"]`).type('National green Cheese').should('have.value', 'National green Cheese');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        issueTag = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', issueTagPageUrlPattern);
    });
  });
});
