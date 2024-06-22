package med.voll.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosCancelamientoConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/consultas")
public class ConsultaController {

  @Autowired
  private AgendaDeConsultaService service;

  @PostMapping
  @Transactional
  public ResponseEntity agendar(@RequestBody @Valid DatosAgendarConsulta datosAgendarConsulta) {

    var response = service.agendar(datosAgendarConsulta);

    return ResponseEntity.ok(response);
  }

  @DeleteMapping
  @Transactional
  public ResponseEntity eliminarConsulta(@RequestBody @Valid DatosCancelamientoConsulta datosCancelamientoConsulta) {
   service.cancelar(datosCancelamientoConsulta);
   return  ResponseEntity.noContent().build();
  }
}
