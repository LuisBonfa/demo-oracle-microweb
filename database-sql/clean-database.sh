#!/usr/bin/env bash
sqlplus -s demooracle/demooracle <<SQL_COMMANDS
BEGIN
  -- Remova all tables:
  FOR i IN (SELECT ut.table_name
              FROM USER_TABLES ut) LOOP
    EXECUTE IMMEDIATE 'drop table '|| i.table_name ||' CASCADE CONSTRAINTS ';
  END LOOP;  

  -- Having problems:
  -- Remove all sequences:
  -- FOR i IN (SELECT us.sequence_name
  --             FROM USER_SEQUENCES us) LOOP
  --   EXECUTE IMMEDIATE 'drop sequence '|| i.sequence_name ||'';
  -- END LOOP;
END;
/
SQL_COMMANDS
