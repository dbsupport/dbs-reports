INSERT INTO tpr_auth_role (id_role, id_authority)
     VALUES (300, 300);

INSERT INTO tpr_authority (id, "name")
     VALUES (300, 'PL000');

INSERT INTO tpr_role (id, "name")
     VALUES (100, 'Admin');

INSERT INTO tpr_role (id, "name")
     VALUES (200, 'Management');

INSERT INTO tpr_role (id, "name")
     VALUES (300, 'User');

INSERT INTO tre_pattern (id,
                         upload_date,
                         manifest,
                         "name",
                         version,
                         "_cdate",
                         "_cgsid",
                         "_edate",
                         "_egsid",
                         "_stamp",
                         creator_id,
                         creator_name)
        VALUES (
                  113,
                  TO_TIMESTAMP ('10/13/2013 19:26:41.949 +0200',
                                'MM/DD/YYYY fmHH24fm:MI:SS.FF Z'),
                  'Manifest-Version: 1.0
Created-By: 1.2 (Sun Microsystems Inc.)

Name: Database-Support-Reports
Reports-Extension-Map: wzor.rtf=pdf;
Reports-Pattern-Version: 1.0.0
Reports-Roles: Admin;Supervisor;Test
Reports-Name-Template: test-${date-time}-suffix
Reports-Pattern-Name: Raport-ZUS
Reports-Init-Sql: init.sql
Reports-Pattern-Author: AdamNowak

',
                  'Raport-ZUS',
                  '1.0.0',
                  TO_TIMESTAMP ('10/13/2013 19:26:41.964 +0200',
                                'MM/DD/YYYY fmHH24fm:MI:SS.FF Z'),
                  'ZZKUBAL',
                  TO_TIMESTAMP ('10/13/2013 19:26:41.964 +0200',
                                'MM/DD/YYYY fmHH24fm:MI:SS.FF Z'),
                  'ZZKUBAL',
                  1,
                  'ZZKUBAL',
                  'Jan JanNowak ');

INSERT INTO tre_pattern_asset (id, "path", pattern_id)
     VALUES (134, 'definicja.sql', 113);

INSERT INTO tre_pattern_asset (id, "path", pattern_id)
     VALUES (136, 'wzor.rtf', 113);

INSERT INTO tre_pattern_asset (id, "path", pattern_id)
     VALUES (135, 'init.sql', 113);

INSERT INTO tre_pattern_role (id_pattern, role)
     VALUES (113, 'Test');

INSERT INTO tre_pattern_role (id_pattern, role)
     VALUES (113, 'Supervisor');

INSERT INTO tre_pattern_role (id_pattern, role)
     VALUES (113, 'Admin');

COMMIT;