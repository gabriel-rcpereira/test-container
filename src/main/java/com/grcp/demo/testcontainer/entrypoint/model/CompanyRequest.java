package com.grcp.demo.testcontainer.entrypoint.model;

import javax.validation.constraints.NotBlank;

public record CompanyRequest(
        @NotBlank
        String name
) {
}
