insert into user_details (first_name, last_name, street, post_code, city)
    values ('Właściciel', 'Serwisu', 'Testowa 1', '00-000', 'Serwisowa');

insert into user_details (first_name, last_name, street, post_code, city)
    values ('Andrzej', 'Kowalski', 'Testowa 1', '00-000', 'Warszawa');

insert into users (username, password, active, email, user_detail_id, created_at, modified_at)
     values ('root', '$2a$12$enZyMcOqcaSgS4oPxthFweOfCvY5zqkEHi1lwy7VZ2QWCfLF9P8/2', true, 'test@test.com', 1, '2024-01-01', '2024-01-01');

 insert into users (username, password, active, email, user_detail_id, created_at, modified_at)
      values ('root2', '$2a$12$enZyMcOqcaSgS4oPxthFweOfCvY5zqkEHi1lwy7VZ2QWCfLF9P8/2', true, 'jakub_wilk@outlook.com', 2, '2024-01-01', '2024-01-01');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_CUSTOMER');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_EMPLOYEE');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_ADMIN');

insert into device(manufacturer, model, serial_number, created_at, modified_at)
    values('Lenovo', 'Thinkpad', 'L14GFR4', '2024-01-01', '2024-01-01');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status, created_at, modified_at)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN', '2024-01-01', '2024-01-01');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status, created_at, modified_at)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN', '2024-01-01', '2024-01-01');

insert into note (author_id, repair_id, visibility, message, created_at,modified_at)
values(1, 1, 1, 'Rozpoczęto diagnostykę sprzętu','2024-01-01', '2024-01-01');

insert into note (author_id, repair_id, visibility, message, created_at, modified_at)
values(1, 1, 0, 'Podejrzewam uszkodzenie baterii', '2024-01-01', '2024-01-01');

insert into note (author_id, repair_id, visibility, message, created_at, modified_at)
values(1, 2, 1, 'test Note', '2024-01-01', '2024-01-01');