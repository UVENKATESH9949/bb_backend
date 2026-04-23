package com.BrainBlitz.dto.request;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkImportRequest {

    // Each object in this list is a raw question map
    // Service layer detects questionType and routes accordingly
    private List<Object> questions;
}