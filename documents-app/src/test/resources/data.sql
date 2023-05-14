INSERT INTO USERS (login, password, role) VALUES  ('login',
                                                   '$2y$10$RwZHnd9sW7XAIM7f92m69uhf10i.xZVcZ2R/j2KL1RUww/fzj7lie',
                                                   'ADMIN');
-- password with BCrypt encryption, value = "password"
INSERT INTO CATALOGUE (id, created_time, name) VALUES (1, NOW(), 'root');
UPDATE id_generator SET sequence = 1 WHERE sequence = 0;