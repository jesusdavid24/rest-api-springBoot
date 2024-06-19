package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRespository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

  @Autowired
  private MedicoRespository medicoRespository;

  @Autowired
  private PacienteRepository pacienteRepository;

  @Autowired
  private ConsultaRepository consultaRepository;

  @Autowired
  List<ValidadorDeConsultas> validadores;

  public DatosDetalleConsulta agendar(DatosAgendarConsulta datosAgendarConsulta) {

    if (!pacienteRepository.findById(datosAgendarConsulta.idPaciente()).isPresent()) {
      throw new ValidacionDeIntegridad("este id para el paciente no fue encontraddo");
    }

    if (datosAgendarConsulta.idMedico() != null && !medicoRespository.existsById(datosAgendarConsulta.idMedico())) {
      throw new ValidacionDeIntegridad("este id para el medico no fue encontraddo");
    }

    validadores.forEach(v -> v.validar(datosAgendarConsulta));

    var paciente = pacienteRepository.findById(datosAgendarConsulta.idPaciente()).get();

    var medico = seleccionMedico(datosAgendarConsulta);

    if (medico == null) {
      throw new ValidacionDeIntegridad("No existen medicos disponibles para este horario y especialidad");
    }

    var consulta = new Consulta(null, medico, paciente, datosAgendarConsulta.fecha());
    consultaRepository.save(consulta);

    return new DatosDetalleConsulta(consulta);
  }

  private Medico seleccionMedico(DatosAgendarConsulta datosAgendarConsulta) {
    if (datosAgendarConsulta.idMedico() != null) {
      return medicoRespository.getReferenceById(datosAgendarConsulta.idMedico());
    }

    if (datosAgendarConsulta.especialidad() == null) {
      throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
    }

    return medicoRespository.seleccionarMedicoConEspecialidadEnFecha(
      datosAgendarConsulta.especialidad(), datosAgendarConsulta.fecha()
    );
  }
}
