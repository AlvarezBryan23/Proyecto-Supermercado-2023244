drop database if exists DBKinalStore;
create database DBKinalStore;
set global time_zone = '-6:00';
use DBKinalStore;

create table Clientes(
	codigoCliente int not null,
	NITcliente varchar(10) not null,
	nombreCliente varchar(50) not null,
	apellidoCliente varchar(50) not null,
	direccionCliente varchar(150),
	telefonoCliente varchar(12),
	correoCliente varchar(55),
	primary key PK_codigoCliente (codigoCliente)
);

create table Proveedores(
	codigoProveedor int not null,
    NITproveedor varchar(10) not null,
    nombreProveedor varchar(60),
    apellidoProveedor varchar(60),
    direccionProveedor varchar(150),
    razonSocial varchar(60),
    contactoPrincipal varchar(100),
    paginaWeb varchar(100),
    primary key PK_codigoProveedor (codigoProveedor)
); 

create table Compras(
	numeroDocumento int not null,
    fechaDocumento date,
    descripcion varchar(90)not null,
    totalDocumento decimal(10,2),
    primary key PK_numeroDocumento (numeroDocumento)
);

create table CargoEmpleado(
	codigoCargoEmpleado int not null,
    nombreCargo varchar(50)not null,
    descripcionCargo varchar(90),
    primary key PK_codgigoCargoEmpleado (codigoCargoEmpleado)
);

create table TipoProducto(
	codigoTipoProducto int not null,
    descripcion varchar(90),
    primary key PK_codigoTipoProducto (codigoTipoProducto)
);

create table TelefonoProveedor(
	codigoTelefonoProveedor int not null,
    numeroPrincipal varchar(8) not null,
    numeroSecundario varchar(8) not null,
    observaciones varchar(45),
    codigoProveedor int not null,
    primary key PK_codigoTelefono_Proveedor (codigoTelefonoProveedor),
    constraint FK_TelefonoProveedor_Proveedores foreign key TelefonoProveedor(codigoProveedor)
				references Proveedores(codigoProveedor)
);


create table Empleados(
	codigoEmpleado int not null,
    nombreEmpleado varchar(50),
    apellidoEmpleado varchar(50),
    sueldo decimal(10,2),
    direccion varchar(150)not null,
    turno varchar(15)not null,
    codigoCargoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_CargoEmpleado foreign key Empleados(codigoCargoEmpleado)
				references CargoEmpleado(codigoCargoEmpleado)
);

create table Factura(
	numeroFactura int not null,
    estado varchar(50),
    totalFactura decimal(10,2),
    fechaFactura varchar(45),
    codigoCliente int not null,
    codigoEmpleado int not null,
    primary key PK_numeroFactura (numeroFactura),
    constraint FK_Factura_Clientes foreign key Factura(codigoCliente)
				references Clientes(codigoCliente),
	constraint FK_Factura_Empleados foreign key Factura(codigoEmpleado)
				references Empleados(codigoEmpleado)
);



create table Productos(
	codigoProducto varchar(15) not null,
    descripcionProducto varchar(100)not null,
    precioUnitario decimal(10,2),
    precioDocena decimal(10,2),
    precioMayor decimal(10,2),
    existencia int not null,
    codigoTipoProducto int not null,
    codigoProveedor int not null,
    primary key PK_codigoProducto (codigoProducto),
    constraint FK_Productos_TipoProducto foreign key Productos(codigoTipoProducto)
				references TipoProducto(codigoTipoProducto),
	constraint FK_Productos_Proveedores foreign key Productos(codigoProveedor)
				references Proveedores(codigoProveedor)
);

create table DetalleCompra(
	codigoDetalleCompra int not null,
    costoUnitario decimal(10,2),
    cantidad int not null,
    codigoProducto varchar(15),
    numeroDocumento int not null,
    primary key PK_codigoDetalleCompra (codigoDetalleCompra),
    constraint FK_DetalleCompra_Productos foreign key (codigoProducto)
				references Productos(codigoProducto),
	constraint FK_DetalleCompra_Compras foreign key (numeroDocumento)
				references Compras(numeroDocumento)
);

create table DetalleFactura(
	codigoDetalleFactura int not null,
    precioUnitario decimal(10,2)not null,
    cantidad int not null,
    numeroFactura int not null,
    codigoProducto varchar(15) not null,
    primary key PK_codigoDetalleFactura (codigoDetalleFactura),
    constraint FK_DetalleFactura_Factura foreign key (numeroFactura)
				references Factura(numeroFactura),
	constraint FK_DetalleFactura_Productos foreign key (codigoProducto)
				references Productos(codigoProducto)
);

-- -------------------- Procesos de Almacenados ----------------------------------------
-- -------------------- Clientes ------------------------------------------------
-- --------------------- Agregar Clientes ----------------------------------------
Delimiter $$
	create procedure sp_AgregarClientes(in codigoCliente int, in NITcliente varchar(10), in nombreCliente varchar(50), 
    in apellidoCliente varchar(50), in direccionCliente varchar(150), in telefonoCliente varchar(12), in correoCliente varchar(55))
		Begin
			Insert into Clientes(codigoCliente, NITcliente, nombreCliente, 
            apellidoCliente, direccionCliente, telefonoCliente, correoCliente) values
            (codigoCliente, NITcliente, nombreCliente, 
            apellidoCliente, direccionCliente, telefonoCliente, correoCliente);
            
		End $$
Delimiter ;

call sp_AgregarClientes (1, '114006350', 'Harol' , 'Luna', 'San Raymundo', '23002626', 'harolyLuna@gamil.com');
call sp_AgregarClientes (2, '230635006', 'Oliver', 'Donis', 'Amatitlan', '53170139', 'OliveryDonis@gamil.com');
call sp_AgregarClientes (3, '202324456', 'Bryan', 'Alvarez', 'Guatemala', '36101639', 'balvarez@gamil.com');
call sp_AgregarClientes (4, '202567789', 'Helen', 'Reynoso', 'Izabal', '21785693', 'helen@gamil.com');

-- -------------------------------Listar Clientes ---------------------------------------------------
Delimiter $$
	create procedure sp_ListarClientes()
		Begin
			select
            C.codigoCliente,
            C.NITcliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C;
		End $$
Delimiter ;

call sp_ListarClientes();

-- -----------------------------------BuscarClientes----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarClientes(in codCli int)
		Begin
			select
			C.codigoCliente,
            C.NITcliente,
            C.nombreCliente,
            C.apellidoCliente,
            C.direccionCliente,
            C.telefonoCliente,
            C.correoCliente
            from Clientes C
            where codigoCliente = codCli;
		End $$
Delimiter ;
call sp_BuscarClientes(1);

-- --------------------------- Eliminar Clientes --------------------------------------------------

Delimiter $$
	create procedure sp_EliminarClientes (in codCli int)
		Begin
			Delete from Clientes
				where codigoCliente = codCli;
		End $$
Delimiter ;

/* call sp_EliminarClientes(1);
call sp_ListarClientes(); */

-- ----------------------------- Editar Clientes ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarClientes (in codCli int, in nCliente varchar(10), in nomCliente varchar(50), in apCliente varchar(50),
    in direcCliente varchar(150), in telCliente varchar(12), in corrCliente varchar(100))
		Begin
			Update Clientes C
            set
            C.NITcliente = nCliente,
            C.nombreCliente = nomCliente,
            C.apellidoCliente = apCliente,
            C.direccionCliente = direcCliente,
            C.telefonoCliente = telCliente,
            C.correoCliente = corrCliente
            where codigoCliente = codCli;
		End $$
Delimiter ;

call sp_EditarClientes(4, '202567789', 'Helen', 'Hernandez', 'Antigua', '21785693', 'helen@gamil.com');


-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------Proveedores------------------------------------------------
-- -----------------------Agregar Proveedores ----------------------------------------
Delimiter $$
	create procedure sp_AgregarProveedores(in codigoProveedor int, in NITproveedor varchar(10), in nombreProveedor varchar(60), 
    in apellidoProveedor varchar(60), in direccionProveedor varchar(150), in razonSocial varchar(60),
    in contactoPrincipal varchar(100), in paginaWeb varchar(100))
		Begin
			Insert into Proveedores(codigoProveedor, NITproveedor, nombreProveedor, apellidoProveedor, direccionProveedor,
            razonSocial, contactoPrincipal, paginaWeb) values
            (codigoProveedor, NITproveedor, nombreProveedor, apellidoProveedor, direccionProveedor,
            razonSocial, contactoPrincipal, paginaWeb);
            
		End $$
Delimiter ;

call sp_AgregarProveedores(01, '123456789', 'Dayrin', 'Alvarez', 'Guastatoya', 'Kinal', '22174589', 'Kinal.com');
call sp_AgregarProveedores(02, '567895462', 'Dulce', 'Rios', 'Cayala', 'Ciclo', '12356489', 'Ciclo.com');
call sp_AgregarProveedores(03, '202324456', 'Jose', 'Ochoa', 'Agua Tibia', 'Tequila', '10203040', 'Tequila.com');
call sp_AgregarProveedores(04, '202178941', 'Michael', 'Gonzales', 'Xelaju', 'Lala', '10504578', 'Lala.com');


-- -------------------------------Listar Proveedores---------------------------------------------------
Delimiter $$
	create procedure sp_ListarProveedores()
		Begin
			select
            P.codigoProveedor,
            P.NITproveedor,
            P.nombreProveedor,
            P.apellidoProveedor,
            P.direccionProveedor,
            P.razonSocial,
            P.contactoPrincipal,
            P.paginaWeb
            from Proveedores P;
		End $$
Delimiter ;

call sp_ListarProveedores();
		
-- -----------------------------------Buscar Proveedores----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarProveedores(in codigoPro int)
		Begin
			select
				P.codigoProveedor,
				P.NITproveedor,
				P.nombreProveedor,
				P.apellidoProveedor,
				P.direccionProveedor,
				P.razonSocial,
				P.contactoPrincipal,
				P.paginaWeb
				from Proveedores P
                where codigoProveedor = codigoPro;
            End $$
Delimiter ;

call sp_BuscarProveedores(03);

-- --------------------------- Eliminar Proveedores--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarProveedores(in codigoPro int)
		Begin
			Delete from Proveedores
				where codigoProveedor = codigoPro;
		End $$
Delimiter ;

/*call sp_EliminarProveedores(1);
call sp_ListarProveedores(); */
            

-- ----------------------------- Editar Proveedores ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarProveedores(in codigoPro int, in NITpro varchar(10), in nombrePro varchar(60), 
    in apellidoPro varchar(60), in direccionPro varchar(150), in razonS varchar(60),
    in contactoPri varchar(100), in Web varchar(100))
		Begin
			Update Proveedores P
            set
            P.NITproveedor = NITpro,
            P.nombreProveedor = nombrePro,
            P.apellidoProveedor = apellidoPro,
            P.direccionProveedor = direccionPro,
            P.razonSocial = razonS,
            P.contactoPrincipal = contactoPri,
            P.paginaWeb = Web
            where codigoProveedor = codigoPro;
		End $$
Delimiter ;

call sp_EditarProveedores(4, '202567789', 'Sara', 'Castillo', 'Mixco', 'Whisky', '22174512', 'Whisky.com');

-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------CargoEmpleado------------------------------------------------
-- -----------------------Agregar Cargo ----------------------------------------
Delimiter $$
	create procedure sp_AgregarCargo(in codigoCargoEmpleado int, in nombreCargo varchar(50), 
    in descripcionCargo varchar(90))
		Begin
			Insert into CargoEmpleado(codigoCargoEmpleado, nombreCargo, descripcionCargo) values
            (codigoCargoEmpleado, nombreCargo, descripcionCargo);
            
		End $$
Delimiter ;

call sp_AgregarCargo(10, 'Coordinador', 'Controlar todo lo que pasa en la empresa');
call sp_AgregarCargo(11, 'Administrador', 'Asginar responsabilidades de la empresa');
call sp_AgregarCargo(12, 'Jefe', 'Supervisar y dirigir');
call sp_AgregarCargo(13, 'Gerente', 'Contratar, capacitar y evaluar');

-- -------------------------------Listar CargoEmpleado---------------------------------------------------
Delimiter $$
	create procedure sp_ListarCargo()
		Begin
			select
            CE.codigoCargoEmpleado,
            CE.nombreCargo,
            CE.descripcionCargo		
            from CargoEmpleado CE;
		End $$
Delimiter ;

call sp_ListarCargo();

-- -----------------------------------Buscar CargoEmpleado----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarCargo(in codigoCE int)
		Begin
			select
            CE.codigoCargoEmpleado,
			CE.nombreCargo,
            CE.descripcionCargo
            from CargoEmpleado CE
            where codigoCargoEmpleado = codigoCE;
		End $$
Delimiter ;

call sp_BuscarCargo(12);

-- --------------------------- Eliminar CargoEmpleado--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarCargo(in codigoCE int)
		Begin
			Delete from CargoEmpleado
				where codigoCargoEmpleado = codigoCE;
		End $$
Delimiter ;

/*call sp_EliminarCargo(11);
call sp_ListarCargo();*/

-- ----------------------------- Editar CargoEmpleado ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarCargo(in codigoCE int, in nombreC varchar(50),
    in descripcionC varchar(90))
		Begin
			Update CargoEmpleado CE
            set	
            CE.nombreCargo = nombreC,
            CE.descripcionCargo = descripcionC
            where codigoCargoEmpleado = codigoCE;
		End $$
Delimiter ;

call sp_EditarCargo(12, 'Jefe de Proyectos', 'Controlar todos los proyectos');	


-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------Empleados------------------------------------------------
-- -----------------------Agregar Empleados ----------------------------------------
Delimiter $$
	create procedure sp_AgregarEmpleados(in codigoEmpleado int, in nombreEmpleado varchar(50), in apellidoEmpleado varchar(50), 
    in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15), in codigoCargoEmpleado int)
		Begin
			Insert into Empleados(codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccion, turno, codigoCargoEmpleado) 
            values (codigoEmpleado, nombreEmpleado, apellidoEmpleado, sueldo, direccion, turno, codigoCargoEmpleado);
            
		End $$
Delimiter ;

call sp_AgregarEmpleados(1, 'Derian', 'Hernandez', '10000', 'Mixco', 'Dia', 10);
call sp_AgregarEmpleados(2, 'Fredy', 'Gomez', '5000', 'Juana de Arco', 'Noche', 11);
call sp_AgregarEmpleados(3, 'Josue', 'Perez', '4500', 'Zona 18', 'Noche', 12);
call sp_AgregarEmpleados(4, 'Jorge', 'Luna', '6700', 'Lomas', 'Dia', 13);

-- -------------------------------Listar Proveedores---------------------------------------------------
Delimiter $$
	create procedure sp_ListarEmpleados()
		Begin
			select
            E.codigoEmpleado,
            E.nombreEmpleado,
            E.apellidoEmpleado,
            E.sueldo,
            E.direccion,
            E.turno,
            E.codigoCargoEmpleado
            from Empleados E;
		End $$
Delimiter ;

call sp_ListarEmpleados();

-- -----------------------------------Buscar Empleados----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarEmpleados(in codigoE int)
		Begin
			select
			E.codigoEmpleado,
            E.nombreEmpleado,
            E.apellidoEmpleado,
            E.sueldo,
            E.direccion,
            E.turno
            from Empleados E
            where codigoEmpleado = codigoE;
		End $$
Delimiter ;
call sp_BuscarEmpleados(2);

-- --------------------------- Eliminar Empleados--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarEmpleados(in codigoE int)
		Begin
			Delete from Empleados
				where codigoEmpleado = codigoE;
		End $$
Delimiter ;

/*call sp_EliminarEmpleados(1);
call sp_ListarEmpleados();*/ 

-- ----------------------------- Editar Empleados ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarEmpleados(in codigoE int, in nombreE varchar(50), in apellidoE varchar(50), 
    in sueldo decimal(10,2), in direccion varchar(150), in turno varchar(15))
		Begin
			Update Empleados E
            set
            E.nombreEmpleado = nombreE,
            E.apellidoEmpleado = apellidoE,
            E.sueldo = sueldo,
            E.direccion = direccion,
            E.turno = turno
            where codigoEmpleado = codigoE;
		End $$
Delimiter ;

call sp_EditarEmpleados(1, 'Anthony', 'Davis', '15500', 'Zacapa', 'Dia');	
	

-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------Compras------------------------------------------------
-- -----------------------Agregar Compras ----------------------------------------
Delimiter $$
	create procedure sp_AgregarCompras(in numeroDocumento int, in fechaDocumento date, in descripcion varchar(90), 
    in totalDocumento decimal(10,2))
		Begin
			Insert into Compras(numeroDocumento, fechaDocumento, descripcion, totalDocumento) values
            (numeroDocumento, fechaDocumento, descripcion, totalDocumento);
            
		End $$
Delimiter ;

call sp_AgregarCompras(1, '2023-02-15', 'Pasta Colgate', '500');
call sp_AgregarCompras(2, '2024-05-9', 'Pelcuhe para la novia', '1000');
call sp_AgregarCompras(3, '2021-09-1', 'Harina para panqueques', '100');
call sp_AgregarCompras(4, '2020-10-25', 'Aceite de bebe', '250');


-- -------------------------------Listar Compras---------------------------------------------------
Delimiter $$
	create procedure sp_ListarCompras()
		Begin
			select
            C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C;
		End $$
Delimiter ;

call sp_ListarCompras();

-- -----------------------------------Buscar Compras----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarCompras(in numeroDo int)
		Begin
			select
			C.numeroDocumento,
            C.fechaDocumento,
            C.descripcion,
            C.totalDocumento
            from Compras C
            where numeroDocumento = numeroDo;
		End $$
Delimiter ;

call sp_BuscarCompras(2);

-- --------------------------- Eliminar Compras--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarCompras(in numeroDo int)
		Begin
			Delete from Compras
				where numeroDocumento = numeroDo;
		End $$
Delimiter ;

/*call sp_EliminarCompras(1);
call sp_ListarCompras();*/

-- ----------------------------- Editar Compras ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarCompras(in numeroDo int,in fechaDoc date, in descripcion varchar(90), 
    in totalDoc decimal(10,2) )
		Begin
			Update Compras C
            set
            C.fechaDocumento = fechaDoc,
            C.descripcion = descripcion,
            C.totalDocumento = totalDoc
            where numeroDocumento = numeroDo;
		End $$
Delimiter ;

call sp_EditarCompras(3, '2025-01-5', 'Leche, Cereal, Crema', '1500');	

-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------TipoProducto------------------------------------------------
-- -----------------------Agregar TipoProducto ----------------------------------------
Delimiter $$
	create procedure sp_AgregarTipoProducto(in codigoTipoProducto int, in descripcion varchar(90))
		Begin
			Insert into TipoProducto(codigoTipoProducto, descripcion) values
            (codigoTipoProducto, descripcion);
            
		End $$
Delimiter ;
	
call sp_AgregarTipoProducto(15, 'Estuche para lapiceros y crayones');  
call sp_AgregarTipoProducto(16, 'Aceite para carros automaticos');  
call sp_AgregarTipoProducto(17, 'Juego de carros con pista armable');  
call sp_AgregarTipoProducto(18, 'Una PlayStation 5 con dos controles');  

-- -------------------------------Listar TipoProducto---------------------------------------------------
Delimiter $$
	create procedure sp_ListarTipoProducto()
		Begin
			select
            TP.codigoTipoProducto,
            TP.descripcion	
            from TipoProducto TP;
		End $$
Delimiter ;

call sp_ListarTipoProducto();

-- -----------------------------------Buscar TipoProducto----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarTipoProducto(in codigoTP int)
		Begin
			select
            TP.codigoTipoProducto,
			TP.descripcion
            from TipoProducto TP
            where codigoTipoProducto = codigoTP;
		End $$
Delimiter ;

call sp_BuscarTipoProducto(17);

-- --------------------------- Eliminar TipoProducto--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarTipoProducto(in codigoTP int)
		Begin
			Delete from TipoProducto
				where codigoTipoProducto = codigoTP;
		End $$
Delimiter ;

/*call sp_EliminarTipoProducto(18);
call sp_ListarTipoProducto();*/

-- ----------------------------- Editar TipoProducto ----------------------------------------------------
Delimiter $$
	create procedure sp_EditarTipoProducto(in codigoTP int, in descripcionTP varchar(90))
		Begin
			Update TipoProducto TP
            set	
            TP.descripcion = descripcionTP
            where codigoTipoProducto = codigoTP;
		End $$
Delimiter ;

call sp_EditarTipoProducto(17, 'Un collar para decoracion de hogar');

-- -------------------- Procesos de Almacenados ----------------------------------------
-- --------------------------Productos ------------------------------------------------
-- -----------------------Agregar Productos  ----------------------------------------
Delimiter $$
	create procedure sp_AgregarProductos (in codigoProducto varchar(15), in descripcionProducto varchar(100), in precioUnitario decimal(10,2), 
    in precioDocena decimal(10,2), in precioMayor decimal(10,2), in existencia int, in codigoTipoProducto int, in codigoProveedor int)
		Begin
			Insert into Productos(codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto,
             codigoProveedor) values
            (codigoProducto, descripcionProducto, precioUnitario, precioDocena, precioMayor, existencia, codigoTipoProducto,
             codigoProveedor);
            
		End $$
Delimiter ;
call sp_AgregarProductos('10', 'Estuche de metal', 100.20, 15.00, 125.50, 200, 15, 01);
call sp_AgregarProductos('11', 'Carros Automaticos', 130.00, 10.00, 150.40, 500, 16, 02);
call sp_AgregarProductos('12', '15 piezas armables', 120.10, 65.00, 115.00, 600, 17, 03);
call sp_AgregarProductos('13', '5 Juegos incluidos', 1000.45, 1500.00, 500.00, 2000, 18, 04);

-- -------------------------------Listar Productos ---------------------------------------------------
Delimiter $$
	create procedure sp_ListarProductos()
		Begin
			select
            P.codigoProducto,
            P.descripcionProducto,
            P.precioUnitario,
            P.precioDocena,
            P.precioMayor,
            P.existencia,
            P.codigoTipoProducto,
            P.codigoProveedor 
            from Productos P;
		End $$
Delimiter ;

call sp_ListarProductos();

-- -----------------------------------Buscar Productos----------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarProductos(in codigoP  int)
		Begin
			select
            P.codigoProducto,
			P.descripcionProducto,
            P.precioUnitario,
            P.precioDocena,
            P.precioMayor,
            P.existencia,
            P.codigoTipoProducto,
            P.codigoProveedor  
            from Productos P
            where codigoProducto  = codigoP;
		End $$
Delimiter ;

call sp_BuscarProductos(13);

-- --------------------------- Eliminar Productos--------------------------------------------------
Delimiter $$
	create procedure sp_EliminarProductos(in codigoP varchar(15))
		Begin
			Delete from Productos 
				where codigoProducto = codigoP;
		End $$
Delimiter ;

/*call sp_EliminarProductos(10);
call sp_ListarProductos();*/


-- ----------------------------- Editar Productos----------------------------------------------------
Delimiter $$
	create procedure sp_EditarProductos(in codigoP varchar(15), in descripcionP varchar(100), in precioUP decimal(10,2), 
    in precioDP decimal(10,2), in precioMP decimal(10,2), in existencia int, in codigoTipoProducto int, in codigoProveedor int)
		Begin
			Update Productos P
            set	
            P.descripcionProducto = descripcionP,
            P.precioUnitario = precioUP,
            P.precioDocena =  precioDP,
            P.precioMayor = precioMP,
            P.existencia = existencia,
            p.codigoTipoProducto = codigoTipoProducto,
            p.codigoProveedor = codigoProveedor 
            where codigoProducto  = codigoP;
		End $$
Delimiter ;

call sp_EditarProductos(12, 'Carros incluidos', 100.10, 25, 1010.50, 25, 16, 03);