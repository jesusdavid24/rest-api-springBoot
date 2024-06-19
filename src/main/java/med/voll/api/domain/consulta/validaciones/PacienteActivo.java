package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PacienteActivo implements ValidadorDeConsultas {
  @Autowired
  private PacienteRepository pacienteRepositoryl;
  public void validar(DatosAgendarConsulta datosAgendarConsulta) {
    if (datosAgendarConsulta == null) {
      return;
    }

    var pacienteActivo = pacienteRepositoryl.findActivoById(datosAgendarConsulta.idPaciente());

    if (!pacienteActivo) {
      throw new ValidationException("No se puede permitir agendar cita ya que el paciente se encuentra inactivo");
    }
  }
}
