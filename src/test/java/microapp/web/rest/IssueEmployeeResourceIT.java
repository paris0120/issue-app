package microapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import microapp.IntegrationTest;
import microapp.domain.IssueEmployee;
import microapp.repository.EntityManager;
import microapp.repository.IssueEmployeeRepository;
import microapp.service.dto.IssueEmployeeDTO;
import microapp.service.mapper.IssueEmployeeMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IssueEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IssueEmployeeResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_ASSIGNMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_ASSIGNMENT_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/issue-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssueEmployeeRepository issueEmployeeRepository;

    @Autowired
    private IssueEmployeeMapper issueEmployeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IssueEmployee issueEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueEmployee createEntity(EntityManager em) {
        IssueEmployee issueEmployee = new IssueEmployee().username(DEFAULT_USERNAME).issueAssignmentTitle(DEFAULT_ISSUE_ASSIGNMENT_TITLE);
        return issueEmployee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueEmployee createUpdatedEntity(EntityManager em) {
        IssueEmployee issueEmployee = new IssueEmployee().username(UPDATED_USERNAME).issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);
        return issueEmployee;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IssueEmployee.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        issueEmployee = createEntity(em);
    }

    @Test
    void createIssueEmployee() throws Exception {
        int databaseSizeBeforeCreate = issueEmployeeRepository.findAll().collectList().block().size();
        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeCreate + 1);
        IssueEmployee testIssueEmployee = issueEmployeeList.get(issueEmployeeList.size() - 1);
        assertThat(testIssueEmployee.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssueEmployee.getIssueAssignmentTitle()).isEqualTo(DEFAULT_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void createIssueEmployeeWithExistingId() throws Exception {
        // Create the IssueEmployee with an existing ID
        issueEmployee.setId(1L);
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        int databaseSizeBeforeCreate = issueEmployeeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployee.setUsername(null);

        // Create the IssueEmployee, which fails.
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueAssignmentTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployee.setIssueAssignmentTitle(null);

        // Create the IssueEmployee, which fails.
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIssueEmployees() {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        // Get all the issueEmployeeList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(issueEmployee.getId().intValue()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].issueAssignmentTitle")
            .value(hasItem(DEFAULT_ISSUE_ASSIGNMENT_TITLE));
    }

    @Test
    void getIssueEmployee() {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        // Get the issueEmployee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, issueEmployee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(issueEmployee.getId().intValue()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.issueAssignmentTitle")
            .value(is(DEFAULT_ISSUE_ASSIGNMENT_TITLE));
    }

    @Test
    void getNonExistingIssueEmployee() {
        // Get the issueEmployee
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIssueEmployee() throws Exception {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();

        // Update the issueEmployee
        IssueEmployee updatedIssueEmployee = issueEmployeeRepository.findById(issueEmployee.getId()).block();
        updatedIssueEmployee.username(UPDATED_USERNAME).issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(updatedIssueEmployee);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueEmployeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployee testIssueEmployee = issueEmployeeList.get(issueEmployeeList.size() - 1);
        assertThat(testIssueEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssueEmployee.getIssueAssignmentTitle()).isEqualTo(UPDATED_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void putNonExistingIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueEmployeeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIssueEmployeeWithPatch() throws Exception {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();

        // Update the issueEmployee using partial update
        IssueEmployee partialUpdatedIssueEmployee = new IssueEmployee();
        partialUpdatedIssueEmployee.setId(issueEmployee.getId());

        partialUpdatedIssueEmployee.issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployee testIssueEmployee = issueEmployeeList.get(issueEmployeeList.size() - 1);
        assertThat(testIssueEmployee.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssueEmployee.getIssueAssignmentTitle()).isEqualTo(UPDATED_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void fullUpdateIssueEmployeeWithPatch() throws Exception {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();

        // Update the issueEmployee using partial update
        IssueEmployee partialUpdatedIssueEmployee = new IssueEmployee();
        partialUpdatedIssueEmployee.setId(issueEmployee.getId());

        partialUpdatedIssueEmployee.username(UPDATED_USERNAME).issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueEmployee.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueEmployee))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployee testIssueEmployee = issueEmployeeList.get(issueEmployeeList.size() - 1);
        assertThat(testIssueEmployee.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssueEmployee.getIssueAssignmentTitle()).isEqualTo(UPDATED_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void patchNonExistingIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, issueEmployeeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIssueEmployee() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeRepository.findAll().collectList().block().size();
        issueEmployee.setId(count.incrementAndGet());

        // Create the IssueEmployee
        IssueEmployeeDTO issueEmployeeDTO = issueEmployeeMapper.toDto(issueEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueEmployee in the database
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIssueEmployee() {
        // Initialize the database
        issueEmployeeRepository.save(issueEmployee).block();

        int databaseSizeBeforeDelete = issueEmployeeRepository.findAll().collectList().block().size();

        // Delete the issueEmployee
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, issueEmployee.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IssueEmployee> issueEmployeeList = issueEmployeeRepository.findAll().collectList().block();
        assertThat(issueEmployeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
