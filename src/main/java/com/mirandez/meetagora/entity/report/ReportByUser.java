package com.mirandez.meetagora.entity.report;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportByUser {

    private String email;
    private String fecha;
    private String hora;
    private String dia;
    private String asignatura;
    private String valida;
}
