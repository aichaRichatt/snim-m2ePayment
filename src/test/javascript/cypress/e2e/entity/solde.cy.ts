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

describe('Solde e2e test', () => {
  const soldePageUrl = '/solde';
  const soldePageUrlPattern = new RegExp('/solde(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const soldeSample = { clientRef: 'honorer' };

  let solde;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/soldes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/soldes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/soldes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (solde) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/soldes/${solde.id}`,
      }).then(() => {
        solde = undefined;
      });
    }
  });

  it('Soldes menu should load Soldes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('solde');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Solde').should('exist');
    cy.url().should('match', soldePageUrlPattern);
  });

  describe('Solde page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(soldePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Solde page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/solde/new$'));
        cy.getEntityCreateUpdateHeading('Solde');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', soldePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/soldes',
          body: soldeSample,
        }).then(({ body }) => {
          solde = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/soldes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/soldes?page=0&size=20>; rel="last",<http://localhost/api/soldes?page=0&size=20>; rel="first"',
              },
              body: [solde],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(soldePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Solde page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('solde');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', soldePageUrlPattern);
      });

      it('edit button click should load edit Solde page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Solde');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', soldePageUrlPattern);
      });

      it.skip('edit button click should load edit Solde page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Solde');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', soldePageUrlPattern);
      });

      it('last delete button click should delete instance of Solde', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('solde').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', soldePageUrlPattern);

        solde = undefined;
      });
    });
  });

  describe('new Solde page', () => {
    beforeEach(() => {
      cy.visit(`${soldePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Solde');
    });

    it('should create an instance of Solde', () => {
      cy.get(`[data-cy="clientRef"]`).type('hé vraisemblablement');
      cy.get(`[data-cy="clientRef"]`).should('have.value', 'hé vraisemblablement');

      cy.get(`[data-cy="clientName"]`).type('loufoque');
      cy.get(`[data-cy="clientName"]`).should('have.value', 'loufoque');

      cy.get(`[data-cy="clientFirstname"]`).type('coupable');
      cy.get(`[data-cy="clientFirstname"]`).should('have.value', 'coupable');

      cy.get(`[data-cy="amount"]`).type('28184.86');
      cy.get(`[data-cy="amount"]`).should('have.value', '28184.86');

      cy.get(`[data-cy="updatingDate"]`).type('2024-10-25');
      cy.get(`[data-cy="updatingDate"]`).blur();
      cy.get(`[data-cy="updatingDate"]`).should('have.value', '2024-10-25');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        solde = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', soldePageUrlPattern);
    });
  });
});
