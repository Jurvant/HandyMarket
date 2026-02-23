package ilya.pon.listing.controller;

import ilya.pon.listing.config.SecurityConfig;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.dto.request.ImageCreateDto;
import ilya.pon.listing.dto.response.AnnouncementResponseDto;
import ilya.pon.listing.dto.response.ImageResponseDto;
import ilya.pon.listing.dto.wrapper.CreateAnnounceImageDto;
import ilya.pon.listing.dto.wrapper.ResponseAnnouncementImageDto;
import ilya.pon.listing.dto.wrapper.UpdateAnnouncementImageDto;
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

        AnnouncementUpdateDto announcementUpdateDto = new AnnouncementUpdateDto(
                "Test Title",
                "Test Description",
                BigDecimal.valueOf(1000.50),
                null,
                null
        );
        List<ImageCreateDto> imageCreateDtos = List.of(
                new ImageCreateDto("Main image", "https://example.com/image.jpg")
        );
        UpdateAnnouncementImageDto updateDto = new UpdateAnnouncementImageDto(
                announcementUpdateDto,
                imageCreateDtos
        );

        AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto(
                id,
                userId,
                "Test Title",
                "Test Description",
                BigDecimal.valueOf(1000.50),
                LocalDateTime.now(),
                "ACTIVE",
                "ELECTRONICS"
        );
        List<ImageResponseDto> imageResponseDtos = List.of(
                new ImageResponseDto(UUID.randomUUID(), "https://example.com/image.jpg", "Main image")
        );
        ResponseAnnouncementImageDto dto = new ResponseAnnouncementImageDto(
                announcementResponseDto,
                imageResponseDtos
        );

        when(service.update(any(UpdateAnnouncementImageDto.class), any(UUID.class), any(Jwt.class))).thenReturn(dto);

        mockMvc.perform(put("/api/v1/announcement/{id}", id)
                        .with(jwt().jwt(builder -> builder.subject(userId.toString())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
        verify(service).update(refEq(updateDto), eq(id), any(Jwt.class));
    }

    @Test
    void shouldFindAnnouncementById() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        AnnouncementResponseDto announcementResponseDto = new AnnouncementResponseDto(
                id,
                userId,
                "Test Title",
                "Test Description",
                BigDecimal.valueOf(1000.50),
                LocalDateTime.now(),
                "ACTIVE",
                "ELECTRONICS"
        );
        List<ImageResponseDto> imageResponseDtos = List.of(
                new ImageResponseDto(UUID.randomUUID(), "https://example.com/image.jpg", "Main image")
        );
        ResponseAnnouncementImageDto dto = new ResponseAnnouncementImageDto(
                announcementResponseDto,
                imageResponseDtos
        );

        when(service.findDtoById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.announcementDto.id").value(id.toString()));

        verify(service, times(1)).findDtoById(id);
    }
    @Test
    void shouldDeleteAnnouncement() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

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

        AnnouncementResponseDto announcementResp = new AnnouncementResponseDto(
                id, userId, "title", "description", BigDecimal.valueOf(100),
                LocalDateTime.now(), "ACTIVE", "CATEGORY"
        );
        List<ImageResponseDto> imagesResp = List.of(
                new ImageResponseDto(UUID.randomUUID(), "url", "desc")
        );
        ResponseAnnouncementImageDto response = new ResponseAnnouncementImageDto(
                announcementResp,
                imagesResp
        );

        when(service.save(any(CreateAnnounceImageDto.class), any(Jwt.class))).thenReturn(response);

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
                .andExpect(jsonPath("$.announcementDto.id").value(id.toString())); // Исправлен путь!

        verify(service, times(1)).save(any(CreateAnnounceImageDto.class), any(Jwt.class));
    }

    @Test
    void shouldReturnAllAnnouncementRelatedToUser() throws Exception {
        UUID userId = UUID.randomUUID();

        AnnouncementResponseDto announcementResp = new AnnouncementResponseDto(
                UUID.randomUUID(), userId, "title", "desc", BigDecimal.TEN,
                LocalDateTime.now(), "ACTIVE", "CATEGORY"
        );
        ResponseAnnouncementImageDto responseItem = new ResponseAnnouncementImageDto(
                announcementResp,
                List.of()
        );

        Page<ResponseAnnouncementImageDto> dto = new PageImpl<>(List.of(responseItem));

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

        AnnouncementResponseDto announcementResp = new AnnouncementResponseDto(
                UUID.randomUUID(), UUID.randomUUID(), "title", "desc", BigDecimal.TEN,
                LocalDateTime.now(), "ACTIVE", "CATEGORY"
        );
        ResponseAnnouncementImageDto responseItem = new ResponseAnnouncementImageDto(
                announcementResp,
                List.of()
        );

        Page<ResponseAnnouncementImageDto> dto = new PageImpl<>(List.of(responseItem));

        when(service.search(eq(parameter), any(Pageable.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement/search")
                        .param("parameter", parameter))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(service, times(1)).search(eq(parameter), any(Pageable.class));
    }

    @Test
    void shouldFindByParameters() throws Exception {
        AnnouncementResponseDto announcementResp = new AnnouncementResponseDto(
                UUID.randomUUID(), UUID.randomUUID(), "title", "desc", BigDecimal.TEN,
                LocalDateTime.now(), "ACTIVE", "CATEGORY"
        );
        ResponseAnnouncementImageDto responseItem = new ResponseAnnouncementImageDto(
                announcementResp,
                List.of()
        );

        Page<ResponseAnnouncementImageDto> dto = new PageImpl<>(List.of(responseItem));

        when(service.findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class))).thenReturn(dto);

        mockMvc.perform(get("/api/v1/announcement?title=title&price=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());

        verify(service, times(1)).findByParameters(any(AnnouncementFilterDto.class), any(Pageable.class));
    }
}