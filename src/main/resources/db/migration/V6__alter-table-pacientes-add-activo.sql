ALTER TABLE pacientes ADD activo tinyint;
update pacientes set activo = 1;