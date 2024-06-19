package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.medico.MedicoRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MedicoActivo implements ValidadorDeConsultas {

  @Autowired
  private MedicoRespository medicoRespository;
  public void validar(DatosAgendarConsulta datosAgendarConsulta) {

    if (datosAgendarConsulta.idMedico() == null) {
      return;
    }

    var medicoActivo = medicoRespository.findActivoById(datosAgendarConsulta.idMedico());

    if (!medicoActivo) {
      throw new ValidationException("Medico se encuentra inactivo en el sistema");
    }
  }
}
