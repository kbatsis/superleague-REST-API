CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `superleaguedb`.`users`
(`id`,
`password`,
`username`)
VALUES
(1,
"$2a$12$PPEfJIiC.uaAZqHCkx46ZeLe54VExDag4ZnpiLcTLMToSRDagZ77.",
"admin");

