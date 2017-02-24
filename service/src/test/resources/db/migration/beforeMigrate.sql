/***********************************************************************************************************************
  File: beforeMigrate.sql

  Desc: Sets the character set and collation to utf8 for both databases.

***********************************************************************************************************************/
ALTER DATABASE configs CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER DATABASE session CHARACTER SET utf8 COLLATE utf8_unicode_ci;