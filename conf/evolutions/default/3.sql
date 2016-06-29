# --- !Ups

CREATE TABLE `user_profile` (
  `upid` int(11) NOT NULL COMMENT '		',
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`upid`),
  KEY `fk_user_id_idx` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`uid`) ON DELETE CASCADE ON UPDATE NO ACTION
);

# --- !Downs

DROP TABLE `user_profile`;