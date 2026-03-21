package ilya.pon.search.dto;

import java.util.List;

public record SearchRequestDto (
        List<String> fields,
        String searchTerm
){}