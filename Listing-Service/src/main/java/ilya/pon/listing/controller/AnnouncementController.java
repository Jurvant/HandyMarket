package ilya.pon.listing.controller;

import ilya.pon.listing.domain.Announcement;
import ilya.pon.listing.dto.request.AnnouncementCreateDto;
import ilya.pon.listing.dto.request.AnnouncementFilterDto;
import ilya.pon.listing.dto.request.AnnouncementUpdateDto;
import ilya.pon.listing.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AnnouncementController {
    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @Operation(summary = "Get a announcement by its parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found announcements",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                    content = @Content)})
    @GetMapping("announcement")
    public Page<Announcement> findByParameters(AnnouncementFilterDto dto, Pageable pageable) {
        return service.findByParameters(dto, pageable);
    }

    @Operation(summary = "Create a new announcement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                    content = @Content)})
    @PostMapping("/announcement/new")
    public Announcement createAnnouncement(
            @RequestHeader("X-User-Id") UUID userId, @RequestBody AnnouncementCreateDto dto) {
        dto.setUserId(userId);
        return service.save(dto);
    }

    @Operation(summary = "Delete a announcement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "404", description = "Announcement not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/announcement/{id}")
    public void deleteAnnouncement(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        service.deleteById(id, userId);
    }

    @Operation(summary = "Get a announcement by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found announcement",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    @GetMapping("announcement/{id}")
    public Announcement findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @Operation(summary = "Update a announcement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    @PutMapping("announcement/{id}")
    public Announcement update(@RequestBody AnnouncementUpdateDto dto, @PathVariable UUID id, @RequestHeader("X-User-Id") UUID userId) {
        return service.update(dto, id, userId);
    }

    @Operation(summary = "Deactivate a announcement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement deactivated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("announcement/{id}/deactivate")
    public void deactivateAnnouncement(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID id) {
        service.deactivateAnouncement(id, userId);
    }

    @Operation(summary = "Activate a announcement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement activated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("announcement/{id}/activate")
    public void activateAnnouncement(@RequestHeader("X-User-Id") UUID userId, @PathVariable UUID id) {
        service.activateAnouncement(id, userId);
    }

    @Operation(summary = "Search for announcements by name and description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All founded Announcements (may be empty)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied")
    })
    @GetMapping("announcement/search")
    public Page<Announcement> search(String parameter, Pageable pageable) {
        return service.search(parameter, pageable);
    }

    @Operation(summary = "Get all announcements of a user")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "All Announcements related to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Announcement.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied")
    })
    @GetMapping("announcement/user")
    public Page<Announcement> myAnnouncement(@RequestHeader("X-User-Id") UUID userId, Pageable pageable) {
        return service.findByUserId(userId, pageable);
    }
}