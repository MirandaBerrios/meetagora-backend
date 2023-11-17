package com.mirandez.meetagora.entity.report;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportBySubject {
    private String email;
    private String fecha;
    private String dia;
    private String presentes;
    private String asignatura;
    private String suma_validas;
    private String suma_no_validas;
}
