package com.nexusforge.nexusforge_backend.util;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageUtil {

    private PageUtil() {
    }

    public static Pageable of(int page, int size, String sortBy, String direction,
                              Set<String> allowedFields, String defaultField) {
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50);
        String field = allowedFields.contains(sortBy) ? sortBy : defaultField;
        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();
        return PageRequest.of(safePage, safeSize, sort);
    }
}
