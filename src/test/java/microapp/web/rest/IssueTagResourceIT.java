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
import microapp.domain.IssueTag;
import microapp.repository.EntityManager;
import microapp.repository.IssueTagRepository;
import microapp.service.dto.IssueTagDTO;
import microapp.service.mapper.IssueTagMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link IssueTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class IssueTagResourceIT {

    private static final Long DEFAULT_ISSUE_ID = 1L;
    private static final Long UPDATED_ISSUE_ID = 2L;

    private static final UUID DEFAULT_ISSUE_UUID = UUID.randomUUID();
    private static final UUID UPDATED_ISSUE_UUID = UUID.randomUUID();

    private static final String DEFAULT_TAG = "AAAAAAAAAA";
    private static final String UPDATED_TAG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/issue-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IssueTagRepository issueTagRepository;

    @Autowired
    private IssueTagMapper issueTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private IssueTag issueTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueTag createEntity(EntityManager em) {
        IssueTag issueTag = new IssueTag().issueId(DEFAULT_ISSUE_ID).issueUuid(DEFAULT_ISSUE_UUID).tag(DEFAULT_TAG);
        return issueTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IssueTag createUpdatedEntity(EntityManager em) {
        IssueTag issueTag = new IssueTag().issueId(UPDATED_ISSUE_ID).issueUuid(UPDATED_ISSUE_UUID).tag(UPDATED_TAG);
        return issueTag;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(IssueTag.class).block();
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
        issueTag = createEntity(em);
    }

    @Test
    void createIssueTag() throws Exception {
        int databaseSizeBeforeCreate = issueTagRepository.findAll().collectList().block().size();
        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeCreate + 1);
        IssueTag testIssueTag = issueTagList.get(issueTagList.size() - 1);
        assertThat(testIssueTag.getIssueId()).isEqualTo(DEFAULT_ISSUE_ID);
        assertThat(testIssueTag.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testIssueTag.getTag()).isEqualTo(DEFAULT_TAG);
    }

    @Test
    void createIssueTagWithExistingId() throws Exception {
        // Create the IssueTag with an existing ID
        issueTag.setId(1L);
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        int databaseSizeBeforeCreate = issueTagRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkIssueIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueTagRepository.findAll().collectList().block().size();
        // set the field null
        issueTag.setIssueId(null);

        // Create the IssueTag, which fails.
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIssueUuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueTagRepository.findAll().collectList().block().size();
        // set the field null
        issueTag.setIssueUuid(null);

        // Create the IssueTag, which fails.
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTagIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueTagRepository.findAll().collectList().block().size();
        // set the field null
        issueTag.setTag(null);

        // Create the IssueTag, which fails.
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllIssueTags() {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        // Get all the issueTagList
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
            .value(hasItem(issueTag.getId().intValue()))
            .jsonPath("$.[*].issueId")
            .value(hasItem(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.[*].issueUuid")
            .value(hasItem(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.[*].tag")
            .value(hasItem(DEFAULT_TAG));
    }

    @Test
    void getIssueTag() {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        // Get the issueTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, issueTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(issueTag.getId().intValue()))
            .jsonPath("$.issueId")
            .value(is(DEFAULT_ISSUE_ID.intValue()))
            .jsonPath("$.issueUuid")
            .value(is(DEFAULT_ISSUE_UUID.toString()))
            .jsonPath("$.tag")
            .value(is(DEFAULT_TAG));
    }

    @Test
    void getNonExistingIssueTag() {
        // Get the issueTag
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingIssueTag() throws Exception {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();

        // Update the issueTag
        IssueTag updatedIssueTag = issueTagRepository.findById(issueTag.getId()).block();
        updatedIssueTag.issueId(UPDATED_ISSUE_ID).issueUuid(UPDATED_ISSUE_UUID).tag(UPDATED_TAG);
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(updatedIssueTag);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
        IssueTag testIssueTag = issueTagList.get(issueTagList.size() - 1);
        assertThat(testIssueTag.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueTag.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testIssueTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    void putNonExistingIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, issueTagDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateIssueTagWithPatch() throws Exception {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();

        // Update the issueTag using partial update
        IssueTag partialUpdatedIssueTag = new IssueTag();
        partialUpdatedIssueTag.setId(issueTag.getId());

        partialUpdatedIssueTag.issueId(UPDATED_ISSUE_ID).tag(UPDATED_TAG);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
        IssueTag testIssueTag = issueTagList.get(issueTagList.size() - 1);
        assertThat(testIssueTag.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueTag.getIssueUuid()).isEqualTo(DEFAULT_ISSUE_UUID);
        assertThat(testIssueTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    void fullUpdateIssueTagWithPatch() throws Exception {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();

        // Update the issueTag using partial update
        IssueTag partialUpdatedIssueTag = new IssueTag();
        partialUpdatedIssueTag.setId(issueTag.getId());

        partialUpdatedIssueTag.issueId(UPDATED_ISSUE_ID).issueUuid(UPDATED_ISSUE_UUID).tag(UPDATED_TAG);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedIssueTag.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedIssueTag))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
        IssueTag testIssueTag = issueTagList.get(issueTagList.size() - 1);
        assertThat(testIssueTag.getIssueId()).isEqualTo(UPDATED_ISSUE_ID);
        assertThat(testIssueTag.getIssueUuid()).isEqualTo(UPDATED_ISSUE_UUID);
        assertThat(testIssueTag.getTag()).isEqualTo(UPDATED_TAG);
    }

    @Test
    void patchNonExistingIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, issueTagDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamIssueTag() throws Exception {
        int databaseSizeBeforeUpdate = issueTagRepository.findAll().collectList().block().size();
        issueTag.setId(count.incrementAndGet());

        // Create the IssueTag
        IssueTagDTO issueTagDTO = issueTagMapper.toDto(issueTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(issueTagDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the IssueTag in the database
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteIssueTag() {
        // Initialize the database
        issueTagRepository.save(issueTag).block();

        int databaseSizeBeforeDelete = issueTagRepository.findAll().collectList().block().size();

        // Delete the issueTag
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, issueTag.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<IssueTag> issueTagList = issueTagRepository.findAll().collectList().block();
        assertThat(issueTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
