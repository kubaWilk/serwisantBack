insert into user_details (first_name, last_name, street, post_code, city)
    values ('Andrzej', 'Kowalski', 'Testowa 1', '00-000', 'Warszawa');

insert into users (username, password, active, email, user_detail_id)
     values ('root', '$2a$12$enZyMcOqcaSgS4oPxthFweOfCvY5zqkEHi1lwy7VZ2QWCfLF9P8/2', true, 'jakub_wilk@outlook.com', 1);

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_CUSTOMER');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_EMPLOYEE');

insert into authorities (user_id, username, authority)
    values (1, 'root', 'ROLE_ADMIN');

insert into device(manufacturer, model, serial_number)
    values('Lenovo', 'Thinkpad', 'L14GFR4');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN');

insert into repair (device_id, issuer_user_id, estimated_cost, description, repair_status)
    values (1, 1, 400, 'Komputer nie włącza się po wciśnięciu przycisku', 'OPEN');

insert into note (author_id, repair_id, visibility, message)
values(1, 1, 1, 'Rozpoczęto diagnostykę sprzętu');

insert into note (author_id, repair_id, visibility, message)
values(1, 1, 0, 'Podejrzewam uszkodzenie baterii');

insert into note (author_id, repair_id, visibility, message)
values(1, 2, 1, 'test Note');