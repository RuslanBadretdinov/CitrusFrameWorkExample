package dto;

import lombok.*;
import lombok.Data;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Support {
    private String text;
    private String url;
}
