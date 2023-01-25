package microapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import microapp.IntegrationTest;
import microapp.domain.Issue;
import microapp.domain.enumeration.IssuePriority;
import microapp.domain.enumeration.IssueStatus;
import microapp.domain.enumeration.IssueType;
import microapp.repository.EntityManager;
import microapp.repository.IssueRepository;
import microapp.service.dto.IssueDTO;
import microapp.service.mapper.IssueMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IssueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IssueResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_CONTENT = "BBBBBBBBBB";

    private static final IssueType DEFAULT_ISSUE_TYPE = IssueType.BUG;
    private static final IssueType UPDATED_ISSUE_TYPE = IssueType.FEATURE;

    private static final IssueStatus DEFAULT_ISSUE_STATUS = IssueStatus.OPEN;
    private static final IssueStatus UPDATED_ISSUE_STATUS = IssueStatus.VERIFIED;

    private static final IssuePriority DEFAULT_ISSUE_PRIORITY = IssuePriority.HIGHEST;
    private static final IssuePriority UPDATED_ISSUE_PRIORITY = IssuePriority.HIGHER;

    private static final UUID DEFAULT_UUID = UUID.randomUUID();
    private static final UUID UPDATED_UUID = UUID.randomUUID();

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DELETED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELETED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/issues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Issue issue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .username(DEFAULT_USERNAME)
            .issueTitle(DEFAULT_ISSUE_TITLE)
            .issueContent(DEFAULT_ISSUE_CONTENT)
            .issueType(DEFAULT_ISSUE_TYPE)
            .issueStatus(DEFAULT_ISSUE_STATUS)
            .issuePriority(DEFAULT_ISSUE_PRIORITY)
            .uuid(DEFAULT_UUID)
            .created(DEFAULT_CREATED)
            .modified(DEFAULT_MODIFIED)
            .deleted(DEFAULT_DELETED);
        return issue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createUpdatedEntity(EntityManager em) {
        Issue issue = new Issue()
            .username(UPDATED_USERNAME)
            .issueTitle(UPDATED_ISSUE_TITLE)
            .issueContent(UPDATED_ISSUE_CONTENT)
            .issueType(UPDATED_ISSUE_TYPE)
            .issueStatus(UPDATED_ISSUE_STATUS)
            .issuePriority(UPDATED_ISSUE_PRIORITY)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .deleted(UPDATED_DELETED);
        return issue;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Issue.class).block();
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
        issue = createEntity(em);
    }

    @Test
    void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().collectList().block().size();
        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssue.getIssueTitle()).isEqualTo(DEFAULT_ISSUE_TITLE);
        assertThat(testIssue.getIssueContent()).isEqualTo(DEFAULT_ISSUE_CONTENT);
        assertThat(testIssue.getIssueType()).isEqualTo(DEFAULT_ISSUE_TYPE);
        assertThat(testIssue.getIssueStatus()).isEqualTo(DEFAULT_ISSUE_STATUS);
        assertThat(testIssue.getIssuePriority()).isEqualTo(DEFAULT_ISSUE_PRIORITY);
        assertThat(testIssue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testIssue.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testIssue.getModified()).isEqualTo(DEFAULT_MODIFIED);
        assertThat(testIssue.getDeleted()).isEqualTo(DEFAULT_DELETED);
    }

    @Test
    void createIssueWithExistingId() throws Exception {
        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        int databaseSizeBeforeCreate = issueRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIssueTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setIssueTitle(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setIssueContent(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setIssueType(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setIssueStatus(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssuePriorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setIssuePriority(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setUuid(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setCreated(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifiedIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().collectList().block().size();
        // set the field null
        issue.setModified(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIssues() {
        // Initialize the database
        issueRepository.save(issue).block();

        // Get all the issueList
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
            .value(hasItem(issue.getId().intValue()))
            .jsonPath("$.[*].username")
            .value(hasItem(DEFAULT_USERNAME))
            .jsonPath("$.[*].issueTitle")
            .value(hasItem(DEFAULT_ISSUE_TITLE))
            .jsonPath("$.[*].issueContent")
            .value(hasItem(DEFAULT_ISSUE_CONTENT))
            .jsonPath("$.[*].issueType")
            .value(hasItem(DEFAULT_ISSUE_TYPE.toString()))
            .jsonPath("$.[*].issueStatus")
            .value(hasItem(DEFAULT_ISSUE_STATUS.toString()))
            .jsonPath("$.[*].issuePriority")
            .value(hasItem(DEFAULT_ISSUE_PRIORITY.toString()))
            .jsonPath("$.[*].uuid")
            .value(hasItem(DEFAULT_UUID.toString()))
            .jsonPath("$.[*].created")
            .value(hasItem(DEFAULT_CREATED.toString()))
            .jsonPath("$.[*].modified")
            .value(hasItem(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.[*].deleted")
            .value(hasItem(DEFAULT_DELETED.toString()));
    }

    @Test
    void getIssue() {
        // Initialize the database
        issueRepository.save(issue).block();

        // Get the issue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, issue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(issue.getId().intValue()))
            .jsonPath("$.username")
            .value(is(DEFAULT_USERNAME))
            .jsonPath("$.issueTitle")
            .value(is(DEFAULT_ISSUE_TITLE))
            .jsonPath("$.issueContent")
            .value(is(DEFAULT_ISSUE_CONTENT))
            .jsonPath("$.issueType")
            .value(is(DEFAULT_ISSUE_TYPE.toString()))
            .jsonPath("$.issueStatus")
            .value(is(DEFAULT_ISSUE_STATUS.toString()))
            .jsonPath("$.issuePriority")
            .value(is(DEFAULT_ISSUE_PRIORITY.toString()))
            .jsonPath("$.uuid")
            .value(is(DEFAULT_UUID.toString()))
            .jsonPath("$.created")
            .value(is(DEFAULT_CREATED.toString()))
            .jsonPath("$.modified")
            .value(is(DEFAULT_MODIFIED.toString()))
            .jsonPath("$.deleted")
            .value(is(DEFAULT_DELETED.toString()));
    }

    @Test
    void getNonExistingIssue() {
        // Get the issue
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIssue() throws Exception {
        // Initialize the database
        issueRepository.save(issue).block();

        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findById(issue.getId()).block();
        updatedIssue
            .username(UPDATED_USERNAME)
            .issueTitle(UPDATED_ISSUE_TITLE)
            .issueContent(UPDATED_ISSUE_CONTENT)
            .issueType(UPDATED_ISSUE_TYPE)
            .issueStatus(UPDATED_ISSUE_STATUS)
            .issuePriority(UPDATED_ISSUE_PRIORITY)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .deleted(UPDATED_DELETED);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssue.getIssueTitle()).isEqualTo(UPDATED_ISSUE_TITLE);
        assertThat(testIssue.getIssueContent()).isEqualTo(UPDATED_ISSUE_CONTENT);
        assertThat(testIssue.getIssueType()).isEqualTo(UPDATED_ISSUE_TYPE);
        assertThat(testIssue.getIssueStatus()).isEqualTo(UPDATED_ISSUE_STATUS);
        assertThat(testIssue.getIssuePriority()).isEqualTo(UPDATED_ISSUE_PRIORITY);
        assertThat(testIssue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testIssue.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIssue.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testIssue.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    void putNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIssueWithPatch() throws Exception {
        // Initialize the database
        issueRepository.save(issue).block();

        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();

        // Update the issue using partial update
        Issue partialUpdatedIssue = new Issue();
        partialUpdatedIssue.setId(issue.getId());

        partialUpdatedIssue
            .issueTitle(UPDATED_ISSUE_TITLE)
            .issueContent(UPDATED_ISSUE_CONTENT)
            .issueStatus(UPDATED_ISSUE_STATUS)
            .issuePriority(UPDATED_ISSUE_PRIORITY)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .deleted(UPDATED_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testIssue.getIssueTitle()).isEqualTo(UPDATED_ISSUE_TITLE);
        assertThat(testIssue.getIssueContent()).isEqualTo(UPDATED_ISSUE_CONTENT);
        assertThat(testIssue.getIssueType()).isEqualTo(DEFAULT_ISSUE_TYPE);
        assertThat(testIssue.getIssueStatus()).isEqualTo(UPDATED_ISSUE_STATUS);
        assertThat(testIssue.getIssuePriority()).isEqualTo(UPDATED_ISSUE_PRIORITY);
        assertThat(testIssue.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testIssue.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIssue.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testIssue.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    void fullUpdateIssueWithPatch() throws Exception {
        // Initialize the database
        issueRepository.save(issue).block();

        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();

        // Update the issue using partial update
        Issue partialUpdatedIssue = new Issue();
        partialUpdatedIssue.setId(issue.getId());

        partialUpdatedIssue
            .username(UPDATED_USERNAME)
            .issueTitle(UPDATED_ISSUE_TITLE)
            .issueContent(UPDATED_ISSUE_CONTENT)
            .issueType(UPDATED_ISSUE_TYPE)
            .issueStatus(UPDATED_ISSUE_STATUS)
            .issuePriority(UPDATED_ISSUE_PRIORITY)
            .uuid(UPDATED_UUID)
            .created(UPDATED_CREATED)
            .modified(UPDATED_MODIFIED)
            .deleted(UPDATED_DELETED);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssue.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssue))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testIssue.getIssueTitle()).isEqualTo(UPDATED_ISSUE_TITLE);
        assertThat(testIssue.getIssueContent()).isEqualTo(UPDATED_ISSUE_CONTENT);
        assertThat(testIssue.getIssueType()).isEqualTo(UPDATED_ISSUE_TYPE);
        assertThat(testIssue.getIssueStatus()).isEqualTo(UPDATED_ISSUE_STATUS);
        assertThat(testIssue.getIssuePriority()).isEqualTo(UPDATED_ISSUE_PRIORITY);
        assertThat(testIssue.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testIssue.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testIssue.getModified()).isEqualTo(UPDATED_MODIFIED);
        assertThat(testIssue.getDeleted()).isEqualTo(UPDATED_DELETED);
    }

    @Test
    void patchNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, issueDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().collectList().block().size();
        issue.setId(count.incrementAndGet());

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIssue() {
        // Initialize the database
        issueRepository.save(issue).block();

        int databaseSizeBeforeDelete = issueRepository.findAll().collectList().block().size();

        // Delete the issue
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, issue.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Issue> issueList = issueRepository.findAll().collectList().block();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
