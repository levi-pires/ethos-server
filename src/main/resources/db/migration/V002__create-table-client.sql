create table e_client (

	id bigint primary key not null auto_increment,
	user varchar (60) not null,
	password varchar (128) not null,
	phone varchar (15) not null,
	imei varchar (15) not null,
	attempt bigint not null);

alter table e_client add constraint fk_e_client_login_attempts
foreign key (attempt) references login_attempts(id);
