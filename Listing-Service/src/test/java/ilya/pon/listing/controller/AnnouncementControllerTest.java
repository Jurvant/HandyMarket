package ilya.pon.listing.controller;

import ilya.pon.listing.config.SecurityConfig;
import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.service.AnnouncementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnnouncementController.class)
@Import(SecurityConfig.class)
class AnnouncementControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AnnouncementService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @MockitoBean
    private Jwt jwtMock;

    @Test
    void shouldUpdateAnnouncement() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setUserId(userId);

        Announcement editedAnnouncement = new Announcement();
        editedAnnouncement.setId(id);
        editedAnnouncement.setStatus(Status.DEACTIVATED);

        when(service.update(any(), any(), any(Jwt.class))).thenReturn(editedAnnouncement);
        AnnouncementUpdateDto dto = new AnnouncementUpdateDto(
                "title",
                "description",
                new BigDecimal(100),
                new Category(),
                Status.ACTIVE
        );

        mockMvc.perform(put("/api/v1/announcement/{id}", id).header("")
                        .with(jwt().jwt(builder -> builder.subject(userId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(service).update(refEq(dto), eq(id), any(Jwt.class));
    }


    @Test
    void shouldFindAnnouncementById() throws Exception {
        Announcement announcement = new Announcement();
        UUID id = UUID.randomUUID();
        announcement.setId(id);
        when(service.findById(id)).thenReturn(announcement);

        mockMvc.perform(get("/api/v1/announcement/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id.toString()));
        verify(service, times(1)).findById(id);
    }

    @Test
    void shouldDeleteAnnouncement() throws Exception {
        Announcement announcement = new Announcement();
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        announcement.setId(id);

        mockMvc.perform(delete("/api/v1/announcement/{id}", id)
                .with(jwt().jwt(builder -> builder.subject(userId.toString())))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
        verify(service, times(1)).deleteById(eq(id), any(Jwt.class));
    }

    @Test
    void shouldCreateAnnouncement() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        Announcement announcement = new Announcement();
        announcement.setId(id);

        when(service.save(any(AnnouncementCreateDto.class), any(Jwt.class))).thenReturn(announcement);
        AnnouncementCreateDto dto = new AnnouncementCreateDto(
                "tittle",
                "description",
                new BigDecimal(100),
                "LUzyWMYJpW",
                null
        );

        mockMvc.perform(post("/api/v1/announcement/new")
                .with(jwt().jwt(builder -> builder.subject(userId.toString())))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
        ).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id.toString()));
        verify(service, times(1)).save(any(AnnouncementCreateDto.class), any(Jwt.class));
    }

    @Test
    void shouldReturnAllAnnouncementRelatedToUser() throws Exception {
        UUID userId = UUID.randomUUID();
        List<Announcement> announcements = List.of(
                new Announcement(),
                new Announcement(),
                new Announcement()
        );
        Page<Announcement> pageAnnouncement = new PageImpl<>(announcements);

        announcements.forEach(a -> a.setUserId(userId));
        when(service.findByUserId(eq(userId), any(Pageable.class))).thenReturn(pageAnnouncement);

        mockMvc.perform(get("/api/v1/announcement/user")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(service, times(1)).findByUserId(eq(userId), any(Pageable.class));
    }

    @Test
    void shouldSearchAnnouncement() throws Exception {
        List<Announcement> announcements = List.of(
                new Announcement(),
                new Announcement(),
                new Announcement()
        );
        Page<Announcement> pageAnnouncement = new PageImpl<>(announcements);
        String parameter = "parameter";
        when(service.search(eq(parameter), any(Pageable.class))).thenReturn(pageAnnouncement);

        mockMvc.perform(get("/api/v1/announcement/search")
                        .param("parameter", parameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
        verify(service, times(1)).search(eq(parameter), any(Pageable.class));
    }

    @Test
    void shouldFindByParameters() throws Exception {
        AnnouncementFilterDto dto = new AnnouncementFilterDto(
                UUID.randomUUID(),
                "tittle",
                null,
                new BigDecimal(100),
                null,
                null,
                null
        );
        List<Announcement> announcements = List.of(
                new Announcement(),
                new Announcement(),
                new Announcement()
        );
        Page<Announcement> pageAnnouncement = new PageImpl<>(announcements);
        when(service.findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class))).thenReturn(pageAnnouncement);

        mockMvc.perform(get("/api/v1/announcement?tittle=tittle&price=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
        verify(service, times(1)).findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class));
    }
}