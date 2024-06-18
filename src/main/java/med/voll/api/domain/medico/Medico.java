package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Medico {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String nombre;
  private String email;
  private String telefono;
  private String documento;
  private Boolean activo;
  @Enumerated(EnumType.STRING)
  private Especialidad especialidad;
  @Embedded
  private Direccion direccion;


  public Medico(DatosRegistrosMedicos datosRegistrosMedicos) {
    this.activo = true;
    this.nombre = datosRegistrosMedicos.nombre();
    this.email = datosRegistrosMedicos.email();
    this.telefono = datosRegistrosMedicos.telefono();
    this.documento = datosRegistrosMedicos.documento();
    this.especialidad = datosRegistrosMedicos.especialidad();
    this.direccion = new Direccion(datosRegistrosMedicos.direccion());
  }

  public void actualizarDatos(DatosActualizarMedicos datosActualizarMedicos) {
    if (datosActualizarMedicos.nombre() != null) {
      this.nombre = datosActualizarMedicos.nombre();
    }
    if (datosActualizarMedicos.documento() != null) {
      this.documento = datosActualizarMedicos.nombre();
    }
    if (datosActualizarMedicos.direccion() != null){
      this.direccion = direccion.actualizarDatos(datosActualizarMedicos.direccion());
    }
  }

  public void desactivarMedico() {
    this.activo = false;
  }
}
