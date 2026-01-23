package ilya.pon.listing.dto.initial;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryInitialDto {
    @JsonProperty("category_id")
    private String id;
    @JsonProperty("category_name")
    private String name;
    @JsonProperty("parent_id")
    private String parentId;
}
