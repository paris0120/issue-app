package microapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import microapp.IntegrationTest;
import microapp.domain.IssueEmployeeAssignment;
import microapp.repository.EntityManager;
import microapp.repository.IssueEmployeeAssignmentRepository;
import microapp.service.dto.IssueEmployeeAssignmentDTO;
import microapp.service.mapper.IssueEmployeeAssignmentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IssueEmployeeAssignmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IssueEmployeeAssignmentResourceIT {

    private static final Long DEFAULT_ISSUE_ID = 1L;
    private static final Long UPDATED_ISSUE_ID = 2L;

    private static final UUID DEFAULT_ISSUE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_ISSUE_UUID = UUID.randomUUID();

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_ASSIGNMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_ASSIGNMENT_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/issue-employee-assignments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssueEmployeeAssignmentRepository issueEmployeeAssignmentRepository;

    @Autowired
    private IssueEmployeeAssignmentMapper issueEmployeeAssignmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IssueEmployeeAssignment issueEmployeeAssignment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueEmployeeAssignment createEntity(EntityManager em) {
        IssueEmployeeAssignment issueEmployeeAssignment = new IssueEmployeeAssignment()
            .issueId(DEFAULT_ISSUE_ID)
            .issueUuid(DEFAULT_ISSUE_UUID)
            .username(DEFAULT_USERNAME)
            .issueAssignmentTitle(DEFAULT_ISSUE_ASSIGNMENT_TITLE);
        return issueEmployeeAssignment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueEmployeeAssignment createUpdatedEntity(EntityManager em) {
        IssueEmployeeAssignment issueEmployeeAssignment = new IssueEmployeeAssignment()
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);
        return issueEmployeeAssignment;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IssueEmployeeAssignment.class).block();
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
        issueEmployeeAssignment = createEntity(em);
    }

    @Test
    void createIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeCreate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeCreate + 1);
        IssueEmployeeAssignment testIssueEmployeeAssignment = issueEmployeeAssignmentList.get(issueEmployeeAssignmentList.size() - 1);
        assertThat(testIssueEmployeeAssignment.getIssueId()).isEqualTo(DEFAULT_ISSUE_ID);
        assertThat(testIssueEmployeeAssignment.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testIssueEmployeeAssignment.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssueEmployeeAssignment.getIssueAssignmentTitle()).isEqualTo(DEFAULT_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void createIssueEmployeeAssignmentWithExistingId() throws Exception {
        // Create the IssueEmployeeAssignment with an existing ID
        issueEmployeeAssignment.setId(1L);
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        int databaseSizeBeforeCreate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIssueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployeeAssignment.setIssueId(null);

        // Create the IssueEmployeeAssignment, which fails.
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployeeAssignment.setIssueUuid(null);

        // Create the IssueEmployeeAssignment, which fails.
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployeeAssignment.setUsername(null);

        // Create the IssueEmployeeAssignment, which fails.
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueAssignmentTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        // set the field null
        issueEmployeeAssignment.setIssueAssignmentTitle(null);

        // Create the IssueEmployeeAssignment, which fails.
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIssueEmployeeAssignments() {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        // Get all the issueEmployeeAssignmentList
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
            .value(hasItem(issueEmployeeAssignment.getId().intValue()))
            .jsonPath("$.[*].issueId")
            .value(hasItem(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.[*].issueUuid")
            .value(hasItem(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].issueAssignmentTitle")
            .value(hasItem(DEFAULT_ISSUE_ASSIGNMENT_TITLE));
    }

    @Test
    void getIssueEmployeeAssignment() {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        // Get the issueEmployeeAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, issueEmployeeAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(issueEmployeeAssignment.getId().intValue()))
            .jsonPath("$.issueId")
            .value(is(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.issueUuid")
            .value(is(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.issueAssignmentTitle")
            .value(is(DEFAULT_ISSUE_ASSIGNMENT_TITLE));
    }

    @Test
    void getNonExistingIssueEmployeeAssignment() {
        // Get the issueEmployeeAssignment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIssueEmployeeAssignment() throws Exception {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();

        // Update the issueEmployeeAssignment
        IssueEmployeeAssignment updatedIssueEmployeeAssignment = issueEmployeeAssignmentRepository
            .findById(issueEmployeeAssignment.getId())
            .block();
        updatedIssueEmployeeAssignment
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(updatedIssueEmployeeAssignment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueEmployeeAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployeeAssignment testIssueEmployeeAssignment = issueEmployeeAssignmentList.get(issueEmployeeAssignmentList.size() - 1);
        assertThat(testIssueEmployeeAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueEmployeeAssignment.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testIssueEmployeeAssignment.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssueEmployeeAssignment.getIssueAssignmentTitle()).isEqualTo(UPDATED_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void putNonExistingIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueEmployeeAssignmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIssueEmployeeAssignmentWithPatch() throws Exception {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();

        // Update the issueEmployeeAssignment using partial update
        IssueEmployeeAssignment partialUpdatedIssueEmployeeAssignment = new IssueEmployeeAssignment();
        partialUpdatedIssueEmployeeAssignment.setId(issueEmployeeAssignment.getId());

        partialUpdatedIssueEmployeeAssignment.issueId(UPDATED_ISSUE_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueEmployeeAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueEmployeeAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployeeAssignment testIssueEmployeeAssignment = issueEmployeeAssignmentList.get(issueEmployeeAssignmentList.size() - 1);
        assertThat(testIssueEmployeeAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueEmployeeAssignment.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testIssueEmployeeAssignment.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssueEmployeeAssignment.getIssueAssignmentTitle()).isEqualTo(DEFAULT_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void fullUpdateIssueEmployeeAssignmentWithPatch() throws Exception {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();

        // Update the issueEmployeeAssignment using partial update
        IssueEmployeeAssignment partialUpdatedIssueEmployeeAssignment = new IssueEmployeeAssignment();
        partialUpdatedIssueEmployeeAssignment.setId(issueEmployeeAssignment.getId());

        partialUpdatedIssueEmployeeAssignment
            .issueId(UPDATED_ISSUE_ID)
            .issueUuid(UPDATED_ISSUE_UUID)
            .username(UPDATED_USERNAME)
            .issueAssignmentTitle(UPDATED_ISSUE_ASSIGNMENT_TITLE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueEmployeeAssignment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueEmployeeAssignment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
        IssueEmployeeAssignment testIssueEmployeeAssignment = issueEmployeeAssignmentList.get(issueEmployeeAssignmentList.size() - 1);
        assertThat(testIssueEmployeeAssignment.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueEmployeeAssignment.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testIssueEmployeeAssignment.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssueEmployeeAssignment.getIssueAssignmentTitle()).isEqualTo(UPDATED_ISSUE_ASSIGNMENT_TITLE);
    }

    @Test
    void patchNonExistingIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, issueEmployeeAssignmentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIssueEmployeeAssignment() throws Exception {
        int databaseSizeBeforeUpdate = issueEmployeeAssignmentRepository.findAll().collectList().block().size();
        issueEmployeeAssignment.setId(count.incrementAndGet());

        // Create the IssueEmployeeAssignment
        IssueEmployeeAssignmentDTO issueEmployeeAssignmentDTO = issueEmployeeAssignmentMapper.toDto(issueEmployeeAssignment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueEmployeeAssignmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueEmployeeAssignment in the database
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIssueEmployeeAssignment() {
        // Initialize the database
        issueEmployeeAssignmentRepository.save(issueEmployeeAssignment).block();

        int databaseSizeBeforeDelete = issueEmployeeAssignmentRepository.findAll().collectList().block().size();

        // Delete the issueEmployeeAssignment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, issueEmployeeAssignment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IssueEmployeeAssignment> issueEmployeeAssignmentList = issueEmployeeAssignmentRepository.findAll().collectList().block();
        assertThat(issueEmployeeAssignmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
