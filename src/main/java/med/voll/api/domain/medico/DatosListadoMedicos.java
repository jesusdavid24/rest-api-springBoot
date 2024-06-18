package med.voll.api.domain.medico;

public record DatosListadoMedicos(
  Long id,
  String nombre,
  Especialidad especialidad,
  String documento,
  String email
) {

  public DatosListadoMedicos(Medico medico) {
    this(medico.getId(), medico.getNombre(), medico.getEspecialidad(), medico.getDocumento(), medico.getEmail());
  }
}
