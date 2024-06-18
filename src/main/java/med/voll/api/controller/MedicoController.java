package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired
  private MedicoRespository medicoRespository;

  @PostMapping
  public ResponseEntity<DatosRespuestaMedico> registrarMedico(
    @RequestBody @Valid DatosRegistrosMedicos datosRegistrosMedicos,
    UriComponentsBuilder uriComponentsBuilder
  ) {
    Medico medico = medicoRespository.save(new Medico(datosRegistrosMedicos));
    DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
        medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
        new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
          medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
          medico.getDireccion().getComplemento()));

    URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
    return ResponseEntity.created(url).body(datosRespuestaMedico);
  }

  @GetMapping
  public ResponseEntity<Page<DatosListadoMedicos>> listadoMedicos(
    @PageableDefault(size = 5, sort = "nombre") Pageable paginacion
  ) {
    //return medicoRespository.findAll(paginacion).map(DatosListadoMedicos::new);
    return ResponseEntity.ok( medicoRespository.findByActivoTrue(paginacion).map(DatosListadoMedicos::new));
  }

  @PutMapping
  @Transactional
  public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedicos datosActualizarMedicos) {
    Medico medico = medicoRespository.getReferenceById(datosActualizarMedicos.id());
    medico.actualizarDatos(datosActualizarMedicos);
    return ResponseEntity.ok(new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
            medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
            new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
                medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
                medico.getDireccion().getComplemento())));
  }

  @DeleteMapping("/{id}")
  @Transactional
  public ResponseEntity eliminarMedico(@PathVariable Long id) {
    Medico medico = medicoRespository.getReferenceById(id);
    medico.desactivarMedico();
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DatosRespuestaMedico> retornaDatosMedicos(@PathVariable Long id) {
    Medico medico = medicoRespository.getReferenceById(id);
    var datosMedicos = new DatosRespuestaMedico(medico.getId(), medico.getNombre(),
      medico.getEmail(), medico.getTelefono(), medico.getEspecialidad().toString(),
      new DatosDireccion(medico.getDireccion().getCalle(), medico.getDireccion().getDistrito(),
        medico.getDireccion().getCiudad(), medico.getDireccion().getNumero(),
        medico.getDireccion().getComplemento()));

    return ResponseEntity.ok(datosMedicos);
  }
}
