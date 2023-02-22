package org.fer.hr.progi.nasiljubimci.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.fer.hr.progi.nasiljubimci.enums.ServiceType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceModel {
    private String description;
    private ServiceType serviceType;
}
