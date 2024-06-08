package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {
  @Autowired
  private MedicoRespository medicoRespository;

  @PostMapping
  public void registrarMedico(@RequestBody @Valid DatosRegistrosMedicos datosRegistrosMedicos) {
    medicoRespository.save(new Medico(datosRegistrosMedicos));
  }

  @GetMapping
  public Page<DatosListadoMedicos> listadoMedicos(@PageableDefault(size = 5, sort = "nombre") Pageable paginacion) {
    //return medicoRespository.findAll(paginacion).map(DatosListadoMedicos::new);
    return medicoRespository.findByActivoTrue(paginacion).map(DatosListadoMedicos::new);
  }

  @PutMapping
  @Transactional
  public void actualizarMedico(@RequestBody @Valid DatosActualizarMedicos datosActualizarMedicos) {
    Medico medico = medicoRespository.getReferenceById(datosActualizarMedicos.id());
    medico.actualizarDatos(datosActualizarMedicos);
  }

  @DeleteMapping("/{id}")
  @Transactional
  public void eliminarMedico(@PathVariable Long id) {
    Medico medico = medicoRespository.getReferenceById(id);
    medico.desactivarMedico();
  }
}
