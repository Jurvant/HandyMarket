package ilya.pon.listing.controller;

import ilya.pon.listing.config.SecurityConfig;
import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.domain.Category;
import ilya.pon.listing.domain.additions.Status;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
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
import java.time.LocalDateTime;
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
        AnnouncementResponseDto dto = new AnnouncementResponseDto(
                id,
                userId,
                "title",
                "description",
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "status",
                "category"
        );
        AnnouncementUpdateDto updateDto = new AnnouncementUpdateDto(
                "title",
                "description",
                new BigDecimal(100),
                new Category(),
                Status.ACTIVE
        );
        when(service.update(any(AnnouncementUpdateDto.class), any(UUID.class), any(Jwt.class))).thenReturn(dto);


        mockMvc.perform(put("/api/v1/announcement/{id}", id)
                        .with(jwt().jwt(builder -> builder.subject(userId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(service).update(refEq(updateDto), eq(id), any(Jwt.class));
    }


    @Test
    void shouldFindAnnouncementById() throws Exception {
        Announcement announcement = new Announcement();
        UUID id = UUID.randomUUID();
        announcement.setId(id);
        AnnouncementResponseDto dto = new AnnouncementResponseDto(
                id,
                UUID.randomUUID(),
                "title",
                "description",
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "status",
                "category"
        );
        when(service.findDtoById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id.toString()));
        verify(service, times(1)).findDtoById(id);
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
        AnnouncementResponseDto response = new AnnouncementResponseDto(
                id,
                userId,
                "title",
                "description",
                BigDecimal.valueOf(100),
                LocalDateTime.now(),
                "status",
                "category"
        );
        when(service.save(any(AnnouncementCreateDto.class), any(Jwt.class))).thenReturn(response);
        AnnouncementCreateDto dto = new AnnouncementCreateDto(
                "title",
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
        Page<AnnouncementResponseDto> dto = new PageImpl<>(
                List.of(new AnnouncementResponseDto(
                        UUID.randomUUID(),
                        userId,
                        "title",
                        "discrption",
                        BigDecimal.valueOf(100),
                        LocalDateTime.now(),
                        "status",
                        "category"
                ))
        );
        when(service.findByUserId(eq(userId), any(Pageable.class))).thenReturn(dto);
        mockMvc.perform(get("/api/v1/announcement/user")
                        .header("X-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(service, times(1)).findByUserId(eq(userId), any(Pageable.class));
    }

    @Test
    void shouldSearchAnnouncement() throws Exception {
        String parameter = "parameter";

        Page<AnnouncementResponseDto> dto = new PageImpl<>(
                List.of(new AnnouncementResponseDto(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "title",
                        "discrption",
                        BigDecimal.valueOf(100),
                        LocalDateTime.now(),
                        "status",
                        "category"
                ))
        );
        when(service.search(eq(parameter), any(Pageable.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement/search")
                        .param("parameter", parameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
        verify(service, times(1)).search(eq(parameter), any(Pageable.class));
    }

    @Test
    void shouldFindByParameters() throws Exception {

        Page<AnnouncementResponseDto> dto = new PageImpl<>(
                List.of(new AnnouncementResponseDto(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "title",
                        "discrption",
                        BigDecimal.valueOf(100),
                        LocalDateTime.now(),
                        "status",
                        "category"
                ))
        );
        when(service.findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement?tittle=tittle&price=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
        verify(service, times(1)).findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class));
    }
}