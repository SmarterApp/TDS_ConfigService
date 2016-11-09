/***********************************************************************************************************************
  File: V1478359035__configs_create_testtool_and_testtooltype.sql

  Desc: Adds the client_testtool and client_testtooltype tables leveraged during the accommodations

***********************************************************************************************************************/

USE configs;

DROP TABLE IF EXISTS client_testtool;
DROP TABLE IF EXISTS client_testtooltype;
DROP TABLE IF EXISTS client_segmentproperties;

CREATE TABLE client_testtooltype (
  clientname varchar(100) NOT NULL,
  toolname varchar(255) NOT NULL,
  allowchange bit(1) NOT NULL DEFAULT 1,
  tideselectable bit(1) DEFAULT NULL,
  rtsfieldname varchar(100) DEFAULT NULL,
  isrequired bit(1) NOT NULL DEFAULT 0,
  tideselectablebysubject bit(1) NOT NULL DEFAULT 0,
  isselectable bit(1) NOT NULL DEFAULT 1,
  isvisible bit(1) NOT NULL DEFAULT 1,
  studentcontrol bit(1) NOT NULL DEFAULT 1,
  tooldescription varchar(255) DEFAULT NULL,
  sortorder int(11) NOT NULL DEFAULT 0,
  dateentered datetime(3) NOT NULL,
  origin varchar(50) DEFAULT NULL,
  source varchar(50) DEFAULT NULL,
  contexttype varchar(50) NOT NULL,
  context varchar(255) NOT NULL,
  dependsontooltype varchar(50) DEFAULT NULL,
  disableonguestsession bit(1) NOT NULL DEFAULT 0,
  isfunctional bit(1) NOT NULL DEFAULT 1,
  testmode varchar(25) NOT NULL DEFAULT 'all',
  isentrycontrol bit(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (clientname,context,contexttype,toolname)
);

CREATE TABLE client_testtool (
  clientname varchar(100) NOT NULL,
  type varchar(255) NOT NULL,
  code varchar(255) NOT NULL,
  value varchar(255) NOT NULL,
  isdefault bit(1) NOT NULL,
  allowcombine bit(1) NOT NULL,
  valuedescription varchar(255) DEFAULT NULL,
  context varchar(255) NOT NULL,
  sortorder int(11) NOT NULL DEFAULT 0,
  origin varchar(50) DEFAULT NULL,
  source varchar(50) DEFAULT NULL,
  contexttype varchar(50) NOT NULL,
  testmode varchar(25) NOT NULL DEFAULT 'all',
  equivalentclientcode varchar(255) DEFAULT NULL,
  PRIMARY KEY (clientname,context,contexttype,type,code),
  KEY ix_clienttooltestid (context),
  CONSTRAINT fk_clienttool_tooltype FOREIGN KEY (clientname, context, contexttype, type) REFERENCES client_testtooltype (clientname, context, contexttype, toolname) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE client_segmentproperties (
  ispermeable int(11) NOT NULL,
  clientname varchar(100) NOT NULL,
  entryapproval int(11) NOT NULL,
  exitapproval int(11) NOT NULL,
  itemreview bit(1) NOT NULL DEFAULT 1,
  segmentid varchar(255) NOT NULL,
  segmentposition int(11) NOT NULL,
  parenttest varchar(255) NOT NULL,
  ftstartdate datetime(3) DEFAULT NULL,
  ftenddate datetime(3) DEFAULT NULL,
  label varchar(255) DEFAULT NULL,
  modekey varchar(250) DEFAULT NULL,
  restart int(11) DEFAULT NULL,
  graceperiodminutes int(11) DEFAULT NULL,
  PRIMARY KEY (clientname,parenttest,segmentid)
);
