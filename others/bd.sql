USE casadocodigo;

CREATE TABLE IF NOT EXISTS `versao` ( 
	`versao` int, 
    PRIMARY KEY (`versao`)
);

INSERT INTO versao (versao)VALUES(1);

CREATE TABLE `produto` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dataLancamento` datetime DEFAULT NULL,
  `descricao` longtext,
  `paginas` int NOT NULL,
  `sumarioPath` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `produto_precos` (
  `Produto_id` int NOT NULL,
  `tipo` int DEFAULT NULL,
  `valor` decimal(19,2) DEFAULT NULL,
  KEY `FK_hl4xdmygc7v2x607r4rbs6x3a` (`Produto_id`),
  CONSTRAINT `FK_hl4xdmygc7v2x607r4rbs6x3a` FOREIGN KEY (`Produto_id`) REFERENCES `produto` (`id`)
);

CREATE TABLE `role` (
  `nome` varchar(255) NOT NULL,
  PRIMARY KEY (`nome`)
);

CREATE TABLE `usuario` (
  `email` varchar(255) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
);

CREATE TABLE `usuario_role` (
  `Usuario_email` varchar(255) NOT NULL,
  `roles_nome` varchar(255) NOT NULL,
  UNIQUE KEY `UK_jnmoadyberbn5oyaybe8dxskj` (`roles_nome`),
  KEY `FK_fpipx83bjblmwmw25qotdyd3` (`Usuario_email`),
  CONSTRAINT `FK_fpipx83bjblmwmw25qotdyd3` FOREIGN KEY (`Usuario_email`) REFERENCES `usuario` (`email`),
  CONSTRAINT `FK_jnmoadyberbn5oyaybe8dxskj` FOREIGN KEY (`roles_nome`) REFERENCES `role` (`nome`)
);

insert into Role values ('ROLE_ADMIN');
insert into Usuario (email, nome, senha) values ('admin@casadocodigo.com.br', 'Administrador', '$2a$04$qP517gz1KNVEJUTCkUQCY.JzEoXzHFjLAhPQjrg5iP6Z/UmWjvUhq');
insert into Usuario_Role(Usuario_email, roles_nome) values ('admin@casadocodigo.com.br', 'ROLE_ADMIN');