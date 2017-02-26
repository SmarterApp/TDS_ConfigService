/***********************************************************************************************************************
  File: V1486288010__configs_create_client.sql

  Desc: Creates the client_itemscoringconfig table containing scoring configuration data

***********************************************************************************************************************/
USE configs;

DROP TABLE IF EXISTS client_itemscoringconfig;

CREATE TABLE client_itemscoringconfig (
  clientname varchar(100) NOT NULL,
  servername varchar(255) NOT NULL DEFAULT '*',
  siteid varchar(255) NOT NULL DEFAULT '*',
  context varchar(255) NOT NULL DEFAULT '*',
  itemtype varchar(50) NOT NULL DEFAULT '*',
  item_in bit(1) DEFAULT NULL,
  priority int(11) DEFAULT NULL,
  _key varbinary(16) NOT NULL,
  serverurl varchar(255) NOT NULL DEFAULT '*',
  environment varchar(50) DEFAULT NULL,
  PRIMARY KEY (_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;