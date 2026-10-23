create database SistemaReservas;
use SistemaReservas;

create table Usuario (
    id varchar(20) not null,
    clave varchar(255) not null,
    rol varchar(20) not null,
    Primary Key (id)
);

create table Funcionario (
    id varchar(20) not null,
    nombre varchar(150) not null,
    telefono varchar(20) not null,
    usuarioId varchar(20) unique,
    Primary Key (id)
);

create table Categoria (
    id varchar(20) not null,
    descripcion varchar(255) not null,
    Primary Key (id)
);

create table Recurso (
    id varchar(20) not null,
    categoriaId varchar(20) not null,
    descripcion varchar(255) not null,
    Primary Key (id)
);

create table Reserva (
    id varchar(20) not null,
    funcionarioId varchar(20) not null,
    actividad varchar(500) not null,
    estado varchar(20) not null,
    fecha date not null,
    horaInicio time not null,
    horaFin time not null,
    Primary Key (id)
);

create table ReservaCategoria (
    reservaId varchar(20) not null,
    categoriaId varchar(20) not null,
    Primary Key (reservaId, categoriaId)
);

create table ReservaRecurso (
    reservaId varchar(20) not null,
    recursoId varchar(20) not null,
    Primary Key (reservaId, recursoId)
);

create table Secuencia (
    tipo varchar(10) not null,
    ultimo bigint not null,
    Primary Key (tipo)
);

ALTER TABLE Funcionario ADD Foreign Key (usuarioId) REFERENCES Usuario(id);
ALTER TABLE Recurso ADD Foreign Key (categoriaId) REFERENCES Categoria(id);
ALTER TABLE Reserva ADD Foreign Key (funcionarioId) REFERENCES Funcionario(id);
ALTER TABLE ReservaCategoria ADD Foreign Key (reservaId) REFERENCES Reserva(id);
ALTER TABLE ReservaCategoria ADD Foreign Key (categoriaId) REFERENCES Categoria(id);
ALTER TABLE ReservaRecurso ADD Foreign Key (reservaId) REFERENCES Reserva(id);
ALTER TABLE ReservaRecurso ADD Foreign Key (recursoId) REFERENCES Recurso(id);

insert into Usuario (id, clave, rol) values
    ('FUN001', '001', 'FUNCIONARIO'),
    ('ADM001', '001', 'ADMIN'),
    ('FUN002', 'FUN002', 'FUNCIONARIO');

insert into Funcionario (id, nombre, telefono, usuarioId) values
    ('FUN001', 'Juan Perez', '88888888', 'FUN001'),
    ('FUN002', 'Maria Salas', '29389282', 'FUN002');

insert into Categoria (id, descripcion) values
    ('CAT-0000001', 'Laptop Windows 11'),
    ('CAT-0000002', 'Proyector epson'),
    ('CAT-0000003', 'Sala para 10 personas');

insert into Recurso (id, categoriaId, descripcion) values
    ('REC001', 'CAT-0000001', 'Laptop #238715'),
    ('REC002', 'CAT-0000002', 'Proyector #128933'),
    ('REC003', 'CAT-0000003', 'Sala 1001');

insert into Reserva (id, funcionarioId, actividad, estado, fecha, horaInicio, horaFin) values
    ('RES-0000001', 'FUN001', 'Reunión de trabajo', 'ACTIVA', '2026-09-10', '08:00', '10:00'),
    ('RES-0000002', 'FUN001', 'Junta de marketing', 'ACTIVA', '2026-09-20', '13:00', '15:00');

insert into ReservaCategoria (reservaId, categoriaId) values
    ('RES-0000001', 'CAT-0000001'),
    ('RES-0000002', 'CAT-0000001');

insert into ReservaRecurso (reservaId, recursoId) values
    ('RES-0000001', 'REC001'),
    ('RES-0000002', 'REC001');

insert into Secuencia (tipo, ultimo) values
    ('CAT', 3),
    ('REC', 3),
    ('RES', 2);
